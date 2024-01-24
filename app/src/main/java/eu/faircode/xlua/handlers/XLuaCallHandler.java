package eu.faircode.xlua.handlers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import eu.faircode.xlua.XPolicy;
import eu.faircode.xlua.api.data.XAssignmentData;
//import eu.faircode.xlua.api.data.XCallData;
//import eu.faircode.xlua.api.data.XCommandCallHandler;
import eu.faircode.xlua.api.xlua.xcall.AssignHooksCommand;
import eu.faircode.xlua.api.xlua.xcall.ClearAppCommand;
import eu.faircode.xlua.api.xlua.xcall.ClearDataCommand;
import eu.faircode.xlua.api.xlua.xcall.GetGroupsCommand;
import eu.faircode.xlua.api.xlua.xcall.GetSettingCommand;
import eu.faircode.xlua.api.xlua.xcall.GetVersionCommand;
import eu.faircode.xlua.api.xlua.xcall.InitAppCommand;
import eu.faircode.xlua.api.xlua.xcall.PutHookCommand;
import eu.faircode.xlua.api.xlua.xcall.PutSettingCommand;
import eu.faircode.xlua.api.xlua.xcall.ReportCommand;
import eu.faircode.xlua.api.xlua.xquery.GetLogCommand;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.api.XProxyContent;


/*public class XLuaCallHandler implements Callable<Bundle> {
    private static final String TAG = "XLua.XLuaCallHandler";
    private static final Map<String, XCommandCallHandler> handlers = new HashMap<>();
    static {
        handlers.putAll(getModules());
    }

    private XCallData data;
    private String method;
    private XPolicy policy;
    public XLuaCallHandler(Context context, String method, Bundle extras, XDataBase db) {
        this.method = method;
        this.data = new XCallData(context, method, extras, db);
        this.policy = new XPolicy();
    }

    public Bundle call() {
        try {
            XGlobalCore.loadHookData(data.getContext());
            this.policy.allowRW();
            XCommandCallHandler handler =  handlers.get(method);
            if(handler != null)
                return handler.handle(data);

        }catch (Throwable e) {
            Log.e(TAG, "Error with call=" + e + "\n" + Log.getStackTraceString(e));
        }finally {
            this.policy.revert();
        }

        Log.e(TAG, "Failed to Find Handler for: " + method);
        return BundleUtil.createResultStatus(false);
    }

    public static XLuaCallHandler create(Context context, String method, Bundle extras, XDataBase db) { return new XLuaCallHandler(context, method, extras, db); }

    public static Bundle invokeCall(Context context, String method) { return invokeCall(context, method, new Bundle()); }
    public static Bundle invokeCall(Context context, String method, Bundle extras) { return XProxyContent.invokeCall(context, "xlua", method, extras); }

    public static Map<String, XCommandCallHandler> getModules() {
        HashMap<String, XCommandCallHandler> hs = new HashMap<>();
        hs.put("assignHooks", AssignHooksCommand.create());
        hs.put("getVersion", GetVersionCommand.create());
        hs.put("putHook", PutHookCommand.create());
        hs.put("getGroups", GetGroupsCommand.create());
        hs.put("report", ReportCommand.create());
        hs.put("getSetting", GetSettingCommand.create());
        hs.put("putSetting", PutSettingCommand.create());
        hs.put("initApp", InitAppCommand.create());
        hs.put("clearApp", ClearAppCommand.create());
        hs.put("clearData", ClearDataCommand.create());
        return hs;
    }
}*/
