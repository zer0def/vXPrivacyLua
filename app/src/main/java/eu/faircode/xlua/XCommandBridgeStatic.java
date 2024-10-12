package eu.faircode.xlua;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import eu.faircode.xlua.api.standard.CommanderService;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.api.xlua.XLuaDatabase;
import eu.faircode.xlua.api.xlua.call.AssignHooksCommand;
import eu.faircode.xlua.api.xlua.call.ClearAppCommand;
import eu.faircode.xlua.api.xlua.call.ClearDataCommand;
import eu.faircode.xlua.api.xlua.call.GetAppCommand;
import eu.faircode.xlua.api.xlua.call.GetGroupsCommand;
import eu.faircode.xlua.api.xlua.call.GetSettingCommand;
import eu.faircode.xlua.api.xlua.call.GetVersionCommand;
import eu.faircode.xlua.api.xlua.call.InitAppCommand;
import eu.faircode.xlua.api.xlua.call.PutHookCommand;
import eu.faircode.xlua.api.xlua.call.PutSettingCommand;
import eu.faircode.xlua.api.xlua.call.ReportCommand;
import eu.faircode.xlua.api.xlua.query.GetAppsCommand;
import eu.faircode.xlua.api.xlua.query.GetAssignedHooksCommand;
import eu.faircode.xlua.api.xlua.query.GetHooksCommand;
import eu.faircode.xlua.api.xlua.query.GetLogCommand;
import eu.faircode.xlua.api.xlua.query.GetSettingsCommand;
import eu.faircode.xlua.api.xmock.XMockDatabase;
import eu.faircode.xlua.api.xmock.call.GetMockCpuCommand;
import eu.faircode.xlua.api.xmock.call.GetMockCpusCommand;
import eu.faircode.xlua.api.xmock.call.KillAppCommand;
import eu.faircode.xlua.api.xmock.call.PutGroupStateCommand;
import eu.faircode.xlua.api.xmock.call.PutMockConfigCommand;
import eu.faircode.xlua.api.xmock.call.PutMockCpuCommand;
import eu.faircode.xlua.api.xmock.call.PutMockPropCommand;
import eu.faircode.xlua.api.xmock.call.PutMockSettingCommand;
import eu.faircode.xlua.api.xmock.query.GetMockConfigsCommand;
import eu.faircode.xlua.api.xmock.query.GetMockPropMapsCommand;
import eu.faircode.xlua.api.xmock.query.GetMockPropertiesCommand;
import eu.faircode.xlua.api.xmock.query.GetMockSettingsCommand;
import eu.faircode.xlua.utilities.BundleUtil;

public class XCommandBridgeStatic {
    public static final String PRO_PACKAGE = "eu.faircode.xlua.pro";
    public static final String X_LUA_PACKAGE = "eu.faircode.xlua";//pretty sure this is a string somewhere already

    private static final String TAG = "XLua.XSettingBridgeStatic";

    public static final CommanderService luaCommandService = new CommanderService("xlua", XLuaDatabase.createEmpty(), 100);
    private static final CommanderService mockCommandService = new CommanderService("mock", XMockDatabase.createEmpty(), 100);

    static {
        luaCommandService
                .registerCall(AssignHooksCommand.class)
                .registerCall(ClearAppCommand.class)
                .registerCall(ClearDataCommand.class)
                .registerCall(GetGroupsCommand.class)
                .registerCall(GetSettingCommand.class)
                .registerCall(GetVersionCommand.class)
                .registerCall(InitAppCommand.class)
                .registerCall(PutHookCommand.class)
                .registerCall(PutSettingCommand.class)
                .registerCall(ReportCommand.class)
                .registerQuery(GetAppsCommand.class, true)
                .registerQuery(GetAssignedHooksCommand.class, true)
                .registerQuery(GetHooksCommand.class, true)
                .registerQuery(GetSettingsCommand.class)
                //didnt register log
                .registerQuery(GetLogCommand.class)
                //Mock Settings that link to XLUA DB
                .registerQuery(GetMockSettingsCommand.class, true)
                .registerCall(PutMockSettingCommand.class)
                .registerCall(GetAppCommand.class);

        mockCommandService
                .registerCall(GetMockCpuCommand.class)
                .registerCall(GetMockCpusCommand.class)
                .registerCall(KillAppCommand.class)
                .registerCall(PutGroupStateCommand.class)
                .registerCall(PutMockConfigCommand.class)
                .registerCall(PutMockCpuCommand.class)
                .registerCall(PutMockPropCommand.class)
                .registerQuery(GetMockConfigsCommand.class, true)
                .registerQuery(GetMockPropertiesCommand.class, true)
                .registerQuery(GetMockPropMapsCommand.class, true);
    }

    public static Bundle vxpCall(Context context, String arg, Bundle extras, String method)  {
        CommanderService cService = null;
        if(luaCommandService.isCommandPrefix(method))
            cService = luaCommandService;
        else if(mockCommandService.isCommandPrefix(method))
            cService = mockCommandService;

        assert cService != null;
        CallPacket packet = new CallPacket(context, method, arg, extras, cService.getDatabase(context));
        return cService.handleCall(packet);
    }

    public static Cursor vxpQuery(Context context, String arg, String[] selection, String method)  {
        CommanderService cService = null;
        if(luaCommandService.isCommandPrefix(method))
            cService = luaCommandService;
        else if(mockCommandService.isCommandPrefix(method))
            cService = mockCommandService;

        assert cService != null;
        QueryPacket packet = new QueryPacket(context, method, arg, selection, cService.getDatabase(context));
        return cService.handleQuery(packet);
    }

    public static void handleQuery(XC_MethodHook.MethodHookParam param, String packageName) {
        QueryPacket packet = luaCommandService.tryCreateQueryPacket(param, packageName);
        if (packet != null) {
            //Lua Command (query)
            param.setResult(luaCommandService.handleQuery(packet));
        }
        else {
            //Mock Commands (query)
            packet = mockCommandService.tryCreateQueryPacket(param, packageName);
            if(packet != null) param.setResult(mockCommandService.handleQuery(packet));
        }
    }

    public static void handeCall(XC_MethodHook.MethodHookParam param, String packageName)  {
        CallPacket packet = luaCommandService.tryCreateCallPacket(param, packageName);
        if(packet != null) {
            //Lua commands (call)
            if(packet.getMethod().equalsIgnoreCase("getVersion"))
                param.setResult(BundleUtil.createSingleInt("version", XLua.version));
            else
                param.setResult(luaCommandService.handleCall(packet));
        }
        else {
            //Mock Commands (call)
            packet = mockCommandService.tryCreateCallPacket(param, packageName);
            if(packet != null) param.setResult(mockCommandService.handleCall(packet));
        }
    }
}
