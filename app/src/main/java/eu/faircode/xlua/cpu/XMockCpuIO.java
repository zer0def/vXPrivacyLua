package eu.faircode.xlua.cpu;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import eu.faircode.xlua.json.IJsonHelper;

/*public class XMockCpuIO extends XMockCpu implements Parcelable, IJsonHelper {
    public static final XMockCpuIO EmptyDefault = new XMockCpuIO("EMPTY", "EMPTY", "EMPTY", "EMPTY");

    public XMockCpuIO() { super(); }
    public XMockCpuIO(String name, String model, String manufacturer, String contents) { super(name, model, manufacturer, contents); }
    public XMockCpuIO(String name, String model, String manufacturer, String contents, boolean selected) { super(name, model, manufacturer, contents, selected); }

    public XMockCpuIO(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.model);
        dest.writeString(this.manufacturer);
        dest.writeString(this.contents);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    @Override
    public void readFromCursor(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex("name"));
        this.model = cursor.getString(cursor.getColumnIndex("model"));
        this.manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
        this.contents = cursor.getString(cursor.getColumnIndex("contents"));
        int selected = cursor.getColumnIndex("selected");
        this.selected = (cursor.getInt(selected) == 1);
    }

    @Override
    public void fromJSONAssets(JSONObject jsonObject, String path) throws JSONException {
        this.name = jsonObject.getString("name");
        this.model = jsonObject.getString("model");
        this.manufacturer = jsonObject.getString("manufacturer");
        this.contents = jsonObject.getString("contents");
        this.selected = jsonObject.getBoolean("selected");
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", this.name);
        values.put("model", this.model);
        values.put("manufacturer", this.manufacturer);
        values.put("contents", this.contents);
        values.put("selected", this.selected);
        return values;
    }

    String toJSON() throws JSONException {
        return toJSONObject().toString(2);
    }

    JSONObject toJSONObject() throws JSONException {
        JSONObject jroot = new JSONObject();
        jroot.put("name", this.name);
        jroot.put("model", this.model);
        jroot.put("manufacturer", this.manufacturer);
        jroot.put("contents", this.contents);
        jroot.put("selected", this.selected);
        return jroot;
    }

    public static final Parcelable.Creator<XMockCpuIO> CREATOR = new Parcelable.Creator<XMockCpuIO>() {
        @Override
        public XMockCpuIO createFromParcel(Parcel source) {
            return new XMockCpuIO(source);
        }

        @Override
        public XMockCpuIO[] newArray(int size) {
            return new XMockCpuIO[size];
        }
    };

    public static class Convert {
        private static final String TAG = "XLua.XMockCpuIO.Convert";

        public static XMockCpuIO fromJSON(String json) throws JSONException {
            return fromJSONObject(new JSONObject(json));
        }

        public static XMockCpuIO fromJSONObject(JSONObject jroot) throws JSONException {
            XMockCpuIO map = new XMockCpuIO();
            map.name = jroot.getString("name");
            map.model = jroot.getString("model");
            map.manufacturer = jroot.getString("manufacturer");
            map.contents = jroot.getString("contents");
            map.selected = jroot.getBoolean("selected");
            return map;
        }

        public static Bundle toBundle(XMockCpuIO map) {
            Bundle b = new Bundle();
            b.putString("name", map.name);
            b.putString("model", map.model);
            b.putString("manufacturer", map.manufacturer);
            b.putString("contents", map.contents);
            b.putBoolean("selected", map.selected);
            return  b;
        }

        public static XMockCpuIO fromBundle(Bundle bundle) {
            String name = bundle.getString("name");
            String model = bundle.getString("model");
            String manufacturer = bundle.getString("manufacturer");
            String contents = bundle.getString("contents");
            boolean selected = bundle.getBoolean("selected");
            return new XMockCpuIO(name, model, manufacturer, contents, selected);
        }

        public static List<XMockCpuIO> fromBundleArray(Bundle bundle) {
            String[] names = bundle.getStringArray("names");
            String[] models = bundle.getStringArray("models");
            String[] manufacturers = bundle.getStringArray("manufacturers");
            String[] contents = bundle.getStringArray("contents");
            boolean[] selected = bundle.getBooleanArray("selected");

            List<XMockCpuIO> maps = new ArrayList<>();

            for (int i = 0; i < names.length; i++)
                maps.add(new XMockCpuIO(names[i], models[i], manufacturers[i], contents[i], selected[i]));

            return maps;
        }

        public static Bundle toBundle(List<XMockCpuIO> maps) {
            Bundle b = new Bundle();
            String[] names = new String[maps.size()];
            String[] models = new String[maps.size()];
            String[] manufacturers = new String[maps.size()];
            String[] contents = new String[maps.size()];
            boolean[] selected = new boolean[maps.size()];

            for(int i = 0; i < maps.size(); i++) {
                XMockCpuIO map = maps.get(i);
                names[i] = map.name;
                models[i] = map.model;
                manufacturers[i] = map.manufacturer;
                contents[i] = map.contents;
                selected[i] = map.selected;
            }

            b.putStringArray("names", names);
            b.putStringArray("models", models);
            b.putStringArray("manufacturers", manufacturers);
            b.putStringArray("contents", contents);
            b.putBooleanArray("selected", selected);

            return  b;
        }

        public static List<XMockCpuIO>  fromCursor(Cursor cursor) {
            //make sure this supports json / marshall
            List<XMockCpuIO> ps = new ArrayList<>();
            while (cursor != null && cursor.moveToNext()) {
                byte[] marshaled = cursor.getBlob(0);
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(marshaled, 0, marshaled.length);
                parcel.setDataPosition(0);
                XMockCpuIO prop = XMockCpuIO.CREATOR.createFromParcel(parcel);
                parcel.recycle();
                ps.add(prop);
            }

            return ps;
        }

        public static Cursor toCursor(List<XMockCpuIO> maps, boolean marshall) {
            Log.i(TAG, "toCursor");
            MatrixCursor result = new MatrixCursor(new String[]{ marshall ? "blob" : "json"});
            try {
                for (XMockCpuIO prop : maps)
                    if (marshall) {
                        Parcel parcel = Parcel.obtain();
                        prop.writeToParcel(parcel, 0);
                        result.newRow().add(parcel.marshall());
                        parcel.recycle();
                    } else
                        result.addRow(new Object[]{prop.toJSON()});
            }catch (Exception ex) {
                Log.e(TAG, "[XMockCpuIO][toCursor] Failed=\n" + ex.getMessage());
            }
            return result;
        }
    }
}
*/