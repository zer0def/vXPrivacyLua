package eu.faircode.xlua.api.objects.xmock.phone;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.api.objects.ISettingsConfig;

public class MockCarrierBase implements ISettingsConfig {
    public static final String JSON = "carrier.json";
    public static final int COUNT = 1;

    protected static final String TAG = "XLua.MockCarrierBase";

    protected String name;
    protected String country;
    protected Map<String, String> settings = new HashMap<>();

    public boolean isValid() {
        return name != null && country != null && (settings != null && settings.size() > 0); }

    public MockCarrierBase() { }
    public MockCarrierBase(String name, String country, Map<String, String> settings) {
        setName(name);
        setCountry(country);
        setSettings(settings);
    }

    @Override
    public String getName() {return this.name;}
    public MockCarrierBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public String getCountry() { return this.country; }
    public MockCarrierBase setCountry(String country) {
        if(country != null) this.country = country;
        return this;
    }

    @Override
    public Map<String, String> getSettings() { return settings; }
    public MockCarrierBase setSettings(Map<String, String> settings) {
        if(settings != null) this.settings = settings;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return name + "::" + country;
    }
}
