package eu.faircode.xlua.api.xmock;

import android.content.Context;
import android.util.Log;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;
import eu.faircode.xlua.api.objects.xmock.phone.MockCarrier;
import eu.faircode.xlua.api.objects.xmock.phone.MockPhone;
import eu.faircode.xlua.api.objects.xmock.phone.MockUniqueId;
import eu.faircode.xlua.api.objects.xmock.prop.MockProp;

public class XMockDatabaseHelp {
    private static final String TAG = "XLua.XMockDatabaseHelp";

    public static void initDatabase(Context context, XDataBase db) {
        if(db == null || !db.isOpen(true))
            return;

        //Can lock ? but the sub functions lock as well :P
        if(db.tableEntries(MockCpu.Table.name) < 1)
            XMockCpuProvider.initCache(context, db);
        if(db.tableEntries(MockProp.Table.name) < 1)
            XMockPropDatabase.getMockProps(context, db);

        //replicate , below for above
        if(db.tableEntries(MockPhone.Table.name) < 1)
            XMockPhoneDatabase.preparePhonesDatabaseTable(context, db);
        if(db.tableEntries(MockCarrier.Table.name) < 1)
            XMockPhoneDatabase.prepareCarriersDatabaseTable(context, db);
        if(db.tableEntries(MockUniqueId.Table.name) < 1)
            XMockPhoneDatabase.prepareUniqueIDsDatabaseTable(context, db);
    }
}
