package eu.faircode.xlua.api.objects.xlua.app;

import android.content.Context;

public interface IListener {
    void onAssign(Context context, String groupName, boolean assign);
}
