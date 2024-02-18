package eu.faircode.xlua.api.standard.interfaces;

import android.content.Context;

public interface IListener {
    void onAssign(Context context, String groupName, boolean assign);
}
