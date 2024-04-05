package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.utilities.BundleUtil;


public class GetSettingCommand extends CallCommandHandler {

    public static GetSettingCommand create() { return new GetSettingCommand(); };
    public GetSettingCommand() {
        name = "getSetting";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        LuaSettingPacket packet = commandData.readExtrasAs(LuaSettingPacket.class);
        packet.resolveUserID();

        if(BuildConfig.DEBUG)
            Log.i("XLua.GetSettingCommand", "handler packet=" + packet);

        if(packet.isGetObject()) {
            return LuaSettingsDatabase.getSetting(
                    commandData.getDatabase(),
                    packet.getName(),
                    packet.getUser(),
                    packet.getCategory()).toBundle();
        }else {
            return BundleUtil.createSingleString("value",
                    LuaSettingsDatabase.getSettingValue(
                            commandData.getContext(),
                            commandData.getDatabase(),
                            packet));
        }
    }

    public static Bundle invoke(Context context, Bundle b) {
        return XProxyContent.luaCall(
            context,
            "getSetting", b);
    }
    public static Bundle invoke(Context context, LuaSettingPacket packet) {
        return XProxyContent.luaCall(
                        context,
                        "getSetting",
                        packet.toBundle());
    }
}
