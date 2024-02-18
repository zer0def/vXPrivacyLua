package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;

public class PutSettingCommand extends CallCommandHandler {
    public static PutSettingCommand create() { return new PutSettingCommand(); };
    public PutSettingCommand() {
        name = "putSetting";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        LuaSettingPacket packet = commandData.readFullPackFrom(LuaSettingPacket.class);
        packet.resolveUserID();
        Log.i("XLua.PutSettingCommand", " packet=" + packet);
        return LuaSettingsDatabase.putSetting(
                commandData.getContext(),
                commandData.getDatabase(),
                packet).toBundle();
    }

    public static Bundle invoke(Context context, LuaSettingPacket packet) {
        return XProxyContent.luaCall(
                context,
                "putSetting",
                packet.toBundle());
    }
}
