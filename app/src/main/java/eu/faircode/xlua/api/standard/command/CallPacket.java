package eu.faircode.xlua.api.standard.command;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.standard.interfaces.IBridgePacketContext;
import eu.faircode.xlua.api.standard.interfaces.ISerial;

public class CallPacket extends BridgePacket implements IBridgePacketContext {
    private static final String TAG = "XLua.CallPacket";

    private final Bundle extras;

    public CallPacket(Context context, String method, Bundle extras, XDatabase db) { this(context, null, method, extras, db, null); }
    public CallPacket(Context context, String commandPrefix, String method, Bundle extras, XDatabase db) { this(context, commandPrefix, method, extras, db, null); }
    public CallPacket(Context context, String commandPrefix, String method, Bundle extras, XDatabase db, String packageName) {
        super(commandPrefix, method, context, db, packageName);
        this.extras = extras;
    }

    public Bundle getExtras() { return extras; }
    public <T extends ISerial> T readFullPackFrom(Class<T> clazz) {
        try {
            T inst = clazz.newInstance();
            inst.fromBundle(extras);
            return inst;
        }catch (Exception e) {
            Log.e(TAG, "Failed to read bundle extras! flag=" + " class type=" + clazz.getName() + " " + this + "\ne=" + e);
            return null;
        }
    }
}
