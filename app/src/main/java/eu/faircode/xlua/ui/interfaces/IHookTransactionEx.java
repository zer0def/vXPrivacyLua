package eu.faircode.xlua.ui.interfaces;

import eu.faircode.xlua.ui.transactions.HookTransactionResult;

public interface IHookTransactionEx {
    void onHookUpdate(HookTransactionResult result);
}
