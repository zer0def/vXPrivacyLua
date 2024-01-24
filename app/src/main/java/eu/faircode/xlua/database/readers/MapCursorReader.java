package eu.faircode.xlua.database.readers;

import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*public class MapCursorReader {
    private static final String TAG = "XLua.MapCursorReader";

    private String[] fieldNames;
    private Field[] fields;
    private int[] fieldIndexes;

    private Cursor cursor;

    public <T extends ICursorReader> void init(Class<T> clazz, String[] fieldNames, Cursor cursor) {
        this.fieldNames = fieldNames.clone();
        this.fields = new Field[fieldNames.length];
        this.fieldIndexes = new int[fieldNames.length];

        try {
            for(int i = 0; i < fieldNames.length; i++) {
                String fieldName = fieldNames[i];
                fieldIndexes[i] = cursor.getColumnIndex(fieldName);
                fields[i] = clazz.getDeclaredField(fieldName);
            }
        }catch (Throwable e) {
            Log.e(TAG, "Failed to create MAP for Cursor!\n" + e + "\n" + Log.getStackTraceString(e));
        }
    }

    public <T extends ICursorReader> boolean readNext(T obj, int flags) {
        if(!cursor.moveToNext())
            return false;

        obj.readCursor(cursor, flags);
        return true;
    }


    public class Test implements IReader {
        public String data;
        public String data2;
        public int data3;
        public String data4;
        public String data5;

        @Override
        public void readCursor(Cursor cursor, int flags) {
            if(flags < 2) {
                int c1 = cursor.getColumnIndex("data");
                data = c1 == -1 ? null : cursor.getString(c1);

                int c2 = cursor.getColumnIndex("data2");
                data2 = c2 == -1 ? null : cursor.getString(c2);
            }

            if(flags < 3) {
                int c3 = cursor.getColumnIndex("data3");
                data3 = c3 == -1 ? null : cursor.getInt(c3);

                int c4 = cursor.getColumnIndex("data4");
                data4 = c4 == -1 ? null : cursor.getString(c4);
            }
        }
    }

    public interface IReader {
        void readCursor(Cursor cursor);
    }

    public <T extends ICursorReader> List<T> read(Class<T> clazz, Cursor cursor) {
        List<T> items = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                T obj = clazz.newInstance();
                for(int i = 0; i < fields.length; i++) {
                    if(fields[i].getType() == int.class) {

                    }
                    else if(fields[i].getType() == String.class) {
                        fields[i].set(obj, cursor.getString(fieldIndexes[i]));
                    }
                    else if(fields[i].getType() == Byte.class) {

                    }
                }

                items.add(obj);
            }
        }catch (Throwable e)  {
            Log.e(TAG, "Error Iterating Cursor\n" + e + "\n" + Log.getStackTraceString(e));
        }

        return items;
    }
}
*/