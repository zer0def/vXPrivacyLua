package eu.faircode.xlua.api;

import android.database.Cursor;
import android.database.MatrixCursor;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.database.DatabaseQuerySnake;

/*public class XSettingsQueryProvider {
    private static final String TAG = "XLua.XSettingsQueryProvider";

    public static Cursor getSettings(String[] selection, XDataBase db) {
        MatrixCursor result = new MatrixCursor(new String[]{"name", "value"});
        if(selection == null || selection.length == 0)
            return result;

        String packageName = selection[0];
        int uid = Integer.parseInt(selection[1]);
        int userid = XUtil.getUserId(uid);

        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create(db, XSetting.Table.name)
                .whereColumn("user", Integer.toString(userid))
                .whereColumn("category", packageName)
                .onlyReturnColumns("name", "value");

        db.readLock();
        Cursor c = snake.query();
        try {
            while (c.moveToNext())
                result.addRow(new String[]{c.getString(0), c.getString(1)});
        }finally {
            snake.clean(c);
            db.readUnlock();
        }

        return result;
    }
}*/
