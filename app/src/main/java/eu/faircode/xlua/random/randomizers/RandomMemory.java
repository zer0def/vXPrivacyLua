package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class RandomMemory implements IRandomizer {
    @Override
    public boolean isSetting(String setting) {
        return setting.equalsIgnoreCase(getSettingName()) || setting.equalsIgnoreCase("memory.total");
    }

    @Override
    public String getSettingName() {  return "memory.available"; }

    @Override
    public String getName() {
        return "MEMORY";
    }

    @Override
    public String getID() {
        return "%memory%";
    }

    @Override
    public String generateString() { return Integer.toString(generateInteger()); }

    @Override
    public int generateInteger() { return ThreadLocalRandom.current().nextInt(1, 999); }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
