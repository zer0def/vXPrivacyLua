package eu.faircode.xlua.api.xmock.provider;

import android.content.Context;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.XDatabase;


/*public class XMockSettingsProvider {
    private static final Object lock = new Object();
    private static final String TAG = "XLua.XMockSettingsProvider";
    private static HashMap<String, XMockMappedSetting> defaultSettings = new HashMap<>();

    public static HashMap<String, XMockMappedSetting> getDefaultSettingsNoValues(Context context, XDatabase db) {
        initCache(context, db);
        HashMap<String, XMockMappedSetting> cDefaultSettings = new HashMap<>(defaultSettings.size());
        for(Map.Entry<String, XMockMappedSetting> e : defaultSettings.entrySet()) {
            XMockMappedSetting s = new XMockMappedSetting(e.getValue());
            cDefaultSettings.put(e.getKey(), s);
        }

        return cDefaultSettings;
    }

    public static HashMap<String, XMockMappedSetting> getDefaultSettingsCopy(Context context, XDatabase db) {
        initCache(context, db);
        return new HashMap<>(defaultSettings);
    }

    public static String getDefaultSettingValue(Context context, XDatabase db, String settingName) {
        initCache(context, db);
        if(settingName != null) {
            XMockMappedSetting defSetting = defaultSettings.get(settingName);
            if(defSetting != null)
                return defSetting.getValue();
        }

        return null;
    }

    public static void initCache(Context context, XDatabase db) {
        if(defaultSettings == null || defaultSettings.isEmpty()) {
            synchronized (lock) {
                if(defaultSettings == null || defaultSettings.isEmpty()) {
                    HashMap<String, XMockMappedSetting> cDefaultSettings = new HashMap<>();
                    Collection<XMockMappedSetting> defaults = XMockSettingsDatabase.getMappedPropSettings(context, db);
                    for(XMockMappedSetting def : defaults) {
                        cDefaultSettings.put(def.getName(), def);
                    }

                    defaultSettings = cDefaultSettings;
                }
            }
        }

        Log.i(TAG, "Cache is google size=" + defaultSettings.size());
    }
}*/
