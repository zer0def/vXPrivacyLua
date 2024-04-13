package eu.faircode.xlua.api.hook.group;

import android.content.ContentValues;

import eu.faircode.xlua.api.xstandard.database.SqlQuerySnake;
import eu.faircode.xlua.api.xstandard.interfaces.IDataDynamic;

public class XLuaGroupDataHelper implements IDataDynamic {
    private final String packageName;
    private final int uid;
    private final String uidString;

    private SqlQuerySnake snake = null;

    public XLuaGroupDataHelper(String packageName, int uid) {
        this.packageName = packageName;
        this.uid = uid;
        this.uidString = Integer.toString(uid);
        snake = SqlQuerySnake.create()
                .whereColumns("package", "uid", "name");
    }


    @Override
    public String getSelectionArgs() {
        return snake.getSelectionArgs();
    }

    @Override
    public String[] createValueArgs(String replaceValue) {
        return new String[] { packageName, uidString, replaceValue };
    }

    @Override
    public ContentValues createContentValues(String replaceValue) {
        ContentValues cv = new ContentValues();
        cv.put("package", packageName);
        cv.put("uid", uidString);
        cv.put("name", replaceValue);
        cv.put("used", -1);
        return cv;
    }
}
