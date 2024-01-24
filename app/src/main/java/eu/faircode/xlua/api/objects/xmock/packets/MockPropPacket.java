package eu.faircode.xlua.api.objects.xmock.packets;

import android.os.Bundle;

import eu.faircode.xlua.api.objects.xmock.prop.MockProp;

public class MockPropPacket extends MockProp {
    protected Integer code;

    public MockPropPacket() { }
    public MockPropPacket(String name, String value) { super(name, value); }
    public MockPropPacket(String name, Boolean enabled) { super(name, enabled); }
    public MockPropPacket(String name, String value, Boolean enabled) { super(name, value,  enabled); }
    public MockPropPacket(String name, String value, String defaultValue, Boolean enabled) { super(name, value, defaultValue, enabled); }
    public MockPropPacket(String name, String value, String defaultValue, Boolean enabled, Integer code) {
        super(name, value, defaultValue, enabled);
        setCode(code);
    }

    @Override
    public Bundle toBundle() {
        Bundle b = super.toBundle();
        if(code != null) b.putInt("code", code);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        super.fromBundle(b);
        if(b.containsKey("code")) code = b.getInt("code");
    }

    public MockPropPacket setCode(Integer code) {
        if(code != null) this.code = code;
        return this;
    }

    public Integer getCode() { return this.code; }
}
