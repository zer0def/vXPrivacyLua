package eu.faircode.xlua.api.objects.xmock.phone;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import eu.faircode.xlua.api.objects.IJsonSerial;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.CursorUtil;

/*public class MockUniqueId extends MockUniqueIdBase implements IJsonSerial, Parcelable {
    public MockUniqueId() { }
    public MockUniqueId(Parcel p) { fromParcel(p); }
    public MockUniqueId(Bundle b) { fromBundle(b); }
    public MockUniqueId(String name, Map<String, String> settings) {
        setName(name);
        setSettings(settings);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("settings", MockPhoneConversions.convertMapToJson(settings).toString());
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            this.name = CursorUtil.getString(cursor, "name");
            this.settings = MockPhoneConversions.convertJsonToMap(CursorUtil.getString(cursor, "settings"));
        }
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("name", name);
        jRoot.put("settings", MockPhoneConversions.convertMapToJson(settings).toString());
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        this.name = obj.getString("name");
        this.settings =  MockPhoneConversions.convertJsonToMap(obj.getString("settings"));
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("name", name);
        MockPhoneConversions.writeSettingsToBundle(b, settings);
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        if(bundle != null) {
            this.name = BundleUtil.readString(bundle, "name");
            this.settings = MockPhoneConversions.readSettingsFromBundle(bundle);
            Log.i(TAG, "Config from bundle, name=" + name + " settings size=" + settings.size());
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        if(in != null) {
            this.name = in.readString();
            this.settings = MockPhoneConversions.convertJsonToMap(in.readString());
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(MockPhoneConversions.convertMapToJson(settings).toString());
    }

    public static final Parcelable.Creator<MockUniqueId> CREATOR = new Parcelable.Creator<MockUniqueId>() {
        @Override
        public MockUniqueId createFromParcel(Parcel source) {
            return new MockUniqueId(source);
        }

        @Override
        public MockUniqueId[] newArray(int size) {
            return new MockUniqueId[size];
        }
    };

    public static class Table {
        public static final String name = "uniqueids";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT");
            put("settings", "TEXT");
        }};
    }
}*/
