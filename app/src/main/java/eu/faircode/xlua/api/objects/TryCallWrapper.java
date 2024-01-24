package eu.faircode.xlua.api.objects;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Callable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import eu.faircode.xlua.utilities.BundleUtil;

public class TryCallWrapper implements Callable<Bundle> {
    private static final String TAG = "XLua.TryCallWrapper";

    private CallPacket packet;
    private String packageName;
    private CallCommandHandler handle;

    private boolean isRunning = false;
    private boolean hasException = false;
    private Throwable exception;
    private XC_MethodHook.MethodHookParam param;

    public TryCallWrapper(CallPacket packet, String packageName, CallCommandHandler handle, XC_MethodHook.MethodHookParam param) {
        this.packet = packet;
        this.packageName = packageName;
        this.handle = handle;
        this.param = param;
    }

    @Override
    public Bundle call() throws Exception {
        try {
            Log.i(TAG, "INSIDE OF CALL [TryCallWrapper]");
            isRunning = true;
            Bundle result = handle.handle(packet);
            param.setResult(result);
            return result;
        }catch (Throwable e) {
            exception = e;
            hasException = true;
            Log.e(TAG, "Call Error: \n" + e + "\n" + Log.getStackTraceString(e));
            //return BundleUtil.createResultStatus(false);
            XposedBridge.log("Call Error");
            param.setResult(e);
            return null;
        }finally {
            isRunning = false;
        }
    }
}
