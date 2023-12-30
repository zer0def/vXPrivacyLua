package eu.faircode.xlua;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.cpu.XMockCpuApi;
import eu.faircode.xlua.cpu.XMockCpuIO;

public class XMockProvider {
    private static final String TAG = "XLua.XMockProvider";

    private static XDataBase db = null;
    private static List<XMockPropIO> cacheProps = new ArrayList<>();
    private static List<XMockCpuIO> cacheCpus = new ArrayList<>();

    public static final String MOCK = "mock.db";
    public static final Object lock = new Object();

    public static void initPropsCache(Context context) {
        //make sure this dosnt need [synchronized] as the called function used [synchronized]
        synchronized (lock) {
            if(cacheProps == null || cacheProps.isEmpty()) {
                Log.i(TAG, "Populating Props Cache...");
                cacheProps = XMockPropApi.getMockProps(context);
            }
        }
    }

    public static void initCpuMapsCache(Context context) {
        synchronized (lock) {
            if(cacheCpus == null || cacheCpus.isEmpty()) {
                Log.i(TAG, "Populating Cpu Maps Cache...");
                cacheCpus = XMockCpuApi.getCpuMaps(context);
            }
        }
    }

    public static List<XMockPropIO> getPropsCache(Context context) {
        initDatabase(context);
        initPropsCache(context);
        return cacheProps;
    }

    public static List<XMockCpuIO> getCpuMapsCache(Context context) {
        initDatabase(context);
        initCpuMapsCache(context);
        return cacheCpus;
    }

    public static void initDatabase(Context context) {
        synchronized (lock) {
            if (db == null) {
                Log.i(TAG, "Creating Mock DB");
                db = new XDataBase(MOCK, context);
            }
        }
    }

    public static XDataBase getDatabase(Context context, boolean initIfNull) {
        if(initIfNull && db == null)
            db = new XDataBase(MOCK, context);

        return db;
    }

    public static Bundle callHandler(Context context, String method, Bundle extras) throws RemoteException, IllegalArgumentException {
        Log.i(TAG, "Call=" + method);
        initDatabase(context);
        //loadData(context);

        Bundle result = null;
        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        try {
            StrictMode.allowThreadDiskReads();
            StrictMode.allowThreadDiskWrites();
            switch (method) {
                case "getMockProps":
                    result = getMockPropsAsBundle(context, false);
                    break;
                case "putMockProp":
                    XSecurity.checkCaller(context);
                    result = putMockProp(context, extras);
                    break;
                case "putMockProps":
                    XSecurity.checkCaller(context);
                    result = putMockProps(context, extras);
                    break;
                case "getMockCpuMaps":
                    result = getMockCpuMapsAsBundle(context, false);
                    break;
                case "putMockCpuMap":
                    XSecurity.checkCaller(context);
                    result = putMockCpuMap(context, extras);
                    break;
                case "getSelectedMockCpuMap":
                    result = getSelectedMockCpuMap(context);
                    break;

            }
        } catch (Exception e) {
            Log.e(TAG, "XMOCK Call Handler Error=\n" + e + "\n" + Log.getStackTraceString(e));
        } finally {
            StrictMode.setThreadPolicy(originalPolicy);
        }

        return result;
    }

    public static Cursor queryHandler(Context context, String method, String[] selection) throws RemoteException {
        Log.i(TAG, "query=" + method);
        //loadData(context);
        initDatabase(context);

        Cursor result = null;
        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();

        try{
            StrictMode.allowThreadDiskReads();
            StrictMode.allowThreadDiskWrites();

            switch (method) {
                case "getMockProps":
                    result = getMockPropsAsCursor(context, false, false);
                    break;
                case "getMockProps2":
                    result = getMockPropsAsCursor(context, false, true);
                    break;
                case "getMockCpuMaps":
                    result = getMockCpuMapsAsCursor(context, false, false);
                    break;
                case "getMockCpuMaps2":
                    result = getMockCpuMapsAsCursor(context, false, true);
                    break;

            }
        } catch (Exception e) {
            Log.e(TAG, "XMOCK Query Handler Error=\n" + e + "\n" + Log.getStackTraceString(e));
        } finally {
            StrictMode.setThreadPolicy(originalPolicy);
        }

        if (result != null)
            result.moveToPosition(-1);
        return result;
    }

    //
    //Cpu Maps
    //

    public static Bundle putMockCpuMap(Context context, Bundle extras) {
        Bundle ret = new Bundle();
        XMockCpuIO map = XMockCpuIO.Convert.fromBundle(extras);
        Log.i(TAG, "[putMockCpuMap] Map=[" + map + "]");
        ret.putInt("result", XMockCpuApi.putCpuMap(context, map) ? 0 : -1);
        return ret;
    }

    public static Bundle getSelectedMockCpuMap(Context context) {
        Log.i(TAG, "[getSelectedMockCpuMap]");
        return XMockCpuIO.Convert.toBundle(XMockCpuApi.getSelectedCpuMapSync(context));
    }

    public static Bundle getMockCpuMapsAsBundle(Context context, boolean useCache) {
        return useCache ?
                XMockCpuIO.Convert.toBundle(getCpuMapsCache(context)) :
                XMockCpuIO.Convert.toBundle(XMockCpuApi.getCpuMapsSync(context));
    }

    public static Cursor getMockCpuMapsAsCursor(Context context, boolean useCache, boolean marshall) {
        return useCache ?
                XMockCpuIO.Convert.toCursor(getCpuMapsCache(context), marshall) :
                XMockCpuIO.Convert.toCursor(XMockCpuApi.getCpuMapsSync(context), marshall);
    }

    //
    //Props
    //

    public static Bundle putMockProp(Context context, Bundle extras) {
        Log.i(TAG, "pre [putMockProp]");
        Bundle ret = new Bundle();
        XMockPropIO prop = XMockPropIO.Convert.fromBundle(extras);
        Log.i(TAG, "[putMockProp] Prop=[" + prop + "]");
        ret.putInt("result", XMockPropApi.putMockPropSync(context, prop) ? 0 : -1);
        return ret;
    }

    public static Bundle putMockProps(Context context, Bundle extras) {
        Log.i(TAG, "pre [putMockProps]");
        Bundle ret = new Bundle();
        List<XMockPropIO> props = XMockPropIO.Convert.fromBundleArray(extras);
        Log.i(TAG, "[putMockProps] Data to Write Size=" + props.size());
        ret.putInt("result", XMockPropApi.putMockPropsSync(context,props)  ? 0 : -1);
        return ret;
    }

    public static Bundle getMockPropsAsBundle(Context context, boolean useCache) {
        return useCache ?
                XMockPropIO.Convert.toBundle(getPropsCache(context)) :
                XMockPropIO.Convert.toBundle(XMockPropApi.getMockPropsSync(context));
    }

    public static Cursor getMockPropsAsCursor(Context context, boolean useCache, boolean marshall) {
        return useCache ?
                XMockPropIO.Convert.toCursor(getPropsCache(context), marshall) :
                XMockPropIO.Convert.toCursor(XMockPropApi.getMockPropsSync(context), marshall);
    }
}
