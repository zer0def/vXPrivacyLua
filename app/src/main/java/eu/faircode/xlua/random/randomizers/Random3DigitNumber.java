package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class Random3DigitNumber implements IRandomizer {
    private static final List<String> SETTINGS = Arrays.asList("gsm.operator.mnc", "gsm.operator.mcc", "gsm.operator.id", "android.build.radio");

    @Override
    public boolean isSetting(String setting) { return SETTINGS.contains(setting); }

    @Override
    public String getSettingName() {
        return "three.digit.number";
    }

    @Override
    public String getName() {
        return "3 Digit Number";
    }

    @Override
    public String getID() {
        return "%three_digit_number%";
    }

    @Override
    public String generateString() { return Integer.toString(ThreadLocalRandom.current().nextInt(100, 999)); }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
