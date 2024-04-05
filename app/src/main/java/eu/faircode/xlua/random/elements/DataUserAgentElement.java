package eu.faircode.xlua.random.elements;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.random.IRandomizerManager;
import eu.faircode.xlua.random.randomizers.RandomUserAgentManager;

public class DataUserAgentElement implements ISpinnerElement, IManagedSpinnerElement {
    public static DataUserAgentElement create(String s, RandomUserAgentManager randomizerParent) { return new DataUserAgentElement(s, randomizerParent); }
    private final String device;
    private final RandomUserAgentManager randomizerParent;
    public DataUserAgentElement(String device, RandomUserAgentManager randomizerParent) { this.device = device; this.randomizerParent = randomizerParent; }

    @Override
    public boolean isNaN() { return device.equalsIgnoreCase(DataNullElement.EMPTY_ELEMENT.getName()); }

    @Override
    public void setAsSelected() { if(this.randomizerParent != null) this.randomizerParent.setSelectedElement(this); }

    @Override
    public IRandomizerManager getManager() { return this.randomizerParent; }

    @Override
    public String generateString(Context context) {  setAsSelected(); return XMockCall.getRandomUserAgent(context, this.device).getUserAgent(); }

    @Override
    public String getName() { return this.device; }

    @Override
    public String getValue() { return this.device; }

    @Override
    public boolean isSetting(String settingName) { return false; }

    @Override
    public String getSettingName() { return Integer.toString(this.device.hashCode()); }

    @Override
    public String getID() { return this.device; }

    @Override
    public String generateString() { return this.device; }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() { return this.device; }
}
