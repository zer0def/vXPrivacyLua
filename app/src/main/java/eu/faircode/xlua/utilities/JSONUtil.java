package eu.faircode.xlua.utilities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

    private static final String TAG = "XLua.JSONUtil";

    public static String getString(JSONObject obj, String jsonFieldName) { return getString(obj, jsonFieldName, null); }
    public static String getString(JSONObject obj, String jsonFieldName, String defaultValue) {
        if(!StringUtil.isValidString(jsonFieldName))
            return defaultValue;

        try {
            return obj.getString(jsonFieldName);
        }catch (JSONException ex) {
            Log.e(TAG, "JSON Field [" + jsonFieldName + "] Failed to get Grab String! " + ex);
            return defaultValue;
        }
    }

    public static Integer getInteger(JSONObject obj, String jsonFieldName) { return  getInteger(obj, jsonFieldName, null); }
    public static Integer getInteger(JSONObject obj, String jsonFieldName, Integer defaultValue) {
        if(!StringUtil.isValidString(jsonFieldName))
            return defaultValue;

        try {
            return obj.getInt(jsonFieldName);
        }catch (JSONException ex) {
            Log.e(TAG, "JSON Field [" + jsonFieldName + "] Failed to get Grab Integer! " + ex);
            return defaultValue;
        }
    }

    public static Boolean getBoolean(JSONObject obj, String jsonFieldName) { return getBoolean(obj, jsonFieldName, false); }
    public static Boolean getBoolean(JSONObject obj, String jsonFieldName, Boolean defaultValue) {
        if(!StringUtil.isValidString(jsonFieldName))
            return defaultValue;

        try {
            return obj.getBoolean(jsonFieldName);
        }catch (JSONException ex) {
            Log.e(TAG, "JSON Field [" + jsonFieldName + "] Failed to get Grab Boolean! " + ex);
            return defaultValue;
        }
    }

}
