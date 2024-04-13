package eu.faircode.xlua.ui.interfaces;

import java.util.List;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.settings.LuaSettingExtended;

public interface ISettingTransaction {
    void onSettingFinished(LuaSettingExtended setting, int position, XResult result);
}
