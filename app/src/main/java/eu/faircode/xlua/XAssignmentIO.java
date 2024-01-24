package eu.faircode.xlua;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

//import eu.faircode.xlua.database.IDataParser;

/*public class XAssignmentIO extends XAssignment implements Parcelable, IDataParser {
    public XAssignmentIO() { super(); }
    public XAssignmentIO(XHookIO hook) { super(hook); }
    public XAssignmentIO(Parcel in) { super(in); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.hook, flags);
        dest.writeLong(this.installed);
        dest.writeLong(this.used);
        dest.writeByte(this.restricted ? (byte) 1 : (byte) 0);
        dest.writeString(this.exception);
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

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

    public static XAssignmentIO fromJSON(String json) throws JSONException { return Convert.fromJSON(json); }
    public static XAssignmentIO fromJSONObject(JSONObject jRoot) throws JSONException { return Convert.fromJSONObject(jRoot); }

    public static final Parcelable.Creator<XAssignmentIO> CREATOR = new Parcelable.Creator<XAssignmentIO>() {
        @Override
        public XAssignmentIO createFromParcel(Parcel source) {
            return new XAssignmentIO(source);
        }

        @Override
        public XAssignmentIO[] newArray(int size) {
            return new XAssignmentIO[size];
        }
    };

    public static class Convert {
        public static XAssignmentIO fromJSON(String json) throws JSONException { return fromJSONObject(new JSONObject(json)); }
        public static XAssignmentIO fromJSONObject(JSONObject jRoot) throws JSONException {
            XAssignmentIO assignment = new XAssignmentIO();
            assignment.hook = XHookIO.Convert.fromJSONObject(jRoot.getJSONObject("hook"));
            assignment.installed = jRoot.getLong("installed");
            assignment.used = jRoot.getLong("used");
            assignment.restricted = jRoot.getBoolean("restricted");
            assignment.exception = (jRoot.has("exception") ? jRoot.getString("exception") : null);
            return assignment;
        }
    }
}*/
