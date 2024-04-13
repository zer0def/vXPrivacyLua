package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class RandomNetAddress implements IRandomizer {
    private static final String IP_FORMAT = "%s.%s.%s.%s";
    private static final List<String> ipList = Arrays.asList("127.0.0.1", "10.0.0.1", "192.168.1.0", "random");

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()) || setting.equalsIgnoreCase("network.gateway"); }

    @Override
    public String getSettingName() {  return "network.host.address"; }

    @Override
    public String getName() {
        return "Host Address";
    }

    @Override
    public String getID() {
        return "%ipaddress%";
    }

    @Override
    public String generateString() {
        String ip = ipList.get(ThreadLocalRandom.current().nextInt(0, ipList.size()));
        if(ip.equals("random")) {
            return String.format(IP_FORMAT,
                    ThreadLocalRandom.current().nextInt(1, 192),
                    ThreadLocalRandom.current().nextInt(0, 168),
                    ThreadLocalRandom.current().nextInt(0, 9),
                    ThreadLocalRandom.current().nextInt(0, 9));
        } return ip;
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
