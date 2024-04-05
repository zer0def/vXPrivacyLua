package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class RandomDNS implements IRandomizer {
    private static final String DNS_FORMAT = "%s.%s.%s.%s";
    private static final List<String> dnsList = Arrays.asList("1.1.1.1", "8.8.8.8", "6.6.6.6", "random");

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "network.dns"; }

    @Override
    public String getName() {
        return "DNS";
    }

    @Override
    public String getID() {
        return "%dns%";
    }

    @Override
    public String generateString() {
        String dns = dnsList.get(ThreadLocalRandom.current().nextInt(0, dnsList.size()));
        if(dns.equals("random")) {
            return String.format(DNS_FORMAT,
                    ThreadLocalRandom.current().nextInt(1, 9),
                    ThreadLocalRandom.current().nextInt(1, 9),
                    ThreadLocalRandom.current().nextInt(1, 9),
                    ThreadLocalRandom.current().nextInt(1, 9));
        } return dns;
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
