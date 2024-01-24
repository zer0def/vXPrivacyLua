package eu.faircode.xlua.api;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.XLua;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.objects.TryCallWrapper;
import eu.faircode.xlua.api.objects.TryQueryWrapper;
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
import eu.faircode.xlua.api.xlua.xquery.GetAppsCommand;
import eu.faircode.xlua.api.xlua.xquery.GetAssignedHooksCommand;
import eu.faircode.xlua.api.xlua.xquery.GetHooksCommand;
import eu.faircode.xlua.api.xlua.xquery.GetLogCommand;
import eu.faircode.xlua.api.xlua.xquery.GetSettingsCommand;
import eu.faircode.xlua.api.xmock.xcall.GetMockCpusCommand;
import eu.faircode.xlua.api.xmock.xcall.GetMockPropCommand;
import eu.faircode.xlua.api.xmock.xcall.GetMockPropsCommand;
import eu.faircode.xlua.api.xmock.xcall.PutMockCpuCommand;
import eu.faircode.xlua.api.xmock.xcall.PutMockPropCommand;
import eu.faircode.xlua.api.xmock.xcall.PutMockPropsCommand;

public class XCommandService {
    private static final String TAG = "XLua.XCommandService";

    private final Object lock = new Object();

    private final Map<String, CallCommandHandler> mockCalls = new HashMap<>();
    private final Map<String, QueryCommandHandler> mockQueries = new HashMap<>();

    private final Map<String, CallCommandHandler> xLuaCalls = new HashMap<>();
    private final Map<String, QueryCommandHandler> xLuaQueries = new HashMap<>();

    private final Map<String, String> appKeys = new HashMap<>();

    private final ExecutorService hookThreads = Executors.newFixedThreadPool(5);//Executors.newSingleThreadExecutor();
    private final ExecutorService mockThreads = Executors.newFixedThreadPool(10);//Executors.newSingleThreadExecutor();

    public XCommandService() {
        //Key is slightly a cool hack
        //in regular user code its never in their context
        //in our code but in there context it exists but only to our eye context ?
        xLuaCalls.putAll(getXLuaCallCommands());
        xLuaQueries.putAll(getXLuaQueryCommands());
        mockCalls.putAll(getXMockCallCommands());

        Log.i(TAG, " XCALLS=" + xLuaCalls.size() + "  XQUERIES=" + xLuaQueries.size() + "  MOCKCALLS=" + mockCalls.size());

    }

    public void handeCall(XC_MethodHook.MethodHookParam param, String packageName)  {
        try {
            String method = (String) param.args[0];
            String arg = (String) param.args[1];
            Bundle extras = (Bundle) param.args[2];

            String lowered = method.toLowerCase();
            if(!lowered.contains("xlua") && !lowered.contains("mock"))
                return;

            Method mGetContext = param.thisObject.getClass().getMethod("getContext");
            Context context = (Context) mGetContext.invoke(param.thisObject);


            Log.i(TAG , "call package=" + packageName + " method=" + method + " arg=" + arg);

            if(arg.equals("getVersion")) {
                Log.i(TAG, "Handing getVersion");
                Bundle result = new Bundle();
                result.putInt("version", XLua.version);
                param.setResult(result);
                return;
            }

            Log.i(TAG, "WE ARE GOING TO CHECK DATABASES");

            //XGlobalCore.checkDatabases(context);

            //if(BuildConfig.DEBUG)
            //    Log.i(TAG , "call package=" + packageName + " method=" + method + " arg=" + arg);

            //boolean authResult = authenticateSecret(packageName, extras);
            directCall(context, method, arg, extras, packageName, param);
        }catch (Exception e) {
            Log.e(TAG, "Call Error: Not our problem :P\n" + e);
            XposedBridge.log(e);
        }
    }

    public Bundle directCall(Context context, String method, String arg, Bundle extras, String packageName, XC_MethodHook.MethodHookParam param) throws ExecutionException, InterruptedException {
        switch (method) {
            case "xlua":
                XGlobalCore.checkDatabases(context);
                return invokeXLuaCall(new CallPacket(context, arg, extras, XGlobalCore.getLuaDatabase(context)), packageName, param);
            case "mock":
                XGlobalCore.checkDatabases(context);
                return invokeMockCall(new CallPacket(context, arg, extras, XGlobalCore.getMockDatabase(context)), packageName, param);
        }

        return null;
    }

    private Bundle invokeMockCall(CallPacket packet, String packageName, XC_MethodHook.MethodHookParam param) throws ExecutionException, InterruptedException {
        Log.i(TAG, "packet=" + packet);
        CallCommandHandler handler = mockCalls.get(packet.getMethod());
        if(handler != null) {
            Log.i(TAG, "HANDLER FOUND=" + handler.getName() + " packet=" + packet);
            TryCallWrapper wrapper = new TryCallWrapper(packet, packageName, handler, param);
            return mockThreads.submit(wrapper).get();
        }
        else {
            Log.e(TAG, "Call has null handler (does not exist), packet=" + packet + " packageName=" + packageName);
            return null;
        }
    }

    private Bundle invokeXLuaCall(CallPacket packet, String packageName, XC_MethodHook.MethodHookParam param) throws ExecutionException, InterruptedException {
        Log.i(TAG, "packet=" + packet);
        CallCommandHandler handler = xLuaCalls.get(packet.getMethod());
        if(handler != null) {
            Log.i(TAG, "HANDLER FOUND=" + handler.getName() + " packet=" + packet);
            TryCallWrapper wrapper = new TryCallWrapper(packet, packageName, handler, param);
            return hookThreads.submit(wrapper).get();
        }else {
            Log.e(TAG, "Call has a null handler (does not exist), packet=" + packet + " packageName=" + packageName);
            return null;
        }
    }

    public void handleQuery(XC_MethodHook.MethodHookParam param, String packageName) {
        try {
            String[] projection = (String[]) param.args[1];
            String[] selection = (String[]) param.args[3];

            if (projection != null && projection.length > 0 && projection[0] != null) {

                if(!projection[0].startsWith("xlua.") && !projection[0].startsWith("mock.")) {
                    Log.i(TAG, "PROJECTION=" + projection[0]);
                    return;
                }

                Log.i(TAG, "XLUA OR MOCK QUERY SO WE ARE GOOD");


                Method mGetContext = param.thisObject.getClass().getMethod("getContext");
                Context context = (Context) mGetContext.invoke(param.thisObject);

                String[] split = projection[0].split("\\.");
                String method = split[0];
                String arg = split[1];

                Log.i(TAG, "WE ARE GOING TO CHECK DATABASES");

                //XGlobalCore.checkDatabases(context);

                //String lowered = method.toLowerCase();
                //if(!lowered.contains("xlua") && !lowered.contains("mock"))
                //    return;

                //Log.i(TAG, "Executing [query] on System [Settings] Context! Handler=" + method + " Method=" + arg);
                Log.i(TAG , "query pkg=" + packageName + " method=" + method + " arg=" + arg);
                //if(BuildConfig.DEBUG)
                //    Log.i(TAG , "query pkg=" + packageName + " method=" + method + " arg=" + arg);

                directQuery(context, method, arg, selection, packageName, param);
            }
        }catch (Throwable e) {
            Log.e(TAG, "Query Error: Not our problem :P\n" + e);
            XposedBridge.log(e);
        }
    }

    public Cursor directQuery(Context context, String method, String arg, String[] selection, String packageName, XC_MethodHook.MethodHookParam param) throws Exception {
        switch (method) {
            case "xlua":
                XGlobalCore.checkDatabases(context);
                return invokeXLuaQuery(new QueryPacket(context, arg, selection, XGlobalCore.getLuaDatabase(context)), packageName, param);
            case "mock":
                //param.setResult(XMockQueryHandler.create(context, arg, selection, xmock_db));
                break;
        }

        return null;
    }


    private Cursor invokeXLuaQuery(QueryPacket packet, String packageName, XC_MethodHook.MethodHookParam param) throws Exception {
        Log.i(TAG, "packet=" + packet);
        QueryCommandHandler handler = xLuaQueries.get(packet.getMethod());
        if(handler != null) {
            Log.i(TAG, "HANDLER FOUND=" + handler.getName() + " packet=" + packet);
            TryQueryWrapper wrapper = new TryQueryWrapper(packet, packageName, handler, param);
            return wrapper.call();
            //return hookThreads.submit(wrapper).get();
        }else {
            Log.e(TAG, "Query has a null handler (does not exist), packet=" + packet + " packageName=" + packageName);
            return null;
        }
    }

    public boolean authenticateSecret(String packageName, Bundle extras) {
        //wait no becz in there context its not cached so it wont exist ?
        //hmm fine either way the command gets passed from unknown to known
        synchronized (lock) {
            //Maybe if app key dosnt exist then allow it (assume it wasnt pinned for a key bcz user didnt want it)
            if(!appKeys.containsKey(packageName))
                return true;//Assume nots added because user disabled

            //yeye
            //but how want B app send to A app key if B app dosnt see key in any context
            //BUT B app needs A's KEY
            //Theory we can use the "globals" ?
            //set the field then use it within the hook  invoke ?

            if(extras.containsKey("sKey")) {
                String k = appKeys.get(packageName);
                if(k == null)
                    return false;

                return k.equals(extras.getString("sKey", "X"));
            }
        }

        return false;
    }

    public void putAppKey(String packageName, String key) {
        synchronized (lock) {
            appKeys.put(packageName, key);
        }
    }

    public static Map<String, QueryCommandHandler> getXLuaQueryCommands() {
        Log.i(TAG, "GETTING XLUA QUERY COMMANDS");
        HashMap<String, QueryCommandHandler> hs = new HashMap<>();
        hs.put("getApps", GetAppsCommand.create(false));
        hs.put("getApps2", GetAppsCommand.create(true));
        hs.put("getHooks", GetHooksCommand.create(false));
        hs.put("getHooks2", GetHooksCommand.create(true));
        hs.put("getSettings", GetSettingsCommand.create());
        hs.put("getAssignedHooks", GetAssignedHooksCommand.create(false));
        hs.put("getAssignedHooks2", GetAssignedHooksCommand.create(true));
        //hs.put("clearApp", ClearAppCommand.create());
        hs.put("getLog", GetLogCommand.create());
        Log.i(TAG, "LUA QUERY COMMANDS=" + hs.size());
        return hs;
    }

    public static Map<String, CallCommandHandler> getXLuaCallCommands() {
        Log.i(TAG, "GETTING XLUA CALL COMMANDS");
        HashMap<String, CallCommandHandler> hs = new HashMap<>();
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
        Log.i(TAG, "LUA CALL COMMANDS=" + hs.size());
        return hs;
    }

    public static Map<String, CallCommandHandler> getXMockCallCommands() {
        Log.i(TAG, "GETTING MOCK CALL COMMANDS");
        HashMap<String, CallCommandHandler> hs = new HashMap<>();
        hs.put("getMockCpu", GetMockCpusCommand.create());
        hs.put("getMockCpus", GetMockCpusCommand.create());
        hs.put("putMockCpu", PutMockCpuCommand.create());

        hs.put("getMockProp", GetMockPropCommand.create());
        hs.put("getMockProps", GetMockPropsCommand.create());
        hs.put("putMockProp", PutMockPropCommand.create());
        hs.put("putMockProps", PutMockPropsCommand.create());
        Log.i(TAG, "MOCK CALL COMMANDS=" + hs.size());
        return hs;
    }
}
