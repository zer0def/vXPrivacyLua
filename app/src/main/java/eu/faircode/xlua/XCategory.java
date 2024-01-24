package eu.faircode.xlua;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

//import eu.faircode.xlua.database.IDatabaseHelper;

/*public class XCategory implements IDatabaseHelper {
    public int userId;
    public String name;

    private List<XSettingIO> settings = new ArrayList<>();

    public XCategory(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public List<XSettingIO> getSettings(String name) {
        List<XSettingIO> sets = new ArrayList<>();
        if(settings == null || settings.isEmpty())
            return sets;

        for(XSettingIO s : settings) {
            if(s.name.equals(name)) {
                sets.add(s);
            }
        }

        return sets;
    }

    @Override
    public void readFromCursor(Cursor cursor) {
        userId = cursor.getInt(cursor.getColumnIndex("user"));
        name = cursor.getString(cursor.getColumnIndex("category"));
    }

    @Override
    public ContentValues createContentValues() {
        return null;
    }
}*/
