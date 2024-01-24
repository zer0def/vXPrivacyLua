package eu.faircode.xlua.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Binder;
import android.os.Process;

import java.util.List;
import java.util.Map;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.XSecurity;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.utilities.CursorUtil;


/*public class XHookQueryProvider {
    private static final String TAG = "XLua.XHookQueryProvider";

    @SuppressLint("WrongConstant")
    public static Cursor getHooks(Context context, String[] selection, boolean marshall, XDataBase db) throws Throwable {
        boolean all = (selection != null && selection.length == 1 && "all".equals(selection[0]));
        return CursorUtil.toMatrixCursor(
                XGlobalCore.getHooks(context, db, all),
                marshall,
                XHook.FLAG_WITH_LUA);
    }


    public static Cursor getAssignedHooks(Context context, String[] selection, boolean marshall, XDataBase db) throws Throwable {
        MatrixCursor result = new MatrixCursor(new String[]{marshall ? "blob" : "json", "used"});
        if(selection == null || selection.length == 0)
            return result;

        String packageName = selection[0];
        int uid = Integer.parseInt(selection[1]);

        List<String> collection = XHookProvider.getCollections(db, XUtil.getUserId(uid));
        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create(db, XAssignmentIO.Table.name)
                .whereColumn("package", packageName)
                .whereColumn("uid", uid)
                .onlyReturnColumns("hook", "used")
                .orderBy("hook");

        db.readLock();
        Cursor c = snake.query();
        try {
            int colHook = c.getColumnIndex("hook");
            int colUsed = c.getColumnIndex("used");
            while (c.moveToNext()) {
                String hook_id = c.getString(colHook);
                String used = c.getString(colUsed);
                XGlobalCore.writeHookFromCache(result, hook_id, used, packageName, collection, marshall);
            }
        }finally {
            snake.clean(c);
            db.readUnlock();
        }

        return result;
    }

    public static Cursor getLog(Context context, String[] selection, XDataBase db) {
        XSecurity.checkCaller(context);

        if (selection != null)
            throw new IllegalArgumentException("selection invalid");

        int userid = XUtil.getUserId(Binder.getCallingUid());
        int start = XUtil.getUserUid(userid, 0);
        int end = XUtil.getUserUid(userid, Process.LAST_APPLICATION_UID);

        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create(db, XAssignmentIO.Table.name)
                .onlyReturnColumns("package", "uid", "hook", "used", "old", "new")
                .whereColumn("restricted", "1")
                .whereColumn("uid", start, ">=")
                .whereColumn("uid", end, "<=")
                .orderBy("used DESC");

        db.readLock();
        Cursor c = snake.query();
        snake.clean(null);
        db.readUnlock();
        return c;
    }
}*/
