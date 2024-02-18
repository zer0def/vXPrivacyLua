package eu.faircode.xlua.api.standard.interfaces;

import android.content.ContentValues;

public interface IDataDynamic {
    public String getSelectionArgs();
    public String[] createValueArgs(String replaceValue);
    public ContentValues createContentValues(String replaceValue);
}
