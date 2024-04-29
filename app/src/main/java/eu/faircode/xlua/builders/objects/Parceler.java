package eu.faircode.xlua.builders.objects;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Parcel;

import org.json.JSONObject;

import eu.faircode.xlua.builders.IIOFace;
import eu.faircode.xlua.utilities.ParcelUtil;
import eu.faircode.xlua.utilities.StringUtil;

public class Parceler implements IIOFace {
    public static Parceler create(Parcel p) { return new Parceler(p, 0); }
    public static Parceler create(Parcel p, int flags) { return new Parceler(p, flags); }

    private int flags;
    private Parcel p;
    private String nullFiller;
    private boolean writeIfNull = false;

    public Parceler writeIfNull(boolean writeIfNull) { this.writeIfNull = writeIfNull; return this; }
    public Parceler setStringFiller(String filler) { this.nullFiller = filler; return this; }
    public int getFlags() { return flags; }

    public Parceler(Parcel p) { this(p, 0); }
    public Parceler(Parcel p, int flags) {
        this.p = p;
        this.flags = flags;
    }

    @Override
    public Parceler wString(String key, String value) { return wString(key, value, null); }
    @Override
    public Parceler wString(String key, String value, String def) {
        if(!isGoodToGo(value, def)) return this;
        this.p.writeString(value != null ? value : def == null ? nullFiller : def);
        return this;
    }

    @Override
    public String rString(String key) { return rString(key, null); }
    @Override
    public String rString(String key, String def) { return ParcelUtil.readString(p, def, nullFiller); }

    @Override
    public Parceler wLong(String key, Long value) { return wLong(key, value, null); }
    @Override
    public Parceler wLong(String key, Long value, Long def) {
        if(!isGoodToGo(value, def)) return this;
        p.writeLong(value != null ? value : def == null ? 0 : def);
        return this;
    }

    @Override
    public Long rLong(String key) { return rLong(key, null); }
    @Override
    public Long rLong(String key, Long def) { return p.readLong(); }

    @Override
    public Parceler wInt(String key, Integer value) { return wInt(key, value, null); }
    @Override
    public Parceler wInt(String key, Integer value, Integer def) {
        if(!isGoodToGo(value, def)) return this;
        p.writeInt(value == null ? def == null ? 0 : def : value);
        return this;
    }

    @Override
    public Integer rInt(String key) { return rInt(key, null); }
    @Override
    public Integer rInt(String key, Integer def) { return p.readInt(); }

    @Override
    public Parceler wBool(String key, Boolean value) { return wBool(key, value, null); }
    @Override
    public Parceler wBool(String key, Boolean value, Boolean def) {
        if(!isGoodToGo(value, def)) return this;
        p.writeInt(value == null ? def == null ? 0 : def ? 1 : 0 : value ? 1 : 0);
        return this;
    }

    @Override
    public Boolean rBool(String key) { return rBool(key, null); }
    @Override
    public Boolean rBool(String key, Boolean def) { return p.readInt() == 1; }

    @Override
    public Bundle rBundle(String key) { return p.readBundle(); }

    @Override
    public Parceler wBundle(String key, Bundle bundle) { p.writeBundle(bundle); return this; }

    @Override
    public Bundle toBundle() { return null; }

    @Override
    public JSONObject toJson() { return null; }

    @Override
    public ContentValues toContentValues() { return null; }

    private boolean isGoodToGo(Object v, Object d) { return ((v != null || d != null) || writeIfNull); }
}
