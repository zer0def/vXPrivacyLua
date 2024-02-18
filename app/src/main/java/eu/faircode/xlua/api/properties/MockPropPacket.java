package eu.faircode.xlua.api.properties;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.interfaces.IPacket;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.StringUtil;

public class MockPropPacket extends MockPropSetting {
    public static final int CODE_DELETE_PROP_MAP = 0x1;
    public static final int CODE_DELETE_PROP_SETTING = 0x2;
    public static final int CODE_DELETE_PROP_SETTING_AND_MAP = 0x3;
    public static final int CODE_INSERT_UPDATE_PROP_SETTING = 0x4;
    public static final int CODE_INSERT_UPDATE_PROP_MAP = 0x5;
    public static final int CODE_INSERT_UPDATE_PROP_MAP_AND_SETTING = 0x6;
    public static final int CODE_GET_ALL = 0x7;
    public static final int CODE_GET_MODIFIED = 0x8;
    public static final int CODE_DELETE_PROP_MAP_AND_SETTING = 0x9;

    public static MockPropPacket create() { return new MockPropPacket(); }
    public static MockPropPacket create(MockPropSetting setting) { return create(setting, null); }
    public static MockPropPacket create(MockPropSetting setting, Integer code) { return new MockPropPacket(setting.getUser(), setting.getCategory(), setting.getName(), setting.getSettingName(), setting.getValue(), code); }
    public static MockPropPacket create(MockPropMap map, Integer code) { return new MockPropPacket(map, code); }

    public static MockPropPacket create(String propertyName, String settingName, Integer value) { return new MockPropPacket(null, null, propertyName, settingName, value, null); }
    public static MockPropPacket create(String propertyName, String settingName, Integer value, Integer code) { return new MockPropPacket(null, null, propertyName, settingName, value, code); }
    public static MockPropPacket create(Integer user, String category, String propertyName, String settingName, Integer value, Integer code) { return new MockPropPacket(user, category, propertyName, settingName, value, code); }

    public static MockPropPacket createQueryRequest(AppGeneric application, boolean onlyGetModified) { return createQueryRequest(application.getUid(), application.getPackageName(), onlyGetModified); }
    public static MockPropPacket createQueryRequest(int user, String category, boolean onlyGetModified) {
        MockPropPacket packet = new MockPropPacket();
        packet.setUser(user);
        packet.setCategory(category);
        packet.setCode(onlyGetModified ? CODE_GET_MODIFIED : CODE_GET_ALL);
        Log.i("XLua.MockPropPacket", " data within => code=" + packet.getCode() + " user=" + packet.getUser() + " category=" + packet.getCategory() + " (params) user=" + user + " category=" + category + " onlygetmodifed=" + onlyGetModified);
        return packet;
    }

    public MockPropPacket(MockPropGroupHolder holder, String name, Integer value, Integer code) { this(holder.getUser(), holder.getPackageName(), name, holder.getSettingName(), value, code); }
    public MockPropPacket(MockPropSetting setting, Integer code) { this(setting.getUser(), setting.getCategory(), setting.getName(), setting.getSettingName(), setting.getValue(), code); }
    public MockPropPacket(MockPropMap map, Integer code) { this(null, null, map.getName(), map.getSettingName(), null, code); }

    public MockPropPacket() { setUseUserIdentity(true); }
    public MockPropPacket(Integer user, String category, String propertyName, String settingName, Integer value, Integer code) {
        this();
        setUser(user);
        setCategory(category);
        setName(propertyName);
        setSettingName(settingName);
        setValue(value);
        setCode(code);
    }

    public boolean isDeleteMap() { return isCodes(CODE_DELETE_PROP_MAP, CODE_DELETE_PROP_SETTING_AND_MAP, CODE_DELETE_PROP_MAP_AND_SETTING); }
    public boolean isDeleteSetting() { return isCodes(CODE_DELETE_PROP_SETTING, CODE_DELETE_PROP_SETTING_AND_MAP, CODE_DELETE_PROP_MAP_AND_SETTING); }
    public boolean isGetAll() { return isCode(CODE_GET_ALL); }
    public boolean isGetModified() { return isCode(CODE_GET_MODIFIED); }

    public MockPropMap createMap() { return MockPropMap.create(this); }
    public MockPropSetting createSetting() { return MockPropSetting.create(this); }

    @Override
    public Bundle toBundle() { return writePacketHeaderBundle(super.toBundle()); }

    @Override
    public void fromBundle(Bundle b) {
        super.fromBundle(b);
        readPacketHeaderBundle(b);
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder(super.toString())
                .append(" code=")
                .append(code).toString();
    }
}
