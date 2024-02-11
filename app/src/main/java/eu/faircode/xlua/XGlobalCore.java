package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Binder;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import eu.faircode.xlua.api.XCommandService;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;
import eu.faircode.xlua.api.objects.xmock.prop.MockProp;
import eu.faircode.xlua.api.xmock.XMockCpuDatabase;
import eu.faircode.xlua.api.xmock.XMockCpuProvider;
import eu.faircode.xlua.api.xmock.XMockDatabaseHelp;
import eu.faircode.xlua.api.xmock.XMockPropDatabase;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.api.xlua.XHookProvider;
import eu.faircode.xlua.database.XLuaUpdater;

public class XGlobalCore {
    private static final String TAG = "XLua.XGlobalCore";
    private static final Object hookLock = new Object();
    private static final Object mockLock = new Object();

    private static HashMap<String, xHook> hooks = new HashMap<>();
    private static HashMap<String, xHook> builtIn = new HashMap<>();

    //public static int version = -1;

    private static final String DB_NAME_LUA = "xlua";
    private static final String DB_NAME_MOCK = "mock";

    private static XDataBase xLua_db = null;
    private static XDataBase xMock_db = null;

    final static String cChannelName = "xlua";

    public static XDataBase getLuaDatabase(Context context) {
        checkDatabases(context);
        synchronized (hookLock) {
            return xLua_db;
        }
    }

    public static XDataBase getMockDatabase(Context context) {
        checkDatabases(context);
        synchronized (mockLock) {
            return xMock_db;
        }
    }

    private static int att = 0;

    public static void reInitDatabase(Context context) {
        synchronized (mockLock) {
            if(xMock_db != null) {
                if(!xMock_db.getPath().contains("xplex")) {
                    Log.w(TAG, "DOES NOT CONTAIN XPLEX");
                    //xLua_db.writeUnlock();
                    //xLua_db.readUnlock();
                    xMock_db.close();
                    xMock_db = null;
                    //att++;
                }
            }
        }

        synchronized (hookLock) {
            if(xLua_db != null) {
                if(!xLua_db.getPath().contains("xplex")) {
                    Log.w(TAG, "DOES NOT CONTAIN XPLEX");
                    xLua_db.close();
                    xLua_db = null;
                    hooks.clear();
                    //att++;
                }
            }
        }

        checkDatabases(context, true);
    }
    public static void checkDatabases(Context context) {
        checkDatabases(context, false);
    }

    public static void checkDatabases(Context context, boolean newDir) {
        //XSecurity.TestFunctions();
        if(DebugUtil.isDebug()) Log.i(TAG, "Checking Databases!");
        try {
            synchronized (mockLock) {
                if(xMock_db == null) {
                    Log.i(TAG, "XMock Database is null, initializing... path=");
                    xMock_db = new XDataBase(DB_NAME_MOCK, context, true);
                    XMockDatabaseHelp.initDatabase(context, xMock_db);
                }else if(DebugUtil.isDebug()) {
                    Log.i(TAG , "XMock Database is db=" + xMock_db);
                }
            }

            //add to String to xDatabase
            synchronized (hookLock) {
                if(xLua_db == null) {
                    Log.i(TAG, "XLua Database is null, initializing... path=");
                    xLua_db = new XDataBase(DB_NAME_LUA, context, true);
                    try {
                        XLuaUpdater.checkForUpdate(xLua_db);
                    }catch (Exception e) {
                        Log.e(TAG, "Failed to check for update: " + e);
                        XposedBridge.log("Failed to check for update: " + e);
                    }
                }else if(DebugUtil.isDebug())
                    Log.i(TAG , "XLua Database is not null... db=" + xLua_db);


                if (hooks == null || hooks.isEmpty()) {
                    if(!xLua_db.isOpen(true))
                        return;
                    XposedBridge.log("XLua Hook Cache is null, initializing...");
                    Log.i(TAG, "XLua Hook Cache is null, initializing...");
                    loadHooks(context);
                }else if(DebugUtil.isDebug())
                    Log.i(TAG , "XLua Hook Cache size=" + hooks.size());
            }
        }catch (Throwable e) {
            Log.e(TAG, "Failed to check Databases\n" + e + "\n" + Log.getStackTraceString(e));
        }
    }

    private static void loadHooks(Context context) throws Throwable {
        Log.i(TAG, "<loadHooks>");
        hooks = new HashMap<>();
        builtIn = new HashMap<>();

        // Read built-in definition
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = pm.getApplicationInfo(BuildConfig.APPLICATION_ID, 0);
        for (xHook builtin : xHook.readHooks(context, ai.publicSourceDir)) {
            builtin.resolveClassName(context);
            builtIn.put(builtin.getId(), builtin);
            hooks.put(builtin.getId(), builtin);
        }

        Log.i(TAG, "loaded hook size=" + hooks.size());

        DatabaseQuerySnake snake = DatabaseQuerySnake.create(xLua_db, xHook.Table.name);

        Cursor c = null;
        try {
            c = snake.query();
            xLua_db.readLock();
            //if(!xLua_db.beginTransaction()) {
            //    Log.e(TAG, "Failed to init hooks, beginTransaction failed");
            //    return;
            //}

            int colDefinition = c.getColumnIndex("definition");
            while (c.moveToNext()) {
                String definition = c.getString(colDefinition);
                xHook hook = new xHook();
                hook.fromJSONObject(new JSONObject(definition));
                hook.resolveClassName(context);
                Log.i(TAG, " loading hook=" + hook.getId());
                hooks.put(hook.getId(), hook);
            }

            //xLua_db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.e(TAG, "Failed to init hooks, e=" + e + "\n" + Log.getStackTraceString(e));
        }finally {
            //xLua_db.endTransaction();
            xLua_db.readUnlock();
            snake.clean(c);
        }

        Log.i(TAG, "Loaded hook definitions hooks=" + hooks.size() + " builtins=" + builtIn.size());
    }


    public static Collection<xHook> getHooks(XDataBase db, boolean all) {
        Log.i(TAG, "Getting Hooks all=" + all + " internal size list =" + hooks.size());
        List<String> collection = XHookProvider.getCollections(db, XUtil.getUserId(Binder.getCallingUid()));
        Log.i(TAG, "collection size=" + collection.size());
        List<xHook> hv = new ArrayList();
        synchronized (hookLock) {
            for (xHook hook : hooks.values())
                if (all || hook.isAvailable(null, collection))
                    hv.add(hook);
        }

        Collections.sort(hv, new Comparator<xHook>() {
            @Override
            public int compare(xHook h1, xHook h2) {
                return h1.getId().compareTo(h2.getId());
            }
        });

        Log.i(TAG, "Getting Hooks returning size=" + hv.size());

        return hv;
    }

    public static List<String> getGroups(XDataBase db) {
        List<String> groups = new ArrayList<>();
        List<String> collections = XHookProvider.getCollections(db, XUtil.getUserId(Binder.getCallingUid()));

        if(DebugUtil.isDebug()) {
            Log.i(TAG, "Returned Collections size=" + collections.size());
            String s = "testing collections=";
            for(String c : collections)
                s += " " + c;
            Log.i(TAG, s);
        }

        synchronized (hookLock) {
            for (xHook hook : hooks.values())
                if (hook.isAvailable(null, collections) && !groups.contains(hook.getGroup()))
                    groups.add(hook.getGroup());
        }

        Log.i(TAG, "Collection size=" + collections.size() + "  groups size=" + groups.size());

        return groups;
    }

    @SuppressLint("WrongConstant")
    public static void writeHookFromCache(
            MatrixCursor writeTo,
            String hookId,
            String used,
            String packageName,
            List<String> collections,
            boolean marshall) throws Throwable {
        synchronized (hookLock) {
            if (hooks.containsKey(hookId))  {
                xHook hook = hooks.get(hookId);
                if(hook == null)
                    return;

                if (hook.isAvailable(packageName, collections)) {
                    if (marshall) {
                        Parcel parcel = Parcel.obtain();
                        hook.writeToParcel(parcel, xHook.FLAG_WITH_LUA);
                        writeTo.newRow()
                                .add(parcel.marshall())
                                .add(used);
                        parcel.recycle();
                    } else //to not marshall use JSON
                        writeTo.newRow()
                                .add(hook.toJSON())
                                .add(used);
                }
            }else if(BuildConfig.DEBUG) {
                Log.w(TAG, "Hook " + hookId + " not found");
            }
        }
    }

    public static List<String> getHookIds(String pkg, List<String> collections) {
        List<String> hook_ids = new ArrayList<>();
        synchronized (hookLock) {
            for (xHook hook : hooks.values())
                if (hook.isAvailable(pkg, collections))
                    hook_ids.add(hook.getId());
        }

        return hook_ids;
    }

    public static xHook getHook(String hookId) {
        synchronized (hookLock) { if (hooks.containsKey(hookId)) return hooks.get(hookId); return null; }
    }

    public static xHook getHook(String hookId, String pkg, List<String> collections) {
        synchronized (hookLock) {
            if (hooks.containsKey(hookId)) {
                xHook hook = hooks.get(hookId);
                if (hook != null && hook.isAvailable(pkg, collections))
                    return hook;
            }

            return null;
        }
    }

    public static boolean updateHookCache(Context context, xHook hook, String extraId) {
        synchronized (hookLock) {
            if (hook == null) {
                if (hooks.containsKey(extraId) && hooks.get(extraId).isBuiltin()) {
                    //throw new IllegalArgumentException("builtin");
                    Log.e(TAG, "Hook id Is built in, id=" + extraId + " , Not allowed to modify built in hooks!");
                    return false;
                }

                Log.i(TAG, "Deleting hook id=" + extraId);
                hooks.remove(extraId);
                if (builtIn.containsKey(extraId)) {
                    Log.i(TAG, "Restoring builtin id=" + extraId);
                    xHook builtin = builtIn.get(extraId);
                    // class name is already resolved
                    hooks.put(extraId, builtin);
                } else
                    Log.w(TAG, "Builtin not found id=" + extraId);
            } else {
                if (!hook.isBuiltin())
                    Log.i(TAG, "Storing hook id=" + extraId);
                hook.resolveClassName(context);
                hooks.put(extraId, hook);
            }

            return true;
        }
    }
}
