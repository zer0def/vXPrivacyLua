package eu.faircode.xlua.ui.dialogs;

import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.ui.interfaces.ISettingTransaction;

public interface ISettingDialogListener {
    void pushSettingPacket(LuaSettingPacket packet);
    void pushSettingPacket(LuaSettingPacket packet, LuaSettingExtended original, int position, ISettingTransaction transactionCallback);
}
