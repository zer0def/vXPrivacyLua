package eu.faircode.xlua.api.objects.xmock.prop;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.api.objects.IJsonSerial;
import eu.faircode.xlua.api.objects.ISerial;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.ParcelUtil;

public class MockProp extends MockPropBase implements ISerial, IDBSerial, IJsonSerial, Parcelable {
    public MockProp() { }
    public MockProp(String name, String value) { super(name, value); }
    public MockProp(String name, Boolean enabled) { super(name, enabled); }
    public MockProp(String name, String value, Boolean enabled) { super(name, value,  enabled); }
    public MockProp(String name, String value, String defaultValue, Boolean enabled) { super(name, value, defaultValue, enabled); }
    public MockProp(Parcel in) { fromParcel(in); }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(name != null) b.putString("name", name);
        if(value != null) b.putString("mockValue", value);
        if(defaultValue != null) b.putString("defaultValue", defaultValue);
        if(enabled != null) b.putBoolean("enabled", enabled);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        this.name = b.getString("name");
        this.value = b.getString("mockValue");
        this.defaultValue = b.getString("defaultValue");
        this.enabled = b.getBoolean("enabled");
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        if(name != null) cv.put("name", name);
        if(value != null) cv.put("mockValue", value);
        if(defaultValue != null) cv.put("defaultValue", defaultValue);
        if(enabled != null) cv.put("enabled", enabled);
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        this.name = CursorUtil.getString(cursor, "name");
        this.value = CursorUtil.getString(cursor, "mockValue");
        this.defaultValue = CursorUtil.getString(cursor, "defaultValue");
        this.enabled = CursorUtil.getBoolean(cursor, "enabled");
    }

    @Override
    public void fromParcel(Parcel in) {
        this.name = in.readString();
        this.value = in.readString();
        this.defaultValue = in.readString();
        this.enabled = ParcelUtil.readBool(in);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.value);
        dest.writeString(this.defaultValue);
        ParcelUtil.writeBool(dest, this.enabled);
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("name", name);
        jRoot.put("mockValue", value);
        jRoot.put("defaultValue", defaultValue);
        jRoot.put("enabled", enabled);
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        this.name = obj.getString("name");
        this.value = obj.getString("mockValue");
        this.defaultValue = obj.getString("defaultValue");
        this.enabled = obj.getBoolean("enabled");
    }

    public static final Parcelable.Creator<MockProp> CREATOR = new Parcelable.Creator<MockProp>() {
        @Override
        public MockProp createFromParcel(Parcel source) {
            return new MockProp(source);
        }

        @Override
        public MockProp[] newArray(int size) {
            return new MockProp[size];
        }
    };

    public static class Table {
        public static final String name = "props";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT");
            put("mockValue", "TEXT");
            put("defaultValue", "TEXT");
            put("enabled", "BOOLEAN");//change this to INTEGER ?
        }};
    }
}
