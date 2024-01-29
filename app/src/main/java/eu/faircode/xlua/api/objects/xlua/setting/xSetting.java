package eu.faircode.xlua.api.objects.xlua.setting;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.api.objects.IJsonSerial;
import eu.faircode.xlua.api.objects.ISerial;
import eu.faircode.xlua.api.objects.xlua.packets.SettingPacket;
import eu.faircode.xlua.utilities.CursorUtil;

public class xSetting extends xSettingBase implements ISerial, IDBSerial, IJsonSerial, Parcelable {
    public static xSetting create(Integer user, String category, String name, String value) { return new SettingPacket(user, category, name, value); }

    public xSetting() { }
    public xSetting(Parcel in) { fromParcel(in); }
    public xSetting(Integer user, String category, String name) { super(user, category, name, null); }
    public xSetting(Integer user, String category, String name, String value) { super(user, category, name, value); }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(user != null) b.putInt("user", user);
        if(category != null) b.putString("category", category);
        if(name != null) b.putString("name", name);
        if(value != null) b.putString("value", value);

        //if(kill != null) b.putBoolean("kill", kill);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        if(b != null) {
            user = b.getInt("user");
            category = b.getString("category");
            name = b.getString("name");
            value = b.getString("value");

            //if(b.containsKey("kill")) kill = b.getBoolean("kill");
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        if(in != null) {
            this.user = in.readInt();
            this.category = in.readString();
            this.name = in.readString();
            this.value = in.readString();
        }
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user);
        dest.writeString(this.category);
        dest.writeString(this.name);
        dest.writeString(this.value);
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            this.user = CursorUtil.getInteger(cursor, "user");
            this.category = CursorUtil.getString(cursor, "category");
            this.name = CursorUtil.getString(cursor, "name");
            this.value = CursorUtil.getString(cursor, "value");
            if(this.value == null) {
                int v = cursor.getColumnIndex("value");
                Log.i("XLua.xSetting.fromCursor", "oops is null ? value, here is my index=" + v);
                if(v != -1) {
                    String vv = cursor.getString(v);
                    Log.i("XLua.xSetting.fromCursor", "Got the value ...." + vv);
                }
            }
        }

        //int killIx = cursor.getColumnIndex("kill");
        //this.kill = killIx == -1 ? null : cursor.getInt(killIx);
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        if(user != null) cv.put("user", user);
        if(category != null) cv.put("category", category);
        if(name != null) cv.put("name", name);
        if(value != null) cv.put("value", value);
        return cv;
    }

    public static final Parcelable.Creator<xSetting> CREATOR = new Parcelable.Creator<xSetting>() {
        @Override
        public xSetting createFromParcel(Parcel source) {
            return new xSetting(source);
        }

        @Override
        public xSetting[] newArray(int size) {
            return new xSetting[size];
        }
    };

    @Override
    public String toJSON() throws JSONException { return null; }

    @Override
    public JSONObject toJSONObject() throws JSONException { return null; }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException { }

    public static class Table {
        public static final String name = "setting";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("user", "INTEGER");
            put("category", "TEXT");
            put("name", "TEXT");
            put("value", "TEXT");
        }};
    }
}
