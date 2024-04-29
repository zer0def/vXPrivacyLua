package eu.faircode.xlua.api.configs;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.xstandard.UserIdentityPacket;
import eu.faircode.xlua.api.xstandard.interfaces.IJsonSerial;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.CursorUtil;

public class MockConfig extends UserIdentityPacket implements IJsonSerial, Parcelable {
    public static MockConfig create(MockConfigPacket packet) { return new MockConfig(packet); }
    public static MockConfig create(String name, List<LuaSettingExtended> settings) {  return new MockConfig(name, settings); }

    protected String name;
    protected List<LuaSettingExtended> settings;

    public MockConfig(MockConfigPacket packet) { this(packet.getName(), packet.getSettings()); }
    public MockConfig() { setUseUserIdentity(false); }
    public MockConfig(Parcel in) { fromParcel(in); }
    public MockConfig(String name, List<LuaSettingExtended> settings) {
        this();
        setName(name);
        setSettings(settings);
    }

    public String getName() { return name; }
    public MockConfig setName(String name) {  if(name != null) this.name = name; return  this; }

    public List<LuaSettingExtended> getSettings() { return settings; }
    public MockConfig setSettings(List<LuaSettingExtended> settings) { if(settings != null) this.settings = settings; return this; }

    public void saveValuesFromInput() {
        for(LuaSettingExtended s : settings) {
            s.updateValue(true);
        }
    }


    public List<LuaSettingExtended> getEnabledSettings() {
        List<LuaSettingExtended> enabled = new ArrayList<>();
        for(LuaSettingExtended setting : settings)
            if(setting.isEnabled()) enabled.add(setting);

        return enabled;
    }

    public List<LuaSettingExtended> getDisabledSettings() {
        List<LuaSettingExtended> enabled = new ArrayList<>();
        for(LuaSettingExtended setting : settings)
            if(!setting.isEnabled()) enabled.add(setting);
        return enabled;
    }

    public void addSetting(LuaSettingExtended setting) {
        if(settings == null)
            settings = new ArrayList<>();

        if(!settings.isEmpty()) {
            for(LuaSettingExtended set : settings) {
                if(set.getName().equalsIgnoreCase(setting.getName()))
                    return;
            }
        }

        settings.add(setting);
    }

    public void pairSettingMaps(List<LuaSettingExtended> settings) {
        if(!CollectionUtil.isValid(this.settings) && !CollectionUtil.isValid(settings)) {
            for(LuaSettingExtended localSet : this.settings) {
                for(LuaSettingExtended set : settings) {
                    if(set.getName().equalsIgnoreCase(localSet.getName()))
                        localSet.setDescription(set.getDescription());
                }
            }
        }
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("settings", MockConfigConversions.getSettingsToJSONObjectString(settings));
        return cv;
    }

    @Override
    public List<ContentValues> createContentValuesList() { return null; }

    @Override
    public void fromContentValuesList(List<ContentValues> contentValues) { }

    @Override
    public void fromContentValues(ContentValues contentValue) { }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            this.name = CursorUtil.getString(cursor, "name");
            this.settings = MockConfigConversions.readSettingsFromJSON(CursorUtil.getString(cursor, "settings"), true);
        }
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("name", name);
        MockConfigConversions.writeSettingsToJSON(jRoot, settings);
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        if(obj != null) {
            this.name = obj.getString("name");
            this.settings = MockConfigConversions.readSettingsFromJSON(obj, false);
        }
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("settings", MockConfigConversions.getSettingsToJSONObjectString(settings));
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        if(bundle != null) {
            this.name = BundleUtil.readString(bundle, "name");
            this.settings = MockConfigConversions.readSettingsFromJSON(bundle.getString("settings"), true);
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        if(in != null) {
            this.name = in.readString();
            this.settings = MockConfigConversions.readSettingsFromJSON(in.readString(), true);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(dest != null) {
            dest.writeString(name);
            dest.writeString(MockConfigConversions.getSettingsToJSONObjectString(settings));
        }
    }

    @Override
    public int describeContents() { return 0; }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    public static final Parcelable.Creator<MockConfig> CREATOR = new Parcelable.Creator<MockConfig>() {
        @Override
        public MockConfig createFromParcel(Parcel source) {
            return new MockConfig(source);
        }

        @Override
        public MockConfig[] newArray(int size) {
            return new MockConfig[size];
        }
    };

    public static class Table {
        public static final String name = "mock_configs";
        public static final String FIELD_NAME = "name";
        public static final String FIELD_SETTINGS = "settings";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put(FIELD_NAME, "TEXT PRIMARY KEY");
            put(FIELD_SETTINGS, "TEXT");
        }};
    }
}
