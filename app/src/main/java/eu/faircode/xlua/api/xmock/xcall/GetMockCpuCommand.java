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

public class GetMockCpuCommand extends CallCommandHandler {
    public static GetMockCpuCommand create() { return new GetMockCpuCommand(); };
    public GetMockCpuCommand() {
        name = "getMockCpu";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        MockCpu packet = commandData.read(MockCpu.class);
        if(packet.getName() == null) {
            return BundleUtil.createFromISerial(
                    XMockCpuProvider.getSelectedCpuMap(
                            commandData.getContext(),
                            commandData.getDatabase()), true);
        }else {
            return BundleUtil.createFromISerial(
                    XMockPropDatabase.getMockProp(
                            commandData.getDatabase(),
                            packet.getName()), true);
        }
    }

    public static Bundle invoke(Context context, String name) {
        return XProxyContent.mockCall(
                context,
                "getMockCpu",
                BundleUtil.createSingleString("name", name));
    }

    public static Bundle invoke(Context context) {
        return XProxyContent.mockCall(
                context,
                "getMockCpu",
                new Bundle());
    }
}
