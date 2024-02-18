package eu.faircode.xlua.api.xlua.database;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.List;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.XGlobals;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.app.LuaSimplePacket;
import eu.faircode.xlua.api.hook.assignment.XLuaAssignmentDataHelper;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.settings.LuaSetting;
import eu.faircode.xlua.api.xlua.provider.XLuaAppProvider;
import eu.faircode.xlua.api.xlua.provider.XLuaHookProvider;
import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;

public class LuaAppDatabase {
    private static final String TAG = "XLua.XAppDatabase";

    public static boolean initAppAssignments(Context context, XDatabase db, Integer user, String category, Boolean kill) { return initAppAssignments(context, db, LuaSimplePacket.create(user, category, kill)); }
    public static boolean initAppAssignments(Context context, XDatabase db, LuaSimplePacket packet) {
        packet.resolveUserID();
        List<String> collection = XLuaHookProvider.getCollections(context, db, packet.getUser());
        List<String> hookIds = XGlobals.getHookIds(packet.getCategory(), collection);
        XLuaAssignmentDataHelper assignmentData = new XLuaAssignmentDataHelper(packet.getCategory(), packet.getOriginalUser());

        try {
            if(!db.beginTransaction(true))
                return false;

            for(String hookId : hookIds) {
                if(!db.insert(LuaAssignment.Table.name, assignmentData.createContentValues(hookId))) {
                    Log.e(TAG, "Error Inserting Assignment , hookId=" + hookId);
                    //throw new Throwable("Error inserting assignment");
                }
            }

            db.setTransactionSuccessful();
        }finally {
            db.endTransaction(true, false);
        }

        if (packet.isKill())
            XLuaAppProvider.forceStop(context, packet.getCategory(), packet.getUser());

        Log.i(TAG, "Init app pkg=" + packet.getCategory() + " uid=" + packet.getUser() + " assignments=" + hookIds.size());
        return true;
    }

    public static boolean clearApp(Context context, XDatabase db, Integer user, String category, Boolean kill, Boolean deleteAllData) { return clearApp(context, db, LuaSimplePacket.create(user, category, kill, deleteAllData)); }
    public static boolean clearApp(Context context, XDatabase db, LuaSimplePacket packet)  {
        packet.resolveUserID();
        if(!DatabaseHelp.deleteItem(packet.createUserQuery(db, LuaAssignment.Table.name)))
            return false;

        if(packet.isDeleteFullData())
            if(!DatabaseHelp.deleteItem(packet.createUserQuery(db, LuaSetting.Table.name)))
                return false;

        if (packet.isKill())
            XLuaAppProvider.forceStop(context, packet.getCategory(), packet.getUser());

        return true;
    }

    public static boolean clearData(int userid, XDatabase db)  {
        Log.i(TAG, "Clearing data user=" + userid);

        boolean result;
        if(userid == 0) {
            //if 0 drop the whole table
            db.beginTransaction(true);
            result = db.delete(LuaAssignment.Table.name) && db.delete(LuaSetting.Table.name);
            db.endTransaction(true, true);
        }else {
            int start = XUtil.getUserUid(userid, 0);
            int end = XUtil.getUserUid(userid, Process.LAST_APPLICATION_UID);

            result = DatabaseHelp.deleteItem(
                    SqlQuerySnake
                            .create(db, LuaAssignment.Table.name)
                            .whereColumn("uid", start, ">=")
                            .whereColumn("uid", end, "<="));

            result = result && DatabaseHelp.deleteItem(
                    SqlQuerySnake
                            .create(db, LuaSetting.Table.name)
                            .whereColumn("user", userid));
        }

        return result;
    }
}
