package eu.faircode.xlua.api.xmock.xcall;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.xmock.prop.MockPropConversions;
import eu.faircode.xlua.api.xmock.XMockPropDatabase;

public class GetMockPropsCommand extends CallCommandHandler {
    public static GetMockPropsCommand create() { return new GetMockPropsCommand(); };

    public GetMockPropsCommand() {
        name = "getMockProps";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        return MockPropConversions.toBundleArray(
                XMockPropDatabase.getMockProps(
                        commandData.getContext(),
                        commandData.getDatabase()));
    }

    public static Bundle invoke(Context context) {
        return XProxyContent.mockCall(
                context,
                "getMockProps");
    }
}
