package eu.faircode.xlua.api.xlua.xcall;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.xlua.packets.HookPacket;
import eu.faircode.xlua.api.xlua.XHookProvider;
import eu.faircode.xlua.utilities.BundleUtil;

public class PutHookCommand extends CallCommandHandler {
    public static PutHookCommand create() { return new PutHookCommand(); };
    public PutHookCommand() {
        name = "putHook";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        HookPacket packet = commandData.read(HookPacket.class);
        return BundleUtil.createResultStatus(
                XHookProvider.putHook(
                    commandData.getContext(),
                    packet.getId(),
                    packet.getDefinition(),
                    commandData.getDatabase()));
    }

    public static Bundle invoke(Context context, String id, String definition) {
        HookPacket packet = new HookPacket(id, definition);
        return invoke(context, packet);
    }

    public static Bundle invoke(Context context, HookPacket packet) {
        return XProxyContent.luaCall(
                context,
                "putHook",
                packet.toBundle());
    }
}
