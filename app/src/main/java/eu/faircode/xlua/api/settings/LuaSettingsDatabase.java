package eu.faircode.xlua.api.settings;

import android.content.Context;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.XGlobals;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.api.standard.UserIdentityPacket;
import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;
import eu.faircode.xlua.api.xlua.provider.XLuaAppProvider;
import eu.faircode.xlua.utilities.CollectionUtil;

public class LuaSettingsDatabase {
    private static final String TAG = "XLua.LuaSettingsDatabase";
    private static final String JSON = "settingdefaults.json";
    private static final int COUNT = 118;

    public static final String DEFAULT_THEME = "dark";
    public static final String DEFAULT_COLLECTIONS = "Privacy,PrivacyEx";

    public static XResult putSetting(Context context, XDatabase db, String settingName) { return putSetting(context, db, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE, settingName, null, false); }
    public static XResult putSetting(Context context, XDatabase db, String settingName, String value) { return putSetting(context, db, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE, settingName, value, false); }
    public static XResult putSetting(Context context, XDatabase db, Integer user, String category, String settingName) { return putSetting(context, db, user, category, settingName, null); }
    public static XResult putSetting(Context context, XDatabase db, Integer user, String category, String settingName, String value) { return putSetting(context, db, user, category, settingName, value, false); }
    public static XResult putSetting(Context context, XDatabase db, Integer user, String category, String settingName, String value, Boolean kill) { return putSetting(context, db, LuaSettingPacket.create(user, category, settingName, value, null, LuaSettingPacket.getCodeFromValue(value), kill)); }
    public static XResult putSetting(Context context, XDatabase db, LuaSettingPacket setting) {
        Log.i(TAG, " putSetting packet=" + setting);
        XResult res = XResult.create().setMethodName("putSetting").setExtra(setting.toString());
        setting.ensureIdentification();
        boolean result =
                !setting.isDelete() ?
                        DatabaseHelp.insertItem(
                                db,
                                LuaSetting.Table.name,
                                LuaSetting.create(setting)) :
                        DatabaseHelp.deleteItem(
                                SqlQuerySnake
                                        .create(db, LuaSetting.Table.name)
                                        .whereColumn("user", setting.getUser())
                                        .whereColumn("category", setting.getCategory())
                                        .whereColumn("name", setting.getName()));//we should query via packageName

        /*if(setting.isDelete() && !result)
            result = SqlQuerySnake.create(db, LuaSetting.Table.name)
                    .whereColumn("user", setting.getUser())
                    .whereColumn("category", setting.getCategory())
                    .whereColumn("name", setting.getName())
                    .exists();*/

        if (result && setting.isKill())
            XLuaAppProvider.forceStop(context, setting.getCategory(), setting.getUser(), res);

        return res.setResult(result);
    }

    /*public static XResult putSettings(Context context, XDatabase db, List<LuaSetting> settings) { return putSettings(context, db, settings, false); }
    public static XResult putSettings(Context context, XDatabase db, List<LuaSetting> settings, boolean kill) {
        XResult res = XResult.create().setMethodName("putSettings");
        if(!CollectionUtil.isValid(settings))
            return res.setFailed("Settings List was empty or null...");

        String category = settings.get(0).getCategory();
        int user = settings.get(0).getUser();
        boolean result = DatabaseHelp.insertItems(
                db,
                LuaSetting.Table.name,
                settings,
                prepareDatabaseTable(db));

        if (result && kill)
            XLuaAppProvider.forceStop(context, category, user, res);

        return res.setResult(result);
    }*/

    public static boolean getSettingBoolean(Context context, XDatabase db, String settingName) { return Boolean.parseBoolean(getSettingValue(context, db, settingName, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE)); }
    public static boolean getSettingBoolean(Context context, XDatabase db, String settingName, int user, String category) { return Boolean.parseBoolean(getSettingValue(context, db, settingName, user, category)); }

    public static String getSettingValue(Context context, XDatabase db, String settingName) { return getSettingValue(context, db, settingName, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE); }
    public static String getSettingValue(Context context, XDatabase db, String settingName, int user, String category) {  return getSettingValue(context, db, LuaSettingPacket.create(user, category, settingName)); }
    public static String getSettingValue(Context context, XDatabase db, LuaSettingPacket packet) {
        packet.ensureIdentification();
        String v = SqlQuerySnake
                .create(db, LuaSetting.Table.name)
                .whereColumn("user", packet.getUser())
                .whereColumn("category", packet.getCategory())
                .whereColumn("name", packet.getName())
                .queryGetFirstString("value", true);

        Log.i(TAG, "within getSettingValue packet=" + packet + " v=" + v + " isglobal=" + packet.isGlobal() + "  code=" + packet.getCode());

        if(v == null) {
            if(packet.getName().equalsIgnoreCase("theme")) {
                putSetting(context, db, LuaSettingPacket.create(packet.getName(), DEFAULT_THEME, LuaSettingPacket.CODE_INSERT_UPDATE_SETTING));
                return DEFAULT_THEME;
            }
            else if(packet.getName().equalsIgnoreCase("collection")) {
                putSetting(context, db, LuaSettingPacket.create(packet.getName(), DEFAULT_COLLECTIONS, LuaSettingPacket.CODE_INSERT_UPDATE_SETTING));
                return DEFAULT_COLLECTIONS;
            }else if(!packet.isGlobal()) {
                v = SqlQuerySnake
                        .create(db, LuaSetting.Table.name)
                        .whereColumn("user", UserIdentityPacket.GLOBAL_USER)
                        .whereColumn("category", UserIdentityPacket.GLOBAL_NAMESPACE)
                        .whereColumn("name", packet.getName())
                        .queryGetFirstString("value", true);
            }

            if(v == null && packet.isGetValueOrDefault())
                return getDefaultMappedSettingValue(db, packet.getName());
        }

        return v;
    }

    public static Collection<LuaSetting> getSettings(XDatabase db, LuaSettingPacket packet) { return getSettings(db, packet.getUser(), packet.getCategory()); }
    public static Collection<LuaSetting> getSettings(XDatabase db, int user, String category) {
        return SqlQuerySnake
                .create(db, LuaSetting.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("user", user)
                .whereColumn("category", category)
                .queryAs(LuaSetting.class, true);
    }

    public static Collection<LuaSettingExtended> getAllSettings(Context context, XDatabase db, LuaSettingPacket packet) { return getAllSettings(context, db, packet.getUser(), packet.getCategory());  }
    public static Collection<LuaSettingExtended> getAllSettings(Context context, XDatabase db, int user, String category) {
        HashMap<String, LuaSettingExtended> allSettings = new HashMap<>();

        Collection<LuaSettingDefault> mappedSettings = getMappedSettings(context, db);
        Collection<LuaSetting> userSettings = getSettings(db, user, category);

        Log.i(TAG, "Settings query for (user=" + user + " pkg=" + category + ") size=" + userSettings.size());
        for(LuaSettingDefault s : mappedSettings) {
            LuaSettingExtended ss = new LuaSettingExtended(s);
            ss.setValueForce(null);
            allSettings.put(s.getName(), ss);
        }

        Log.i(TAG, "current map settings=" + allSettings.size() + " mapped settings=" + mappedSettings.size() + " user settings=" + userSettings.size());
        if(CollectionUtil.isValid(userSettings)) {
            for(LuaSetting s : userSettings) {
                String sName = s.getName();
                LuaSettingDefault set = allSettings.get(sName);
                if(set != null) {
                    set.setValue(s.getValue());
                }else {
                    allSettings.put(s.getName(), new LuaSettingExtended(s));
                }
            }
        }

        Log.i(TAG, "current map settings=" + allSettings.size() + " [before hook settings parse]");
        Collection<XLuaHook> hooks = XGlobals.getHooks(context, db, true);
        for(XLuaHook hook : hooks) {
            String[] settings = hook.getSettings();
            if(settings == null || settings.length < 1)
                continue;

            for(String s : settings) {
                if(!allSettings.containsKey(s)) {
                    LuaSettingExtended obj = new LuaSettingExtended();
                    obj.setUser(user);
                    obj.setCategory(category);
                    obj.setName(s);

                    allSettings.put(s, obj);
                }
            }
        }

        Log.i(TAG, "current map settings=" + allSettings.size() + "  after hook settings parsed!");
        return allSettings.values();
    }

    public static LuaSetting getSetting(XDatabase db, String settingName, int user, String category) { return getSetting(db, LuaSettingPacket.create(user, category, settingName)); }
    public static LuaSetting getSetting(XDatabase db, LuaSettingPacket packet) {
        packet.ensureIdentification();
        return SqlQuerySnake
                .create(db, LuaSetting.Table.name)
                .whereColumns("user", "category", "name")
                .whereColumnValues(Integer.toString(packet.getCode()), packet.getCategory(), packet.getName())
                .queryGetFirstAs(LuaSetting.class, true);
    }

    public static XResult putDefaultMappedSetting(Context context, XDatabase db, LuaSettingPacket setting) {
        XResult res = XResult.create().setMethodName("putDefaultSetting").setExtra(setting.toString());
        boolean result =
                !setting.isDeleteDefault() ?
                        DatabaseHelp.insertItem(
                                db,
                                LuaSettingDefault.Table.name,
                                setting.createDefault()) :
                        DatabaseHelp.deleteItem(
                                SqlQuerySnake
                                        .create(db, LuaSettingDefault.Table.name)
                                        .whereColumn("name", setting.getName()));

        if(setting.isDeleteDefault() && !result)
            result = SqlQuerySnake.create(db, LuaSettingDefault.Table.name)
                    .whereColumn("name", setting.getName())
                    .exists();

        Log.i(TAG, "Default packet insert return=" + result);
        return res.setResult(result);
    }

    public static String getDefaultMappedSettingValue(XDatabase db, String settingName) {
        return SqlQuerySnake
                .create(db, LuaSettingDefault.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("name", settingName)
                .queryGetFirstString("value", true);
    }

    public static LuaSettingDefault getMappedSetting(Context context, XDatabase db, String settingName) {
        return SqlQuerySnake
                .create(db, LuaSettingDefault.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("name", settingName)
                .queryGetFirstAs(LuaSettingDefault.class, true);
    }

    public static Collection<LuaSettingDefault> getMappedSettings(Context context, XDatabase db) {
        return DatabaseHelp.getOrInitTable(
                context,
                db,
                LuaSettingDefault.Table.name,
                LuaSettingDefault.Table.columns,
                JSON,
                true,
                LuaSettingDefault.class,
                COUNT);
    }

    public static boolean prepareDatabaseTable(XDatabase db) { return DatabaseHelp.prepareDatabase(db, LuaSetting.Table.name, LuaSetting.Table.columns); }
    public static boolean forceDatabaseCheck(Context context, XDatabase db) {
        return DatabaseHelp.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                LuaSettingDefault.Table.name,
                LuaSettingDefault.Table.columns,
                JSON,
                true,
                LuaSettingDefault.class,
                DatabaseHelp.DB_FORCE_CHECK);
    }
}
