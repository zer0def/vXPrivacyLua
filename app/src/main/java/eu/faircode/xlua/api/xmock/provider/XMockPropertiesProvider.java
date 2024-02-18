package eu.faircode.xlua.api.xmock.provider;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.DebugUtil;
import eu.faircode.xlua.XDatabase;

import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.utilities.MockUtils;
import eu.faircode.xlua.utilities.StringUtil;

/*public class XMockPropertiesProvider {
    private static final String TAG = "XLua.XMockPropertiesProvider";

    private static Map<String, XMockPropGroup> groups = new HashMap<>();
    private static Map<String, XMockPropMapped> properties = new HashMap<>();
    private static final Object lock = new Object();

    public static XMockPropGroup getSettingGroup(Context context, XDatabase db, String settingName) {
        if(properties == null || properties.isEmpty())
            initCache(context, db);

        if(DebugUtil.isDebug())
            Log.i(TAG, "[getSettingGroup] setting=" + settingName);

        XMockPropGroup group = groups.get(settingName);
        if(settingName == null)
            return null;

        return group;
    }

    public static String getPropertySettingName(Context context, XDatabase db, String propertyName) {
        if(properties == null || properties.isEmpty())
            initCache(context, db);

        if(DebugUtil.isDebug())
            Log.i(TAG, "[getPropertySettingName] propertyName=" + propertyName);

        XMockPropMapped setting = properties.get(propertyName);
        if(setting == null || !setting.isEnabled())
            return null;

        return setting.getSettingName();
    }

    public static Collection<XMockPropMapped> getAllProperties(Context context, XDatabase db) {
        if(properties == null || properties.isEmpty())
            initCache(context, db);

        //maybe add filters ?
        //then for each without a group
        return properties.values();
    }


    public static boolean setGroupState(Context context, XDatabase db, XMockPropMapped setting) { return setGroupState(context, db, setting.getSettingName(), setting.isEnabled()); }
    public static boolean setGroupState(Context context, XDatabase db, String settingName, Boolean enabled) {
        if(enabled == null)
            return false;

        if(properties == null || properties.isEmpty())
            initCache(context, db);

        if(DebugUtil.isDebug())
            Log.i(TAG, "setting group state for [" + settingName + "] to [" + enabled + "]");

        XMockPropGroup group = groups.get(settingName);
        if(group != null) {
            for(XMockPropMapped setting : group.getProperties())
                setting.setIsEnabled(enabled);

            if(DebugUtil.isDebug())
                Log.i(TAG, "SIZE of PROPERTIES TO SET=" + group.getProperties().size());

            return DatabaseHelp.insertItems(db, XMockPropMapped.Table.name, group.getProperties());
        }

        return false;
    }

    public static Collection<XMockPropGroup> getGroups(Context context, XDatabase db, String packageName) { return getGroups(context, db, 0, packageName); }
    public static Collection<XMockPropGroup> getGroups(Context context, XDatabase db, Integer uid, String packageName) {
        if(packageName == null)
            packageName = "Global";

        if(uid == null)
            uid = 0;

        if(DebugUtil.isDebug())
            Log.i(TAG, " uid=" + uid + " packageName=" + packageName);

        Collection<XMockPropGroup> groups = new ArrayList<>(initCache(context, db));
        if(DebugUtil.isDebug())
            Log.i(TAG, "groups=" + groups.size());

        for(XMockPropGroup group : groups) {
            String settingName = group.getSettingName();
            String value = XLuaSettingsDatabase.getSettingValue(context, db, settingName, uid, packageName);
            if(value != null)
                group.setValue(value);
            else {
                value = XMockSettingsProvider.getDefaultSettingValue(context, db, settingName);
                if(value != null)
                    group.setValue(value);
                else {
                    Log.e(TAG, " no default setting for [" + settingName + "]");
                }
            }

            if(DebugUtil.isDebug())
                Log.i(TAG, " value=" + value + " group=" + group + " group value=" + group.getValue() + " settings=" + group.getProperties().size());
        }

        if(DebugUtil.isDebug())
            Log.i(TAG, "size of groups=" + groups.size());

        return groups;
    }

    public static XMockProp getMockPropValue(Context context, XDatabase db, XMockProp prop) {
        if(prop == null)
            return (XMockProp) new XMockProp().setValue(MockUtils.NOT_BLACKLISTED);

        if(DebugUtil.isDebug())
            Log.i(TAG, "[getMockPropValue]=" + prop);

        if(prop.getPropertyName() == null) {
            prop.setValue(MockUtils.NOT_BLACKLISTED);
            return prop;
        }

        String settingName = getPropertySettingName(context, db, prop.getPropertyName());
        if(settingName == null)
            prop.setValue(MockUtils.NOT_BLACKLISTED);
        else {
            String category = StringUtil.isValidString(prop.getPackageName()) ? prop.getPackageName() : "global";
            String value = XLuaSettingsDatabase.getSettingValue(context, db, settingName, XLuaSettingsDatabase.GLOBAL_USER, category);
            if(value == null) {
                XMockPropGroup group = getSettingGroup(context, db, settingName);
                if(group == null)
                    prop.setValue(MockUtils.NOT_BLACKLISTED);
                else {
                    String gValue = group.getValue();
                    if(gValue == null) prop.setValue(MockUtils.NOT_BLACKLISTED);
                    else prop.setValue(gValue);
                }
            }
        }

        return prop;
    }

    public static Collection<XMockPropGroup> initCache(Context context, XDatabase db) {
        if(properties == null || properties.isEmpty() || groups == null || groups.isEmpty()) {
            synchronized (lock) {
                if(groups == null || groups.isEmpty()) {
                    Map<String, XMockPropGroup> cGroups = new HashMap<>();
                    Map<String, XMockPropMapped> cPropSettings = new HashMap<>();

                    Collection<XMockPropMapped> settings = XMockPropertiesDatabase.initDatabase(context, db);
                    for(XMockPropMapped setting : settings) {
                        String settingName = setting.getSettingName();
                        String propName = setting.getPropertyName();
                        if(!cPropSettings.containsKey(propName)) {
                            cPropSettings.put(propName, setting);

                            if(cGroups.containsKey(settingName)) {
                                XMockPropGroup grp = cGroups.get(settingName);
                                assert grp != null;
                                grp.addProperty(setting);
                            }else {
                                XMockPropGroup group = new XMockPropGroup(settingName);
                                group.addProperty(setting);
                                cGroups.put(settingName, group);
                            }
                        }else {
                            Log.e(TAG, "Property already exist ... property=" + propName + " setting=" + settingName);
                        }
                    }

                    if (DebugUtil.isDebug())
                        Log.i(TAG, "Internal cache finished: properties=" + cPropSettings.size() + "  groups=" + cGroups.size());

                    properties = cPropSettings;
                    groups = cGroups;
                }
            }
        }

        return groups.values();
    }
}*/
