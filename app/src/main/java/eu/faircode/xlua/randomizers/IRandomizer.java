package eu.faircode.xlua.randomizers;

import java.util.List;

import eu.faircode.xlua.randomizers.elements.ISpinnerElement;

public interface IRandomizer {
    boolean isSetting(String settingName);
    String getSettingName();
    String getName();
    String getID();
    String generateString();
    int generateInteger();

    List<ISpinnerElement> getOptions();
    //byte[] generateBytes();
    //9177334325 jonny
}
