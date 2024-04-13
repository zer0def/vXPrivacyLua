package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomBuildTags implements IRandomizer {
    private static final String[] DEFAULT_BUILD_TAGS = new String[] {
        "release-keys",
            "test-keys",
            "dev-keys",
            "debug",
            "eng",
            "user",
            "userdebug",
            "keys",
            "release",
            "unofficial"
    };

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "android.build.tags"; }

    @Override
    public String getName() {
        return "Build Tag";
    }

    @Override
    public String getID() {
        return "%build_tag%";
    }

    @Override
    public String generateString() { return DEFAULT_BUILD_TAGS[ThreadLocalRandom.current().nextInt(0, DEFAULT_BUILD_TAGS.length)]; }

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
