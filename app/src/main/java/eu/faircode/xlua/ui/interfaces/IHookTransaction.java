package eu.faircode.xlua.ui.interfaces;

import java.util.List;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;

public interface IHookTransaction {
    void onGroupFinished(List<LuaAssignment> assignments, int position, boolean assign, XResult result);
}
