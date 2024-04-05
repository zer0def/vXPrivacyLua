package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.DataNullElement;
import eu.faircode.xlua.random.elements.DataStringElement;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class RandomNetworkType implements IRandomizer {
    private final List<ISpinnerElement> dataStates = new ArrayList<>();
    public RandomNetworkType() {
        dataStates.add(DataNullElement.EMPTY_ELEMENT);
        dataStates.add(DataStringElement.create("unknown"));
        dataStates.add(DataStringElement.create("GPRS"));
        dataStates.add(DataStringElement.create("EDGE"));
        dataStates.add(DataStringElement.create("UMTS"));
    }

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "gsm.network.type"; }

    @Override
    public String getName() {
        return "Network Type 2";
    }

    @Override
    public String getID() {
        return "%net_type_2%";
    }

    @Override
    public String generateString() { return dataStates.get(ThreadLocalRandom.current().nextInt(1, dataStates.size())).getValue(); }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return this.dataStates; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
