package eu.faircode.xlua.api.xmock.xcall;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.xmock.packets.MockPropPacket;
import eu.faircode.xlua.api.xmock.XMockPropDatabase;
import eu.faircode.xlua.utilities.BundleUtil;

public class GetMockPropCommand extends CallCommandHandler {
    public static GetMockPropCommand create() { return new GetMockPropCommand(); };

    public GetMockPropCommand() {
        name = "getMockProp";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        MockPropPacket packet = commandData.read(MockPropPacket.class);
        if(packet.getName() == null)
            return BundleUtil.createResultStatus(false);

        //Integer code = packet.getCode();
        //if(code == null)
        //    return new Bundle();
        //for now just return all
        return BundleUtil.createFromISerial(
                XMockPropDatabase.getMockProp(
                        commandData.getDatabase(),
                        packet.getName()),
                true);
    }

    public static Bundle invoke(Context context, String name) {
        return XProxyContent.mockCall(
                context,
                "getMockProp",
                BundleUtil.createSingleString("name", name));
    }
}
