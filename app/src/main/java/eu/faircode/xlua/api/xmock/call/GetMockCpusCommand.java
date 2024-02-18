package eu.faircode.xlua.api.xmock.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.cpu.MockCpuConversions;
import eu.faircode.xlua.api.xmock.database.XMockCpuDatabase;

public class GetMockCpusCommand extends CallCommandHandler {
    public static GetMockCpusCommand create() { return new GetMockCpusCommand(); };

    public GetMockCpusCommand() {
        name = "getMockCpus";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
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
