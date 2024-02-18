package eu.faircode.xlua.ui.dialogs;

import eu.faircode.xlua.api.settings.LuaSettingPacket;

public interface ISettingDialogListener {
    void pushSettingPacket(LuaSettingPacket packet);
}
