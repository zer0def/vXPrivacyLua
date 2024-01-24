package eu.faircode.xlua.api.objects.xlua.setting;

import android.os.Bundle;

import eu.faircode.xlua.api.objects.xlua.packets.SettingPacket;

public class xSettingConversions {
    public static xSetting fromBundle(Bundle b) {
        xSetting setting = new SettingPacket();
        setting.fromBundle(b);
        return setting;
    }
}
