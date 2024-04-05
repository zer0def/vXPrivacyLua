package eu.faircode.xlua.random.elements;

import androidx.annotation.NonNull;

import java.util.List;

public class DataBoolElement implements ISpinnerElement {
    public static final DataBoolElement TRUE = new DataBoolElement(true);
    public static final DataBoolElement FALSE = new DataBoolElement(false);

    private String displayName;
    private Boolean value;

    public DataBoolElement() { }
    public DataBoolElement(boolean b) {
        this.value = b;
        this.displayName = Boolean.toString(b);
    }

    @Override
    public String getName() { return this.displayName; }

    @Override
    public String getValue() { return Boolean.toString(this.value); }

    @Override
    public boolean isSetting(String settingName) { return false; }

    @Override
    public String getSettingName() { return Integer.toString(this.displayName.hashCode()); }

    @Override
    public String getID() { return Boolean.toString(this.value); }

    @Override
    public String generateString() { return Boolean.toString(this.value); }

    @Override
    public int generateInteger() { return !this.value ? 0 : 1; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }


    @NonNull
    @Override
    public String toString() { return this.displayName; }
}
