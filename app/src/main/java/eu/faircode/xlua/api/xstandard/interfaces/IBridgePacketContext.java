package eu.faircode.xlua.api.xstandard.interfaces;

import android.content.Context;

import eu.faircode.xlua.XDatabase;

public interface IBridgePacketContext {
    String getCommandPrefix();
    String getMethod();
    Context getContext();
    XDatabase getDatabase();
    String getPackageName();
}
