package eu.faircode.xlua.api.xmock;

import android.content.Context;

import java.util.Collection;
import java.util.List;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.xmock.phone.MockCarrier;
import eu.faircode.xlua.api.objects.xmock.phone.MockPhone;
import eu.faircode.xlua.api.objects.xmock.phone.MockUniqueId;
import eu.faircode.xlua.api.objects.xmock.prop.MockProp;
import eu.faircode.xlua.database.DatabaseHelperEx;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.utilities.StringUtil;

public class XMockPhoneDatabase {

    public static MockPhone getPhoneConfig(Context context, String name, XDataBase db) {
        if(!StringUtil.isValidString(name))
            return null;

        return DatabaseQuerySnake
                .create(db, MockPhone.Table.name)
                    .whereColumn("name", name)
                    .queryGetFirstAs(MockPhone.class, true);
    }

    public static boolean putUniqueIdConfig(Context context, MockUniqueId config, XDataBase db) {
        if(config == null || !config.isValid())
            return false;

        return DatabaseHelperEx.insertItem(
                db,
                MockUniqueId.Table.name,
                config,
                preparePhonesDatabaseTable(context, db));
    }

    public static boolean putCarrierConfig(Context context, MockCarrier config, XDataBase db) {
        if(config == null || !config.isValid())
            return false;

        return DatabaseHelperEx.insertItem(
                db,
                MockCarrier.Table.name,
                config,
                preparePhonesDatabaseTable(context, db));
    }

    public static boolean putPhoneConfig(Context context, MockPhone config, XDataBase db) {
        if(config == null || !config.isValid())
           return false;

        return DatabaseHelperEx.insertItem(
                db,
                MockPhone.Table.name,
                config,
                preparePhonesDatabaseTable(context, db));
    }

    public static Collection<MockCarrier> getMockCarriers(Context context, XDataBase db) {
        return DatabaseHelperEx.initDatabase(
                context,
                db,
                MockCarrier.Table.name,
                MockCarrier.Table.columns,
                MockCarrier.JSON,
                true,
                MockCarrier.class,
                MockCarrier.COUNT);
    }

    public static Collection<MockUniqueId> getMockUniqueIDs(Context context, XDataBase db) {
        return DatabaseHelperEx.initDatabase(
                context,
                db,
                MockUniqueId.Table.name,
                MockUniqueId.Table.columns,
                MockUniqueId.JSON,
                true,
                MockUniqueId.class,
                MockUniqueId.COUNT);
    }

    public static Collection<MockPhone> getMockPhones(Context context, XDataBase db) {
        return DatabaseHelperEx.initDatabase(
                context,
                db,
                MockPhone.Table.name,
                MockPhone.Table.columns,
                MockPhone.JSON,
                true,
                MockPhone.class,
                MockPhone.COUNT);
    }

    public static boolean prepareCarriersDatabaseTable(Context context, XDataBase db) {
        return DatabaseHelperEx.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                MockCarrier.Table.name,
                MockCarrier.Table.columns,
                MockCarrier.JSON,
                true,
                MockCarrier.class,
                MockCarrier.COUNT);
    }

    public static boolean prepareUniqueIDsDatabaseTable(Context context, XDataBase db) {
        return DatabaseHelperEx.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                MockUniqueId.Table.name,
                MockUniqueId.Table.columns,
                MockUniqueId.JSON,
                true,
                MockUniqueId.class,
                MockUniqueId.COUNT);
    }

    public static boolean preparePhonesDatabaseTable(Context context, XDataBase db) {
        return DatabaseHelperEx.prepareTableIfMissingOrInvalidCount(
                context,
                db,
                MockPhone.Table.name,
                MockPhone.Table.columns,
                MockPhone.JSON,
                true,
                MockPhone.class,
                MockPhone.COUNT);
    }
}
