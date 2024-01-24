package eu.faircode.xlua;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//import eu.faircode.xlua.database.IDataParser;
//import eu.faircode.xlua.database.IDatabaseHelper;

/*public class XHookIO extends XHook implements Parcelable, IDatabaseHelper, IDataParser {
    private static final String TAG = "XLua.XHookIO";

    public XHookIO() { super(); }
    public XHookIO(Parcel in) { super(in); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.builtin ? (byte) 1 : (byte) 0);
        dest.writeString(this.collection);
        dest.writeString(this.group);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeInt(this.version);
        dest.writeString(this.description);
        dest.writeString(this.className);
        dest.writeString(this.resolvedClassName);
        dest.writeString(this.methodName);
        dest.writeStringArray(this.parameterTypes);
        dest.writeString(this.returnType);
        dest.writeInt(this.minSdk);
        dest.writeInt(this.maxSdk);
        dest.writeInt(this.minApk);
        dest.writeInt(this.maxApk);
        dest.writeStringArray(this.excludePackages);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.optional ? (byte) 1 : (byte) 0);
        dest.writeByte(this.usage ? (byte) 1 : (byte) 0);
        dest.writeByte(this.notify ? (byte) 1 : (byte) 0);
        if ((flags & FLAG_WITH_LUA) == 0)
            dest.writeString(null);
        else
            dest.writeString(this.luaScript);
        dest.writeStringArray(this.settings);
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();

        jRoot.put("builtin", this.builtin);
        jRoot.put("collection", this.collection);
        jRoot.put("group", this.group);
        jRoot.put("name", this.name);
        jRoot.put("author", this.author);
        jRoot.put("version", this.version);
        if (this.description != null)
            jRoot.put("description", this.description);

        jRoot.put("className", this.className);
        if (this.resolvedClassName != null)
            jRoot.put("resolvedClassName", this.resolvedClassName);
        if (this.methodName != null)
            jRoot.put("methodName", this.methodName);

        JSONArray jParam = new JSONArray();
        for (int i = 0; i < this.parameterTypes.length; i++)
            jParam.put(this.parameterTypes[i]);
        jRoot.put("parameterTypes", jParam);

        if (this.returnType != null)
            jRoot.put("returnType", this.returnType);

        jRoot.put("minSdk", this.minSdk);
        jRoot.put("maxSdk", this.maxSdk);

        jRoot.put("minApk", this.minApk);
        jRoot.put("maxApk", this.maxApk);

        if (this.excludePackages != null)
            jRoot.put("excludePackages", TextUtils.join(",", this.excludePackages));

        jRoot.put("enabled", this.enabled);
        jRoot.put("optional", this.optional);
        jRoot.put("usage", this.usage);
        jRoot.put("notify", this.notify);

        jRoot.put("luaScript", this.luaScript);

        if (this.settings != null) {
            JSONArray jSettings = new JSONArray();
            for (int i = 0; i < this.settings.length; i++)
                jSettings.put(this.settings[i]);
            jRoot.put("settings", jSettings);
        }
        return jRoot;
    }

    public static final Parcelable.Creator<XHookIO> CREATOR = new Parcelable.Creator<XHookIO>() {
        @Override
        public XHookIO createFromParcel(Parcel source) {
            return new XHookIO(source);
        }

        @Override
        public XHookIO[] newArray(int size) {
            return new XHookIO[size];
        }
    };

    @Override
    public void readFromCursor(Cursor cursor) {
        //Do add
    }

    @Override
    public ContentValues createContentValues()  {
        ContentValues cv = new ContentValues();
        cv.put("id", getId());

        try {
            String json = toJSON();
            cv.put("definition", json);
        }catch (JSONException e) {
            Log.e(TAG, "Failed to get JSON Data from HOOK. " + e + "\n" + Log.getStackTraceString(e));
            return null;
        }

        return cv;
    }

    public static class Convert {
        public static XHookIO fromJSON(String json) throws JSONException { return fromJSONObject(new JSONObject(json)); }
        public static XHookIO fromJSONObject(JSONObject jRoot) throws JSONException  {
            XHookIO hook = new XHookIO();

            hook.builtin = (jRoot.has("builtin") ? jRoot.getBoolean("builtin") : false);
            hook.collection = jRoot.getString("collection");
            hook.group = jRoot.getString("group");
            hook.name = jRoot.getString("name");
            hook.author = jRoot.getString("author");
            hook.version = (jRoot.has("version") ? jRoot.getInt("version") : 0);
            hook.description = (jRoot.has("description") ? jRoot.getString("description") : null);

            hook.className = jRoot.getString("className");
            hook.resolvedClassName = (jRoot.has("resolvedClassName") ? jRoot.getString("resolvedClassName") : null);
            hook.methodName = (jRoot.has("methodName") ? jRoot.getString("methodName") : null);

            JSONArray jParam = jRoot.getJSONArray("parameterTypes");
            hook.parameterTypes = new String[jParam.length()];
            for (int i = 0; i < jParam.length(); i++)
                hook.parameterTypes[i] = jParam.getString(i);

            hook.returnType = (jRoot.has("returnType") ? jRoot.getString("returnType") : null);

            hook.minSdk = jRoot.getInt("minSdk");
            hook.maxSdk = (jRoot.has("maxSdk") ? jRoot.getInt("maxSdk") : 999);

            hook.minApk = (jRoot.has("minApk") ? jRoot.getInt("minApk") : 0);
            hook.maxApk = (jRoot.has("maxApk") ? jRoot.getInt("maxApk") : Integer.MAX_VALUE);

            hook.excludePackages = (jRoot.has("excludePackages")
                    ? jRoot.getString("excludePackages").split(",") : null);

            hook.enabled = (jRoot.has("enabled") ? jRoot.getBoolean("enabled") : true);
            hook.optional = (jRoot.has("optional") ? jRoot.getBoolean("optional") : false);
            hook.usage = (jRoot.has("usage") ? jRoot.getBoolean("usage") : true);
            hook.notify = (jRoot.has("notify") ? jRoot.getBoolean("notify") : false);

            hook.luaScript = jRoot.getString("luaScript");

            if (jRoot.has("settings")) {
                JSONArray jSettings = jRoot.getJSONArray("settings");
                hook.settings = new String[jSettings.length()];
                for (int i = 0; i < jSettings.length(); i++)
                    hook.settings[i] = jSettings.getString(i);
            } else
                hook.settings = null;

            return hook;
        }

        public static List<XHookIO> fromCursor(Cursor cursor, boolean marshall, boolean close) {
            List<XHookIO> ps = new ArrayList<>();
            try {
                if(marshall) {
                    while (cursor != null && cursor.moveToNext()) {
                        byte[] marshaled = cursor.getBlob(0);
                        Parcel parcel = Parcel.obtain();
                        parcel.unmarshall(marshaled, 0, marshaled.length);
                        parcel.setDataPosition(0);
                        XHookIO hook = XHookIO.CREATOR.createFromParcel(parcel);
                        parcel.recycle();
                        ps.add(hook);
                    }
                }else {

                }
            }finally {
                if(close) cursor.close();
            }

            return ps;
        }
    }
}*/
