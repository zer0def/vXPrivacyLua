package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.List;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomMAC implements IRandomizer {
    @Override
    public boolean isSetting(String setting) {
        return setting.equalsIgnoreCase(getSettingName()) ||
                setting.equalsIgnoreCase("bluetooth.id") ||
                setting.equalsIgnoreCase("unique.bluetooth.address") ||
                setting.equalsIgnoreCase("net.bssid") ||
                setting.equalsIgnoreCase("unique.network.mac.address");
    }

    @Override
    public String getSettingName() { return "net.mac"; }

    @Override
    public String getName() { return "MAC Format"; }

    @Override
    public String getID() { return "%net_mac%"; }

    @Override
    public String generateString() {
        String rawString = RandomStringGenerator.generateRandomAlphanumericString(12, RandomStringGenerator.UPPER_LETTERS);
        StringBuilder sb = new StringBuilder();
        int rawLen = rawString.length();

        for(int i = 0; i < rawString.length(); i += 2) {
            sb.append(rawString, i, Math.min(i + 2, rawLen));

            if(i + 2 < rawLen)
                sb.append(":");
        }

        return sb.toString();
    }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
