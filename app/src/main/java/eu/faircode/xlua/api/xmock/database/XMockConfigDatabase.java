/*package eu.faircode.xlua.api.xmock.database;

import android.content.Context;

import java.util.Collection;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.utilities.StringUtil;

public class XMockConfigDatabase {
    private static final String JSON = "configs.json";
    private static final int COUNT = 3;

    public static boolean putMockConfig(Context context, XMockConfig config, XDatabase db) {
        if(config == null || !StringUtil.isValidString(config.getName()))
            return false;

        return DatabaseHelp.insertItem(
                db,
                XMockConfig.Table.name,
                config,
                prepareDatabaseTable(context, db));
    }


    public static Collection<XMockConfig> getMockConfigs(Context context, XDatabase db) {
        return DatabaseHelp.getOrInitTable(
                context,
                db,
                XMockConfig.Table.name,
                XMockConfig.Table.columns,
                JSON,
                true,
                XMockConfig.class,
                COUNT);
    }

    public static boolean prepareDatabaseTable(Context context, XDatabase db) {
        return DatabaseHelp.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                XMockConfig.Table.name,
                XMockConfig.Table.columns,
                JSON,
                true,
                XMockConfig.class,
                COUNT);
    }

    public static boolean forceDatabaseCheck(Context context, XDatabase db) {
        return DatabaseHelp.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                XMockConfig.Table.name,
                XMockConfig.Table.columns,
                JSON,
                true,
                XMockConfig.class,
                DatabaseHelp.DB_FORCE_CHECK);
    }
}
*/