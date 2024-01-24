package eu.faircode.xlua.api.objects.xlua.hook;

import android.content.ContentValues;
import android.database.Cursor;

import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.utilities.CursorUtil;

public class GroupDatabaseEntry extends GroupDatabaseBase implements IDBSerial {
    public GroupDatabaseEntry() {  }
    public GroupDatabaseEntry(String packageName, Integer uid, String name, Long used) { super(packageName, uid, name, used); }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("package", packageName);
        cv.put("uid", uid);
        cv.put("name", name);
        cv.put("used", used);
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        this.packageName = CursorUtil.getString(cursor, "package");
        this.uid = CursorUtil.getInteger(cursor, "uid");
        this.name = CursorUtil.getString(cursor, "name");
        this.used = CursorUtil.getLong(cursor, "used");
    }
}
