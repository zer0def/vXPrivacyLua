package eu.faircode.xlua.builders.objects;

import android.content.ContentValues;
import android.os.Bundle;

import org.json.JSONObject;

import eu.faircode.xlua.builders.IIOFace;
import eu.faircode.xlua.utilities.StringUtil;

public class Contenter implements IIOFace {
    public static Contenter create() { return new Contenter(); }
    public static Contenter create(ContentValues c) { return new Contenter(c); }

    private ContentValues c;

    private boolean writeIfNull = false;
    public Contenter writeIfNull(boolean writeIfNull) { this.writeIfNull = writeIfNull; return this; }

    @Override
    public int getFlags() {
        return 0;
    }

    public Contenter() { this(new ContentValues()); }
    public Contenter(ContentValues c) { this.c = c; }

    @Override
    public Contenter wString(String key, String value) { return wString(key, value, null); }
    @Override
    public Contenter wString(String key, String value, String def) {
        if(!isGoodToGo(key, value, def)) return this;
        c.put(key, value == null ? def : value);
        return this;
    }

    @Override
    public String rString(String key) { return rString(key, null); }
    @Override
    public String rString(String key, String def) {
        if(!c.containsKey(key)) return def;
        return c.getAsString(key);
    }

    @Override
    public Contenter wLong(String key, Long value) { return wLong(key, value, null); }
    @Override
    public Contenter wLong(String key, Long value, Long def) {
        if(!isGoodToGo(key, value, def)) return this;
        c.put(key, value == null ? def : value);
        return this;
    }

    @Override
    public Long rLong(String key) { return rLong(key, null); }
    @Override
    public Long rLong(String key, Long def) {
        if(!c.containsKey(key)) return def;
        return c.getAsLong(key);
    }

    @Override
    public Contenter wInt(String key, Integer value) { return wInt(key, value, null); }
    @Override
    public Contenter wInt(String key, Integer value, Integer def) {
        if(!isGoodToGo(key, value, def)) return this;
        c.put(key, value == null ? def : value);
        return this;
    }

    @Override
    public Integer rInt(String key) { return rInt(key, null); }
    @Override
    public Integer rInt(String key, Integer def) {
        if(!c.containsKey(key)) return def;
        return c.getAsInteger(key);
    }

    @Override
    public Contenter wBool(String key, Boolean value) { return wBool(key, value, null); }
    @Override
    public Contenter wBool(String key, Boolean value, Boolean def) {
        if(!isGoodToGo(key, value, def)) return this;
        c.put(key, value == null ? def : value);
        return this;
    }

    @Override
    public Boolean rBool(String key) { return rBool(key, null); }
    @Override
    public Boolean rBool(String key, Boolean def) {
        if(!c.containsKey(key)) return def;
        return c.getAsBoolean(key);
    }

    @Override
    public Contenter wBundle(String key, Bundle bundle) { return this; }

    @Override
    public Bundle rBundle(String key) { return null; }

    @Override
    public Bundle toBundle() { return null; }

    @Override
    public JSONObject toJson() { return null; }

    @Override
    public ContentValues toContentValues() { return c; }

    private boolean isGoodToGo(String k, Object v, Object d) {
        return StringUtil.isValidAndNotWhitespaces(k)
                && ((v != null || d != null) || writeIfNull);
    }
}
