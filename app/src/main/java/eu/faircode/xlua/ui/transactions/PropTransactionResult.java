package eu.faircode.xlua.ui.transactions;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropSetting;
import eu.faircode.xlua.utilities.CollectionUtil;

public class PropTransactionResult extends Transaction {
    public List<MockPropSetting> settings = new ArrayList<>();
    public List<MockPropPacket> packets = new ArrayList<>();
    public List<MockPropSetting> failed = new ArrayList<>();
    public List<MockPropSetting> succeeded = new ArrayList<>();

    public int getAdapterPosition() { return settings.size() == 1 ? adapterPosition : -1; }
    public boolean hasAnyFailed() { return CollectionUtil.isValid(failed); }
    public boolean hasAnySucceeded() { return CollectionUtil.isValid(succeeded); }
    public boolean isBatch() { return settings != null && settings.size() > 1; }


    public MockPropPacket getPacket() { return !packets.isEmpty() ? packets.get(0) : null; }
    public MockPropPacket getPacket(int index) { return !packets.isEmpty() && packets.size() > index ? packets.get(index) : null;  }
    public MockPropSetting getSucceeded() { return hasAnySucceeded() ? succeeded.get(0) : null; }
    public MockPropSetting getSucceeded(int index) { return settings.size() > index ? succeeded.get(index) : null; }
    public MockPropSetting getFailed() { return hasAnyFailed() ? failed.get(0) : null; }
    public MockPropSetting getFailed(int index) { return failed.size() > index ? failed.get(index) : null; }
    public MockPropSetting getSetting() { return !settings.isEmpty() ? settings.get(0) : null; }
    public MockPropSetting getSetting(int index) { return  settings.size() > index ? settings.get(index) : null; }

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
