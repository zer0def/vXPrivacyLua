package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class RandomBuildType implements IRandomizer {
    private static final String[] DEFAULT_BUILD_TYPES = new String[] {
            "user",
            "userdebug",
            "eng"
    };

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "android.build.type"; }

    @Override
    public String getName() {
        return "Build Type";
    }

    @Override
    public String getID() {
        return "%build_type%";
    }

    @Override
    public String generateString() { return DEFAULT_BUILD_TYPES[ThreadLocalRandom.current().nextInt(0, DEFAULT_BUILD_TYPES.length)]; }

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
