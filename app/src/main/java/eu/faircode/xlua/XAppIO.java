package eu.faircode.xlua;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import eu.faircode.xlua.database.IDataParser;

/*public class XAppIO extends XApp implements Parcelable, IDataParser {
    public XAppIO() { }
    public XAppIO(Parcel in) { super(in); }

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeTypedList(this.assignments);
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
        for (XAssignmentIO assignment : this.assignments)
            jAssignments.put(assignment.toJSONObject());
        jRoot.put("assignments", jAssignments);

        return jRoot;
    }

    public static XApp fromJSON(String json) throws JSONException { return fromJSONObject(new JSONObject(json)); }
    public static XApp fromJSONObject(JSONObject jroot) throws JSONException {
        XApp app = new XApp();

        app.packageName = jroot.getString("packageName");
        app.uid = jroot.getInt("uid");
        app.icon = jroot.getInt("icon");
        app.label = (jroot.has("label") ? jroot.getString("label") : null);
        app.enabled = jroot.getBoolean("enabled");
        app.persistent = jroot.getBoolean("persistent");
        app.system = jroot.getBoolean("system");
        app.forceStop = jroot.getBoolean("forcestop");

        app.assignments = new ArrayList<>();
        JSONArray jAssignment = jroot.getJSONArray("assignments");
        for (int i = 0; i < jAssignment.length(); i++)
            app.assignments.add(XAssignmentIO.fromJSONObject((JSONObject) jAssignment.get(i)));

        return app;
    }

    public static final Parcelable.Creator<XAppIO> CREATOR = new Parcelable.Creator<XAppIO>() {
        @Override
        public XAppIO createFromParcel(Parcel source) {
            return new XAppIO(source);
        }

        @Override
        public XAppIO[] newArray(int size) {
            return new XAppIO[size];
        }
    };

    public static class Convert {
        public static List<XAppIO> fromCursor(Cursor cursor, boolean marshall, boolean close) {
            List<XAppIO> apps = new ArrayList<>();
            if(cursor == null) return apps;

            try {
                if(marshall) {
                    while (cursor.moveToNext()) {
                        byte[] marshaled = cursor.getBlob(0);
                        Parcel parcel = Parcel.obtain();
                        parcel.unmarshall(marshaled, 0, marshaled.length);
                        parcel.setDataPosition(0);
                        XAppIO app = XAppIO.CREATOR.createFromParcel(parcel);
                        parcel.recycle();
                        apps.add(app);
                    }
                }else {

                }
            }finally {
                if(close) cursor.close();
            }

            return apps;
        }
    }
}*/
