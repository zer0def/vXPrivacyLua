package eu.faircode.xlua.randomizers;

import androidx.annotation.NonNull;

import java.util.List;

import eu.faircode.xlua.randomizers.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomGSF implements IRandomizer {
    @Override
    public boolean isSetting(String setting) {
        return setting.equalsIgnoreCase(getSettingName()) || setting.equalsIgnoreCase("unique.gsf.id");
    }

    @Override
    public String getSettingName() { return "gsf.id"; }

    @Override
    public String getName() {
        return "GSF ID";
    }

    @Override
    public String getID() {
        return "%gsf_id%";
    }

    @Override
    public String generateString() {
        return RandomStringGenerator.generateRandomNumberString(19);
    }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
