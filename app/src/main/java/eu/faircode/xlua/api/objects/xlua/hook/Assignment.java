package eu.faircode.xlua.api.objects.xlua.hook;

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

public class Assignment extends AssignmentBase implements ISerial, IDBSerial, IJsonSerial, Parcelable {
    public Assignment() { }
    public Assignment(Parcel p) { fromParcel(p); }
    public Assignment(xHook hook) {
        this.hook = hook;
    }

    @Override
    public ContentValues createContentValues() { return null; }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            this.installed = CursorUtil.getLong(cursor, "installed");
            this.used = CursorUtil.getLong(cursor, "used");
            this.restricted = CursorUtil.getBoolean(cursor, "restricted");
            this.exception = CursorUtil.getString(cursor, "exception");
        }
    }

    @Override
    public Bundle toBundle() { return null; }

    @Override
    public void fromBundle(Bundle bundle) { }

    @Override
    public void fromParcel(Parcel in) {
        this.hook = in.readParcelable(xHook.class.getClassLoader());
        this.installed = in.readLong();
        this.used = in.readLong();
        this.restricted = (in.readByte() != 0);
        this.exception = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.hook, flags);
        dest.writeLong(this.installed);
        dest.writeLong(this.used);
        dest.writeByte(this.restricted ? (byte) 1 : (byte) 0);
        dest.writeString(this.exception);
    }

    public static final Parcelable.Creator<Assignment> CREATOR = new Parcelable.Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    @Override
    public String toJSON() throws JSONException {
        return toJSONObject().toString(2);
    }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("hook", this.hook.toJSONObject());
        jRoot.put("installed", this.installed);
        jRoot.put("used", this.used);
        jRoot.put("restricted", this.restricted);
        jRoot.put("exception", this.exception);
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        xHook hook = new xHook();
        hook.fromJSONObject(obj.getJSONObject("hook"));
        this.hook = hook;
        this.installed = obj.getLong("installed");
        this.used = obj.getLong("used");
        this.restricted = obj.getBoolean("restricted");
        this.exception = (obj.has("exception") ? obj.getString("exception") : null);
    }

    public static class Table {
        public static final String name = "assignment";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("package", "TEXT");
            put("uid", "INTEGER");
            put("hook", "TEXT");
            put("installed", "INTEGER");
            put("used", "INTEGER");
            put("restricted", "INTEGER");
            put("exception", "TEXT");
            put("old", "TEXT");
            put("new", "TEXT");
        }};
    }
}
