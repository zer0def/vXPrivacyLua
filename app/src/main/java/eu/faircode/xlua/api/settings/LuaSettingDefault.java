package eu.faircode.xlua.api.settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import eu.faircode.xlua.api.standard.interfaces.IJsonSerial;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.JSONUtil;
import eu.faircode.xlua.utilities.ParcelUtil;
import eu.faircode.xlua.utilities.StringUtil;

public class LuaSettingDefault extends LuaSetting implements IJsonSerial, Parcelable {
    public static LuaSettingDefault create() { return new LuaSettingPacket(); }
    public static LuaSettingDefault create(String name, String defaultValue, String description) { return new LuaSettingDefault(name, defaultValue, description); }
    public static LuaSettingDefault create(LuaSetting setting) { return new LuaSettingDefault(setting); }
    public static LuaSettingDefault create(LuaSetting setting, String description) { return new LuaSettingDefault(setting, description); }
    public static LuaSettingDefault create(LuaSetting setting, String defaultValue, String description) { return new LuaSettingDefault(setting, defaultValue, description); }
    public static LuaSettingDefault create(LuaSettingPacket packet) { return new LuaSettingDefault(packet); }
    public static LuaSettingDefault create(LuaSettingExtended extended) { return new LuaSettingDefault(extended); }

    protected String description;
    protected String defaultValue;

    public LuaSettingDefault(LuaSetting setting) { this(setting.getName(), setting.getValue(), null); }
    public LuaSettingDefault(LuaSetting setting, String description) { this(setting.getName(), setting.getValue(), description); }
    public LuaSettingDefault(LuaSetting setting, String defaultValue, String description) { this(setting.getName(), defaultValue, description); }
    public LuaSettingDefault(LuaSettingPacket packet) { this(packet.getName(), packet.getDefaultValue(true), packet.getDescription()); }
    public LuaSettingDefault(LuaSettingExtended extended) { this(extended.getName(), extended.getDefaultValue(true), extended.getDescription()); }

    public LuaSettingDefault() {  enforceDefaultStructure(); }
    public LuaSettingDefault(Parcel in) {  this(); fromParcel(in); }
    public LuaSettingDefault(String name, String defaultValue, String description) {
        this();
        setName(name);
        setDefaultValue(defaultValue);
        setDescription(description);
    }

    public void enforceDefaultStructure() { setUseUserIdentity(false); setValueForce(null); }

    public String getDescription() { return this.description; }
    public LuaSettingDefault setDescription(String description) { if(description != null) this.description = description; return this; }

    public String getDefaultValue() { return getDefaultValue(false); }
    public String getDefaultValue(boolean useValueIfDefIsNull) { return useValueIfDefIsNull ? !StringUtil.isValidString(this.defaultValue) ? this.value : this.defaultValue : this.defaultValue; }
    public LuaSettingDefault setDefaultValue(String defaultValue) { if(defaultValue != null) this.defaultValue = defaultValue; return this; }

    public LuaSetting createSetting() { return LuaSetting.create(this); }
    public LuaSettingExtended createExtended() { return LuaSettingExtended.create(this);  }
    public LuaSettingExtended createExtended(Integer user, String category) { return LuaSettingExtended.create(this, user, category, getDefaultValue(true));  }
    public LuaSettingExtended createExtended(Integer user, String category, String value) { return LuaSettingExtended.create(this, user, category, value);  }

    public LuaSettingPacket createPacket() { return LuaSettingPacket.create(this);  }
    public LuaSettingPacket createPacket(Integer code) { return LuaSettingPacket.create(this, code);  }
    public LuaSettingPacket createPacket(Integer code, Boolean kill) { return LuaSettingPacket.create(this, code, kill);  }

    @Override
    public List<ContentValues> createContentValuesList() { return null; }

    @Override
    public void fromContentValuesList(List<ContentValues> contentValues) { }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = super.createContentValues();
        if(this.description != null) cv.put("description", this.description);
        if(this.defaultValue != null) cv.put("defaultValue", this.defaultValue);
        return cv;
    }

    @Override
    public void fromContentValues(ContentValues contentValue) {
        if (contentValue != null) {
            super.fromContentValues(contentValue);
            this.description = contentValue.getAsString("description");
            this.defaultValue = contentValue.getAsString("defaultValue");
        }
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            super.fromCursor(cursor);
            this.description = CursorUtil.getString(cursor, "description");
            this.defaultValue = CursorUtil.getString(cursor, "defaultValue");
        }
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = super.toJSONObject();
        if(this.description != null) jRoot.put("description", this.description);
        if(this.defaultValue != null) jRoot.put("defaultValue", this.description);
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        if(obj != null) {
            super.fromJSONObject(obj);
            this.description = JSONUtil.getString(obj,"description");
            this.defaultValue = JSONUtil.getString(obj,"defaultValue");
        }
    }

    @Override
    public Bundle toBundle() {
        Bundle b = super.toBundle();
        if(this.description != null) b.putString("description", this.description);
        if(this.defaultValue != null) b.putString("defaultValue", this.defaultValue);
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        if(bundle != null) {
            super.fromBundle(bundle);
            this.description = BundleUtil.readString(bundle, "description");
            this.defaultValue = BundleUtil.readString(bundle, "defaultValue");
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        if(in != null) {
            super.fromParcel(in);
            this.description = ParcelUtil.readString(in, null, ParcelUtil.IGNORE_VALUE);
            this.defaultValue = ParcelUtil.readString(in, null, ParcelUtil.IGNORE_VALUE);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(StringUtil.isValidString(name) && dest != null) {
            super.writeToParcel(dest, flags);
            ParcelUtil.writeString(dest, this.description, ParcelUtil.IGNORE_VALUE, false);
            ParcelUtil.writeString(dest, this.defaultValue, ParcelUtil.IGNORE_VALUE, false);
        }
    }

    @Override
    public int describeContents() { return 0; }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder(super.toString())
                .append(" defaultValue=")
                .append(this.defaultValue)
                .append(" description=")
                .append(this.description).toString();
    }

    public static class Table {
        public static final String name = "default_settings";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT PRIMARY KEY");
            put("defaultValue", "TEXT");
            put("description", "TEXT");
        }};
    }
}
