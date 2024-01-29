package eu.faircode.xlua.api.objects.xmock.phone;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.api.objects.ISettingsConfig;

public class MockUniqueIdBase implements ISettingsConfig {
    public static final String JSON = "uniqueids.json";
    public static final int COUNT = 1;

    protected static final String TAG = "XLua.MockUniqueIdBase";

    protected String name;
    protected Map<String, String> settings = new HashMap<>();

    public MockUniqueIdBase() { }
    public MockUniqueIdBase(String name, Map<String, String> settings) {
        setName(name);
        setSettings(settings);
    }

    public boolean isValid() {
        return name != null && (settings != null && settings.size() > 0); }

    @Override
    public String getName() {return this.name;}
    public MockUniqueIdBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    @Override
    public Map<String, String> getSettings() { return settings; }
    public MockUniqueIdBase setSettings(Map<String, String> settings) {
        if(settings != null) this.settings = settings;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
