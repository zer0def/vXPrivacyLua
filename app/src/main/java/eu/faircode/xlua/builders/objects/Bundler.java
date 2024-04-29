package eu.faircode.xlua.builders.objects;

import android.content.ContentValues;
import android.os.Bundle;

import org.json.JSONObject;

import eu.faircode.xlua.builders.IIOFace;
import eu.faircode.xlua.utilities.StringUtil;


//Dude we can make interface like IWriter IReader
//Lets test this Concept on Report Logger thing

public class Bundler implements IIOFace {
    public static Bundler create() { return new Bundler(); }
    public static Bundler create(Bundle b) { return new Bundler(b); }

    private boolean writeIfNull = false;
    private Bundle b;

    public Bundler() { b = new Bundle(); }
    public Bundler(Bundle b) { this.b = b; }

    public Bundler writeIfNull(boolean writeIfNull) { this.writeIfNull = writeIfNull; return this; }

    @Override
    public int getFlags() { return 0; }

    @Override
    public Bundler wString(String key, String value) { return wString(key, value, null); }
    @Override
    public Bundler wString(String key, String value, String def) {
        if(!isGoodToGo(key, value, def)) return this;
        b.putString(key, value == null ? def : value);
        return this;
    }

    @Override
    public String rString(String key) { return rString(key, null); }
    @Override
    public String rString(String key, String def) {
        if(!b.containsKey(key)) return def;
        return b.getString(key, def);
    }

    @Override
    public Bundler wLong(String key, Long value) { return wLong(key, value, null); }
    @Override
    public Bundler wLong(String key, Long value, Long def) {
        if(!isGoodToGo(key, value, def)) return this;
        b.putLong(key, value == null ? def : value);
        return this;
    }

    @Override
    public Long rLong(String key) { return rLong(key, null); }
    @Override
    public Long rLong(String key, Long def) {
        if(!b.containsKey(key)) return def;
        return b.getLong(key, def);
    }

    @Override
    public Bundler wInt(String key, Integer value) { return wInt(key, value, null); }
    @Override
    public Bundler wInt(String key, Integer value, Integer def) {
        if(!isGoodToGo(key, value, def)) return this;
        b.putInt(key, value == null ? def : value);
        return this;
    }

    @Override
    public Integer rInt(String key) { return rInt(key, null); }
    @Override
    public Integer rInt(String key, Integer def) {
        if(!b.containsKey(key)) return def;
        return b.getInt(key, def);
    }

    @Override
    public Bundler wBool(String key, Boolean value) { return wBool(key, value, null); }
    @Override
    public Bundler wBool(String key, Boolean value, Boolean def) {
        if(!isGoodToGo(key, value, def)) return this;
        b.putBoolean(key, value == null ? def : value);
        return this;
    }

    @Override
    public Boolean rBool(String key) { return rBool(key, null); }
    @Override
    public Boolean rBool(String key, Boolean def) {
        if(!b.containsKey(key)) return def;
        return b.getBoolean(key, def);
    }

    @Override
    public Bundler wBundle(String key, Bundle bundle) {
        if(!StringUtil.isValidAndNotWhitespaces(key) || bundle == null) return this;
        b.putBundle(key, bundle);
        return this;
    }

    @Override
    public Bundle rBundle(String key) {
        if(!b.containsKey(key)) return null;
        return b.getBundle(key);
    }

    @Override
    public Bundle toBundle() { return b; }

    @Override
    public JSONObject toJson() { return null; }

    @Override
    public ContentValues toContentValues() { return null; }

    private boolean isGoodToGo(String k, Object v, Object d) {
        return StringUtil.isValidAndNotWhitespaces(k)
                && ((v != null || d != null) || writeIfNull);
    }
}
