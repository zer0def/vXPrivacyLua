package eu.faircode.xlua.api.xlua.xcall;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.xlua.XSettingsDatabase;
import eu.faircode.xlua.api.objects.xlua.packets.SettingPacket;
import eu.faircode.xlua.utilities.BundleUtil;

import eu.faircode.xlua.api.objects.xlua.setting.xSetting;
import eu.faircode.xlua.api.objects.xlua.setting.xSettingConversions;



public class GetSettingCommand extends CallCommandHandler {
    public static GetSettingCommand create() { return new GetSettingCommand(); };
    public GetSettingCommand() {
        name = "getSetting";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        SettingPacket packet = commandData.read(SettingPacket.class);

        if(BuildConfig.DEBUG)
            Log.i("XLua.GetSettingCommand", "handler packet=" + packet);

        if(packet.getValue() != null && packet.getValue().equals("*")) {
            return XSettingsDatabase.getSetting(
                    commandData.getDatabase(),
                    packet.getUser(),
                    packet.getCategory(),
                    packet.getName()).toBundle();
        } else {
            return BundleUtil.
                    createSingleString("value",
                            XSettingsDatabase.getSettingValue(
                                    commandData.getDatabase(),
                                    packet.getUser(),
                                    packet.getCategory(),
                                    packet.getName()));
        }
    }

    public static Bundle invoke(Context context, Integer userId, String category, String name) { return invoke(context, userId, category, name, null); }
    public static Bundle invoke(Context context, Integer userId, String category, String name, String value) {
        SettingPacket packet = new SettingPacket(userId, category, name, value);
        return invoke(context, packet);
    }

    public static Bundle invoke(Context context, SettingPacket packet) {
        return XProxyContent.luaCall(
                        context,
                        "getSetting",
                        packet.toBundle());
    }
}
