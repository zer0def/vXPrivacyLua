package eu.faircode.xlua.randomizers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.randomizers.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomAlphaNumeric implements IRandomizer {
    @Override
    public boolean isSetting(String setting) {
        return setting.equalsIgnoreCase("gsm.setting.display.name");
    }

    @Override
    public String getSettingName() {
        return "alpha.str";
    }

    @Override
    public String getName() {
        return "Alphanumeric String";
    }

    @Override
    public String getID() {
        return "%alpha_string%";
    }

    @Override
    public String generateString() { return RandomStringGenerator.generateRandomAlphanumericString(ThreadLocalRandom.current().nextInt(7, 18)); }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
