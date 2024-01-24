package eu.faircode.xlua.api.xlua;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.List;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.api.objects.xlua.setting.xSetting;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.objects.XAssignmentDataHelper;
import eu.faircode.xlua.api.objects.xlua.hook.Assignment;
import eu.faircode.xlua.database.DatabaseHelperEx;
import eu.faircode.xlua.database.DatabaseQuerySnake;

public class XAppDatabase {
    private static final String TAG = "XLua.XAppDatabase";

    public static boolean initAppAssignments(
            Context context,
            String packageName,
            int uid,
            boolean kill,
            XDataBase db) throws Throwable {

        int userid = XUtil.getUserId(uid);
        List<String> collection = XHookProvider.getCollections(db, userid);
        List<String> hookIds = XGlobalCore.getHookIds(packageName, collection);
        XAssignmentDataHelper assignmentData = new XAssignmentDataHelper(packageName, uid);

        try {
            if(!db.beginTransaction(true))
                return false;

            for(String hookId : hookIds) {
                if(!db.insert(Assignment.Table.name, assignmentData.createContentValues(hookId))) {
                    Log.e(TAG, "Error Inserting Assignment , hookId=" + hookId);
                    //throw new Throwable("Error inserting assignment");
                }
            }

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction(true, false);
        }

        if (kill)
            XAppProvider.forceStop(context, packageName, userid);

        Log.i(TAG, "Init app pkg=" + packageName + " uid=" + uid + " assignments=" + hookIds.size());
        return true;
    }

    public static boolean clearApp(
            Context context,
            String packageName,
            int uid,
            boolean full,
            boolean kill,
            XDataBase db) throws Throwable {

        int userid = XUtil.getUserId(uid);
        DatabaseQuerySnake assSnake = DatabaseQuerySnake
                .create()
                .whereColumn("package", packageName)
                .whereColumn("uid", uid);

        try {
            if(!db.beginTransaction(true))
                return false;

            if(!db.delete(Assignment.Table.name, assSnake.getSelectionArgs(), assSnake.getSelectionCompareValues()))
                return false;

            if(full) {
                DatabaseQuerySnake setSnake = DatabaseQuerySnake
                        .create()
                        .whereColumn("user", userid)
                        .whereColumn("category", packageName);

                if(!db.delete(xSetting.Table.name, setSnake.getSelectionArgs(), setSnake.getSelectionCompareValues()))
                    return false;
            }

            if (kill)
                XAppProvider.forceStop(context, packageName, userid);

            db.setTransactionSuccessful();
            return true;
        }finally {
            db.endTransaction(true, false);
        }
    }

    public static boolean clearData(int userid, XDataBase db)  {
        Log.i(TAG, "Clearing data user=" + userid);

        boolean result;

        if(userid == 0) {
            db.beginTransaction(true);
            result = db.delete(Assignment.Table.name) && db.delete(xSetting.Table.name);
            db.endTransaction(true, true);
        }else {
            int start = XUtil.getUserUid(userid, 0);
            int end = XUtil.getUserUid(userid, Process.LAST_APPLICATION_UID);

            result = DatabaseHelperEx.deleteItem(
                    DatabaseQuerySnake
                            .create(db, Assignment.Table.name)
                            .whereColumn("uid", start, ">=")
                            .whereColumn("uid", end, "<="));

            result = result && DatabaseHelperEx.deleteItem(
                    DatabaseQuerySnake
                            .create(db, xSetting.Table.name)
                            .whereColumn("user", userid));
        }

        return result;
    }
}
