package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.app.LuaSimplePacket;
import eu.faircode.xlua.api.xstandard.CallCommandHandler;
import eu.faircode.xlua.api.xstandard.command.CallPacket;
import eu.faircode.xlua.api.xlua.database.LuaAppManager;
import eu.faircode.xlua.utilities.BundleUtil;

public class ClearDataCommand extends CallCommandHandler {
    @SuppressWarnings("unused")
    public ClearDataCommand() {
        name = "clearData";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        return BundleUtil.createResultStatus(
                LuaAppManager.clearData(
                        commandData.getExtras().getInt("user"),
                        commandData.getDatabase()));
    }

    public static Bundle invoke(Context context, LuaSimplePacket packet) {
        return XProxyContent.luaCall(
                context,
                "clearData",
                packet.toBundle());
    }
}
