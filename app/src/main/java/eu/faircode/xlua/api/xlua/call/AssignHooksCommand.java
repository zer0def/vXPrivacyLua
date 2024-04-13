package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.app.LuaSimplePacket;
import eu.faircode.xlua.api.xstandard.CallCommandHandler;
import eu.faircode.xlua.api.xstandard.command.CallPacket;
import eu.faircode.xlua.api.xlua.database.LuaHookManager;
import eu.faircode.xlua.api.hook.assignment.LuaAssignmentPacket;
import eu.faircode.xlua.logger.XLog;

public class AssignHooksCommand extends CallCommandHandler {
    public AssignHooksCommand() {
        name = "assignHooks";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        LuaAssignmentPacket packet = commandData.readExtrasAs(LuaAssignmentPacket.class);
        if(packet == null) return XResult.fromInvalidPacket(name, LuaSimplePacket.class).toBundle();
        return LuaHookManager.assignHooks(
                commandData.getContext(),
                commandData.getDatabase(),
                packet).toBundle();
    }

    public static Bundle invoke(Context context, LuaAssignmentPacket packet) {
        return XProxyContent.luaCall(
                context,
                "assignHooks",
                packet.toBundle());
    }
}
