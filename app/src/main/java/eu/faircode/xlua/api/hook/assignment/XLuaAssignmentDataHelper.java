package eu.faircode.xlua.api.hook.assignment;

//Or have this as the 'base' / low level packet ?
//Stores nothing but data ..
//No then defeats the purpose as this object allows to modify the fields
//But then it makes alot of noise / copies and is just messy when not wanted to use
//Tho not forced then just ungly ??? lets tyr it


//
//Hmm let it cook
//

import android.content.ContentValues;

import eu.faircode.xlua.api.xstandard.database.SqlQuerySnake;
import eu.faircode.xlua.api.xstandard.interfaces.IDataDynamic;

public class XLuaAssignmentDataHelper implements IDataDynamic {
    private String packageName;
    private int uid;
    private String uidString = null;

    private SqlQuerySnake snake = null;

    public XLuaAssignmentDataHelper(String packageName, int uid) {
        this.packageName = packageName;
        this.uid = uid;
        this.uidString = Integer.toString(uid);
        snake = SqlQuerySnake.create()
                .whereColumns("hook", "package", "uid");
    }

    @Override
    public String getSelectionArgs() {
        return snake.getSelectionArgs();
    }

    @Override
    public String[] createValueArgs(String replaceValue) {
        return new String[] { replaceValue, packageName,  uidString };
    }

    @Override
    public ContentValues createContentValues(String replaceValue) {
        ContentValues cv = new ContentValues();
        cv.put("package", packageName);
        cv.put("uid", uid);
        cv.put("hook", replaceValue);

        //Default values
        cv.put("installed", -1);
        cv.put("used", -1);
        cv.put("restricted", 0);
        cv.putNull("exception");
        return cv;
    }
}
