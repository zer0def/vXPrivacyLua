package eu.faircode.xlua.api.objects.xmock.phone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.api.objects.IJsonSerial;
import eu.faircode.xlua.api.objects.ISerial;
import eu.faircode.xlua.api.objects.xlua.setting.xSetting;
import eu.faircode.xlua.api.xlua.XSettingsDatabase;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.CursorUtil;

public class MockPhone extends MockPhoneBase implements IJsonSerial, Parcelable {

    public MockPhone() { }
    public MockPhone(Parcel in) { fromParcel(in);  }
    public MockPhone(String name, String model, String manufacturer, String carrier) { super(name, model, manufacturer, carrier); }
    public MockPhone(String name, String model, String manufacturer, String carrier, Map<String, String> settings) { super(name, model, manufacturer, carrier, settings); }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("model", model);
        cv.put("manufacturer", manufacturer);
        cv.put("carrier", carrier);
        cv.put("settings", MockPhoneConversions.convertMapToJson(settings).toString());
        //cv.put("settings", MockPhoneConversions.convertSettingsToJson(settings).toString());
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        this.name = CursorUtil.getString(cursor, "name");
        this.model = CursorUtil.getString(cursor, "model");
        this.manufacturer = CursorUtil.getString(cursor, "manufacturer");
        this.carrier = CursorUtil.getString(cursor, "carrier");
        this.settings = MockPhoneConversions.convertJsonToMap(CursorUtil.getString(cursor, "settings"));
        //new ArrayList<>(MockPhoneConversions.convertJsonToSettings(CursorUtil.getString(cursor, "settings")));
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("name", name);
        jRoot.put("model", model);
        jRoot.put("manufacturer", manufacturer);
        jRoot.put("carrier", carrier);
        jRoot.put("settings", MockPhoneConversions.convertMapToJson(settings).toString());
        //jRoot.put("settings", MockPhoneConversions.convertSettingsToJson(settings).toString());
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        this.name = obj.getString("name");
        this.model = obj.getString("model");
        this.manufacturer = obj.getString("manufacturer");
        this.carrier = obj.getString("carrier");
        this.settings =  MockPhoneConversions.convertJsonToMap(obj.getString("settings"));
        //new ArrayList<>(MockPhoneConversions.convertJsonToSettings(obj.getString("settings")));
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("name", name);
        b.putString("model", model);
        b.putString("manufacturer", manufacturer);
        b.putString("carrier", carrier);
        MockPhoneConversions.writeSettingsToBundle(b, settings);
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        if(bundle != null) {
            this.name = BundleUtil.readString(bundle, "name");
            this.model = BundleUtil.readString(bundle, "model");
            this.manufacturer = BundleUtil.readString(bundle, "manufacturer");
            this.carrier = BundleUtil.readString(bundle, "carrier");
            this.settings = MockPhoneConversions.readSettingsFromBundle(bundle);
            Log.i(TAG, "Config from bundle, name=" + name + " model=" + model + " settings size=" + settings.size());
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        if(in != null) {
            this.name = in.readString();
            this.model = in.readString();
            this.manufacturer = in.readString();
            this.carrier = in.readString();
            this.settings = MockPhoneConversions.convertJsonToMap(in.readString());
            //this.settings = new ArrayList<>(MockPhoneConversions.convertJsonToSettings(in.readString()));
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.model);
        dest.writeString(this.manufacturer);
        dest.writeString(this.carrier);
        dest.writeString(MockPhoneConversions.convertMapToJson(settings).toString());
        //dest.writeString(MockPhoneConversions.convertSettingsToJson(settings).toString());
    }

    public static final Parcelable.Creator<MockPhone> CREATOR = new Parcelable.Creator<MockPhone>() {
        @Override
        public MockPhone createFromParcel(Parcel source) {
            return new MockPhone(source);
        }

        @Override
        public MockPhone[] newArray(int size) {
            return new MockPhone[size];
        }
    };

    public static class Table {
        public static final String name = "phones";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT");
            put("model", "TEXT");
            put("manufacturer", "TEXT");
            put("carrier", "TEXT");
            put("settings", "TEXT");
        }};
    }
}
