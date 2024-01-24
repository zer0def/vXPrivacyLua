package eu.faircode.xlua.api.xlua;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Binder;
import android.os.Process;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.api.objects.xlua.setting.xSetting;
import eu.faircode.xlua.api.objects.xlua.setting.xSettingConversions;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.XposedUtil;
import eu.faircode.xlua.api.objects.xlua.hook.Assignment;
import eu.faircode.xlua.api.objects.xlua.hook.AssignmentWriter;
import eu.faircode.xlua.api.objects.xlua.packets.HookPacket;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.api.objects.xlua.app.xApp;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;

public class XAppProvider {
    private static final String TAG = "XLua.XAppProvider";

    public static int getVersion(Context context) throws Throwable {
        if (XposedUtil.isVirtualXposed()) {
            PackageInfo pi = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);
            return pi.versionCode;
        } else
            return -55;
    }

    public static void forceStop(Context context, String packageName, int userid) throws Throwable {
        Log.i(TAG, "forceStop on package=" + packageName + " userid=" + userid);
        // Access activity manager as system user
        long identity = Binder.clearCallingIdentity();
        try {
            // public void forceStopPackageAsUser(String packageName, int userId)
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Method mForceStop = am.getClass().getMethod("forceStopPackageAsUser", String.class, int.class);
            mForceStop.invoke(am, packageName, userid);
        } finally {
            Binder.restoreCallingIdentity(identity);
        }
    }

    public static Map<String, xApp> getApps(Context context, int userId, XDataBase db, boolean initForceToStop, boolean initSettings) {
        Map<String, xApp> apps = new HashMap<>();

        long identity = Binder.clearCallingIdentity();
        try {
            //https://github.com/zer0def/vXPrivacyLua/commit/c030dd1880afda81b989185f8e2527c8358db799
            //For TaiChi support
            Context contextForUser = XUtil.createContextForUser(context, userId);
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

                        xApp app = new xApp();
                        app.setUid(ai.uid);
                        app.setPackageName(ai.packageName);
                        app.setIcon(ai.icon);
                        app.setLabel((String) pm.getApplicationLabel(ai));
                        app.setEnabled(enabled);
                        app.setPersistent(persistent);
                        app.setSystem(system);
                        app.setForceStop((!persistent && !system));
                        app.setAssignments(new ArrayList<Assignment>());
                        apps.put(app.getPackageName(), app);
                    } catch (Throwable ex) {
                        Log.e(TAG, ex + "\n" + Log.getStackTraceString(ex));
                    }
        }catch (Throwable e) {
            Log.e(TAG, "Failed to get Apps: " + e + "\n" + Log.getStackTraceString(e));
        } finally {
            Binder.restoreCallingIdentity(identity);
        }

        Log.i(TAG, "Installed apps=" + apps.size() + " userId=" + userId);
        if(initForceToStop)
            initAppForceToStop(apps, db, userId);

        if(initSettings)
            initAppDatabaseSettings(apps, db, userId);
            //initAppDatabaseSettings(context, apps, db, userId);

        return apps;
    }

    private static void initAppForceToStop(Map<String, xApp> apps, XDataBase db, int userid) {
        //direct insert here
        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create(db, xSetting.Table.name)
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
                    xApp app = apps.get(pkg);
                    if(app != null)
                        app.setForceStop(Boolean.parseBoolean(c.getString(1)));
                } else
                    Log.i(TAG, "Package " + pkg + " not found (force stop)");
            }
        }finally {
            snake.clean(c);
            db.readUnlock();
        }
    }

    private static void initAppDatabaseSettings(Map<String, xApp> apps, XDataBase db, int userid) {
        int start = XUtil.getUserUid(userid, 0);
        int end = XUtil.getUserUid(userid, Process.LAST_APPLICATION_UID);
        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create(db, Assignment.Table.name)
                .onlyReturnColumns("package", "uid", "hook", "installed", "used", "restricted", "exception")
                .whereColumn("uid", start, ">=")
                .whereColumn("uid", end, "<=");

        List<String> collections = XHookProvider.getCollections(db, userid);

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
                    xApp app = apps.get(pkg);
                    if(app == null)
                        continue;

                    if (app.getUid() != uid)
                        continue;

                    xHook hook = XGlobalCore.getHook(hookId, pkg, collections);
                    if(hook != null) {
                        AssignmentWriter assignment = new AssignmentWriter(hook);
                        assignment.setInstalled(c.getLong(colInstalled));
                        assignment.setUsed(c.getLong(colUsed));
                        assignment.setRestricted((c.getInt(colRestricted) == 1));
                        assignment.setException(c.getString(colException));
                        app.addAssignment(assignment);
                    }
                } else
                    Log.i(TAG, "Package " + pkg + " not found");
            }
        }finally {
            snake.clean(c);
            db.readUnlock();
        }
    }
}
