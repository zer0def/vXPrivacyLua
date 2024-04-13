package eu.faircode.xlua.ui.transactions;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.hook.assignment.LuaAssignmentPacket;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropSetting;
import eu.faircode.xlua.ui.HookGroup;
import eu.faircode.xlua.utilities.CollectionUtil;

public class HookTransactionResult extends Transaction {
    public List<XLuaHook> hooks = new ArrayList<>();
    public List<LuaAssignmentPacket> packets = new ArrayList<>();
    public List<XLuaHook> failed = new ArrayList<>();
    public List<XLuaHook> succeeded = new ArrayList<>();
    public List<LuaAssignment> assignments = new ArrayList<>();

    public HookGroup group;

    public int getAdapterPosition() { return hooks.size() == 1 ? adapterPosition : -1; }
    public boolean hasAnyFailed() { return CollectionUtil.isValid(failed); }
    public boolean hasAnySucceeded() { return CollectionUtil.isValid(succeeded); }
    public boolean isBatch() { return hooks != null && hooks.size() > 1; }

    public LuaAssignmentPacket getPacket() { return !packets.isEmpty() ? packets.get(0) : null; }
    public LuaAssignmentPacket getPacket(int index) { return !packets.isEmpty() && packets.size() > index ? packets.get(index) : null;  }
    public XLuaHook getSucceeded() { return hasAnySucceeded() ? succeeded.get(0) : null; }
    public XLuaHook getSucceeded(int index) { return hooks.size() > index ? succeeded.get(index) : null; }
    public XLuaHook getFailed() { return hasAnyFailed() ? failed.get(0) : null; }
    public XLuaHook getFailed(int index) { return failed.size() > index ? failed.get(index) : null; }
    public XLuaHook getHook() { return !hooks.isEmpty() ? hooks.get(0) : null; }
    public XLuaHook getHook(int index) { return  hooks.size() > index ? hooks.get(index) : null; }

    public LuaAssignment getAssignment() { return !assignments.isEmpty() ? assignments.get(0) : null; }
    public LuaAssignment getAssignment(int index) { return  assignments.size() > index ? assignments.get(index) : null; }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.toString())
                .append(" succeeded=")
                .append(succeeded.size()).append("\n")
                .append(" failed=")
                .append(failed.size()).append("\n")
                .append(" hooks=")
                .append(hooks.size()).toString();
    }
}
