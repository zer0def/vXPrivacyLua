package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.app.LuaSimplePacket;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.xlua.database.XLuaHookDatabase;
import eu.faircode.xlua.api.hook.assignment.LuaAssignmentPacket;

public class AssignHooksCommand extends CallCommandHandler {
    public static AssignHooksCommand create() { return new AssignHooksCommand(); };
    public AssignHooksCommand() {
        name = "assignHooks";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        LuaAssignmentPacket packet = commandData.readFullPackFrom(LuaAssignmentPacket.class);
        if(packet == null) return XResult.fromInvalidPacket(name, LuaSimplePacket.class).toBundle();
        return XLuaHookDatabase.assignHooks(
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
