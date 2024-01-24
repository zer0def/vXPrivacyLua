package eu.faircode.xlua.api.xlua;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.objects.xlua.hook.Assignment;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.database.DatabaseHelperEx;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.api.objects.XAssignmentDataHelper;
import eu.faircode.xlua.api.objects.XGroupDataHelper;
import eu.faircode.xlua.hooks.XReport;

public class XHookDatabase {
    private static final String TAG = "XLua.XHookDatabase";

    public static boolean updateHook(XDataBase db, xHook hook, String extraId) {
        Log.i(TAG, "updating Hook, id=" + extraId);
        if(hook == null || !hook.isBuiltin())
            return hook == null ?
                    deleteHook(db, extraId) :
                    putHook(db, hook);

        return true;
    }

    public static boolean putHook(XDataBase db, xHook hook) {
        //Make sure we do not need to prepare db what not
        return !DatabaseHelperEx.insertItem(
                db,
                xHook.Table.name,
                hook);
    }

    public static boolean deleteHook(XDataBase db, String id) {
        return !DatabaseHelperEx.deleteItem(db, DatabaseQuerySnake
                .create("hook")
                .whereColumn("id", id));
    }

    public static boolean assignHooks(Context context, List<String> hookIds, String packageName, int uid, boolean delete, boolean kill, XDataBase db) throws Throwable {
        //Assign Hook(s) to a App (package name, uid)
        List<String> groups = new ArrayList<>();
        XAssignmentDataHelper assignmentData = new XAssignmentDataHelper(packageName, uid);
        XGroupDataHelper groupData = new XGroupDataHelper(packageName, uid);

        try {
            if(!db.beginTransaction(true))
                return false;

            if(!db.hasTable(Assignment.Table.name)) {
                Log.e(TAG, "Table does not exist [" + Assignment.Table.name + "] in Database [" + db + "]");
                return false;
            }

            for(String hookId : hookIds) {
                xHook hook = XGlobalCore.getHook(hookId);

                //Add its Group to the group list
                if (hook != null && !groups.contains(hook.getGroup()))
                    groups.add(hook.getGroup());

                if(delete) {
                    Log.i(TAG, packageName + ":" + uid + "/" + hookId + " deleted");
                    if(!db.delete(Assignment.Table.name, assignmentData.getSelectionArgs(), assignmentData.createValueArgs(hookId))) {
                        Log.e(TAG, "Failed to Delete Assignment ID=" + hookId);
                        //return false;
                    }
                }else {
                    Log.i(TAG, packageName + ":" + uid + "/" + hookId + " added");
                    if(!db.insert(Assignment.Table.name, assignmentData.createContentValues(hookId))) {
                        Log.e(TAG, "Failed to Insert Assignment ID=" + hookId);
                        //return false , keep going ???
                    }
                }
            }

            if (!delete)
                for (String group : groups) {
                    if(!db.delete("`group`", groupData.getSelectionArgs(), groupData.createValueArgs(group))) {
                        Log.e(TAG, "Failed to Delete Group=" + group);
                        //return false;
                    }
                }

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction(true, false);
        }

        if (kill)
            XAppProvider.forceStop(context, packageName, XUtil.getUserId(uid));

        return true;
    }

    public static long report(XReport report, xHook hook, XDataBase db) {
        Log.i(TAG , "Updating Assignment: " + report);

        //Update Assignment , make it a function ?
        long used = -1;
        if(!DatabaseHelperEx.updateItem(db, Assignment.Table.name, report.generateQuery(), report))
            Log.w(TAG, "Error updating Assignment: " + report);

        //Update Group
        if(hook != null && report.event.equals("use") && report.getRestricted() == 1 && report.getNotify(db)) {
            used = DatabaseQuerySnake
                    .create(db , "`group`")
                    .whereColumns("package", "uid", "name")
                    .whereColumnValues(report.packageName, Integer.toString(report.uid), hook.getGroup())
                    .queryGetFirstLong("used", true);

            //ahh fix this ugly code
            //dupes too many standardize query snake
            if(!DatabaseHelperEx.insertItem(db, "`group`",  report.createGroupObject(hook, used)))
                Log.e(TAG, "Error inserting group: " + report);
        }

        return used;
    }
}
