package eu.faircode.xlua.ui.interfaces;

import eu.faircode.xlua.ui.transactions.SettingTransactionResult;

public interface ISettingUpdateEx {
    //void onSettingUpdatedSuccessfully(Context context, LuaSettingExtended setting, XResult result, int position);
    void onSettingUpdate(SettingTransactionResult result);
}
