package eu.faircode.xlua.randomizers;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.randomizers.elements.DataBoolElement;
import eu.faircode.xlua.randomizers.elements.DataNullElement;
import eu.faircode.xlua.randomizers.elements.ISpinnerElement;

public class RandomBoolean implements IRandomizer {
    private final List<ISpinnerElement> dataStates = new ArrayList<>();
    public RandomBoolean() {
        dataStates.add(DataNullElement.EMPTY_ELEMENT);
        dataStates.add(DataBoolElement.FALSE);
        dataStates.add(DataBoolElement.TRUE);
    }

    @Override
    public boolean isSetting(String setting) { return setting.endsWith(".bool") || setting.startsWith(".bool") || setting.contains(".bool."); }

    @Override
    public String getSettingName() {  return ".bool"; }

    @Override
    public String getName() {
        return "Boolean";
    }

    @Override
    public String getID() {
        return "%bool%";
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
