package eu.faircode.xlua;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

//import eu.faircode.xlua.cpu.XMockCpuIO;

/*
 * This is used for the Hooks within the Apps, onEnter for Hooks will use this API to Communicate, bridge for Communication
 */
/*public class XMockProxyApi {
    private static final String TAG = "XLua.ProxyApi";

    static Cursor invokeQuery(Context context, String method) { return invokeQuery(context, method, null, null); }
    static Cursor invokeQuery(Context context, String method, String[] args_selection) { return invokeQuery(context, method, args_selection, null); }
    static Cursor invokeQuery(Context context, String method, String[] args_selection, String filter) {
        //filter => "pkg = ? AND uid = ?" (dosnt get used in hook call)
        Log.i(TAG, "invokeQuery=" + method);
        return context.getContentResolver()
                .query(XSecurity.getURI(), new String[]{"mock." + method }, filter, args_selection, null);
    }

    static Bundle invokeCall(Context context, String method) { return invokeCall(context, method, new Bundle()); }
    static Bundle invokeCall(Context context, String method, Bundle extras) {
        Log.i(TAG, "invokeCall=" + method);
        return context.getContentResolver()
                .call(XSecurity.getURI(), "mock", method, extras);
    }

    //
    //Cpu Maps
    //

    public static Bundle callPutMockCpuMap(Context context, XMockCpuIO map) {
        return invokeCall(context, "putMockCpuMap",
                XMockCpuIO.Convert.toBundle(map));
    }

    public static XMockCpuIO callGetSelectedMockCpuMap(Context context) {
        return XMockCpuIO.Convert.fromBundle(
                invokeCall(context, "getSelectedMockCpuMap"));
    }

    public static List<XMockCpuIO> queryGetMockCpuMaps(Context context) {
        return XMockCpuIO.Convert.fromCursor(
                invokeQuery(context, "getMockCpuMaps2"));
    }

    public static List<XMockCpuIO> callGetMockCpuMaps(Context context) {
        return XMockCpuIO.Convert.fromBundleArray(
                invokeCall(context, "getMockCpuMaps"));
    }

    //
    //Props
    //

    public static Bundle callPutMockProp(Context context, XMockPropIO prop) {
        Log.i(TAG, "callMockProp=" + prop);
        return invokeCall(context, "putMockProp",
                XMockPropIO.Convert.toBundle(prop));
    }

    public static Bundle callPutMockProps(Context context, List<XMockPropIO> props) {
        Log.i(TAG, "callPutMockProps=" + props.size());
        return invokeCall(context, "putMockProps",
                XMockPropIO.Convert.toBundle(props));
    }

    public static List<XMockPropIO> queryGetMockProps(Context context) {
        return XMockPropIO.Convert.fromCursor(
                        invokeQuery(context, "getMockProps2"));
    }

    public static List<XMockPropIO> callGetMockProps(Context context) {
        return XMockPropIO.Convert.fromBundleArray(
                invokeCall(context, "getMockProps"));
    }
}*/
