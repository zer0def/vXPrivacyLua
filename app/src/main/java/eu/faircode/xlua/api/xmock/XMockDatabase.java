package eu.faircode.xlua.api.xmock;

import android.content.Context;

import androidx.annotation.NonNull;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.XGlobals;
import eu.faircode.xlua.api.configs.MockConfigDatabase;
import eu.faircode.xlua.api.properties.MockPropDatabase;
import eu.faircode.xlua.api.standard.interfaces.IInitDatabase;
import eu.faircode.xlua.api.xmock.database.XMockCpuDatabase;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.DatabasePathUtil;

public class XMockDatabase implements IInitDatabase {
    private XDatabase db;
    private boolean init = false;
    private boolean setPerms = true;

    private boolean check_1 = false;
    private boolean check_2 = false;
    private boolean check_3 = false;
    private boolean check_4 = false;

    private final Object lock = new Object();

    public static XMockDatabase createEmpty() { return new XMockDatabase(); }

    public XMockDatabase() {  }
    public XMockDatabase(Context context) {
        this(context, true);
    }
    public XMockDatabase(Context context, boolean setPerms) { this.db = new XDatabase("mock", context, setPerms); this.setPerms = setPerms; }

    @Override
    public XDatabase getDatabase(Context context) {
        initDatabase(context, true);
        return db;
    }

    @Override
    public boolean initDatabase(Context context, boolean checkIsReady) {
        synchronized (lock) {
            if(checkIsReady && db != null) {
                if(!XDatabase.isReady(db))
                    reset(true);
            }

            if(db == null) {
                db = new XDatabase(XGlobals.DB_NAME_MOCK, context, setPerms);
                DatabasePathUtil.log("Created XMOCK DB =>" + db, false);
                reset(false);
                if(!db.isOpen(true))
                    return false;
            }

            if(!init) {
                if(!check_1) check_1 = MockConfigDatabase.forceDatabaseCheck(context, db);
                if(!check_2) check_2 = XMockCpuDatabase.forceDatabaseCheck(context, db);
                if(!check_3) check_3 = CollectionUtil.isValid(MockPropDatabase.forceCheckMapsDatabase(context, db));
                if(!check_4) check_4 = MockPropDatabase.ensurePropSettingsDatabase(context, db);

                DatabasePathUtil.log("Config Check=" + check_1 + " Cpu Config Check=" + check_2 + " properties=" + check_3 + " prop settings = " + check_4, false);
                init = check_1 && check_2 && check_3 && check_4;
            }

            return init;
        }
    }

    @Override
    public void reset(boolean setDatabaseNull) {
        if(setDatabaseNull) {
            db.close();
            db = null;
        }

        init = false;
        check_1 = false;
        check_2 = false;
        check_3 = false;
        check_4 = false;
    }

    @NonNull
    @Override
    public String toString() {
        if(db != null) return db.toString();
        return "null";
    }
}
