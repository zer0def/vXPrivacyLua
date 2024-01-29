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
import eu.faircode.xlua.api.objects.ISettingsConfig;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.CursorUtil;

public class MockCarrier extends MockCarrierBase implements IJsonSerial, Parcelable {
    public MockCarrier() { }
    public MockCarrier(Parcel p) { fromParcel(p); }
    public MockCarrier(Bundle b) { fromBundle(b); }
    public MockCarrier(String name, String country, Map<String, String> settings) { super(name, country, settings); }

    @Override
    public int describeContents() { return 0; }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("country", country);
        cv.put("settings", MockPhoneConversions.convertMapToJson(settings).toString());
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            this.name = CursorUtil.getString(cursor, "name");
            this.country = CursorUtil.getString(cursor, "country");
            this.settings = MockPhoneConversions.convertJsonToMap(CursorUtil.getString(cursor, "settings"));
        }
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("name", name);
        jRoot.put("country", country);
        jRoot.put("settings", MockPhoneConversions.convertMapToJson(settings).toString());
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        this.name = obj.getString("name");
        this.country = obj.getString("country");
        this.settings =  MockPhoneConversions.convertJsonToMap(obj.getString("settings"));
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("country", country);
        MockPhoneConversions.writeSettingsToBundle(b, settings);
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        if(bundle != null) {
            this.name = BundleUtil.readString(bundle, "name");
            this.country = BundleUtil.readString(bundle, "country");
            this.settings = MockPhoneConversions.readSettingsFromBundle(bundle);
            Log.i(TAG, "Config from bundle, name=" + name +  " country=" + country + " settings size=" + settings.size());
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        if(in != null) {
            this.name = in.readString();
            this.country = in.readString();
            this.settings = MockPhoneConversions.convertJsonToMap(in.readString());
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(country);
        dest.writeString(MockPhoneConversions.convertMapToJson(settings).toString());
    }

    public static final Parcelable.Creator<MockCarrier> CREATOR = new Parcelable.Creator<MockCarrier>() {
        @Override
        public MockCarrier createFromParcel(Parcel source) {
            return new MockCarrier(source);
        }

        @Override
        public MockCarrier[] newArray(int size) {
            return new MockCarrier[size];
        }
    };

    public static class Table {
        public static final String name = "carriers";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT");
            put("country", "TEXT");
            put("settings", "TEXT");
        }};
    }
}
