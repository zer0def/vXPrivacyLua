package eu.faircode.xlua;

import android.content.Context;

import java.util.List;

//import eu.faircode.xlua.database.DatabaseHelper;

/*public class XMockPropApi {
    //Add TableName and Columns here ?
    //Also its possible we now can Make this into Object Instance ?
    //All sync functions will not take in the DATABASE Param as SYNC is only needed when same target DB is being used as well as UI Setting shit from DATABASE Linking to UI
    //As well as Cache functions, the caller of the Cache functions in provider they init cache with SYNC
    private static final String TAG = "XLua.XMockPropApi";
    private static final int COUNT = 66;
    private static final String JSON = "props.json";

    public static boolean putMockPropsSync(Context context, List<XMockPropIO> props) {
        synchronized (XMockProvider.lock) {
            return putMockProps(context, props);
        }
    }

    public static boolean putMockProps(Context context, List<XMockPropIO> props) {
        return putMockProps(context, XMockProvider.getDatabase(context, true), props);
    }

    public static boolean putMockProps(Context context, XDataBase xmockdb, List<XMockPropIO> props) {
        if(!prepareDatabaseTable(context, xmockdb))
            return false;

        return DatabaseHelper.insertItems(xmockdb, XMockProp.Table.name, props);
    }

    public static boolean putMockPropSync(Context context, XMockPropIO prop) {
        synchronized (XMockProvider.lock) {
            return putMockProp(context, prop);
        }
    }

    public static boolean putMockProp(Context context, XMockPropIO prop) {
        return putMockProp(context, XMockProvider.getDatabase(context, true), prop);
    }

    public static boolean putMockProp(Context context, XDataBase xmockdb, XMockPropIO prop) {
        if(!prepareDatabaseTable(context, xmockdb))
            return false;

        return DatabaseHelper.insertItem(xmockdb, XMockProp.Table.name, prop);
    }

    public static boolean prepareDatabaseTableSync(Context context) {
        synchronized (XMockProvider.lock) {
            return prepareDatabaseTable(context);
        }
    }

    public static boolean prepareDatabaseTable(Context context) {
        return prepareDatabaseTable(context, XMockProvider.getDatabase(context, true));
    }

    public static boolean prepareDatabaseTable(Context context, XDataBase xmockdb) {
        return DatabaseHelper.prepareTableIfMissingOrInvalidCount(
                context,
                xmockdb,
                XMockProp.Table.name,
                XMockProp.Table.columns,
                JSON,
                true,
                XMockPropIO.class,
                COUNT);
    }

    public static List<XMockPropIO> getMockPropsSync(Context context) {
        synchronized (XMockProvider.lock) {
            return getMockProps(context, XMockProvider.getDatabase(context, true));
        }
    }

    public static List<XMockPropIO> getMockProps(Context context) {
        return getMockProps(context, XMockProvider.getDatabase(context, true));
    }

    public static List<XMockPropIO> getMockProps(Context context, XDataBase xmockdb) {
        return DatabaseHelper.initDatabase(
                context,
                xmockdb,
                XMockProp.Table.name,
                XMockProp.Table.columns,
                JSON,
                true,
                XMockPropIO.class,
                COUNT);
    }
}*/
