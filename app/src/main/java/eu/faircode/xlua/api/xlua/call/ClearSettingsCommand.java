package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.app.LuaSimplePacket;
import eu.faircode.xlua.api.xlua.database.LuaAppManager;
import eu.faircode.xlua.api.xstandard.CallCommandHandler;
import eu.faircode.xlua.api.xstandard.command.CallPacket;
import eu.faircode.xlua.utilities.BundleUtil;

public class ClearSettingsCommand extends CallCommandHandler {
    @SuppressWarnings("unused")
    public ClearSettingsCommand() {
        name = "clearSettings";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable { return LuaAppManager.clearSettings(commandData.getExtras().getString("packageName"), commandData.getDatabase()).toBundle(); }
    public static XResult invoke(Context context, String packageName) {
        Bundle b = new Bundle();
        b.putString("packageName", packageName);
        return XResult.from(XProxyContent.luaCall(
                context,
                "clearSettings",
                b));
    }
}
