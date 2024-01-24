package eu.faircode.xlua.handlers;

import android.content.Context;
import android.database.Cursor;
import android.os.StrictMode;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.XPolicy;
//import eu.faircode.xlua.api.data.XCommandCallHandler;
//import eu.faircode.xlua.api.data.XCommandQueryHandler;
//import eu.faircode.xlua.api.data.XQueryData;
import eu.faircode.xlua.api.xlua.xcall.ClearAppCommand;

import eu.faircode.xlua.api.XProxyContent;


import eu.faircode.xlua.api.xlua.xquery.GetAppsCommand;
import eu.faircode.xlua.api.xlua.xquery.GetAssignedHooksCommand;
import eu.faircode.xlua.api.xlua.xquery.GetHooksCommand;
import eu.faircode.xlua.api.xlua.xquery.GetLogCommand;
import eu.faircode.xlua.api.xlua.xquery.GetSettingsCommand;

/*public class XLuaQueryHandler implements Callable<Cursor> {
    private static final String TAG = "XLua.XLuaQueryHandler";
    private static Map<String, XCommandQueryHandler> handlers = new HashMap<>();
    static {
        handlers = getModules();
    }

    private String method;
    private XQueryData data;
    private XPolicy policy;
    public XLuaQueryHandler(Context context, String method, String[] selection, XDataBase db) {
        this.method = method;
        this.data = new XQueryData(context, selection, db);
        this.policy = new XPolicy();
    }

    public Cursor call() {
        try {
            XGlobalCore.loadHookData(data.getContext());
            this.policy.allowRW();
            XCommandQueryHandler handler = handlers.get(method);
            if(handler != null)
                handler.handle(data);

        }catch (Throwable e) {
            Log.e(TAG, "Error with query=" + e + "\n" + Log.getStackTraceString(e));
        }finally {
            this.policy.revert();
        }

        Log.i(TAG, "Handler is missing from [query]: " + method);
        return null;
    }

    public static XLuaQueryHandler create(Context context, String method, String[] selection, XDataBase db) { return new XLuaQueryHandler(context, method, selection, db); }
    public static Cursor invokeQuery(Context context, String method) { return invokeQuery(context, method, null, null); }
    public static Cursor invokeQuery(Context context, String method, String[] args_selection) { return invokeQuery(context, method, args_selection, null); }
    public static Cursor invokeQuery(Context context, String method, String[] args_selection, String selection) { return XProxyContent.invokeQuery(context, "xlua", method, args_selection, selection); }

    public static Map<String, XCommandQueryHandler> getModules() {
        HashMap<String, XCommandQueryHandler> hs = new HashMap<>();
        hs.put("getApps", GetAppsCommand.create(false));
        hs.put("getApps2", GetAppsCommand.create(true));
        hs.put("getHooks", GetHooksCommand.create(false));
        hs.put("getHooks2", GetHooksCommand.create(true));
        hs.put("getSettings", GetSettingsCommand.create());
        hs.put("getAssignedHooks", GetAssignedHooksCommand.create(false));
        hs.put("getAssignedHooks2", GetAssignedHooksCommand.create(true));
        //hs.put("clearApp", ClearAppCommand.create());
        hs.put("getLog", GetLogCommand.create());
        return hs;
    }
}*/
