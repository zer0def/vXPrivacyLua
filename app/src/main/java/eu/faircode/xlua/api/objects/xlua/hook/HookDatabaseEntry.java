package eu.faircode.xlua.api.objects.xlua.hook;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;

import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.api.objects.ISerial;
import eu.faircode.xlua.utilities.CursorUtil;

public class HookDatabaseEntry extends HookDatabaseBase implements ISerial, IDBSerial {
    public HookDatabaseEntry() { }
    public HookDatabaseEntry(String id, String definition) { super(id, definition); }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("definition", definition);
        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {
        if(cursor != null) {
            this.id = CursorUtil.getString(cursor, "id");
            this.definition = CursorUtil.getString(cursor, "definition");
        }
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(id != null) b.putString("id", id);
        if(definition != null) b.putString("definition", definition);
        return b;
    }

    @Override
    public void fromBundle(Bundle bundle) {
        if(bundle != null) {
            id = bundle.getString("id", null);
            definition = bundle.getString("definition", null);
        }
    }

    @Override
    public void fromParcel(Parcel in) {
        if(in != null) {
            this.id = in.readString();
            this.definition = in.readString();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.definition);
    }
}
