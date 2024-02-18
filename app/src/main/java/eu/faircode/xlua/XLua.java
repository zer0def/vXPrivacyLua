/*
    This file is part of XPrivacyLua.

    XPrivacyLua is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    XPrivacyLua is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with XPrivacyLua.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2017-2019 Marcel Bokhorst (M66B)
 */

package eu.faircode.xlua;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.Varargs;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import eu.faircode.xlua.api.properties.MockPropConversions;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xmock.XMockQuery;
import eu.faircode.xlua.hooks.XHookUtil;

//package eu.faircode.xlua;

import java.util.Collection;

import eu.faircode.xlua.api.xlua.XLuaQuery;
import eu.faircode.xlua.hooks.LuaHookWrapper;
import eu.faircode.xlua.hooks.LuaScriptHolder;
import eu.faircode.xlua.hooks.XReporter;
import eu.faircode.xlua.hooks.XResolved;
import eu.faircode.xlua.randomizers.GlobalRandoms;
import eu.faircode.xlua.randomizers.IRandomizer;
import eu.faircode.xlua.utilities.BundleUtil;

import eu.faircode.xlua.api.hook.XLuaHook;

public class XLua implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    private static final String TAG = "XLua.XCoreStartup";
    public XReporter report = new XReporter();
    public static int version = -1;

    public void initZygote(final IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        Log.i(TAG, "initZygote system=" + startupParam.startsSystemServer + " debug=" + BuildConfig.DEBUG);
    }

    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        int uid = Process.myUid();
        Log.i(TAG, "Loaded " + lpparam.packageName + ":" + uid);
        XposedBridge.log(TAG + " Loaded " + lpparam.packageName + ":" + uid);

        if ("android".equals(lpparam.packageName))
            hookAndroid(lpparam);

        if ("com.android.providers.settings".equals(lpparam.packageName))
            hookSettings(lpparam);

        if (!"android".equals(lpparam.packageName) &&
                !lpparam.packageName.startsWith(BuildConfig.APPLICATION_ID)) {
            hookApplication(lpparam);
        }
    }

    private void hookSettings(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // https://android.googlesource.com/platform/frameworks/base/+/master/packages/SettingsProvider/src/com/android/providers/settings/SettingsProvider.java
        Class<?> clsSet = Class.forName("com.android.providers.settings.SettingsProvider", false, lpparam.classLoader);
        XposedBridge.hookMethod(
                clsSet.getMethod("call", String.class, String.class, Bundle.class),
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XCommandBridgeStatic.handeCall(param, lpparam.packageName);
                    }
                });

        // Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
        XposedBridge.hookMethod(
                clsSet.getMethod("query", Uri.class, String[].class, String.class, String[].class, String.class),
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        XCommandBridgeStatic.handleQuery(param, lpparam.packageName);
                    }
                });
    }

    private void hookAndroid(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // https://android.googlesource.com/platform/frameworks/base/+/master/services/core/java/com/android/server/am/ActivityManagerService.java
        Class<?> clsAM = Class.forName("com.android.server.am.ActivityManagerService", false, lpparam.classLoader);

        XposedBridge.hookAllMethods(clsAM, "systemReady", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                try {
                    Log.i(TAG, "Preparing system");
                    XposedBridge.log(TAG + " Preparing system");
                    Context context = getContext(param.thisObject);
                    hookPackage(lpparam, Process.myUid(), context);

                } catch (Throwable ex) {
                    Log.e(TAG, Log.getStackTraceString(ex));
                    XposedBridge.log(ex);
                }
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                try {
                    Log.i(TAG, "System ready");
                    XposedBridge.log(TAG + " System ready");
                    Context context = getContext(param.thisObject);

                    // Store current module version
                    PackageInfo pi = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);
                    version = pi.versionCode;
                    Log.i(TAG, "Module version " + version);

                    // public static UserManagerService getInstance()
                    Class<?> clsUM = Class.forName("com.android.server.pm.UserManagerService", false, param.thisObject.getClass().getClassLoader());
                    Object um = clsUM.getDeclaredMethod("getInstance").invoke(null);

                    //  public int[] getUserIds()
                    int[] userids = (int[]) um.getClass().getDeclaredMethod("getUserIds").invoke(um);

                    // Listen for package changes
                    for (int userid : userids) {
                        Log.i(TAG, "Registering package listener user=" + userid);
                        IntentFilter ifPackageAdd = new IntentFilter();
                        ifPackageAdd.addAction(Intent.ACTION_PACKAGE_ADDED);
                        ifPackageAdd.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
                        ifPackageAdd.addDataScheme("package");
                        XUtil.createContextForUser(context, userid).registerReceiver(new ReceiverPackage(), ifPackageAdd);
                    }
                } catch (Throwable ex) {
                    Log.e(TAG, Log.getStackTraceString(ex));
                    XposedBridge.log(ex);
                }
            }

            @NonNull
            private Context getContext(Object am) throws Throwable {
                // Searching for context
                Context context = null;
                Class<?> cAm = am.getClass();
                while (cAm != null && context == null) {
                    for (Field field : cAm.getDeclaredFields())
                        if (field.getType().equals(Context.class)) {
                            field.setAccessible(true);
                            context = (Context) field.get(am);
                            Log.i(TAG, "Context found in " + cAm + " as " + field.getName());
                            break;
                        }
                    cAm = cAm.getSuperclass();
                }
                if (context == null)
                    throw new Throwable("Context not found");

                return context;
            }
        });
    }

    public void hookApplication(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        final int uid = android.os.Process.myUid();
        final boolean tiramisuOrHigher = (Build.VERSION.SDK_INT >= 33);
        // https://android.googlesource.com/platform/frameworks/base/+/169aeafb2d97b810ae123ad036d0c58336961c55%5E%21/#F1
        Class<?> at = Class.forName(tiramisuOrHigher ? "android.app.Instrumentation" : "android.app.LoadedApk", false, lpparam.classLoader);

        XposedBridge.hookAllMethods(at,
                tiramisuOrHigher ? "newApplication" : "makeApplication", new XC_MethodHook() {
                    private boolean made = false;

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            if(!made) {
                                made = true;
                                Context context = (Application) param.getResult();

                                //Check for isolate process
                                int userid = XUtil.getUserId(uid);
                                int start = XUtil.getUserUid(userid, 99000);
                                int end = XUtil.getUserUid(userid, 99999);
                                boolean isolated = (uid >= start && uid <= end);
                                if (isolated) {
                                    Log.i(TAG, "Skipping isolated " + lpparam.packageName + ":" + uid);
                                    return;
                                }

                                hookPackage(lpparam, uid, context);
                            }
                        }catch (Throwable ex) {
                            Log.e(TAG, Log.getStackTraceString(ex));
                            XposedBridge.log(ex);
                        }
                    }
                });
    }

    public static final HashMap<String, String> keys = new HashMap<>();

    public void hookPackage(final XC_LoadPackage.LoadPackageParam lpparam, int uid, final Context context) throws Throwable {

        //
        //
        //
        //
        //Havee as main param "key" then sun clas method like invoke
        //since the hook will be in a different context this can help
        //final String password
        //i hope ?

        String pName = lpparam.packageName;

        //Since we are calling let us just check caller hash
        //For functions like this just check caller hash
        //boolean protectCommunication = XLuaCall.getSettingBoolean(context, 1337, "protect", Integer.toString(pName.hashCode()));
        //if(protectCommunication) { }

        //We can make it so if a key returns from the DB
        //Again we as the caller were authed already
        //if returns from the DB then there is a protect flag and that is the key
        //simply unchecking and re-checking will reset the key
        //On System side we can init keys in cache
        //in order for the UI to do as needed we will keep cache on this context updated
        //we can also each load get keys

        final String key = UUID.randomUUID().toString();
        Log.i(TAG, "Key created! pkg=" + pName + " key=" + key);
        keys.put(pName, key);

        Collection<XLuaHook> hooks =
                XLuaQuery.getAssignments(context, pName, uid, true);

        Log.i(TAG, "pkg=" + pName + " uid=" + uid + " hooks=" + hooks.size());

        final Map<String, String> settings = XLuaQuery.getGlobalSettings(context, uid);
        settings.putAll(XLuaQuery.getSettings(context, uid, pName, true));

        List<IRandomizer> randomizers = GlobalRandoms.getRandomizers();
        for(Map.Entry<String, String> s : settings.entrySet()) {
            if(s.getValue().equalsIgnoreCase("%random%") || s.getValue().equalsIgnoreCase("%randomize%")) {
                for(IRandomizer r : randomizers) {
                    if(r.isSetting(s.getKey())) {
                        String nv = r.generateString();
                        settings.put(s.getKey(), nv);
                        break;
                    }
                }
            }
        }

        //why global settings ? I suspect if null still use ?
        //nvm global is good, as in defined for all, then ovveride if need with the putAll
        //can be 0 globals to how ever many

        //final Map<String, Integer> propSettings = MockPropConversions.toMap(XMockQuery.getModifiedProperties(context));
        //propSettings.putAll(MockPropConversions.toMap(XMockQuery.getModifiedProperties(context, uid, pName)));
        final Map<String, Integer> propSettings = MockPropConversions.toMap(XMockQuery.getModifiedProperties(context, uid, pName));
        final Map<String, String> propMaps = XMockQuery.getMockPropMapsMap(context, true, settings, false);
        //we can also lazy load it ?

        Log.i(TAG,"pkg [" + pName + "] settings=" + settings.size() + " properties=" + propSettings.size() + " prop maps=" + propMaps.size());

        Map<LuaScriptHolder, Prototype> scriptPrototype = new HashMap<>();
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

        for(final XLuaHook hook : hooks) {
            try {
                //SDK Check min & max
                if(!hook.isAvailable(pInfo.versionCode))
                    continue;

                //get time & Compile Script
                final long install = SystemClock.elapsedRealtime();
                final Prototype compiledScript = XHookUtil.compileScript(scriptPrototype, hook);

                XResolved target = XHookUtil.resolveTargetHook(context, hook);

                Log.i(TAG, "Created Target Hook For: " + target);

                if(target.isField()) {
                    final Field field = target.tryGetField(true);
                    if(field != null) {
                        try {
                            if (target.paramTypes.length > 0)  throw new NoSuchFieldException("Field with parameters");
                            long run = SystemClock.elapsedRealtime();

                            //Init lua runtime / Hook
                            LuaHookWrapper luaField = LuaHookWrapper.createField(
                                    context,
                                    hook,
                                    settings,
                                    propSettings,
                                    propMaps,
                                    compiledScript,
                                    field,
                                    key);

                            if(!luaField.isValid()) {
                                Log.w(TAG, "Skipping over Field: " + field.getName() + " because its not a AFTER function...");
                                continue;
                            }

                            // Run function
                            Varargs result = luaField.invoke();
                            report.reportUsage(hook, result, run, "after", context);
                        }catch (Exception e) {
                            report.reportFieldException(context, e, hook, field);
                        }
                    }
                }else {
                    final Member member = target.tryGetMember();
                    if(member != null) {
                        target.throwIfMismatchReturn(member);

                        XposedBridge.hookMethod(member, new XC_MethodHook() {
                            //within here set key
                            private final WeakHashMap<Thread, Globals> threadGlobals = new WeakHashMap<>();
                            //private final String SecretKey
                            //Some how in the given context grab the key ???
                            //upon first

                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                execute(param, "before");
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                execute(param, "after");
                            }

                            private void execute(MethodHookParam param, String function) {
                                long run = SystemClock.elapsedRealtime();

                                try {
                                    LuaHookWrapper luaMember;
                                    synchronized (threadGlobals) {
                                        Thread thread = Thread.currentThread();
                                        if (!threadGlobals.containsKey(thread))
                                            threadGlobals.put(thread, XHookUtil.getGlobals(
                                                    context,
                                                    hook,
                                                    settings,
                                                    propSettings,
                                                    propMaps,
                                                    key));

                                        Globals globals = threadGlobals.get(thread);

                                        // Initialize Lua runtime
                                        luaMember = LuaHookWrapper
                                                .createMember(
                                                        context,
                                                        hook,
                                                        settings,
                                                        propSettings,
                                                        propMaps,
                                                        compiledScript,
                                                        function,
                                                        param,
                                                        globals,
                                                        key);

                                        if(!luaMember.isValid())
                                            return;
                                    }

                                    // Run function
                                    Varargs result = luaMember.invoke();
                                    report.reportUsage(hook, result, run, function, context);
                                }catch (Exception ex) {
                                    synchronized (threadGlobals) { threadGlobals.remove(Thread.currentThread()); }
                                    report.reportMemberException(context, ex, hook, member, function, param);
                                }
                            }
                        });
                    }else {
                        Log.e(TAG, "Member NULL: hook=" + hook.getName() + " id=" + hook.getId());
                    }
                }
                // Report install
                if (BuildConfig.DEBUG) {
                    report.pushReport(context, hook.getId(), "none", "install",
                            BundleUtil.createSingleLong(
                                    "duration",
                                    SystemClock.elapsedRealtime() - install));
                }
            }catch (Throwable fe) {
                if (hook.isOptional() && XHookUtil.isReflectError(fe))
                    Log.i(TAG, "Optional hook=" + hook.getId() + ": " + fe.getClass().getName() + ": " + fe.getMessage());
                else {
                    Log.e(TAG, hook.getId() + ": " + Log.getStackTraceString(fe));
                    // Report install error
                    report.pushReport(context, hook.getId(), "none", "install",
                            BundleUtil.createSingleString(
                                    "exception",
                                    fe instanceof LuaError ? fe.getMessage() : Log.getStackTraceString(fe)));
                }
            }
        }
    }
}
