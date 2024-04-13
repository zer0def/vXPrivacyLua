package eu.faircode.xlua.ui.transactions;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.utilities.CollectionUtil;

public class SettingTransactionResult extends Transaction {
    public List<LuaSettingPacket> packets = new ArrayList<>();
    public List<LuaSettingExtended> settings = new ArrayList<>();
    public List<LuaSettingExtended> failed = new ArrayList<>();
    public List<LuaSettingExtended> succeeded = new ArrayList<>();

    public int getAdapterPosition() { return settings.size() == 1 ? adapterPosition : -1; }
    public boolean hasAnyFailed() { return CollectionUtil.isValid(failed); }
    public boolean hasAnySucceeded() { return CollectionUtil.isValid(succeeded); }
    public boolean isBatch() { return settings != null && settings.size() > 1; }

    public LuaSettingPacket getPacket() { return !packets.isEmpty() ? packets.get(0) : null; }
    public LuaSettingPacket getPacket(int index) { return !packets.isEmpty() && packets.size() > index ? packets.get(index) : null;  }
    public LuaSettingExtended getSucceeded() { return hasAnySucceeded() ? succeeded.get(0) : null; }
    public LuaSettingExtended getSucceeded(int index) { return settings.size() > index ? succeeded.get(index) : null; }
    public LuaSettingExtended getFailed() { return hasAnyFailed() ? failed.get(0) : null; }
    public LuaSettingExtended getFailed(int index) { return failed.size() > index ? failed.get(index) : null; }
    public LuaSettingExtended getSetting() { return !settings.isEmpty() ? settings.get(0) : null; }
    public LuaSettingExtended getSetting(int index) { return  settings.size() > index ? settings.get(index) : null; }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.toString())
                .append(" succeeded=")
                .append(succeeded.size()).append("\n")
                .append(" failed=")
                .append(failed.size()).append("\n")
                .append(" settings=")
                .append(settings.size()).toString();
    }
}
