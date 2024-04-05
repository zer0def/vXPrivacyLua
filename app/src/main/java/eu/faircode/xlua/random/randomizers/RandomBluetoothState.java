package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.DataNameValueElement;
import eu.faircode.xlua.random.elements.DataNullElement;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class RandomBluetoothState implements IRandomizer {
    private final List<ISpinnerElement> dataStates = new ArrayList<>();
    public RandomBluetoothState() {
        dataStates.add(DataNullElement.EMPTY_ELEMENT);
        dataStates.add(DataNameValueElement.create("STATE_OFF", 10));
        dataStates.add(DataNameValueElement.create("STATE_TURNING_ON", 11));
        dataStates.add(DataNameValueElement.create("STATE_ON", 12));
        dataStates.add(DataNameValueElement.create("STATE_TURNING_OFF", 13));
    }


    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "bluetooth.state"; }

    @Override
    public String getName() {
        return "Bluetooth State";
    }

    @Override
    public String getID() {
        return "%bluetooth_state%";
    }

    @Override
    public String generateString() {
        ISpinnerElement el = dataStates.get(ThreadLocalRandom.current().nextInt(1, dataStates.size()));
        return el.getValue();
    }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return this.dataStates; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}

