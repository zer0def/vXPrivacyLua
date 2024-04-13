package eu.faircode.xlua.ui.interfaces;

import eu.faircode.xlua.ui.transactions.ConfigTransactionResult;

public interface IConfigUpdate {
    void onConfigUpdate(ConfigTransactionResult result);
}
