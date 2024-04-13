package eu.faircode.xlua.api.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import eu.faircode.xlua.api.xstandard.interfaces.IDBSerial;
import eu.faircode.xlua.api.xstandard.interfaces.IJsonSerial;
import eu.faircode.xlua.api.xstandard.interfaces.ISerial;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;

public class XLuaApp extends XLuaAppBase implements ISerial, IDBSerial, IJsonSerial, Parcelable {
    public static XLuaApp create(Bundle b) { return new XLuaApp(b); }

    private static final String TAG = "XLua.XLuaApp";

    public XLuaApp() { }
    public XLuaApp(Bundle b) { fromBundle(b); }
    public XLuaApp(Parcel in) { fromParcel(in); }

    @Override
    public int describeContents() { return 0; }

    @Override
    public ContentValues createContentValues() { return null; }

    @Override
    public List<ContentValues> createContentValuesList() { return null; }

    @Override
    public void fromContentValuesList(List<ContentValues> contentValues) { }

    @Override
    public void fromContentValues(ContentValues contentValue) { }

    @Override
    public void fromCursor(Cursor cursor) { }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        Log.i(TAG, "To Got bundle spoofing: " + this.packageName + this.toString());
        try { b.putString("app", toJSON());
        }catch (Exception e) {
            Log.e(TAG, "[toBundle] to JSON Error App: " + this.packageName + " e=" + e + " stack=\n" + Log.getStackTraceString(e));
            b.putString("app", "{ }");
        } return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        Log.i(TAG, "From Bundle...");
        String dataBlob = bundle.getString("app");
        Log.i(TAG, "Blob: " + dataBlob);
        try { fromJSONObject(new JSONObject(dataBlob));
        }catch (Exception e) {
            Log.e(TAG, "[fromBundle] from JSON Error App: " + this.packageName + " e=" + e + " stack=\n" + Log.getStackTraceString(e));
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        this.packageName = in.readString();
        this.uid = in.readInt();
        this.icon = in.readInt();
        this.label = in.readString();
        this.enabled = (in.readByte() != 0);
        this.persistent = (in.readByte() != 0);
        this.system = (in.readByte() != 0);
        this.forceStop = (in.readByte() != 0);
        this.assignments = in.createTypedArrayList(LuaAssignment.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeInt(this.uid);
        dest.writeInt(this.icon);
        dest.writeString(this.label);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.persistent ? (byte) 1 : (byte) 0);
        dest.writeByte(this.system ? (byte) 1 : (byte) 0);
        dest.writeByte(this.forceStop ? (byte) 1 : (byte) 0);
        dest.writeTypedList(new ArrayList<>(this.assignments));
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();

        jRoot.put("packageName", this.packageName);
        jRoot.put("uid", this.uid);
        jRoot.put("icon", this.icon);
        jRoot.put("label", this.label);
        jRoot.put("enabled", this.enabled);
        jRoot.put("persistent", this.persistent);
        jRoot.put("system", this.system);
        jRoot.put("forcestop", this.forceStop);

        JSONArray jAssignments = new JSONArray();
        for (LuaAssignment assignment : this.assignments)
            jAssignments.put(assignment.toJSONObject());
        jRoot.put("assignments", jAssignments);

        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        // return fromJSONObject(new JSONObject(json));
        this.packageName = obj.getString("packageName");
        this.uid = obj.getInt("uid");
        this.icon = obj.getInt("icon");
        this.label = (obj.has("label") ? obj.getString("label") : null);
        this.enabled = obj.getBoolean("enabled");
        this.persistent = obj.getBoolean("persistent");
        this.system = obj.getBoolean("system");
        this.forceStop = obj.getBoolean("forcestop");

        this.assignments = new ArrayList<>();
        JSONArray jAssignment = obj.getJSONArray("assignments");
        for (int i = 0; i < jAssignment.length(); i++) {
            LuaAssignment assignment = new LuaAssignment();
            assignment.fromJSONObject((JSONObject) jAssignment.get(i));
            this.assignments.add(assignment);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return this.packageName;
    }

    public static final Parcelable.Creator<XLuaApp> CREATOR = new Parcelable.Creator<XLuaApp>() {
        @Override
        public XLuaApp createFromParcel(Parcel source) {
            return new XLuaApp(source);
        }

        @Override
        public XLuaApp[] newArray(int size) {
            return new XLuaApp[size];
        }
    };
}
