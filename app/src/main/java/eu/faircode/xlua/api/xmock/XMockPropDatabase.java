package eu.faircode.xlua.api.xmock;

import android.content.Context;

import java.util.Collection;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.xmock.prop.MockProp;
import eu.faircode.xlua.api.objects.xmock.packets.MockPropPacket;
import eu.faircode.xlua.database.DatabaseHelperEx;
import eu.faircode.xlua.database.DatabaseQuerySnake;

public class XMockPropDatabase {
    private static final int COUNT = 66;
    private static final String JSON = "props.json";

    public static MockProp getMockProp(XDataBase db, String name) {
        return DatabaseQuerySnake
                .create(db, MockProp.Table.name)
                .whereColumn("name", name)
                .queryGetFirstAs(MockProp.class, true);
    }

    public static boolean insertMockProps(Context context, XDataBase db, Collection<MockProp> props) {
        return DatabaseHelperEx.insertItems(
                db,
                MockProp.Table.name,
                props,
                prepareDatabaseTable(context, db));
    }

    public static boolean updateMockProp(Context context, XDataBase db, String name, String value) {
        return updateMockProp(
                context,
                db,
                name,
                value,
                null);
    }

    public static boolean updateMockProp(Context context, XDataBase db, String name, String value, Boolean enabled) {
        MockPropPacket packet = new MockPropPacket(name, value, enabled);
        return updateMockProp(context, db, packet);
    }

    public static boolean updateMockProp(Context context, XDataBase db, MockPropPacket packet) {
        //would this work as the "where" wont update anything but the 'name' ??
        return DatabaseHelperEx.updateItem(
                db,
                MockProp.Table.name,
                DatabaseQuerySnake.create().whereColumn("name", packet.getName()),
                packet,
                prepareDatabaseTable(context, db));
    }

    public static boolean putMockProp(Context context, XDataBase db, MockPropPacket prop) {
        return DatabaseHelperEx.insertItem(
                db,
                MockProp.Table.name,
                prop,
                prepareDatabaseTable(context, db));
    }

    public static Collection<MockProp> getMockProps(Context context, XDataBase db) {
        return DatabaseHelperEx.initDatabase(
                context,
                db,
                MockProp.Table.name,
                MockProp.Table.columns,
                JSON,
                true,
                MockProp.class,
                COUNT);
    }

    public static boolean prepareDatabaseTable(Context context, XDataBase db) {
        return DatabaseHelperEx.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                MockProp.Table.name,
                MockProp.Table.columns,
                JSON,
                true,
                MockProp.class,
                COUNT);
    }
}
