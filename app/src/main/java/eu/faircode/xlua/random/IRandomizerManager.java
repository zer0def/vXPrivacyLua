package eu.faircode.xlua.random;

import android.content.Context;

import eu.faircode.xlua.random.elements.IManagedSpinnerElement;

public interface IRandomizerManager {
    boolean hasNaNSelected();
    void setSelectedElement(IManagedSpinnerElement el);
    IManagedSpinnerElement getSelectedElement();
    String generateString(Context context);
}
