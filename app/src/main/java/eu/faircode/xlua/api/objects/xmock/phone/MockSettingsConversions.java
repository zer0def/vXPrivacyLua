package eu.faircode.xlua.api.objects.xmock.phone;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MockSettingsConversions {
    //make this a helper class
    //Converions helper universal but for JSON
    private static final String TAG = "XLua.MockSettingsConversions";

    public static Map<String, String> readSettingsFromString(String data) {
        if(data == null || data.isEmpty())
            return new HashMap<>();

        Map<String , String> settingsMap = new HashMap<>();
        try {
            JSONObject obj = new JSONObject(data);
            //return MockSettingsConversions.readSettingsFromJSON(obj); this will read settings field
            Iterator<String> keys = obj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = obj.getString(key);
                settingsMap.put(key, value);
            }
        }catch (Exception e) {
            Log.e(TAG, "Failed to read settings from JSON String: " + e);
        }

        Log.i(TAG, "settings size=" + settingsMap.size());
        return settingsMap;
    }

    public static String createSettingsString(Map<String, String> settings) {
        JSONObject obj = createSettingsObject(settings);
        if(obj == null) {
            return "{}";
        }else {
            return obj.toString();
        }
    }

    public static Map<String, String> readSettingsFromJSON(JSONObject obj) throws JSONException {
        Map<String, String> settingsMap = new HashMap<>();

        JSONObject settings = obj.getJSONObject("settings");
        Iterator<String> keys = settings.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = settings.getString(key);
            settingsMap.put(key, value);
        }

        Log.i(TAG, "settings size=" + settingsMap.size());
        return settingsMap;
    }

    public static JSONObject createSettingsObject(Map<String, String> settings) {
        try {
            JSONObject settingsObj = new JSONObject();
            for(Map.Entry<String, String> r : settings.entrySet())
                settingsObj.put(r.getKey(), r.getValue());

            return settingsObj;
        }catch (Exception e) {
            Log.e(TAG, "Failed to write Settings to JSON: " + e);
            return null;
        }
    }

    public static void writeSettingsToJSON(JSONObject rootObject, Map<String, String> settings) {
        try {
            JSONObject settingsObj = new JSONObject();
            for(Map.Entry<String, String> r : settings.entrySet())
                settingsObj.put(r.getKey(), r.getValue());

            rootObject.put("settings", settingsObj);
            //rootObject.put("settings", settings);
        }catch (Exception e) {
            Log.e(TAG, "Failed to write Settings to JSON: " + e);
        }
    }
}
