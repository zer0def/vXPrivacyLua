package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.xlua.database.LuaAppDatabase;
import eu.faircode.xlua.api.app.LuaSimplePacket;

public class ClearAppCommand extends CallCommandHandler {
    public static ClearAppCommand create() { return new ClearAppCommand(); };
    public ClearAppCommand() {
        name = "clearApp";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        LuaSimplePacket packet = commandData.readExtrasAs(LuaSimplePacket.class);
        if(packet == null) return XResult.fromInvalidPacket(name, LuaSimplePacket.class).toBundle();
        return XResult.create()
                .setMethodName("clearApp")
                .setExtra(packet.toString())
                .setResult(LuaAppDatabase.clearApp(
                        commandData.getContext(), commandData.getDatabase(), packet)).toBundle();
    }

    public static Bundle invoke(Context context, LuaSimplePacket packet) {
        return XProxyContent.luaCall(
                context,
                "clearApp",
                packet.toBundle());
    }
}
