package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomStringOne implements IRandomizer {
    private static final List<String> SETTINGS = Arrays.asList("android.build.incremental", "android.build.host", "android.build.fingerprint", "android.build.codename", "android.build.description", "android.build.display.id", "android.build.flavor");

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()) || SETTINGS.contains(setting); }

    @Override
    public String getSettingName() {  return "random.string.one"; }

    @Override
    public String getName() {
        return "Random String (1)";
    }

    @Override
    public String getID() {
        return "%random_string_one%";
    }

    @Override
    public String generateString() { return RandomStringGenerator.generateRandomAlphanumericString(ThreadLocalRandom.current().nextInt(6, 25));
    }

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
