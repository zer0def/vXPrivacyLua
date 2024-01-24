package eu.faircode.xlua.api.objects;

import android.content.ContentValues;
import android.database.Cursor;

public interface IDBSerial {
    ContentValues createContentValues();
    void fromCursor(Cursor cursor);
}
