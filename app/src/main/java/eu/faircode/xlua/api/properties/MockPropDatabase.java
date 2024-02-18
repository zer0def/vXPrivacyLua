package eu.faircode.xlua.api.properties;

import android.content.Context;

import java.util.Collection;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;

public class MockPropDatabase {
    private static final String TAG = "XLua.MockPropDatabase";
    private static final String JSON = "propmaps.json";

    public static XResult putPropertySetting(XDatabase db, MockPropPacket packet) { return putPropertySetting(db, packet.createSetting(), packet.isDeleteSetting()); }
    public static XResult putPropertySetting(XDatabase db, MockPropSetting setting, boolean delete) {
        return XResult.create()
                .setMethodName("putMockPropSetting")
                .setExtra(setting.toString())
                .setResult(delete ?
                        DatabaseHelp.deleteItem(setting.createQuery(db)) :
                        DatabaseHelp.insertItem(db, MockPropSetting.Table.name, setting,
                                DatabaseHelp.prepareDatabase(db, MockPropSetting.Table.name, MockPropSetting.Table.columns)));
    }

    public static int getPropertySettingCode(XDatabase db, MockPropSetting setting) { return getPropertySettingCode(db, setting.getName(), setting.getUser(), setting.getCategory()); }
    public static int getPropertySettingCode(XDatabase db, String propertyName, int userId, String packageName) {
        return SqlQuerySnake.create(db, MockPropSetting.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("user", userId)
                .whereColumn("category", packageName)
                .whereColumn("name", propertyName)
                .queryGetFirstInt("value", MockPropSetting.PROP_NULL, true);
    }

    public static Collection<MockPropSetting> getPropertySettingsForUser(XDatabase db, MockPropSetting setting) { return getPropertySettingsForUser(db, setting.getUser(), setting.getCategory()); }
    public static Collection<MockPropSetting> getPropertySettingsForUser(XDatabase db, int userId, String packageName) {
        return SqlQuerySnake.create(db, MockPropSetting.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("user", userId)
                .whereColumn("category", packageName)
                .queryAs(MockPropSetting.class, true);
    }

    public static XResult putSettingMapForProperty(XDatabase db, MockPropPacket packet) { return putSettingMapForProperty(db, packet.createMap(), packet.isDeleteMap()); }
    public static XResult putSettingMapForProperty(XDatabase db, MockPropMap mapSetting, boolean delete) {
        return XResult.create()
                .setMethodName("putMockPropMap")
                .setExtra(mapSetting.toString())
                .setResult(delete ?
                        DatabaseHelp.deleteItem(mapSetting.createQuery(db)) :
                        DatabaseHelp.insertItem(db, MockPropMap.Table.name, mapSetting,
                                DatabaseHelp.prepareDatabase(db, MockPropMap.Table.name, MockPropMap.Table.columns)));
    }

    public static Collection<MockPropMap> getPropertiesForSettingMap(XDatabase db, String settingName) {
        return SqlQuerySnake.create(db, MockPropMap.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("settingName", settingName)
                .queryAs(MockPropMap.class, true);
    }

    public static String getSettingMapNameForProperty(XDatabase db, String propertyName) {
        return SqlQuerySnake.create(db, MockPropMap.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("name", propertyName)
                .queryGetFirstString("settingName", true);
    }

    public static MockPropMap getSettingMapForProperty(XDatabase db, String propertyName) {
        return SqlQuerySnake.create(db, MockPropMap.Table.name)
                .ensureDatabaseIsReady()
                .whereColumn("name", propertyName)
                .queryGetFirstAs(MockPropMap.class, true);
    }

    public static boolean ensurePropSettingsDatabase(Context context, XDatabase db) {
        return DatabaseHelp.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                MockPropSetting.Table.name,
                MockPropSetting.Table.columns,
                MockPropSetting.class);
    }

    public static Collection<MockPropMap> forceCheckMapsDatabase(Context context, XDatabase db) {
        return DatabaseHelp.initDatabaseLists(
                context,
                db,
                MockPropMap.Table.name,
                MockPropMap.Table.columns,
                JSON,
                true,
                MockPropGroupHolder.class,
                MockPropMap.class,
                true);
    }
}
