package eu.faircode.xlua.api.xmock.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.configs.MockConfigDatabase;
import eu.faircode.xlua.api.configs.MockConfigPacket;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;

public class PutMockConfigCommand extends CallCommandHandler {
    public static PutMockConfigCommand create() { return new PutMockConfigCommand(); };
    public PutMockConfigCommand() {
        name = "putMockConfig";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        MockConfigPacket packet = commandData.readFullPackFrom(MockConfigPacket.class);
        if(packet == null) return XResult.create().setMethodName("putMockConfig").setFailed("Mock Config Packet is NULL!").toBundle();

        packet.ensureCode(MockConfigPacket.CODE_APPLY_CONFIG);
        switch (packet.getCode()) {
            case MockConfigPacket.CODE_DELETE_CONFIG:
            case MockConfigPacket.CODE_APPLY_CONFIG:
                return MockConfigDatabase.putMockConfig(commandData.getContext(), commandData.getDatabase(), packet).toBundle();
        }

        return XResult.create().setMethodName("putMockConfig").setFailed("Failed to find Command Handler for Mock Config Packet! Code=" + packet.getCode()).toBundle();
    }

    public static Bundle invoke(Context context, MockConfigPacket packet) {
        return XProxyContent.mockCall(
                context,
                "putMockConfig",
                packet.toBundle());
    }
}
