package eu.faircode.xlua.api.xlua;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.database.DatabaseHelperEx;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.api.objects.xlua.packets.SettingPacket;

import eu.faircode.xlua.api.objects.xlua.app.xApp;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.api.objects.xlua.setting.xSetting;
import eu.faircode.xlua.api.objects.xlua.setting.xCategory;

public class XSettingsDatabase {
    private static final String TAG = "XLua.XSettingsDatabase";

    public static final String DEFAULT_THEME = "dark";
    public static final String DEFAULT_COLLECTIONS = "Privacy,PrivacyEx";


    public static boolean putSetting(XDataBase db, SettingPacket packet) {
        boolean result =
                packet.getValue() != null ?
                        DatabaseHelperEx.insertItem(
                                db,
                                xSetting.Table.name,
                                packet) :
                        DatabaseHelperEx.deleteItem(
                                DatabaseQuerySnake
                                    .create(db, xSetting.Table.name)
                                    .whereColumn("user", packet.getUser())
                                    .whereColumn("name", packet.getName()));

        return result;
    }

    public static boolean putSetting(Context context, String name, String value, boolean kill, XDataBase db) { return putSetting(context, 0, name, value, kill, db); }
    public static boolean putSetting(Context context, int user, String name, String value, boolean kill, XDataBase db) { return putSetting(context, user, "global", name, value, kill, db); }
    public static boolean putSetting(Context context, int user, String category, String name, String value, boolean kill, XDataBase db) {
        boolean result =
                DatabaseHelperEx.insertItem(
                        db,
                        xSetting.Table.name,
                        xSetting.create(user, category, name, value));
        if (!result && kill) {
            try {
                XAppProvider.forceStop(context, category, user);
            }catch (Throwable e) {
                Log.e(TAG, "Failed to Kill user=" + user + "\n" + e);
            }
        }

        return result;
    }

    public static boolean putSetting(Context context, xSetting setting, boolean kill, XDataBase db) throws Throwable {
        Log.i(TAG, "[putSetting] " + setting);

        boolean result =
                setting.getValue() != null ?
                        DatabaseHelperEx.insertItem(db, xSetting.Table.name, setting) :
                        DatabaseHelperEx.deleteItem(DatabaseQuerySnake
                                .create(db, xSetting.Table.name)
                                .whereColumn("user", setting.getUser())
                                .whereColumn("name", setting.getName()));

        if (!result && kill)
            XAppProvider.forceStop(context, setting.getCategory(), setting.getUser());

        return result;
    }

    public static boolean putSettings(Context context, XDataBase db, List<xSetting> settings) {
        return DatabaseHelperEx.insertItems(
                db,
                xSetting.Table.name,
                settings,
                prepareDatabaseTable(context, db));
    }

    public static boolean putSetting(Context context, XDataBase db, xSetting setting) {
        return DatabaseHelperEx.insertItem(
                db,
                xSetting.Table.name,
                setting,
                prepareDatabaseTable(context, db));
    }

    public static boolean prepareDatabaseTable(Context context, XDataBase db) {
        //if(context == null) return true;//Assume its handled
        return DatabaseHelperEx.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                xSetting.Table.name,
                xSetting.Table.columns,
                xSetting.class);
    }

    public static Collection<xSetting> getSettingsFromName(XDataBase db, String settingsName) {
        return DatabaseQuerySnake
                .create(db, xSetting.Table.name)
                .whereColumn("name", settingsName)
                .queryAs(xSetting.class, true);
    }

    public static Collection<xSetting> getSettings(XDataBase db, int userId, String categoryName) { return getSettings(db, new xCategory(userId, categoryName)); }
    public static Collection<xSetting> getSettings(XDataBase db, xCategory category) {
        return DatabaseQuerySnake
                .create(db, xSetting.Table.name)
                .whereColumns("user", "category")
                .whereColumnValues(Integer.toString(category.getUserId()), category.getName())
                .queryAs(xSetting.class, true);
    }

    public static Collection<xSetting> getSettingsByName(XDataBase db, int userId, String settingName) {
        return DatabaseQuerySnake
                .create(db, xSetting.Table.name)
                .whereColumns("user", "name")
                .whereColumnValues(Integer.toString(userId), settingName)
                .queryAs(xSetting.class, true);
    }

    public static boolean getSettingBoolean(XDataBase db, String category, String settingName) { return getSettingBoolean(db, XUtil.getUserId(Process.myUid()), category, settingName); }
    public static boolean getSettingBoolean(XDataBase db,  int userId, String category, String settingName) {
        return Boolean.parseBoolean(getSettingValue(db, userId, category, settingName));
    }

    public static String getSettingValue(XDataBase db, xSetting setting) { return getSettingValue(db, setting.getUser(), setting.getCategory(), setting.getName()); }
    public static String getSettingValue(XDataBase db, int userId, String category, String settingName) {
        String v = DatabaseQuerySnake.
                create(db, xSetting.Table.name)
                .whereColumns("user", "category", "name")
                .whereColumnValues(Integer.toString(userId), category, settingName)
                .onlyReturnColumn("value")
                .queryGetFirstAs(xSetting.class, true)
                .getValue();

        if(v == null) {
            if(settingName.equals("theme")) {
                SettingPacket packet = new SettingPacket(userId, category, settingName);
                packet.setValue(DEFAULT_THEME);
                putSetting(db, packet);
                return DEFAULT_THEME;
            }
            else if(settingName.equals("collection")) {
                SettingPacket packet = new SettingPacket(userId, category, settingName);
                packet.setValue(DEFAULT_COLLECTIONS);
                putSetting(db, packet);
                return DEFAULT_COLLECTIONS;
            }
        }

        return v;
    }

    public static xSetting getSetting(XDataBase db, xCategory category, String settingName) { return getSetting(db, category.getUserId(), category.getName(), settingName);  }
    public static xSetting getSetting(XDataBase db, int userId, String category, String settingName) {
        return DatabaseQuerySnake
                .create(db, xSetting.Table.name)
                .whereColumns("user", "category", "name")
                .whereColumnValues(Integer.toString(userId), category, settingName)
                .queryGetFirstAs(xSetting.class, true);
    }

    public static Collection<String> getCategoriesFromUID(XDataBase db, int userId) {
        return DatabaseQuerySnake
                .create(db, xSetting.Table.name)
                .whereColumn("user", Integer.toString(userId))
                .queryAsStringList("category", true);
    }

    public static Collection<xCategory> getCategories(XDataBase db) {
        return DatabaseQuerySnake
                .create(db, xSetting.Table.name)
                .onlyReturnColumns("user", "category")
                .queryAll(xCategory.class, true);
    }

    public static Map<Integer, List<String>> getCategoriesFromUid(XDataBase db){
        //return DatabaseHelper.getFromDatabase(db, "setting", XCategory.class);
        final Map<Integer, List<String>> categories = new HashMap<>();

        for(xCategory c : getCategories(db)) {
            List<String> names = categories.get(c.getUserId());
            if(names == null) {
                List<String> vs = new ArrayList<>();
                vs.add(c.getName());
                categories.put(c.getUserId(), vs);
            }else {
                if(!names.contains(c.getName()))
                    names.add(c.getName());
            }
        }

        return categories;
    }
}
