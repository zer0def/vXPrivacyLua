package eu.faircode.xlua.randomizers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.randomizers.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class Random3DigitNumber implements IRandomizer {
    @Override
    public boolean isSetting(String setting) {
        return setting.equalsIgnoreCase("gsm.operator.mnc") ||
                setting.equalsIgnoreCase("gsm.operator.mcc") ||
                setting.equalsIgnoreCase("gsm.operator.id"); }

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
