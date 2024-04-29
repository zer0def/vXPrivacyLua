package eu.faircode.xlua.builders.objects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import org.json.JSONObject;

import eu.faircode.xlua.builders.IIOFace;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.StringUtil;

public class Cursorer implements IIOFace {
    public static Cursorer create(Cursor c) { return new Cursorer(c); }

    private boolean writeIfNull = false;

    public Cursorer writeIfNull(boolean writeIfNull) { this.writeIfNull = writeIfNull; return this; }

    @Override
    public int getFlags() { return 0; }

    Cursor c;
    public Cursorer(Cursor c) {
        this.c = c;
    }

    @Override
    public Cursorer wString(String key, String value) { return wString(key, value, null); }
    @Override
    public Cursorer wString(String key, String value, String def) { return this; }

    @Override
    public String rString(String key) { return rString(key, null); }
    @Override
    public String rString(String key, String def) {
        int index = c.getColumnIndex(key);
        if(index == -1) return def;
        return c.getString(index);
    }

    @Override
    public Cursorer wLong(String key, Long value) { return wLong(key, value, null); }
    @Override
    public Cursorer wLong(String key, Long value, Long def) { return this; }

    @Override
    public Long rLong(String key) { return rLong(key, null); }
    @Override
    public Long rLong(String key, Long def) {
        int index = c.getColumnIndex(key);
        if(index == -1) return def;
        return c.getLong(index);
    }

    @Override
    public Cursorer wInt(String key, Integer value) { return wInt(key, value, null); }
    @Override
    public Cursorer wInt(String key, Integer value, Integer def) { return this; }

    @Override
    public Integer rInt(String key) { return rInt(key, null); }
    @Override
    public Integer rInt(String key, Integer def) {
        int index = c.getColumnIndex(key);
        if(index == -1) return def;
        return c.getInt(index);
    }

    @Override
    public Cursorer wBool(String key, Boolean value) { return wBool(key, value, null); }
    @Override
    public Cursorer wBool(String key, Boolean value, Boolean def) { return this; }

    @Override
    public Boolean rBool(String key) { return rBool(key, null); }
    @Override
    public Boolean rBool(String key, Boolean def) {
        int index = c.getColumnIndex(key);
        if(index == -1) return def;
        return c.getInt(index) == 1;
    }

    @Override
    public Cursorer wBundle(String key, Bundle bundle) { return this; }

    @Override
    public Bundle rBundle(String key) { return null; }

    @Override
    public Bundle toBundle() { return null; }

    @Override
    public JSONObject toJson() { return null; }

    @Override
    public ContentValues toContentValues() { return null; }

    private boolean isGoodToGo(String k, Object v, Object d) {
        return StringUtil.isValidAndNotWhitespaces(k)
                && ((v != null || d != null) || writeIfNull);
    }
}
