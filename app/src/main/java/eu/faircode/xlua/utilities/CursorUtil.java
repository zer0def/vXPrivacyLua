package eu.faircode.xlua.utilities;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.api.objects.IJsonSerial;
import eu.faircode.xlua.api.objects.ISerial;

public class CursorUtil {
    public static void closeCursor(Cursor c) {
        if(c != null) {
            try {
                c.close();
            }catch (Throwable e) {

            }
        }
    }

    public static Long getLong(Cursor c, String columnName) {
        int ix = c.getColumnIndex(columnName);
        if(ix == -1) return null;

        //if(c.getType(ix) == Cursor.)
        //    return c.getInt(ix);

        return c.getLong(ix);
    }
    public static Integer getInteger(Cursor c, String columnName) {
        int ix = c.getColumnIndex(columnName);
        if(ix == -1) return null;

        if(c.getType(ix) == Cursor.FIELD_TYPE_INTEGER)
            return c.getInt(ix);

        return null;
    }

    public static String getString(Cursor c, String columnName) { return getString(c, columnName, null); }
    public static String getString(Cursor c, String columnName, String defaultValue) {
        int ix = c.getColumnIndex(columnName);
        if(ix == -1)
            return defaultValue;

        return c.getString(ix);
    }

    public static Boolean getBoolean(Cursor c, String columnName) {
        int ix = c.getColumnIndex(columnName);
        if(ix == -1) return null;
        String v = c.getString(ix).toLowerCase();
        if(v.equals("true"))
            return true;
        if(v.equals("false"))
            return false;

        try {
            return Integer.parseInt(v) == 1;
        }catch (NumberFormatException e) {
            return null;
        }
    }

    public static Map<String, String> toDictionary(Cursor c, boolean close) {
        final Map<String, String> dic = new HashMap<>();

        try {
            if(c != null) {
                while (c.moveToNext())
                    dic.put(c.getString(0), c.getString(1));
            }
        }finally {
            if(close && c != null)
                 closeCursor(c);
        }

        return dic;
    }


    //Check make sure the 'else' wont fuck some of this shit up
    public static <T extends IJsonSerial> Cursor toMatrixCursor(Collection<T> items, boolean marshall, int flags) throws JSONException {
        MatrixCursor result = new MatrixCursor(new String[]{marshall ? "blob" : "json"});
        for(IJsonSerial item : items) {
            if(marshall) {
                Parcel parcel = Parcel.obtain();
                item.writeToParcel(parcel, flags);
                //item.fromParcel();
                //item.writeToParcel(parcel, flags);
                result.newRow().add(parcel.marshall());
                parcel.recycle();
            }else {
                //result.newRow().add(item.toJSON());
                result.addRow(new Object[]{item.toJSON()});
            }
        }

        return result;
    }

    public static <T extends Parcelable> Collection<T> readCursorAs(Cursor cursor, Parcelable.Creator<T> creator) {
        Collection<T> items = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            byte[] marshaled = cursor.getBlob(0);
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(marshaled, 0, marshaled.length);
            parcel.setDataPosition(0);
            T p = creator.createFromParcel(parcel);
            parcel.recycle();
            items.add(p);
        }

        return items;
    }
}
