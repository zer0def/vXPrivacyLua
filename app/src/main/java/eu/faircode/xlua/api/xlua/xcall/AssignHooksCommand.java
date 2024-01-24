package eu.faircode.xlua.api.xlua.xcall;

import android.content.Context;
import android.os.Bundle;

import java.util.List;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.xlua.XHookDatabase;
import eu.faircode.xlua.api.objects.xlua.packets.AssignmentPacket;
import eu.faircode.xlua.utilities.BundleUtil;

public class AssignHooksCommand extends CallCommandHandler {
    public static AssignHooksCommand create() { return new AssignHooksCommand(); };
    public AssignHooksCommand() {
        name = "assignHooks";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        AssignmentPacket com = commandData.read(AssignmentPacket.class);
        return BundleUtil.createResultStatus(XHookDatabase.assignHooks(
                commandData.getContext(),
                com.hookIds,
                com.packageName,
                com.uid,
                com.delete,
                com.kill,
                commandData.getDatabase()));
    }

    public static Bundle invoke(Context context, Bundle args) {
        return XProxyContent.luaCall(
                context,
                "assignHooks",
                args);
    }

    public static Bundle invoke(Context context, List<String> hookIds, String packageName, Integer uid, Boolean delete, Boolean kill) {
        AssignmentPacket packet = new AssignmentPacket();
        packet.hookIds = hookIds;
        packet.packageName = packageName;
        packet.uid = uid;
        packet.delete = delete;
        packet.kill = kill;
        return invoke(context, packet);
    }

    public static Bundle invoke(Context context, AssignmentPacket packet) {
        return XProxyContent.luaCall(
                context,
                "assignHooks",
                packet.toBundle());
    }
}
