package eu.faircode.xlua.api.configs;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.List;

import eu.faircode.xlua.api.settings.LuaSettingExtended;

public class MockConfigPacket extends MockConfig {
    public static MockConfigPacket create(String name, List<LuaSettingExtended> settings) { return new MockConfigPacket(name, settings); }
    public static final int CODE_INSERT_UPDATE_CONFIG = 0x1;
    public static final int CODE_DELETE_CONFIG = 0x2;
    public static final int CODE_APPLY_CONFIG = 0x3;

    public boolean isDelete() { return isCode(CODE_DELETE_CONFIG); }
    public boolean isInsertOrUpdate() { return isCode(true, CODE_INSERT_UPDATE_CONFIG); }

    public MockConfigPacket() { setUseUserIdentity(false); }
    public MockConfigPacket(String name, List<LuaSettingExtended> settings) {
        this();
        setName(name);
        setSettings(settings);
    }

    public MockConfig createConfig() { return MockConfig.create(this); }

    @Override
    public Bundle toBundle() { return writePacketHeaderBundle(super.toBundle()); }

    @Override
    public void fromBundle(Bundle b) { if(b != null) { super.fromBundle(b); readPacketHeaderBundle(b); } }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder(super.toString())
                .append(" code=")
                .append(code).toString();
    }
}
