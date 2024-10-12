package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomHostName implements IRandomizer {
    private static final List<String> hostNames = Arrays.asList("google.org", "duckduckorg", "ducky", "cia.hq", "fbi.hq", "nsa.hq", "privacy.org", "funny.mommy", "random");

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "network.host.name"; }

    @Override
    public String getName() {
        return "Host Name";
    }

    @Override
    public String getID() {
        return "%host%";
    }

    @Override
    public String generateString() {
        String host = hostNames.get(ThreadLocalRandom.current().nextInt(0, hostNames.size()));
        if(host.equals("random")) {
            return RandomStringGenerator.generateRandomAlphanumericString(ThreadLocalRandom.current().nextInt(5, 25), RandomStringGenerator.LOWER_LETTERS + RandomStringGenerator.UPPER_LETTERS);
        } return host;
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
