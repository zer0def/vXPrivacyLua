package eu.faircode.xlua.api.xmock.database;

import android.content.Context;

import java.util.Collection;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.XResult;

import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;

/*public class XMockSettingsDatabase {
    private static final String TAG = "XLua.XMockSettingsDatabase";
    private static final String JSON = "settingdefaults.json";
    private static final int COUNT = 116;

    public static int getPropSettingCode(XDatabase db, String propName) { return getPropSettingCode(db, XMockPropSetting.create(propName));  }
    public static int getPropSettingCode(XDatabase db, String propName, int user, String packageName) { return getPropSettingCode(db, XMockPropSetting.create(propName, user, packageName)); }
    public static int getPropSettingCode(XDatabase db, XMockPropSetting setting) {
        return setting.createQuery(db)
                .queryGetFirstInt("value", XMockPropSetting.PROP_NULL, true);
    }

    public static XResult putPropSetting(XDatabase db, XMockPropSetting setting) {
        return XResult.create().setMethodName("putPropSetting").setExtra(setting.toString())
                .setResult(setting.getValue() == XMockPropSetting.PROP_DELETE ?
                                DatabaseHelp.deleteItem(setting.createQuery(db)) :
                                DatabaseHelp.insertItem(db, XMockPropSetting.Table.name, setting));
    }

    public static Collection<XMockPropSetting> getPropSettings(XDatabase db, int user, String packageName) {
        return SqlQuerySnake.create(db, XMockPropSetting.Table.name)
                .whereColumn("user", user)
                .whereColumn("packageName", packageName)
                .queryAs(XMockPropSetting.class, true);
    }

    public static Collection<XMockPropSetting> getPropSettings(Context context, XDatabase db) {
        return DatabaseHelp.getOrInitTable(
                context,
                db,
                XMockPropSetting.Table.name,
                XMockPropSetting.Table.columns,
                XMockPropSetting.class);
    }

    public static Collection<XMockMappedSetting> getMappedPropSettings(Context context, XDatabase db) {
        return DatabaseHelp.getOrInitTable(
                context,
                db,
                XMockMappedSetting.Table.name,
                XMockMappedSetting.Table.columns,
                JSON,
                true,
                XMockMappedSetting.class,
                COUNT);
    }

    public static boolean forceDatabaseCheck(Context context, XDatabase db) {
        return DatabaseHelp.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                XMockMappedSetting.Table.name,
                XMockMappedSetting.Table.columns,
                JSON,
                true,
                XMockMappedSetting.class,
                DatabaseHelp.DB_FORCE_CHECK);
    }
}*/
