package eu.faircode.xlua.api.objects.xmock;

import androidx.annotation.NonNull;

public class ConfigSetting {
    protected String name;
    protected String value;
    protected Boolean enabled;

    public ConfigSetting() { }
    public ConfigSetting(String name, String value) { this(name, value, true); }
    public ConfigSetting(String name, String value, Boolean enabled) {
        setName(name);
        setValue(value);
        setEnabled(enabled);
    }

    public String getName() { return this.name; }
    public ConfigSetting setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public String getValue() { return this.value; }
    public ConfigSetting setValue(String value) {
        if(value != null) this.value = value;
        return this;
    }

    public Boolean isEnabled() { return this.enabled; }
    public ConfigSetting setEnabled(Boolean enabled) {
        if(enabled != null) this.enabled = enabled;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return name + "::" + value;
    }
}
