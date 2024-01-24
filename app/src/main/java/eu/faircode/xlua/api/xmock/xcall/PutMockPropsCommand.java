package eu.faircode.xlua.api.xmock.xcall;

import android.content.Context;
import android.os.Bundle;

import java.util.Collection;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.xmock.prop.MockProp;
import eu.faircode.xlua.api.objects.xmock.prop.MockPropConversions;
import eu.faircode.xlua.api.xmock.XMockPropDatabase;
import eu.faircode.xlua.utilities.BundleUtil;

public class PutMockPropsCommand  extends CallCommandHandler {
    public static PutMockPropsCommand create() { return new PutMockPropsCommand(); };

    public PutMockPropsCommand() {
        name = "putMockProps";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        Collection<MockProp> items = MockPropConversions.fromBundleArray(commandData.getExtras());
        return BundleUtil.createResultStatus(
                XMockPropDatabase.insertMockProps(
                        commandData.getContext(),
                        commandData.getDatabase(),
                        items));
    }

    public static Bundle invoke(Context context, Collection<MockProp> props) {
        return XProxyContent.mockCall(
                context,
                "putMockProps",
                MockPropConversions.toBundleArray(props));
    }
}
