package eu.faircode.xlua.api.xmock.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.properties.MockPropMap;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.utilities.BundleUtil;

public class PutGroupStateCommand extends CallCommandHandler {
    public static PutGroupStateCommand create() { return new PutGroupStateCommand(); };
    public PutGroupStateCommand() {
        name = "putGroupState";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        /*MockPropMap packet = commandData.read(MockPropMap.class);
        return BundleUtil.createResultStatus(
                XMockPropertiesProvider.setGroupState(commandData.getContext(), commandData.getDatabase(), packet));*/

        return null;
    }

    public static Bundle invoke(Context context, MockPropMap settingPacket) {
        return XProxyContent.mockCall(
                context,
                "putGroupState", settingPacket.toBundle());
    }
}
