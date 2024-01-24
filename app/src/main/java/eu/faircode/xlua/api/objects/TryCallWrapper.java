package eu.faircode.xlua.api.objects;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.Callable;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import eu.faircode.xlua.XPolicy;
import eu.faircode.xlua.utilities.BundleUtil;

public class TryCallWrapper implements Callable<Bundle> {
    private static final String TAG = "XLua.TryCallWrapper";

    private CallPacket packet;
    private String packageName;
    private CallCommandHandler handle;

    private boolean isRunning = false;
    //private boolean hasException = false;
    private Throwable exception;

    public static TryCallWrapper create(CallPacket packet, CallCommandHandler handler) { return new TryCallWrapper(packet, handler); }

    public TryCallWrapper(CallPacket packet, CallCommandHandler handle) {
        this.packet = packet;
        this.handle = handle;
    }
;
    public TryCallWrapper(CallPacket packet, String packageName, CallCommandHandler handle) {
        this.packet = packet;
        this.packageName = packageName;
        this.handle = handle;
    }

    @Override
    public Bundle call() {
        XPolicy policy = XPolicy.policyAllowRW();
        try {
            isRunning = true;
            return handle.handle(packet);
        }catch (Throwable e) {
            exception = e;
            Log.e(TAG, "Call Error: \n" + e + "\n" + Log.getStackTraceString(e));
            XposedBridge.log("Call Error");
            return null;
        }finally {
            policy.revert();
            isRunning = false;
        }
    }

    public boolean isRunning() { return isRunning; }
    public boolean hasException() { return exception != null; }
    public Throwable getException() { return this.exception; }
}
