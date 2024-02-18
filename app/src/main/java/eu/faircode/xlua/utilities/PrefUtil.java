package eu.faircode.xlua.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtil {
    public static boolean hasPreference(Context context, String pref) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.contains(pref);
    }

    public static Boolean getPreferenceBoolean(Context context, String pref) { return getPreferenceBoolean(context, pref, null, false); }
    public static Boolean getPreferenceBoolean(Context context, String pref, Boolean defaultValue, boolean setIfNotExists) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefs.contains(pref)) {
            if(setIfNotExists)
                setPreferenceBoolean(context, pref, defaultValue);

            return defaultValue;
        }

        return prefs.getBoolean(pref, defaultValue);
    }

    public static void setPreferenceBoolean(Context context, String pref, boolean value) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(pref, value).apply();
    }
}
