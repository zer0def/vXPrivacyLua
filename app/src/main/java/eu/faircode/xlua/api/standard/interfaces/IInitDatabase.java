package eu.faircode.xlua.api.standard.interfaces;

import android.content.Context;

import eu.faircode.xlua.XDatabase;

public interface IInitDatabase {
    XDatabase getDatabase(Context context);
    boolean initDatabase(Context context, boolean checkIsReady);
    void reset(boolean setDatabaseNull);
}
