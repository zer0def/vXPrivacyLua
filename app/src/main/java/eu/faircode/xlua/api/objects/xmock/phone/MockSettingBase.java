package eu.faircode.xlua.api.objects.xmock.phone;

import androidx.annotation.NonNull;

public class MockSettingBase {
    protected String name;
    protected String value;

    public MockSettingBase() {  }
    public MockSettingBase(String name, String value) {
        setName(name);
        setValue(value);
    }

    public String getName() { return this.name; }
    public String getValue() { return this.value; }

    public MockSettingBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public MockSettingBase setValue(String value) {
        if(value != null) this.value = value;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return name + "::" + value;
    }
}
