package eu.faircode.xlua.api.cpu;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

import eu.faircode.xlua.api.standard.interfaces.IDBSerial;
import eu.faircode.xlua.api.standard.interfaces.IJsonSerial;
import eu.faircode.xlua.api.standard.interfaces.ISerial;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.ContentValuesUtil;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.ParcelUtil;

public class MockCpu implements ISerial, IDBSerial, IJsonSerial, Parcelable {
    public static MockCpu EMPTY_DEFAULT = new MockCpu("EMPTY", "EMPTY", "EMPTY", "EMPTY");

    protected String name;
    protected String model;
    protected String manufacturer;
    protected String contents;
    protected Boolean selected;

    public MockCpu() { }
    public MockCpu(Parcel in) { fromParcel(in); }
    public MockCpu(String name, String model, String manufacturer, String contents) { this(name, model, manufacturer, contents, null); }
    public MockCpu(String name, String model, String manufacturer, String contents, Boolean selected) {
        setName(name);
        setModel(model);
        setManufacturer(manufacturer);
        setContents(contents);
        setSelected(selected);
    }

    public String getName() { return this.name; }
    public MockCpu setName(String name) { if(name != null) this.name = name; return this; }

    public String getModel() { return this.model; }
    public MockCpu setModel(String model) { if(model != null) this.model = model; return this; }

    public String getManufacturer() { return this.manufacturer; }
    public MockCpu setManufacturer(String manufacturer) { if(manufacturer != null) this.manufacturer = manufacturer; return this; }

    public String getContents() { return this.contents; }
    public MockCpu setContents(String contents) { if(contents != null) this.contents = contents; return this; }

    public Boolean isSelected() { return this.selected; }
    public MockCpu setSelected(Boolean selected) { if(selected != null) this.selected = selected; return this; }


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
    public List<ContentValues> createContentValuesList() { return null; }

    @Override
    public void fromContentValuesList(List<ContentValues> contentValues) { }

    @Override
    public void fromContentValues(ContentValues contentValue) {
        this.name = ContentValuesUtil.getString(contentValue, "name");
        this.model = ContentValuesUtil.getString(contentValue, "model");
        this.manufacturer = ContentValuesUtil.getString(contentValue, "manufacturer");
        this.contents = ContentValuesUtil.getString(contentValue, "contents");
        this.selected = ContentValuesUtil.getBoolean(contentValue, "selected");
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
    public void writeToParcel(Parcel dest, int flags) {
        if(name != null) dest.writeString(name);
        if(model != null) dest.writeString(model);
        if(manufacturer != null) dest.writeString(manufacturer);
        if(contents != null) dest.writeString(contents);
        //if(selected != null) dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        ParcelUtil.writeBool(dest, this.selected);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        //Better way of implementing this bullshit
        String name = null;
        if(obj instanceof String)
            name = (String)obj;
        else if(obj instanceof MockCpu)
            name = ((MockCpu) obj).getName();

        return this.getName().equalsIgnoreCase(name);
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" name=");
        sb.append(name);
        sb.append(" model=");
        sb.append(model);
        sb.append(" manufacturer=");
        sb.append(manufacturer);
        sb.append(" contents=");
        //sb.append(contents)
        sb.append(" empty cuz too much");
        sb.append(" selected=");
        sb.append(selected);
        return sb.toString();
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
        public static final String name = "cpu_maps";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT PRIMARY KEY");
            put("model", "TEXT");
            put("manufacturer", "TEXT");
            put("contents", "TEXT");
            put("selected", "BOOLEAN");
        }};
    }
}
