package eu.faircode.xlua.api.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;

import eu.faircode.xlua.AppGeneric;

public class LuaSettingPacket extends LuaSettingExtended  {
    public static LuaSettingPacket create(LuaSetting setting) { return new LuaSettingPacket(setting); }
    public static LuaSettingPacket create(LuaSetting setting, Integer code) { return new LuaSettingPacket(setting, code); }
    public static LuaSettingPacket create(LuaSetting setting, Integer code, Boolean kill) { return new LuaSettingPacket(setting, code, kill);}

    public static LuaSettingPacket create(LuaSettingDefault defaultSetting) { return new LuaSettingPacket(defaultSetting); }
    public static LuaSettingPacket create(LuaSettingDefault defaultSetting, Integer code) { return new LuaSettingPacket(defaultSetting, code); }
    public static LuaSettingPacket create(LuaSettingDefault defaultSetting, Integer code, Boolean kill) { return new LuaSettingPacket(defaultSetting, code, kill); }

    public static LuaSettingPacket create(LuaSettingExtended extended) { return new LuaSettingPacket(extended); }
    public static LuaSettingPacket create(LuaSettingExtended extended, Integer code) { return new LuaSettingPacket(extended, code); }
    public static LuaSettingPacket create(LuaSettingExtended extended, Integer code, Boolean kill) { return new LuaSettingPacket(extended, code, kill); }

    public static LuaSettingPacket create() { return new LuaSettingPacket(); }
    public static LuaSettingPacket create(String name, String value) { return new LuaSettingPacket(name, value); }
    public static LuaSettingPacket create(String name, String value, Integer code) {  return new LuaSettingPacket(name, value, code); }


    public static LuaSettingPacket create(String name, String defaultValue, String description) { return new LuaSettingPacket(name, defaultValue, description); }

    public static LuaSettingPacket create(Integer user, String category, String name, String value, Integer code) { return new LuaSettingPacket(user, category, name, value,  code); }
    public static LuaSettingPacket create(Integer user, String category, String name, String value, Integer code, Boolean kill) { return new LuaSettingPacket(user, category, name, value,  code, kill); }

    public static LuaSettingPacket create(Integer user, String category, String name) { return  new LuaSettingPacket(user, category, name); }

    public static LuaSettingPacket create(Integer user, String category, String name, String value) { return new LuaSettingPacket(user, category, name, value); }
    public static LuaSettingPacket create(Integer user, String category, String name, String value, String description, Integer code) { return new LuaSettingPacket(user, category, name, value, description, code); }
    public static LuaSettingPacket create(Integer user, String category, String name, String value, String description, Integer code, Boolean kill) { return new LuaSettingPacket(user, category, name, value, description, code, kill); }
    public static LuaSettingPacket create(Integer user, String category, String name, String value, String description, Integer code, Boolean kill, String defaultValue) { return new LuaSettingPacket(user, category, name, value, description, code, kill, defaultValue); }

    public static LuaSettingPacket createQueryRequest(Integer code) { return new LuaSettingPacket(GLOBAL_USER, GLOBAL_NAMESPACE, code); }
    public static LuaSettingPacket createQueryRequest(AppGeneric application, Integer code) { return new LuaSettingPacket(application.getUid(), application.getPackageName(), code); }
    public static LuaSettingPacket createQueryRequest(Integer user, String category, Integer code) { return new LuaSettingPacket(user, category, code); }

    public static LuaSettingPacket createQueryRequest() { return new LuaSettingPacket(GLOBAL_USER, GLOBAL_NAMESPACE, CODE_GET_MODIFIED); }
    public static LuaSettingPacket createQueryRequest(AppGeneric application, boolean getAll) { return new LuaSettingPacket(application.getUid(), application.getPackageName(), getCodeForQuery(getAll)); }
    public static LuaSettingPacket createQueryRequest(Integer user, String category, boolean getAll) { return new LuaSettingPacket(user, category,  getCodeForQuery(getAll)); }

    public static final int CODE_GET_VALUE = 0x0;
    public static final int CODE_INSERT_UPDATE_DEFAULT_AND_SETTING = 0x1;
    public static final int CODE_DELETE_SETTING = 0x2;
    public static final int CODE_GET_OBJECT = 0x3;
    public static final int CODE_GET_VALUE_OR_DEFAULT = 0x4;
    public static final int CODE_VERSION_ONE = 0x5;
    public static final int CODE_INSERT_UPDATE_DEFAULT_SETTING = 0x6;
    public static final int CODE_INSERT_UPDATE_SETTING = 0x7;

    public static final int CODE_DELETE_DEFAULT_SETTING = 0x8;
    public static final int CODE_DELETE_DEFAULT_AND_SETTING = 0x9;

    public static final int CODE_GET_ALL = 0x10;
    public static final int CODE_GET_MODIFIED = 0x11;

    public LuaSettingPacket(LuaSetting setting) { this(setting.getUser(), setting.getCategory(), setting.getName(), setting.getValue()); }
    public LuaSettingPacket(LuaSetting setting, Integer code) { this(setting.getUser(), setting.getCategory(), setting.getName(), setting.getValue(), null, code, null, null); }
    public LuaSettingPacket(LuaSetting setting, Integer code, Boolean kill) { this(setting.getUser(), setting.getCategory(), setting.getName(), setting.getValue(), null, code, kill, null); }

    public LuaSettingPacket(LuaSettingDefault defaultSetting) { this(null, null, defaultSetting.getName(), null, defaultSetting.getDescription(), null, null, defaultSetting.getDefaultValue(true)); }
    public LuaSettingPacket(LuaSettingDefault defaultSetting, Integer code) { this(null, null, defaultSetting.getName(), null, defaultSetting.getDescription(), code, null, defaultSetting.getDefaultValue(true)); }
    public LuaSettingPacket(LuaSettingDefault defaultSetting, Integer code, Boolean kill) { this(null, null, defaultSetting.getName(), null, defaultSetting.getDescription(), code, kill, defaultSetting.getDefaultValue(true)); }

    public LuaSettingPacket(LuaSettingExtended extended) { this(extended.getUser(), extended.getCategory(), extended.getName(), extended.getValue(), extended.getDescription(), null, null, null); }
    public LuaSettingPacket(LuaSettingExtended extended, Integer code) { this(extended.getUser(), extended.getCategory(), extended.getName(), extended.getValue(), extended.getDescription(), code, null, null); }
    public LuaSettingPacket(LuaSettingExtended extended, Integer code, Boolean kill) { this(extended.getUser(), extended.getCategory(), extended.getName(), extended.getValue(), extended.getDescription(), code, kill, null); }

    public LuaSettingPacket() { setUseUserIdentity(true); }


    public LuaSettingPacket(String name, String value) { this(null, null, name, value); }
    public LuaSettingPacket(String name, String value, Integer code) { this(null, null, name, value, null, code); }

    public LuaSettingPacket(String name, String defaultValue, String description) { this(null, null, name, null, description, null, null, defaultValue); }

    public LuaSettingPacket(Integer user, String category, Integer code) { this(user, category, null, null, null, code, null, null); }

    public LuaSettingPacket(Integer user, String category, String name, String value, Integer code) { this(user, category, name, value, null, code, null, null); }
    public LuaSettingPacket(Integer user, String category, String name, String value, Integer code, Boolean kill) { this(user, category, name, value, null, code, kill, null); }

    public LuaSettingPacket(Integer user, String category, String name) { this(user, category, name, null, null, null, null, null); }
    public LuaSettingPacket(Integer user, String category, String name, String value) { this(user, category, name, value, null, null, null, null); }
    public LuaSettingPacket(Integer user, String category, String name, String value, String description, Integer code) { this(user, category, name, value, description, code, null, null); }
    public LuaSettingPacket(Integer user, String category, String name, String value, String description, Integer code, Boolean kill) { this(user, category, name, value, description, code, kill, null); }
    public LuaSettingPacket(Integer user, String category, String name, String value, String description, Integer code, Boolean kill, String defaultValue) {
        this();
        setUser(user);
        setCategory(category);
        setName(name);
        setValue(value);
        setDescription(description);
        setCode(code);
        setKill(kill);
        setDefaultValue(defaultValue);
    }

    public boolean isGetValue() { return isCode(CODE_GET_VALUE);  }
    public boolean isInsertOrUpdateSetting() { return isCodes(true, CODE_INSERT_UPDATE_SETTING, CODE_INSERT_UPDATE_DEFAULT_AND_SETTING); }
    public boolean isInsertOrUpdateDefault() { return isCodes(true, CODE_INSERT_UPDATE_DEFAULT_SETTING, CODE_INSERT_UPDATE_DEFAULT_AND_SETTING); }


    public boolean isGetObject() { return isCode(CODE_GET_OBJECT); }
    public boolean isGetValueOrDefault() { return isCode(CODE_GET_VALUE_OR_DEFAULT); }
    public boolean isOriginalCall() {  return isCode(CODE_VERSION_ONE); }
    public boolean isDelete() { return isDeleteSetting() || isDeleteDefault(); }
    public boolean isDeleteSetting() { return (isNullOrEmptyCode() && value == null) || isCodes( CODE_DELETE_SETTING , CODE_DELETE_DEFAULT_AND_SETTING); }
    public boolean isDeleteDefault() { return isCodes(CODE_DELETE_DEFAULT_AND_SETTING , CODE_DELETE_DEFAULT_SETTING); }
    public boolean isGetAll() { return isCode(CODE_GET_ALL); }
    public boolean isGetModified() { return isCode(CODE_GET_MODIFIED); }


    public LuaSetting createSetting() { return LuaSetting.create(this);  }
    public LuaSettingDefault createDefault() { return LuaSettingDefault.create(this); }
    public LuaSettingExtended createExtended() { return LuaSettingExtended.create(this); }

    public LuaSettingPacket copyIdentification(AppGeneric application) { identificationFromApplication(application); return this; }
    public LuaSettingPacket setCodeEx(Integer code) { if(code != null) this.code = code; return this; }
    public LuaSettingPacket setKillEx(Boolean kill) { if(kill != null) this.kill = kill; return this; }

    @Override
    public Bundle toBundle() { return writePacketHeaderBundle(super.toBundle()); }

    @Override
    public void fromBundle(Bundle b) { if(b != null) { super.fromBundle(b); readPacketHeaderBundle(b); } }

    @NonNull
    @Override
    public String toString() { return new StringBuilder(super.toString()).append(" code=").append(code).toString(); }

    public static int getCodeInsertOrDelete(boolean delete) { return delete ? CODE_DELETE_SETTING : CODE_INSERT_UPDATE_SETTING; }
    public static int getCodeForQuery(boolean getAll) { return getAll ? CODE_GET_ALL : CODE_GET_MODIFIED; }
    public static int getCodeForGetValue(boolean getValueOrDefault) { return getValueOrDefault ? CODE_GET_VALUE_OR_DEFAULT : CODE_GET_VALUE; }
    public static int getCodeFromValue(String value) { return value == null ? CODE_DELETE_SETTING : CODE_INSERT_UPDATE_SETTING; }
    public static int getCodeForInsertOrUpdate(boolean setting, boolean defaultSetting) {  return setting && defaultSetting ? CODE_INSERT_UPDATE_DEFAULT_AND_SETTING : defaultSetting ? CODE_INSERT_UPDATE_DEFAULT_SETTING : CODE_INSERT_UPDATE_SETTING; }
    public static int getCodeForDeletion(boolean deleteSetting, boolean deleteDefault) { return deleteSetting && deleteDefault ? CODE_DELETE_DEFAULT_AND_SETTING : deleteDefault ? CODE_DELETE_DEFAULT_SETTING : CODE_DELETE_SETTING; }
}
