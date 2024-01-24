package eu.faircode.xlua;

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

/*public class XMockPropIO extends XMockProp implements Parcelable, IJsonHelper {
    public XMockPropIO() { super(); }
    public XMockPropIO(Parcel in) {
        super(in);
    }
    public XMockPropIO(String name, String defaultValue) {
        super(name, defaultValue);
    }
    public XMockPropIO(String name, String mockValue, String defaultValue) { super(name, mockValue, defaultValue); }
    public XMockPropIO(String name, String mockValue, String defaultValue, boolean enabled) { super(name, mockValue, defaultValue, enabled); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.mockValue);
        dest.writeString(this.defaultValue);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
    }

    public String toJSON() throws JSONException { return toJSONObject().toString(2); }
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("name", this.name);
        jRoot.put("mockValue", this.mockValue);
        jRoot.put("defaultValue", this.defaultValue);
        jRoot.put("enabled", this.enabled);
        return jRoot;
    }

    @Override
    public void readFromCursor(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex("name"));
        this.mockValue = cursor.getString(cursor.getColumnIndex("mockValue"));
        this.defaultValue = cursor.getString(cursor.getColumnIndex("defaultValue"));
        int enabled_c = cursor.getColumnIndex("enabled");
        this.enabled = (cursor.getInt(enabled_c) == 1);
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", this.name);
        values.put("mockValue", this.mockValue);
        values.put("defaultValue", this.defaultValue);
        values.put("enabled", this.enabled);
        return values;
    }

    @Override
    public void fromJSONAssets(JSONObject jsonObject, String path) throws JSONException {
        this.name = jsonObject.getString("name");
        this.mockValue = jsonObject.getString("mockValue");
        this.defaultValue = jsonObject.getString("defaultValue");
        this.enabled = jsonObject.getBoolean("enabled");
    }

    public static final Parcelable.Creator<XMockPropIO> CREATOR = new Parcelable.Creator<XMockPropIO>() {
        @Override
        public XMockPropIO createFromParcel(Parcel source) {
            return new XMockPropIO(source);
        }

        @Override
        public XMockPropIO[] newArray(int size) {
            return new XMockPropIO[size];
        }
    };

    public static class Convert {
        private static final String TAG = "XLua.XMockProp.Convert";

        public static XMockPropIO fromJSON(String json) throws JSONException { return fromJSONObject(new JSONObject(json)); }
        public static XMockPropIO fromJSONObject(JSONObject jroot) throws JSONException {
            XMockPropIO mockProp = new XMockPropIO();
            mockProp.name = jroot.getString("name");
            mockProp.mockValue = jroot.getString("mockValue");
            mockProp.defaultValue = jroot.getString("defaultValue");
            mockProp.enabled = jroot.getBoolean("enabled");
            return mockProp;
        }

        public static Bundle toBundle(XMockPropIO prop) {
            Bundle b = new Bundle();
            b.putString("name", prop.getName());
            b.putString("mockValue", prop.getMockValue());
            b.putString("defaultValue", prop.getDefaultValue());
            b.putBoolean("enabled", prop.getIsEnabled());
            return  b;
        }

        public static XMockPropIO fromBundle(Bundle bundle) {
            String name = bundle.getString("name");
            String value = bundle.getString("mockValue");
            String defaultValue = bundle.getString("defaultValue");
            boolean enabled = bundle.getBoolean("enabled");
            return new XMockPropIO(name, value, defaultValue, enabled);
        }

        public static List<XMockPropIO> fromBundleArray(Bundle bundle) {
            String[] names = bundle.getStringArray("names");
            String[] values = bundle.getStringArray("values");
            String[] defValues = bundle.getStringArray("defaultValues");
            boolean[] enValues = bundle.getBooleanArray("enabledValues");

            List<XMockPropIO> ps = new ArrayList<>();

            Log.i(TAG, "XMockPropIO.fromBundleArray(Bundle)=" + names.length);

            for (int i = 0; i < names.length; i++)
                ps.add(new XMockPropIO(names[i], values[i], defValues[i], enValues[i]));

            return ps;
        }

        public static Bundle toBundle(List<XMockPropIO> props) {
            Bundle b = new Bundle();
            String[] names = new String[props.size()];
            String[] values = new String[props.size()];
            String[] defValues = new String[props.size()];
            boolean[] enValues = new boolean[props.size()];

            for(int i = 0; i < props.size(); i++) {
                XMockPropIO prop = props.get(i);
                names[i] = prop.getName();
                values[i] = prop.getMockValue();
                defValues[i] = prop.getDefaultValue();
                enValues[i] = prop.getIsEnabled();
            }

            b.putStringArray("names", names);
            b.putStringArray("values", values);
            b.putStringArray("defaultValues", defValues);
            b.putBooleanArray("enabledValues", enValues);

            return  b;
        }

        public static List<XMockPropIO>  fromCursor(Cursor cursor) {
            //make sure this supports json / marshall
            List<XMockPropIO> ps = new ArrayList<>();
            while (cursor != null && cursor.moveToNext()) {
                byte[] marshaled = cursor.getBlob(0);
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(marshaled, 0, marshaled.length);
                parcel.setDataPosition(0);
                XMockPropIO prop = XMockPropIO.CREATOR.createFromParcel(parcel);
                parcel.recycle();
                ps.add(prop);
            }

            return ps;
        }

        public static Cursor toCursor(List<XMockPropIO> props, boolean marshall) {
            Log.i(TAG, "toCursor");
            MatrixCursor result = new MatrixCursor(new String[]{ marshall ? "blob" : "json"});
            try {
                for (XMockPropIO prop : props)
                    if (marshall) {
                        Parcel parcel = Parcel.obtain();
                        prop.writeToParcel(parcel, 0);
                        result.newRow().add(parcel.marshall());
                        parcel.recycle();
                    } else
                        result.addRow(new Object[]{prop.toJSON()});
            }catch (Exception ex) {
                Log.e(TAG, "[XMockProp][toCursor] Failed=\n" + ex.getMessage());
            }
            return result;
        }
    }
}*/
