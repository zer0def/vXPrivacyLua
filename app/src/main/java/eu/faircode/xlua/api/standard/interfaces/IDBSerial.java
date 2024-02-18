package eu.faircode.xlua.api.standard.interfaces;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface IDBSerial {
    ContentValues createContentValues();
    List<ContentValues> createContentValuesList();

    void fromContentValuesList(List<ContentValues> contentValues);
    void fromContentValues(ContentValues contentValue);

    void fromCursor(Cursor cursor);
}
