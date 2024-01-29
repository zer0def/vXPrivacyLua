package eu.faircode.xlua.api.objects.xmock.phone;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.api.objects.ISettingsConfig;

public class MockPhoneBase implements ISettingsConfig {
    public static final String JSON = "phones.json";
    public static final int COUNT = 1;

    protected static final String TAG = "XLua.MockPhoneBase";


    protected String name;
    protected String model;
    protected String manufacturer;
    protected String carrier;
    protected Map<String, String> settings = new HashMap<>();

    public MockPhoneBase() {  }
    public MockPhoneBase(String name, String model, String manufacturer, String carrier){ this(name, model, manufacturer, carrier, null); }
    public MockPhoneBase(String name, String model, String manufacturer, String carrier, Map<String, String> settings) {
        setName(name);
        setModel(model);
        setManufacturer(manufacturer);
        setCarrier(carrier);
        setSettings(settings);
    }

    public MockPhoneBase setSettings(Map<String, String> settings) {
        if(settings != null) this.settings = settings;
        return this;
    }

    @Override
    public Map<String, String> getSettings() { return this.settings; }
    public boolean isValid() {
        return name != null && model != null && manufacturer != null && carrier != null && (settings != null && settings.size() > 0); }

    @Override
    public String getName() { return this.name; }
    public MockPhoneBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public String getModel() { return this.model; }
    public MockPhoneBase setModel(String model) {
       if(model != null) this.model = model;
       return this;
    }

    public String getManufacturer() { return this.manufacturer; }
    public MockPhoneBase setManufacturer(String manufacturer) {
        if(manufacturer != null) this.manufacturer = manufacturer;
        return this;
    }

    public String getCarrier() { return this.carrier; }
    public MockPhoneBase setCarrier(String carrier) {
        if(carrier != null) this.carrier = carrier;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return name + "::" + model + "::" + manufacturer + "::" + carrier;
    }
}
