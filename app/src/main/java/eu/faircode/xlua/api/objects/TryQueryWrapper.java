package eu.faircode.xlua.api.objects;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Callable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import eu.faircode.xlua.XposedUtil;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.CursorUtil;

public class TryQueryWrapper implements Callable<Cursor> {
    private static final String TAG = "XLua.TryQueryWrapper";

    private QueryPacket packet;
    private String packageName;
    private QueryCommandHandler handle;
    private XC_MethodHook.MethodHookParam param;

    private boolean isRunning = false;
    private boolean hasException = false;
    private Throwable exception;

    public TryQueryWrapper(QueryPacket packet, String packageName, QueryCommandHandler handle, XC_MethodHook.MethodHookParam param) {
        this.packet = packet;
        this.packageName = packageName;
        this.handle = handle;
        this.param = param;
    }

    @Override
    public Cursor call() throws Exception {
        try {
            Log.i(TAG, "INSIDE OF QUERY [TryQueryWrapper]");
            isRunning = true;
            Cursor cursor = handle.handle(packet);
            param.setResult(cursor);
            return cursor;
        }catch (Throwable e) {
            exception = e;
            hasException = true;
            Log.e(TAG, "Query Error: \n" + e + "\n" + Log.getStackTraceString(e));
            XposedBridge.log("Query Error");
            param.setResult(e);
            return null;
        }finally {
            isRunning = false;
        }
    }
}
