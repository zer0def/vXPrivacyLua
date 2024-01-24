package eu.faircode.xlua;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*public class XHookProxyApi {
    private static final String TAG = "XLua.XHookProxyApi";

    static Cursor invokeQuery(Context context, String method) { return invokeQuery(context, method, null, null); }
    static Cursor invokeQuery(Context context, String method, String[] args_selection) { return invokeQuery(context, method, args_selection, null); }
    static Cursor invokeQuery(Context context, String method, String[] args_selection, String filter) {
        //filter => "pkg = ? AND uid = ?" (dosnt get used in hook call)
        Log.i(TAG, "invokeQuery=" + method);
        return context.getContentResolver()
                .query(XSecurity.getURI(),
                        new String[]{"xlua." + method },
                        filter,
                        args_selection,
                        null);
    }

    static Bundle invokeCall(Context context, String method) { return invokeCall(context, method, new Bundle()); }
    static Bundle invokeCall(Context context, String method, Bundle extras) {
        Log.i(TAG, "invokeCall=" + method);
        return context.getContentResolver()
                .call(XSecurity.getURI(), "xlua", method, extras);
    }

    public static Map<String, String> querySettingsBy(Context context, int uid, String category) {
        Cursor settings = null;
        Map<String, String> setMap = new HashMap<>();
        try {

            settings =
                    invokeQuery(context,
                            "getSettings",
                            new String[] { category, Integer.toString(uid) },
                            "pkg = ? AND uid = ?");



            settings = context.getContentResolver()
                    .query(XSecurity.getURI(),
                            new String[]{"xlua.getSettings"},
                            "pkg = ? AND uid = ?",
                            new String[]{"global", Integer.toString(uid)},
                            null);
            while (settings != null && settings.moveToNext())
                setMap.put(settings.getString(0), settings.getString(1));
        }catch (Exception e) {
            Log.e(TAG, "Failed to read settings: \n" + e + "\n" + Log.getStackTraceString(e));
        }finally {
            if(settings != null) settings.close();
        }

        return setMap;
    }

    public static List<XHookIO> queryHooks(Context context, String packageName, int uid) {
        return XHookIO.Convert.fromCursor
                (context.getContentResolver()
                        .query(XSecurity.getURI(), new String[]{"xlua.getAssignedHooks2"},
                        "pkg = ? AND uid = ?", new String[]{packageName, Integer.toString(uid)},
                        null));
    }

    public static Map<String, String> getSettings(Context context, int uid) {
        final Map<String, String> settings = new HashMap<>();
        Cursor csettings1 = null;
        try {
            csettings1 = context.getContentResolver()
                    .query(XSecurity.getURI(), new String[]{"xlua.getSettings"},
                            "pkg = ? AND uid = ?", new String[]{"global", Integer.toString(uid)},
                            null);
            while (csettings1 != null && csettings1.moveToNext())
                settings.put(csettings1.getString(0), csettings1.getString(1));
        } finally {
            if (csettings1 != null)
                csettings1.close();
        }

        return settings;
    }

    public static Map<String, String> getAppSettings(Context context, String packageName, int uid) {
        //lpparam.packageName
        final Map<String, String> settings = new HashMap<>();
        // Get app settings
        Cursor csettings2 = null;
        try {
            csettings2 = context.getContentResolver()
                    .query(XSecurity.getURI(), new String[]{"xlua.getSettings"},
                            "pkg = ? AND uid = ?", new String[]{packageName, Integer.toString(uid)},
                            null);
            while (csettings2 != null && csettings2.moveToNext())
                settings.put(csettings2.getString(0), csettings2.getString(1));
        } finally {
            if (csettings2 != null)
                csettings2.close();
        }

        return settings;
    }
}*/
