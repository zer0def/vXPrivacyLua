package eu.faircode.xlua.randomizers;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.randomizers.elements.DataNullElement;
import eu.faircode.xlua.randomizers.elements.DataNameValueElement;
import eu.faircode.xlua.randomizers.elements.ISpinnerElement;

public class RandomSIMType implements IRandomizer {
    private final List<ISpinnerElement> dataStates = new ArrayList<>();
    public RandomSIMType() {
        dataStates.add(DataNullElement.EMPTY_ELEMENT);
        dataStates.add(DataNameValueElement.create("LOCAL_SIM", 0));
        dataStates.add(DataNameValueElement.create("REMOTE_SIM", 1));
    }

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "gsm.sim.type"; }

    @Override
    public String getName() {
        return "SIM Type";
    }

    @Override
    public String getID() {
        return "%sim_type%";
    }

    @Override
    public String generateString() {
        ISpinnerElement el = dataStates.get(ThreadLocalRandom.current().nextInt(1, dataStates.size()));
        return el.getValue();
    }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return this.dataStates; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
