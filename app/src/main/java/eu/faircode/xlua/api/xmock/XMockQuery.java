package eu.faircode.xlua.api.xmock;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.api.configs.MockConfig;
import eu.faircode.xlua.api.properties.MockPropMap;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropSetting;


import eu.faircode.xlua.api.settings.LuaSetting;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.UserIdentityPacket;
import eu.faircode.xlua.api.useragent.MockUserAgent;
import eu.faircode.xlua.api.xmock.query.GetMockAgentsCommand;
import eu.faircode.xlua.api.xmock.query.GetMockConfigsCommand;
import eu.faircode.xlua.api.xmock.query.GetMockPropMapsCommand;
import eu.faircode.xlua.api.xmock.query.GetMockPropertiesCommand;
import eu.faircode.xlua.api.xmock.query.GetMockSettingsCommand;
import eu.faircode.xlua.utilities.CursorUtil;

public class XMockQuery {
    private static final String TAG = "XLua.XMockQuery";


    public static Collection<MockUserAgent> getUserAgents(Context context, String device) { return CursorUtil.readCursorAs(GetMockAgentsCommand.invoke(context, device, true), true, MockUserAgent.class); }
    public static Collection<MockUserAgent> getUserAgents(Context context, String device, boolean marshall) { return CursorUtil.readCursorAs(GetMockAgentsCommand.invoke(context, device, marshall), marshall, MockUserAgent.class); }

    public static Map<String, String> getMockPropMapsMap(Context context) { return getMockPropMapsMap(context, true, null, true); }
    public static Map<String, String> getMockPropMapsMap(Context context, boolean marshall) { return  getMockPropMapsMap(context, marshall, null, true); }
    public static Map<String, String> getMockPropMapsMap(Context context, boolean marshall, Map<String, String> settings, boolean ignoreEmpty) {
        ArrayList<MockPropMap> propMaps = new ArrayList<>(CursorUtil.readCursorAs(GetMockPropMapsCommand.invoke(context, marshall), marshall, MockPropMap.class));
        Map<String, String>  maps = new HashMap<>();
        if(settings != null && !settings.isEmpty()) {
            for(MockPropMap map : propMaps) {
                if(settings.containsKey(map.getSettingName()))
                    maps.put(map.getName(), map.getSettingName());
            }
        }else if(ignoreEmpty) {
            for(MockPropMap map : propMaps)
                maps.put(map.getName(), map.getSettingName());
        }

        Log.i(TAG, "[getMockPropMaps] prop maps size=" + propMaps.size() + " maps size=" + maps.size());
        return maps;
    }

    /**
     * Get a Collection of the Build Properties that have been modified
     * @param context
     * @param application Application Info where Modified Properties are saved UnderApplication Info where Modified Properties are saved Under
     * @return Collection of the Modified Properties only.
     */
    public static Collection<MockPropSetting> getModifiedProperties(Context context, AppGeneric application) { return getProperties(context, true, MockPropPacket.createQueryRequest(application, true)); }
    /**
     * Get a Collection of the Build Properties that have been modified
     * @param context
     * @param user User ID where Properties are Saved too
     * @param category Category or Package name Where properties are Saved too
     * @return Collection of the Modified Properties only.
     */
    public static Collection<MockPropSetting> getModifiedProperties(Context context, int user, String category) { return getProperties(context, true, MockPropPacket.createQueryRequest(user, category, true)); }
    /**
     * Get a Collection of the Build Properties that have been modified
     * @param context
     * @param user User ID where Properties are Saved too
     * @param marshall Marshall Data instead of using JSON Parsing use Parcel
     * @param category Category or Package name Where properties are Saved too
     * @return Collection of the Modified Properties only.
     */
    public static Collection<MockPropSetting> getModifiedProperties(Context context, boolean marshall, int user, String category) { return getProperties(context, marshall, MockPropPacket.createQueryRequest(user, category, true)); }


    /**
     * Get a Collection of the Build Properties that have been modified as well as Unmodified but Mapped Properties (all)
     * @param context
     * @param application Application Info where Modified Properties are saved Under
     * @return Collection of the Modified Properties AND Unmodified Properties.
     */
    public static Collection<MockPropSetting> getAllProperties(Context context, AppGeneric application) { return getProperties(context, true, MockPropPacket.createQueryRequest(application, false));}
    /**
     * Get a Collection of the Build Properties that have been modified as well as Unmodified but Mapped Properties (all)
     * @param context
     * @param user User ID where Properties are Saved too
     * @param category Category or Package name Where properties are Saved too
     * @return Collection of the Modified Properties AND Unmodified Properties.
     */
    public static Collection<MockPropSetting> getAllProperties(Context context, int user, String category) { return getProperties(context, true, MockPropPacket.createQueryRequest(user, category, false)); }
    /**
     * Get a Collection of the Build Properties that have been modified as well as Unmodified but Mapped Properties (all)
     * @param context
     * @param user User ID where Properties are Saved too
     * @param marshall Marshall Data instead of using JSON Parsing use Parcel
     * @param category Category or Package name Where properties are Saved too
     * @return Collection of the Modified Properties AND Unmodified Properties.
     */
    public static Collection<MockPropSetting> getAllProperties(Context context, boolean marshall, int user, String category) { return getProperties(context, marshall, MockPropPacket.createQueryRequest(user, category, false)); }


    /**
     * Get a Collection of the Build Properties
     * @param context
     * @param packet Packet to be used for the Command
     * @return Collection of the Properties
     */
    public static Collection<MockPropSetting> getProperties(Context context, MockPropPacket packet) { return getProperties(context, true, packet); }
    /**
     * Get a Collection of the Build Properties
     * @param context
     * @param packet Packet to be used for the Command
     * @param marshall Marshall Data instead of using JSON Parsing use Parcel
     * @return Collection of the Properties
     */
    public static Collection<MockPropSetting> getProperties(Context context, boolean marshall, MockPropPacket packet) { return CursorUtil.readCursorAs(GetMockPropertiesCommand.invoke(context, marshall, packet), marshall, MockPropSetting.class); }

    public static Collection<LuaSetting> getSettings(Context context, AppGeneric application) { return getSettings(context, application.getUid(), application.getPackageName()); }
    public static Collection<LuaSetting> getSettings(Context context) { return getSettings(context, true, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE); }
    public static Collection<LuaSetting> getSettings(Context context, int user, String category) { return getSettings(context, true, user, category); }
    public static Collection<LuaSetting> getSettings(Context context, boolean marshall, int user, String category) { return CursorUtil.readCursorAs(GetMockSettingsCommand.invoke(context, marshall, LuaSettingPacket.createQueryRequest(user, category, false)), marshall, LuaSetting.class); }

    public static Collection<LuaSettingExtended> getAllSettings(Context context, AppGeneric application) { return getAllSettings(context, application.getUid(), application.getPackageName()); }
    public static Collection<LuaSettingExtended> getAllSettings(Context context) { return getAllSettings(context, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE); }
    public static Collection<LuaSettingExtended> getAllSettings(Context context, int user, String category) { return getAllSettings(context, true, user, category); }
    public static Collection<LuaSettingExtended> getAllSettings(Context context, boolean marshall, int user, String category) { return CursorUtil.readCursorAs(GetMockSettingsCommand.invoke(context, marshall, LuaSettingPacket.createQueryRequest(user, category, true)), marshall, LuaSettingExtended.class); }

    public static Collection<MockConfig> getConfigs(Context context) { return getConfigs(context, true); }
    public static Collection<MockConfig> getConfigs(Context context, boolean marshall) { return CursorUtil.readCursorAs(GetMockConfigsCommand.invoke(context, marshall), marshall, MockConfig.class); }
}
