package eu.faircode.xlua.api.xmock.xcall;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpuConversions;
import eu.faircode.xlua.api.objects.xmock.prop.MockPropConversions;
import eu.faircode.xlua.api.xmock.XMockCpuDatabase;
import eu.faircode.xlua.api.xmock.XMockPropDatabase;

public class GetMockCpusCommand extends CallCommandHandler {
    public static GetMockCpusCommand create() { return new GetMockCpusCommand(); };

    public GetMockCpusCommand() {
        name = "getMockCpus";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        return MockCpuConversions.toBundleArray(
                XMockCpuDatabase.getCpuMaps(
                        commandData.getContext(),
                        commandData.getDatabase()));
    }

    public static Bundle invoke(Context context) {
        return XProxyContent.mockCall(
                context,
                "getMockCpus");
    }
}
