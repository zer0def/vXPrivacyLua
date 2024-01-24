package eu.faircode.xlua.api.objects.xmock.cpu;

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
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.ParcelUtil;

public class MockCpu extends MockCpuBase implements ISerial, IDBSerial, IJsonSerial, Parcelable {
    public static MockCpu EMPTY_DEFAULT = new MockCpu("EMPTY", "EMPTY", "EMPTY", "EMPTY");

    public MockCpu() { }
    public MockCpu(Parcel in) { fromParcel(in); }
    public MockCpu(String name, String model, String manufacturer, String contents) { super(name, model, manufacturer, contents, null); }
    public MockCpu(String name, String model, String manufacturer, String contents, Boolean selected) { super(name, model, manufacturer, contents, selected); }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        if(name != null) cv.put("name", name);
        if(model != null) cv.put("model", model);
        if(manufacturer != null) cv.put("manufacturer", manufacturer);
        if(contents != null) cv.put("contents", contents);
        if(selected != null) cv.put("selected", selected);
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        this.name = CursorUtil.getString(cursor, "name");
        this.model = CursorUtil.getString(cursor, "model");
        this.manufacturer = CursorUtil.getString(cursor, "manufacturer");
        this.contents = CursorUtil.getString(cursor, "contents");
        this.selected = CursorUtil.getBoolean(cursor, "selected");
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("name", this.name);
        jRoot.put("model", this.model);
        jRoot.put("manufacturer", this.manufacturer);
        jRoot.put("contents", this.contents);
        jRoot.put("selected", this.selected);
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        this.name = obj.getString("name");
        this.model = obj.getString("model");
        this.manufacturer = obj.getString("manufacturer");
        this.contents = obj.getString("contents");
        this.selected = obj.getBoolean("selected");
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(name != null) b.putString("name", name);
        if(model != null) b.putString("model", model);
        if(manufacturer != null) b.putString("manufacturer", manufacturer);
        if(contents != null) b.putString("contents", contents);
        if(selected != null) b.putBoolean("selected", selected);
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        this.name = bundle.getString("name");
        this.model = bundle.getString("model");
        this.manufacturer = bundle.getString("manufacturer");
        this.contents = bundle.getString("contents");
        this.selected = BundleUtil.readBoolean(bundle, "selected");
    }

    @Override
    public void fromParcel(Parcel in) {
        this.name = in.readString();
        this.model = in.readString();
        this.manufacturer = in.readString();
        this.contents = in.readString();
        this.selected = ParcelUtil.readBool(in);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(name != null) dest.writeString(name);
        if(model != null) dest.writeString(model);
        if(manufacturer != null) dest.writeString(manufacturer);
        if(contents != null) dest.writeString(contents);
        //if(selected != null) dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        ParcelUtil.writeBool(dest, this.selected);
    }

    public static final Parcelable.Creator<MockCpu> CREATOR = new Parcelable.Creator<MockCpu>() {
        @Override
        public MockCpu createFromParcel(Parcel source) {
            return new MockCpu(source);
        }

        @Override
        public MockCpu[] newArray(int size) {
            return new MockCpu[size];
        }
    };

    public static class Table {
        public static final String name = "cpumaps";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT");
            put("model", "TEXT");
            put("manufacturer", "TEXT");
            put("contents", "TEXT");
            put("selected", "BOOLEAN");
        }};
    }
}
