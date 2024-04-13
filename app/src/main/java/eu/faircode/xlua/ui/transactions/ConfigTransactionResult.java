package eu.faircode.xlua.ui.transactions;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.api.configs.MockConfig;
import eu.faircode.xlua.api.configs.MockConfigPacket;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropSetting;
import eu.faircode.xlua.utilities.CollectionUtil;

public class ConfigTransactionResult extends Transaction {
    public List<MockConfig> configs = new ArrayList<>();
    public List<MockConfigPacket> packets = new ArrayList<>();
    public List<MockConfig> failed = new ArrayList<>();
    public List<MockConfig> succeeded = new ArrayList<>();

    public int getAdapterPosition() { return configs.size() == 1 ? adapterPosition : -1; }
    public boolean hasAnyFailed() { return CollectionUtil.isValid(failed); }
    public boolean hasAnySucceeded() { return CollectionUtil.isValid(succeeded); }
    public boolean isBatch() { return configs != null && configs.size() > 1; }

    public MockConfigPacket getPacket() { return !packets.isEmpty() ? packets.get(0) : null; }
    public MockConfigPacket getPacket(int index) { return !packets.isEmpty() && packets.size() > index ? packets.get(index) : null;  }
    public MockConfig getSucceeded() { return hasAnySucceeded() ? succeeded.get(0) : null; }
    public MockConfig getSucceeded(int index) { return configs.size() > index ? succeeded.get(index) : null; }
    public MockConfig getFailed() { return hasAnyFailed() ? failed.get(0) : null; }
    public MockConfig getFailed(int index) { return failed.size() > index ? failed.get(index) : null; }
    public MockConfig getConfig() { return !configs.isEmpty() ? configs.get(0) : null; }
    public MockConfig getConfig(int index) { return  configs.size() > index ? configs.get(index) : null; }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(super.toString())
                .append(" succeeded=")
                .append(succeeded.size()).append("\n")
                .append(" failed=")
                .append(failed.size()).append("\n")
                .append(" configs=")
                .append(configs.size()).toString();
    }
}
