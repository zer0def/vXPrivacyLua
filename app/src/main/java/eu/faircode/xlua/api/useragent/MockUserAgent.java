package eu.faircode.xlua.api.useragent;

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

import eu.faircode.xlua.api.standard.interfaces.ICheckable;
import eu.faircode.xlua.api.standard.interfaces.IJsonSerial;
import eu.faircode.xlua.utilities.ContentValuesUtil;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.StringUtil;

public class MockUserAgent implements IJsonSerial, ICheckable, Parcelable {
    public static final MockUserAgent DEFAULT_UA = new MockUserAgent("android", "Mozilla/5.0 (Linux; U; Android 7.1.1; Pixel XL Build/NME91E) AppleWebKit/600.44 (KHTML, like Gecko)  Chrome/53.0.2311.298 Mobile Safari/537.0");
    public static final String GET_UA_ALL = "*";
    public static final String GET_UA_ANDROID = "android";
    public static final String GET_UA_IPHONE = "iphone";
    public static final String GET_UA_WINDOWS = "windows";
    public static final String GET_UA_LINUX = "linux";
    public static final String GET_UA_MACINTOSH = "macintosh";
    public static final String GET_UA_CUSTOM = "custom";

    protected String device;
    protected String userAgent;

    public MockUserAgent() { }
    public MockUserAgent(Parcel in) { fromParcel(in); }
    public MockUserAgent(Bundle b) { fromBundle(b); }
    public MockUserAgent(String device, String userAgent) {
        setDevice(device);
        setUserAgent(userAgent);
    }

    public String getDevice() { return this.device; }
    public void setDevice(String device) { if(device != null) this.device = device; }

    public String getUserAgent() { return this.userAgent; }
    public void setUserAgent(String userAgent) { if(userAgent != null) this.userAgent = userAgent;  }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        if(this.device != null) cv.put("device", this.device);
        if(this.userAgent != null) cv.put("useragent", this.userAgent);
        return cv;
    }

    @Override
    public List<ContentValues> createContentValuesList() { return null; }

    @Override
    public void fromContentValuesList(List<ContentValues> contentValues) { }

    @Override
    public void fromContentValues(ContentValues contentValue) {
        this.device = ContentValuesUtil.getString(contentValue, "device");
        this.userAgent = ContentValuesUtil.getString(contentValue, "useragent");
    }

    @Override
    public void fromCursor(Cursor cursor) {
        this.device = CursorUtil.getString(cursor, "device");
        this.userAgent = CursorUtil.getString(cursor, "useragent");
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        jRoot.put("device", this.device);
        jRoot.put("useragent", this.userAgent);
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        this.device = obj.getString("device");
        this.userAgent = obj.getString("useragent");
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(this.device != null) b.putString("device", this.device);
        if(this.userAgent != null) b.putString("useragent", this.userAgent);
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        this.device = bundle.getString("device");
        this.userAgent = bundle.getString("useragent");
    }

    @Override
    public void fromParcel(Parcel in) {
        this.device = in.readString();
        this.userAgent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(this.device != null) dest.writeString(this.device);
        if(this.userAgent != null) dest.writeString(this.userAgent);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public boolean isValid() { return StringUtil.isValidString(this.device) && StringUtil.isValidString(this.userAgent); }

    @Override
    public int hashCode() {
        return this.getUserAgent().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        String agent = null;
        if(obj instanceof String) agent = (String)obj;
        else if(obj instanceof MockUserAgent) agent = ((MockUserAgent) obj).getUserAgent();
        return this.getUserAgent().equalsIgnoreCase(agent);
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("Device=").append(this.device).append("\n")
                .append("Agent=").append(this.userAgent)
                .toString();
    }

    public static final Parcelable.Creator<MockUserAgent> CREATOR = new Parcelable.Creator<MockUserAgent>() {
        @Override
        public MockUserAgent createFromParcel(Parcel source) {
            return new MockUserAgent(source);
        }

        @Override
        public MockUserAgent[] newArray(int size) {
            return new MockUserAgent[size];
        }
    };

    public static class Table {
        public static final String NAME = "user_agents";
        public static final String FIELD_DEVICE = "device";
        public static final String FIELD_USER_AGENT = "useragent";
        public static final LinkedHashMap<String, String> COLUMNS = new LinkedHashMap<String, String>() {{
            put(FIELD_DEVICE, "TEXT");
            put(FIELD_USER_AGENT, "TEXT PRIMARY KEY");
        }};
    }
}
