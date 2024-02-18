package eu.faircode.xlua.api.xmock.call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;

public class PutMockSettingCommand extends CallCommandHandler {
    public PutMockSettingCommand() {
        name = "putMockSetting";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        LuaSettingPacket packet = commandData.readFullPackFrom(LuaSettingPacket.class);
        if(packet == null)
            return XResult.create().setMethodName("putMockSetting").setFailed("Setting Packet is NULL").toBundle();

        packet.resolveUserID();
        packet.ensureCode(LuaSettingPacket.CODE_INSERT_UPDATE_SETTING);
        Log.i("XLua.PutMockSettingCommand", "setting packet=" + packet);

        XResult res1 = null;
        XResult res2 = null;

        switch (packet.getCode()) {
            case LuaSettingPacket.CODE_INSERT_UPDATE_SETTING:
            case LuaSettingPacket.CODE_DELETE_SETTING:
                res1 = LuaSettingsDatabase.putSetting(commandData.getContext(), commandData.getDatabase(), packet);
                break;
            case LuaSettingPacket.CODE_INSERT_UPDATE_DEFAULT_SETTING:
            case LuaSettingPacket.CODE_DELETE_DEFAULT_SETTING:
                res2 = LuaSettingsDatabase.putDefaultMappedSetting(commandData.getContext(), commandData.getDatabase(), packet);
                break;
            default:
            case LuaSettingPacket.CODE_INSERT_UPDATE_DEFAULT_AND_SETTING:
            case LuaSettingPacket.CODE_DELETE_DEFAULT_AND_SETTING:
                res1 = LuaSettingsDatabase.putSetting(commandData.getContext(), commandData.getDatabase(), packet);
                res2 = LuaSettingsDatabase.putDefaultMappedSetting(commandData.getContext(), commandData.getDatabase(), packet);
                break;
        }

        return XResult.combine(res1, res2).toBundle();
    }

    public static Bundle invoke(Context context, LuaSettingPacket packet) {
        return XProxyContent.luaCall(
                context,
                "putMockSetting",
                packet.toBundle());
    }
}
