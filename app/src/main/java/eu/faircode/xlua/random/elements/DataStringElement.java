package eu.faircode.xlua.random.elements;

import androidx.annotation.NonNull;

import java.util.List;

public class DataStringElement implements ISpinnerElement {
    public static DataStringElement create(String s) { return new DataStringElement(s); }
    public static DataStringElement create(String n, String v) { return new DataStringElement(n, v); }

    private String displayName;
    private String value;

    public DataStringElement() { }
    public DataStringElement(String s) {
        this.displayName = s;
        this.value = s;
    }

    public DataStringElement(String n, String v) {
        this.displayName = n;
        this.value = v;
    }

    @Override
    public String getName() { return this.displayName; }

    @Override
    public String getValue() { return this.value; }

    @Override
    public boolean isSetting(String settingName) { return false; }

    @Override
    public String getSettingName() { return Integer.toString(this.displayName.hashCode()); }

    @Override
    public String getID() { return this.value; }

    @Override
    public String generateString() { return this.value; }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }


    @NonNull
    @Override
    public String toString() { return this.displayName; }
}
