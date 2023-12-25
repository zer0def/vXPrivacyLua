package eu.faircode.xlua.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface IDatabaseHelper {
    void readFromCursor(Cursor cursor);
    ContentValues createContentValues();
}
