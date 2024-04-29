package eu.faircode.xlua.builders.objects;

import android.content.ContentValues;
import android.os.Bundle;

import org.json.JSONObject;

import eu.faircode.xlua.builders.IIOFace;
import eu.faircode.xlua.utilities.StringUtil;

public class Jsoner implements IIOFace {
    public static Jsoner create() { return new Jsoner(); }
    public static Jsoner create(JSONObject j) { return new Jsoner(j); }

    private JSONObject j;

    private boolean writeIfNull = false;
    public Jsoner writeIfNull(boolean writeIfNull) { this.writeIfNull = writeIfNull; return this; }

    @Override
    public int getFlags() { return 0; }

    public Jsoner() { this(new JSONObject()); }
    public Jsoner(JSONObject j) {
        this.j = j;
    }

    @Override
    public Jsoner wString(String key, String value) { return wString(key, value, null); }
    @Override
    public Jsoner wString(String key, String value, String def) {
        if(!isGoodToGo(key, value, def)) return this;
        try { j.put(key, value == null ? def : value);
        }catch (Exception ignored) { }
        return this;
    }

    @Override
    public String rString(String key) { return rString(key, null); }
    @Override
    public String rString(String key, String def) {
        try { return j.getString(key);
        }catch (Exception ignored) { return def; }
    }

    @Override
    public Jsoner wLong(String key, Long value) { return wLong(key, value, null); }
    @Override
    public Jsoner wLong(String key, Long value, Long def) {
        if(!isGoodToGo(key, value, def)) return this;
        try { j.put(key, value == null ? def : value);
        }catch (Exception ignored) { }
        return this;
    }

    @Override
    public Long rLong(String key) { return rLong(key, null); }
    @Override
    public Long rLong(String key, Long def) {
        try { return j.getLong(key);
        }catch (Exception ignored) { return def; }
    }

    @Override
    public Jsoner wInt(String key, Integer value) { return wInt(key, value, null); }
    @Override
    public Jsoner wInt(String key, Integer value, Integer def) {
        if(!isGoodToGo(key, value, def)) return this;
        try { j.put(key, value == null ? def : value);
        }catch (Exception ignored) { }
        return this;
    }

    @Override
    public Integer rInt(String key) { return rInt(key, null); }
    @Override
    public Integer rInt(String key, Integer def) {
        try { return j.getInt(key);
        }catch (Exception ignored) { return def; }
    }

    @Override
    public Jsoner wBool(String key, Boolean value) { return wBool(key, value, null); }
    @Override
    public Jsoner wBool(String key, Boolean value, Boolean def) {
        if(!isGoodToGo(key, value, def)) return this;
        try { j.put(key, value == null ? def : value);
        }catch (Exception ignored) { }
        return this;
    }

    @Override
    public Boolean rBool(String key) { return rBool(key, null); }
    @Override
    public Boolean rBool(String key, Boolean def) {
        try {  return j.getBoolean(key);
        }catch (Exception ignored) { return def; }
    }

    @Override
    public Jsoner wBundle(String key, Bundle bundle) { return this; }

    @Override
    public Bundle rBundle(String key) { return null; }

    @Override
    public Bundle toBundle() { return null; }

    @Override
    public JSONObject toJson() { return j; }

    @Override
    public ContentValues toContentValues() { return null; }

    private boolean isGoodToGo(String k, Object v, Object d) {
        return StringUtil.isValidAndNotWhitespaces(k)
                && ((v != null || d != null) || writeIfNull);
    }
}
