package eu.faircode.xlua.api.xmock.xcall;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;
import eu.faircode.xlua.api.objects.xmock.packets.MockPropPacket;
import eu.faircode.xlua.api.xmock.XMockCpuProvider;
import eu.faircode.xlua.api.xmock.XMockPropDatabase;
import eu.faircode.xlua.utilities.BundleUtil;

public class PutMockCpuCommand extends CallCommandHandler {
    public static PutMockCpuCommand create() { return new PutMockCpuCommand(); };
    public PutMockCpuCommand() {
        name = "putMockCpu";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        MockCpu packet = commandData.read(MockCpu.class);
        return BundleUtil.createResultStatus(
                XMockCpuProvider.putCpuMap(
                        commandData.getDatabase(),
                        packet.getName(),
                        packet.getSelected()));
    }

    public static Bundle invoke(Context context, String cpuMapName, boolean selected) {
        MockCpu map = new MockCpu(cpuMapName, null, null, null, selected);
        return invoke(context, map);
    }

    public static Bundle invoke(Context context, MockCpu packet) {
        return XProxyContent.mockCall(
                context,
                "putMockCpu",
                packet.toBundle());
    }
}
