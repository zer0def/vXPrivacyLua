package eu.faircode.xlua.api.xmock.xcall;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;


import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.objects.xmock.packets.MockPropPacket;
import eu.faircode.xlua.api.xmock.XMockPropDatabase;
import eu.faircode.xlua.utilities.BundleUtil;

public class PutMockPropCommand extends CallCommandHandler {
    public static PutMockPropCommand create() { return new PutMockPropCommand(); };

    public static final int PROP_UPDATE_COMMAND = 0x1;
    public static final int PROP_INSERT_COMMAND = 0x2;

    public PutMockPropCommand() {
        name = "putMockProp";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        MockPropPacket packet = commandData.read(MockPropPacket.class);
        Integer code = packet.getCode();

        Log.i("XLua.PutMockPropCommand", " incoming packet=" + packet);

        if(code == null || code == 0x0)
            code = packet.getDefaultValue() == null ? PROP_UPDATE_COMMAND : PROP_INSERT_COMMAND;
            //packet.setCode(packet.getDefaultValue() == null ? PROP_UPDATE_COMMAND : PROP_INSERT_COMMAND);

        switch (code) {
            case PROP_UPDATE_COMMAND:
                return BundleUtil.createResultStatus(
                        XMockPropDatabase.updateMockProp(
                                commandData.getContext(),
                                commandData.getDatabase(),
                                packet));
            case PROP_INSERT_COMMAND:
                return BundleUtil.createResultStatus(
                        XMockPropDatabase.putMockProp(
                                commandData.getContext(),
                                commandData.getDatabase(),
                                packet));
        }

        return BundleUtil.createResultStatus(false);
    }

    public static Bundle invokeUpdate(Context context, String name, Boolean enabled) { return invokeUpdate(context, name, null, enabled); }
    public static Bundle invokeUpdate(Context context, String name, String value) { return invokeUpdate(context, name, value, null); }
    public static Bundle invokeUpdate(Context context, String name, String value, Boolean enabled) { return invoke(context, name, value, null, enabled, true); }

    public static Bundle invokeInsert(Context context, String name, String value, String defaultValue, Boolean enabled) {
        MockPropPacket packet = new MockPropPacket(name, value, defaultValue, enabled);
        packet.setCode(defaultValue == null ? PROP_UPDATE_COMMAND : PROP_INSERT_COMMAND);
        return invoke(context, name, value, defaultValue, enabled, defaultValue == null);
    }

    public static Bundle invoke(Context context, String name, String value, String defaultValue, Boolean enabled, Boolean update) {
        MockPropPacket packet = new MockPropPacket(name, value, defaultValue, enabled);
        packet.setCode(update ? PROP_UPDATE_COMMAND : PROP_INSERT_COMMAND);
        return invoke(context, packet);
    }

    public static Bundle invoke(Context context, MockPropPacket packet) {
        Log.i("XLua.PutMockPropCommand", " packet=" + packet);
        return XProxyContent.mockCall(
                context,
                "putMockProp",
                packet.toBundle());
    }
}
