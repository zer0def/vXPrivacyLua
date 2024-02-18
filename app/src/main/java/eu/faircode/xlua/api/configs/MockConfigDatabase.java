package eu.faircode.xlua.api.configs;

import android.content.Context;

import java.util.Collection;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.properties.MockPropMap;
import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;
import eu.faircode.xlua.utilities.StringUtil;

public class MockConfigDatabase {
    private static final String JSON = "configs.json";
    private static final int COUNT = 3;

    public static XResult putMockConfig(Context context, XDatabase db, MockConfigPacket packet) {
        XResult res = XResult.create().setMethodName("putMockConfig").setExtra(packet.toString());
        if(!StringUtil.isValidString(packet.getName()))
            return res.setFailed("Mock Config Name is Null!");

        boolean result =
                !packet.isDelete() ?
                        DatabaseHelp.insertItem(
                                db,
                                MockConfig.Table.name,
                                packet,
                                prepareDatabaseTable(context, db)) :
                        DatabaseHelp.deleteItem(SqlQuerySnake
                                .create(db, MockConfig.Table.name)
                                .whereColumn("name", packet.getName()));

        if(packet.isDelete() && !result)
            result = SqlQuerySnake.create(db, MockPropMap.Table.name)
                    .whereColumn("name", packet.getName())
                    .exists();

        return res.setResult(result);
    }

    public static Collection<MockConfig> getMockConfigs(Context context, XDatabase db) {
        return DatabaseHelp.getOrInitTable(
                context,
                db,
                MockConfig.Table.name,
                MockConfig.Table.columns,
                JSON,
                true,
                MockConfig.class,
                COUNT);
    }

    public static boolean prepareDatabaseTable(Context context, XDatabase db) {
        return DatabaseHelp.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                MockConfig.Table.name,
                MockConfig.Table.columns,
                JSON,
                true,
                MockConfig.class,
                COUNT);
    }

    public static boolean forceDatabaseCheck(Context context, XDatabase db) {
        return DatabaseHelp.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                MockConfig.Table.name,
                MockConfig.Table.columns,
                JSON,
                true,
                MockConfig.class,
                DatabaseHelp.DB_FORCE_CHECK);
    }
}
