package eu.faircode.xlua.cpu;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.XMockProvider;
import eu.faircode.xlua.database.DatabaseHelper;
import eu.faircode.xlua.XDataBase;


public class XMockCpuApi {
    private static final String TAG = "XLua.XMockCpuApi";
    private static final int COUNT = 43;
    private static final String JSON = "cpumaps.json";

    public static boolean putCpuMapSync(Context context, XMockCpuIO map) {
        synchronized (XMockProvider.lock) {
            return putCpuMap(context, map);
        }
    }

    public static boolean putCpuMap(Context context, XMockCpuIO map) { return putCpuMap(context, XMockProvider.getDatabase(context, true), map); }
    public static boolean putCpuMap(Context context, XDataBase xmockdb, XMockCpuIO map) {
        if(!prepareDatabaseTable(context, xmockdb))
            return false;

        return DatabaseHelper.updateItem(xmockdb, XMockCpu.Table.name, map);
    }

    public static XMockCpuIO getSelectedCpuMapSync(Context context) {
        synchronized (XMockProvider.lock) {
            return getSelectedCpuMap(context);
        }
    }

    public static XMockCpuIO getSelectedCpuMap(Context context) { return getSelectedCpuMap(context, XMockProvider.getDatabase(context, true)); }
    public static XMockCpuIO getSelectedCpuMap(Context context, XDataBase xmockdb) {
        List<XMockCpuIO> maps = getCpuMaps(context, xmockdb);
        if(maps.isEmpty()) {
            Log.e(TAG, "ERROR [getSelectedCpuMap] LIST IS NULL!! ??");
            return XMockCpuIO.EmptyDefault;
        }

        XMockCpuIO map = null;
        for (XMockCpuIO m : maps) {
            if(m.selected) {
                map = m;
                break;
            }
        }

        if(map == null) {
            Log.w(TAG, "No Cpu Map is Selected, Selecting Random Map...");
            //map = maps.get(maps.size() - 3);
            //Randomize
            map = maps.get(ThreadLocalRandom.current().nextInt(0, maps.size()));
        }

        return map;
    }

    public static boolean prepareDatabaseTableSync(Context context) {
        synchronized (XMockProvider.lock) {
            return prepareDatabaseTable(context);
        }
    }

    public static boolean prepareDatabaseTable(Context context) { return prepareDatabaseTable(context, XMockProvider.getDatabase(context, true)); }
    public static boolean prepareDatabaseTable(Context context, XDataBase xmockdb) {
        return DatabaseHelper.prepareTableIfMissingOrInvalidCount(
                context,
                xmockdb,
                XMockCpu.Table.name,
                XMockCpu.Table.columns,
                JSON,
                true,
                XMockCpuIO.class,
                COUNT);
    }



    public static List<XMockCpuIO> getCpuMapsSync(Context context) {
        synchronized (XMockProvider.lock) {
            return getCpuMaps(context);
        }
    }

    public static List<XMockCpuIO> getCpuMaps(Context context) { return getCpuMaps(context, XMockProvider.getDatabase(context, true)); }
    public static List<XMockCpuIO> getCpuMaps(Context context, XDataBase xmockdb) {
        return DatabaseHelper.initDatabse(
                context,
                xmockdb,
                XMockCpu.Table.name,
                XMockCpu.Table.columns,
                JSON,
                true,
                XMockCpuIO.class,
                COUNT);
    }
}
