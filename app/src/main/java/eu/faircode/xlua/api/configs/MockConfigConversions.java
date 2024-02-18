package eu.faircode.xlua.api.configs;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.faircode.xlua.api.settings.LuaSettingExtended;

public class MockConfigConversions {
    private static final String TAG = "XLua.MockConfigConversions";

    public static void writeSettingsToJSON(JSONObject obj, List<LuaSettingExtended> settings) {
        try {
            JSONObject settingsObj = new JSONObject();
            for(LuaSettingExtended setting : settings)
                settingsObj.put(setting.getName(), setting.getValue());

            obj.put("settings", settingsObj);
        }catch (JSONException ex) {
            Log.e(TAG, "Failed to write settings: " + ex + "\n" + Log.getStackTraceString(ex));
        }
    }

    public static String getSettingsToJSONObjectString(List<LuaSettingExtended> settings) {
        try {
            return getSettingsToJSONObject(settings).toString(2);
        }catch (JSONException ex) {
            Log.e(TAG, "JSON Object to String failed: " + ex + "\n" + Log.getStackTraceString(ex));
            return "{}";
        }
    }

    public static JSONObject getSettingsToJSONObject(List<LuaSettingExtended> settings) {
        JSONObject settingsObj = new JSONObject();
        try {
            for(LuaSettingExtended setting : settings)
                settingsObj.put(setting.getName(), setting.getValue());

            return settingsObj;
        }catch (JSONException ex) {
            Log.e(TAG, "Failed to write settings: " + ex + "\n" + Log.getStackTraceString(ex));
            return settingsObj;
        }
    }

    public static List<LuaSettingExtended> readSettingsFromJSON(String jsonText, boolean isRootObject) {
        try {
            return readSettingsFromJSON(new JSONObject(jsonText), isRootObject);
        }catch (JSONException ex) {
            Log.e(TAG, "Failed to String to JSON: " + ex + "\n" + Log.getStackTraceString(ex));
            return new ArrayList<>();
        }
    }

    public static List<LuaSettingExtended> readSettingsFromJSON(JSONObject obj, boolean isRootObject) {
        List<LuaSettingExtended> settingsList = new ArrayList<>();
        try {
            JSONObject baseObj = obj;
            Iterator<String> keys = null;
            if(!isRootObject) {
                baseObj = obj.getJSONObject("settings");
                keys = baseObj.keys();
            }else {
                keys = obj.keys();
            }

            while (keys.hasNext()) {
                String key = keys.next();
                String value = baseObj.getString(key);
                LuaSettingExtended setting = new LuaSettingExtended();
                setting.setName(key);
                setting.setValue(value);
                settingsList.add(setting);
            }

            return settingsList;
        }catch (JSONException ex) {
            Log.e(TAG, "Failed to read JSON object: " + ex + "\n" + Log.getStackTraceString(ex));
            return settingsList;
        }
    }
}
