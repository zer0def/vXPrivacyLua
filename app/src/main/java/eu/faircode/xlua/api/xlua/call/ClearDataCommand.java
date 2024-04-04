package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.app.LuaSimplePacket;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.xlua.database.LuaAppDatabase;
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
                LuaAppDatabase.clearData(
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
