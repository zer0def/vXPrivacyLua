package eu.faircode.xlua.ui.interfaces;

import eu.faircode.xlua.ui.transactions.PropTransactionResult;

public interface IPropertyUpdate {
    void onPropertyUpdate(PropTransactionResult result);
}
