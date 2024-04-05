package eu.faircode.xlua.random.elements;

import android.content.Context;

import eu.faircode.xlua.random.IRandomizerManager;

public interface IManagedSpinnerElement extends ISpinnerElement {
    boolean isNaN();
    void setAsSelected();
    IRandomizerManager getManager();
    String generateString(Context context);
}
