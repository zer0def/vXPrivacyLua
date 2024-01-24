package eu.faircode.xlua.hooks;

import android.util.Log;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class LuaLog extends OneArgFunction {
    private static final String TAG = "XLua.LuaLog";

    private final String packageName;
    private final int uid;
    private final String hook;

    LuaLog(String packageName, int uid, String hook) {
        this.packageName = packageName;
        this.uid = uid;
        this.hook = hook;
    }

    @Override
    public LuaValue call(LuaValue arg) {
        Log.i(TAG, "Log " + packageName + ":" + uid + " " + hook + " " +
                arg.toString() + " (" + arg.typename() + ")");
        return LuaValue.NIL;
    }
}
