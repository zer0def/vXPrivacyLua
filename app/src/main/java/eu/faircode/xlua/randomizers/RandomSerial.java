package eu.faircode.xlua.randomizers;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.randomizers.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomSerial implements IRandomizer {

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "unique.serial.no"; }

    @Override
    public String getName() {
        return "Serial Number";
    }

    @Override
    public String getID() {
        return "%serial%";
    }

    @Override
    public String generateString() { return RandomStringGenerator.generateRandomAlphanumericString(ThreadLocalRandom.current().nextInt(8, 18), RandomStringGenerator.UPPER_LETTERS); }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
