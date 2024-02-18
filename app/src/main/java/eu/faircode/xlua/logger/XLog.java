package eu.faircode.xlua.logger;

import org.luaj.vm2.Varargs;

import eu.faircode.xlua.api.hook.XLuaHook;

public class XLog {
    private XLuaHook hook;
    private Varargs result;
    private long startTime;
    private String functionName;
    private String exception;

    //public XLog(XLuaHook hook, )

    public static XLog create(XLuaHook hook, String packageName, int uid, long time, String data) {
        return null;
    }

    //
    //hook
    //packageName
    //uid
    //event => (use .....)
    //time
    //data  =>  [function]
    //          [restricted]
    //          [duration]
    //              [old]
    //              [new]
    //
    //


    ///XLuaHook hook, Varargs result, long startTime, String funName
    public static XLog create() { return new XLog(); }

    //public static XLog setHook()
}
