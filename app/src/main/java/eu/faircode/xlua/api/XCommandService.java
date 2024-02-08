package eu.faircode.xlua.api;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.robv.android.xposed.XC_MethodHook;
import eu.faircode.xlua.DebugUtil;
import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.XSettingBridgeStatic;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.objects.TryCallWrapper;
import eu.faircode.xlua.api.objects.TryQueryWrapper;

public class XCommandService {
    private static final String TAG = "XLua.XCommandService";

    private final Object lock = new Object();

    private final Map<String, CallCommandHandler> mockCalls = new HashMap<>();
    private final Map<String, QueryCommandHandler> mockQueries = new HashMap<>();

    private final Map<String, CallCommandHandler> xLuaCalls = new HashMap<>();
    private final Map<String, QueryCommandHandler> xLuaQueries = new HashMap<>();

    private final Map<String, String> appKeys = new HashMap<>();

    private final ExecutorService hookThreads = Executors.newFixedThreadPool(5);//Executors.newSingleThreadExecutor();
    private final ExecutorService mockThreads = Executors.newFixedThreadPool(5);//Executors.newSingleThreadExecutor();

    public XCommandService() {
        //Key is slightly a cool hack
        //in regular user code its never in their context
        //in our code but in there context it exists but only to our eye context ?
        xLuaCalls.putAll(XSettingBridgeStatic.getXLuaCallCommands());
        xLuaQueries.putAll(XSettingBridgeStatic.getXLuaQueryCommands());
        mockCalls.putAll(XSettingBridgeStatic.getXMockCallCommands());
        mockQueries.putAll(XSettingBridgeStatic.getMockQueryCommands());

        Log.i(TAG, "xLua calls=" + xLuaCalls.size() +
                        "\nxLua Queries=" + xLuaQueries.size() +
                        "\nMock Calls=" + mockCalls.size() +
                        "\nMock Queries=" + mockQueries.size());
    }

    public Bundle executeCall(Context context, String method, String arg, Bundle extras, String packageName) throws Exception {
        //we can combine all the commands :P
        //Be careful when VXP + Direct Invoke when it ATTEMPTS to set param value aka clean up param shit

        CallCommandHandler command = null;
        XDataBase dataBase = null;
        ExecutorService executorService = null;

        switch (method) {
            case "xlua":
                command = xLuaCalls.get(arg);
                dataBase = XGlobalCore.getLuaDatabase(context);
                executorService = hookThreads;
                break;
            case "mock":
                command = mockCalls.get(arg);
                dataBase = XGlobalCore.getMockDatabase(context);
                executorService = mockThreads;
                break;
        }

        if(command == null || dataBase == null || executorService == null)
            throw new Exception("Not a valid Command: " + method);

        CallPacket packet = new CallPacket(context, arg, extras, dataBase);
        if(DebugUtil.isDebug()) Log.i(TAG, "Found Command & Database, packet=" + packet);
        return executorService.submit(
                TryCallWrapper.create(
                        packet,
                        command)).get();
    }

    public Cursor executeCursor(Context context, String method, String arg, String[] selection, String packageName) throws Exception {
        QueryCommandHandler command = null;
        XDataBase dataBase = null;
        ExecutorService executorService = null;

        Log.i(TAG, "executeCursor   method=" + method + "  arg=" + arg);

        switch (method) {
            case "xlua":
                command = xLuaQueries.get(arg);
                dataBase = XGlobalCore.getLuaDatabase(context);
                executorService = hookThreads;
                break;
            case "mock":
                command = mockQueries.get(arg);
                dataBase = XGlobalCore.getMockDatabase(context);
                executorService = mockThreads;
                break;
        }

        if(command == null || dataBase == null || executorService == null)
            throw new Exception("Not a valid Command: " + method + "  arg=" + arg);

        QueryPacket packet = new QueryPacket(context, arg, selection, dataBase);
        if(DebugUtil.isDebug()) Log.i(TAG, "Found Command & Database, packet=" + packet);
        return executorService.submit(
                TryQueryWrapper.create(
                        packet,
                        command)).get();
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
}
