package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomGameID implements IRandomizer {
    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "unique.google.game.id"; }

    @Override
    public String getName() {
        return "Google Game ID";
    }

    @Override
    public String getID() {
        return "%google_game_id%";
    }

    @Override
    public String generateString() { return "g" + RandomStringGenerator.generateRandomNumberString(ThreadLocalRandom.current().nextInt(9, 15)); }

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
