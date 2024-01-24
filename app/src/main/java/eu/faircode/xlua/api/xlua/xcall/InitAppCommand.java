package eu.faircode.xlua.api.xlua.xcall;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.xlua.XAppDatabase;
import eu.faircode.xlua.api.objects.xlua.packets.AppPacket;
import eu.faircode.xlua.utilities.BundleUtil;

public class InitAppCommand extends CallCommandHandler {
    public static InitAppCommand create() { return new InitAppCommand(); };
    public InitAppCommand() {
        name = "initApp";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        AppPacket packet = commandData.read(AppPacket.class);
        return BundleUtil.createResultStatus(
                XAppDatabase.initAppAssignments(
                        commandData.getContext(),
                        packet.packageName,
                        packet.uid,
                        packet.kill,
                        commandData.getDatabase()));
    }

    public  static Bundle invoke(Context context, String packageName, Integer uid, Boolean kill) {
        AppPacket packet = new AppPacket();
        packet.packageName = packageName;
        packet.uid = uid;
        packet.kill = kill;
        return invoke(context, packet);
    }

    public static Bundle invoke(Context context, AppPacket packet) {
        return XProxyContent.luaCall(
                context,
                "initApp",
                packet.toBundle());
    }
}
