package eu.faircode.xlua.api.xlua.provider;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Binder;
import android.os.Process;
import android.util.Log;

import com.bumptech.glide.util.Util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.XGlobals;
import eu.faircode.xlua.api.XResult;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.XposedUtil;
import eu.faircode.xlua.api.app.AppPacket;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.hook.assignment.LuaAssignmentWriter;
import eu.faircode.xlua.api.settings.LuaSetting;
import eu.faircode.xlua.api.xstandard.UserIdentityPacket;
import eu.faircode.xlua.api.xstandard.database.SqlQuerySnake;
import eu.faircode.xlua.api.app.XLuaApp;
import eu.faircode.xlua.api.hook.XLuaHook;

public class XLuaAppProvider {
    private static final String TAG = "XLua.XAppProvider";
    public static int getVersion(Context context) throws Throwable {
        if (XposedUtil.isVirtualXposed()) {
            PackageInfo pi = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);
            return pi.versionCode;
        } else
            return -55;
    }

    public static boolean forceStop(Context context, String packageName, int userid) { return forceStop(context, packageName, userid, null); }
    public static boolean forceStop(Context context, String packageName, int userid, XResult res) {
        if(packageName.equalsIgnoreCase(UserIdentityPacket.GLOBAL_NAMESPACE)) {
            XResult.logError(TAG, res, "Cannot Kill Global or UID: " + userid + " pgk=" + packageName);
            return false;
        }

        Log.i(TAG, "forceStop on package=" + packageName + " userid=" + userid);
        try {
            // Access activity manager as system user
            long identity = Binder.clearCallingIdentity();
            try {
                // public void forceStopPackageAsUser(String packageName, int userId)
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                Method mForceStop = am.getClass().getMethod("forceStopPackageAsUser", String.class, int.class);
                mForceStop.invoke(am, packageName, userid);
                return true;
            } catch (Exception e) {
                XResult.logError(TAG, res, "Failed to kill user [forceStopPackageAsUser] user=" + userid + "pkg=" + packageName + " er=" + e);
                return false;
            } finally {
                Binder.restoreCallingIdentity(identity);
            }
        }catch (Exception e) {
            XResult.logError(TAG, res, "Failed to kill [forceStopPackageAsUser] user=" + userid + "pkg=" + packageName + " er=" + e);
            return false;
        }
    }


    public static XLuaApp getApp(Context context, XDatabase db, int cuid, String packageName, boolean initForceStop, boolean initSettings) {
        long identity = Binder.clearCallingIdentity();
        int userid = XUtil.getUserId(cuid);
        try {
            Context contextForUser = XUtil.createContextForUser(context, userid);
            PackageManager pm = contextForUser.getPackageManager();

            Log.i(TAG, "getApp for Package=" + packageName + " uid=" + userid + " cuid=" + cuid + " initForceStop=" + initForceStop + " initSettings=" + initSettings);

            for (ApplicationInfo ai : XposedUtil.getApplications(contextForUser))
                if (ai.packageName.equalsIgnoreCase(packageName)) {
                    try {
                        Log.i(TAG, "Found package for GetApp: " + packageName);


                        int enabledSetting = pm.getApplicationEnabledSetting(ai.packageName);
                        boolean enabled = (ai.enabled &&
                                (enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT ||
                                        enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED));
                        boolean persistent = ((ai.flags & ApplicationInfo.FLAG_PERSISTENT) != 0 ||
                                "android".equals(ai.packageName));
                        boolean system = ((ai.flags &
                                (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0);

                        Log.i(TAG, "Created the app shit almost.. " + packageName);
                        XLuaApp app = new XLuaApp();
                        app.setUid(ai.uid);
                        app.setPackageName(ai.packageName);
                        app.setIcon(ai.icon);
                        app.setLabel((String) pm.getApplicationLabel(ai));
                        app.setEnabled(enabled);
                        app.setPersistent(persistent);
                        app.setSystem(system);
                        app.setForceStop((!persistent && !system));
                        app.setAssignments(new ArrayList<LuaAssignment>());

                        Log.i(TAG, "Created XLuaApp Object pkg=" + packageName + " o=" + app);

                        final HashMap<String, XLuaApp> apps = new HashMap<>();
                        apps.put(app.getPackageName(), app);
                        if(initForceStop) initAppForceToStop(apps, db, userid);
                        if(initSettings) initAppDatabaseSettings(context, db, apps, userid);

                        Log.i(TAG, "Settings=" + app.getAssignments().size());
                        return app;
                    } catch (Throwable ex) {
                        Log.e(TAG, ex + "\n" + Log.getStackTraceString(ex));
                    }
                }else {
                    //Log.w(TAG, "Not Pkg=" + ai.packageName);
                }
        }catch (Throwable e) {
            Log.e(TAG, "Failed to get App: pkg=" + packageName + " uid=" + userid + " e=" + e + "\n" + Log.getStackTraceString(e));
        }finally {
            Binder.restoreCallingIdentity(identity);
        } return new XLuaApp();
    }

    public static Map<String, XLuaApp> getApps(Context context, XDatabase db, int cuid, boolean initForceToStop, boolean initSettings) {
        int userid = XUtil.getUserId(cuid);
        Map<String, XLuaApp> apps = new HashMap<>();

        long identity = Binder.clearCallingIdentity();
        try {
            //https://github.com/zer0def/vXPrivacyLua/commit/c030dd1880afda81b989185f8e2527c8358db799
            //For TaiChi support
            Context contextForUser = XUtil.createContextForUser(context, userid);
            PackageManager pm = contextForUser.getPackageManager();

            for (ApplicationInfo ai : XposedUtil.getApplications(contextForUser))
                if (!ai.packageName.startsWith(BuildConfig.APPLICATION_ID))
                    try {
                        int enabledSetting = pm.getApplicationEnabledSetting(ai.packageName);
                        boolean enabled = (ai.enabled &&
                                (enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT ||
                                        enabledSetting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED));
                        boolean persistent = ((ai.flags & ApplicationInfo.FLAG_PERSISTENT) != 0 ||
                                "android".equals(ai.packageName));
                        boolean system = ((ai.flags &
                                (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0);

                        XLuaApp app = new XLuaApp();
                        app.setUid(ai.uid);
                        app.setPackageName(ai.packageName);
                        app.setIcon(ai.icon);
                        app.setLabel((String) pm.getApplicationLabel(ai));
                        app.setEnabled(enabled);
                        app.setPersistent(persistent);
                        app.setSystem(system);
                        app.setForceStop((!persistent && !system));
                        app.setAssignments(new ArrayList<LuaAssignment>());
                        apps.put(app.getPackageName(), app);
                    } catch (Throwable ex) {
                        Log.e(TAG, ex + "\n" + Log.getStackTraceString(ex));
                    }
        }catch (Throwable e) {
            Log.e(TAG, "Failed to get Apps: " + e + "\n" + Log.getStackTraceString(e));
        } finally {
            Binder.restoreCallingIdentity(identity);
        }

        Log.i(TAG, "Installed apps=" + apps.size() + " userId=" + userid + " cuid=" + cuid);
        if(initForceToStop)
            initAppForceToStop(apps, db, userid);

        if(initSettings)
            initAppDatabaseSettings(context, db, apps, userid);
            //initAppDatabaseSettings(context, apps, db, userId);

        return apps;
    }

    private static void initAppForceToStop(Map<String, XLuaApp> apps, XDatabase db, int userid) {
        //direct insert here
        SqlQuerySnake snake = SqlQuerySnake
                .create(db, LuaSetting.Table.name)
                .whereColumn("user", Integer.toString(userid))
                .whereColumn("name", "'forcestop'")
                .onlyReturnColumns("category", "value");


        /*cursor = db.query(
                "setting",
                new String[]{"category", "value"},
                "user = ? AND name = 'forcestop'",
                new String[]{Integer.toString(userid)},
                null, null, null);*/

        db.readLock();
        Cursor c = snake.query();
        try {
            while (c.moveToNext()) {
                String pkg = c.getString(0);
                if (apps.containsKey(pkg)) {
                    XLuaApp app = apps.get(pkg);
                    if(app != null) {
                        app.setForceStop(Boolean.parseBoolean(c.getString(1)));
                        if(apps.size() == 1)
                            return;
                    }
                } else
                    Log.i(TAG, "Package " + pkg + " not found (force stop)");
            }
        }finally {
            snake.clean(c);
            db.readUnlock();
        }
    }

    private static void initAppDatabaseSettings(Context context, XDatabase db, Map<String, XLuaApp> apps, int userid) {
        int start = XUtil.getUserUid(userid, 0);
        int end = XUtil.getUserUid(userid, Process.LAST_APPLICATION_UID);
        SqlQuerySnake snake = SqlQuerySnake
                .create(db, LuaAssignment.Table.name)
                .onlyReturnColumns("package", "uid", "hook", "installed", "used", "restricted", "exception")
                .whereColumn("uid", start, ">=")
                .whereColumn("uid", end, "<=");

        List<String> collections = XLuaHookProvider.getCollections(context, db, userid);

        db.readLock();
        Cursor c = snake.query();
        try {
            int colPkg = c.getColumnIndex("package");
            int colUid = c.getColumnIndex("uid");
            int colHook = c.getColumnIndex("hook");
            int colInstalled = c.getColumnIndex("installed");
            int colUsed = c.getColumnIndex("used");
            int colRestricted = c.getColumnIndex("restricted");
            int colException = c.getColumnIndex("exception");

            while (c.moveToNext()) {
                String pkg = c.getString(colPkg);
                int uid = c.getInt(colUid);
                String hookId = c.getString(colHook);

                if (apps.containsKey(pkg)) {
                    XLuaApp app = apps.get(pkg);
                    if(app == null)
                        continue;

                    if (app.getUid() != uid)
                        continue;

                    XLuaHook hook = XGlobals.getHook(hookId, pkg, collections);
                    if(hook != null) {
                        LuaAssignmentWriter assignment = new LuaAssignmentWriter(hook);
                        assignment.setInstalled(c.getLong(colInstalled));
                        assignment.setUsed(c.getLong(colUsed));
                        assignment.setRestricted((c.getInt(colRestricted) == 1));
                        assignment.setException(c.getString(colException));
                        app.addAssignment(assignment);
                    }

                    //if(apps.size() == 1) {
                    //    Log.i(TAG, "Returning from Init Setting sfrom app as it is one");
                    //    return;
                    //}

                } else
                    Log.i(TAG, "Package " + pkg + " not found");
            }
        }finally {
            snake.clean(c);
            db.readUnlock();
        }
    }
}
