package eu.faircode.xlua.utilities;

import android.content.ContentValues;
import android.util.Log;

public class ContentValuesUtil {
    private static final String TAG = "XLua.ContentValuesUtil";

    public static Integer getInteger(ContentValues cv, String key) { return getInteger(cv, key, null); }
    public static Integer getInteger(ContentValues cv, String key, Integer defaultValue) {
        if(!cv.containsKey(key))
            return defaultValue;

        try {
            return cv.getAsInteger(key);
        }catch (Exception e) {
            Log.e(TAG, "Failed to get Integer Key [" + key + "] From ContentValues: " + e);
            return defaultValue;
        }
    }

    public static String getString(ContentValues cv, String key) { return getString(cv, key, null); }
    public static String getString(ContentValues cv, String key, String defaultValue) {
        if(!cv.containsKey(key))
            return defaultValue;

        try {
            return cv.getAsString(key);
        }catch (Exception e) {
            Log.e(TAG, "Failed to get String Key [" + key + "] From ContentValues: " + e);
            return defaultValue;
        }
    }

    public static Boolean getBoolean(ContentValues cv, String key) { return getBoolean(cv, key, null); }
    public static Boolean getBoolean(ContentValues cv, String key, Boolean defaultValue) {
        if(!cv.containsKey(key))
            return defaultValue;

        try {
            return cv.getAsBoolean(key);
        }catch (Exception e) {
            Log.e(TAG, "Failed to get String Key [" + key + "] From ContentValues: " + e);
            return defaultValue;
        }
    }
}
