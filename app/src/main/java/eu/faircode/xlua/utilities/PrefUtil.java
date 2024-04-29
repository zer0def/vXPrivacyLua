package eu.faircode.xlua.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import eu.faircode.xlua.logger.XLog;

public class PrefUtil {
    public static final String PREF_LAST_RUN = "lastRun";

    public static boolean hasPreference(Context context, String pref) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.contains(pref);
    }

    public static void setString(Context context, String pref, String value) {
        try {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit().putString(pref, value).apply();
        }catch (Exception e) {
            XLog.e("Failed to set String pref", e, true);
        }
    }

    public static String getString(Context context, String pref) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if(!prefs.contains(pref))
                return null;

            return prefs.getString(pref, null);
        }catch (Exception e) {
            XLog.e("Failed to open String pref", e, true);
            return null;
        }
    }

    public static Boolean getBoolean(Context context, String pref) { return getBoolean(context, pref, null, false); }
    public static Boolean getBoolean(Context context, String pref, Boolean defaultValue, boolean setIfNotExists) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefs.contains(pref)) {
            if(setIfNotExists)
                setBoolean(context, pref, defaultValue);

            return defaultValue;
        }

        return prefs.getBoolean(pref, defaultValue);
    }

    public static void setBoolean(Context context, String pref, boolean value) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(pref, value).apply();
    }
}
