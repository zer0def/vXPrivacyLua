package eu.faircode.xlua.utilities;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.api.standard.interfaces.IJsonSerial;

public class CursorUtil {
    private static final String TAG = "XLua.CursorUtil";

    public static MatrixCursor copyKeyValue(Cursor c, String replaceKeyName, String newValue) {
        MatrixCursor newCursor = new MatrixCursor(c.getColumnNames());
        try {
            c.moveToFirst();
            int keyIx = c.getColumnIndex("key");
            int valIx = c.getColumnIndex("value");
            do {
                MatrixCursor.RowBuilder rowBuilder = newCursor.newRow();
                String kName = c.getString(keyIx);
                if(kName == null) continue;
                rowBuilder.add(kName);
                if(kName.equalsIgnoreCase(replaceKeyName)) {
                    rowBuilder.add(newValue);
                }else {
                    rowBuilder.add(c.getString(valIx));
                }
            }while (c.moveToNext());
            newCursor.moveToPosition(-1);
            //newCursor.moveToFirst();
            return newCursor;
        }catch (Exception e) {
            Log.e(TAG, "Failed to copy key value matrix cursor! e=" + e);
            //newCursor.moveToFirst();
            newCursor.moveToPosition(-1);
            return newCursor;
        }
    }

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
        return getInteger(c, columnName, null);
    }

    public static Integer getInteger(Cursor c, String columnName, Integer defaultValue) {
        int ix = c.getColumnIndex(columnName);
        if(ix == -1) return defaultValue;

        if(c.getType(ix) == Cursor.FIELD_TYPE_INTEGER)
            return c.getInt(ix);

        return defaultValue;
    }

    public static String getString(Cursor c, String columnName) { return getString(c, columnName, null); }
    public static String getString(Cursor c, String columnName, String defaultValue) {
        int ix = c.getColumnIndex(columnName);
        if(ix < 0)
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
    public static <T extends IJsonSerial> Cursor toMatrixCursor(Collection<T> items, boolean marshall, int flags)  {
        MatrixCursor result = new MatrixCursor(new String[]{marshall ? "blob" : "json"});
        IJsonSerial lastItem = null;
        int finished = 0;
        try {
            for (IJsonSerial item : items) {
                lastItem = item;
                if (marshall) {
                    Parcel parcel = Parcel.obtain();
                    item.writeToParcel(parcel, flags);
                    //item.fromParcel();
                    //item.writeToParcel(parcel, flags);
                    result.newRow().add(parcel.marshall());
                    parcel.recycle();
                } else {
                    //result.newRow().add(item.toJSON());
                    result.addRow(new Object[]{item.toJSON()});
                }

                finished++;
            }
        }catch (JSONException je) {
            Log.e(TAG, "[toMatrixCursor] Error, JSON Error [toJson] => " + lastItem + " finished=" + finished + " e=" + je + " stack=\n" + Log.getStackTraceString(je));
        }catch (Exception e) {
            Log.e(TAG, "[toMatrixCursor] Error => " + lastItem.toString() + " finished=" + finished + " e=" + e + " stack=\n" + Log.getStackTraceString(e));
        }finally {
            Log.i(TAG, "[toMatrixCursor] marshall=" + marshall + " from items=" + items.size() + " finished items=" + finished + " finished converting to a matrix cursor");
        }

        return result;
    }

    public static <T extends IJsonSerial> Collection<T> readCursorAs(Cursor cursor, boolean marshall, Class<T> classType) {
        Collection<T> items = new ArrayList<>();
        int pos = 0;
        try {
            if (marshall) {
                while (cursor != null && cursor.moveToNext()) {
                    T inst = classType.newInstance();
                    byte[] marshaled = cursor.getBlob(0);
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(marshaled, 0, marshaled.length);
                    parcel.setDataPosition(0);
                    //XMockConfig config = XMockConfig.CREATOR.createFromParcel(parcel);
                    inst.fromParcel(parcel);
                    parcel.recycle();
                    items.add(inst);
                    pos++;
                }
            } else {
                while (cursor != null && cursor.moveToNext()) {
                    T inst = classType.newInstance();
                    String json = cursor.getString(0);
                    inst.fromJSONObject(new JSONObject(json));
                    items.add(inst);
                    pos++;
                }
            }
        }catch (JSONException je) {
            Log.e(TAG, "[readCursorAs] Failed to convert from Cursor JSON! size=" + items.size() + " pos=" + pos + " e=" + je + "stack=\n" + Log.getStackTraceString(je));
        }catch (Exception e) {
            Log.e(TAG, "[readCursorAs] Failed to convert from Cursor! size=" + items.size() + " pos=" + pos + " e=" + e + "stack=\n" + Log.getStackTraceString(e));
        }finally {
            Log.i(TAG, "[readCursorAs] Items read=" + items.size() + " last pos=" + pos);
        }

        return items;
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
