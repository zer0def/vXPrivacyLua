package eu.faircode.xlua.handlers;

import android.content.Context;
import android.os.Bundle;

import java.util.concurrent.Callable;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.XProxyContent;

/*public class XMockCallHandler implements Callable<Bundle>  {
    private static final String TAG = "XLua.XMockCallHandler";

    private Context context;
    private String method;
    private Bundle extras;
    private XDataBase db;

    public XMockCallHandler(Context context, String method, Bundle extras, XDataBase db) {
        this.context = context;
        this.method = method;
        this.extras = extras;
        this.db = db;
    }

    public static XMockCallHandler create(Context context, String method, Bundle extras, XDataBase db) {
        return new XMockCallHandler(context, method, extras, db);
    }

    public static Bundle invokeCall(Context context, String method) { return invokeCall(context, method, new Bundle()); }
    public static Bundle invokeCall(Context context, String method, Bundle extras) { return XProxyContent.invokeCall(context, "mock", method, extras); }

    public Bundle call() {
        Bundle result = null;



        return result;
    }
}*/
