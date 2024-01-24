package eu.faircode.xlua.api.objects.xlua.setting;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.faircode.xlua.api.objects.IDBSerial;

public class xCategory extends xCategoryBase implements IDBSerial {
    protected Collection<xSetting> settings = new ArrayList<>();

    public xCategory() { }
    public xCategory(Integer userId, String name) { super(userId, name); }

    public Collection<xSetting> getSettings(String name) {
        Collection<xSetting> sets = new ArrayList<>();
        if(settings == null || settings.isEmpty())
            return sets;

        for(xSetting s : settings) {
            if(s.getName().equals(name)) {
                sets.add(s);
            }
        }

        return sets;
    }

    public xCategory addSetting(xSetting setting) {
        if(setting != null)
            settings.add(setting);

        return this;
    }

    public xCategory setSettings(Collection<xSetting> settings) {
        if(settings != null) this.settings = settings;
        return this;
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("user", userId);
        cv.put("name", name);
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            userId = cursor.getInt(cursor.getColumnIndex("user"));
            name = cursor.getString(cursor.getColumnIndex("category"));
        }
    }
}
