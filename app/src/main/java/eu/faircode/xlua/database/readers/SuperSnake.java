package eu.faircode.xlua.database.readers;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




//public class SuperSnake {
    /*private Map<Field, Integer> fields = new HashMap<>();

    private List<String> fieldReturn = new ArrayList<>();
    private List<Integer> fieldOffsets = new ArrayList<>();

    public SuperSnake(Cursor cursor) {
        for(String f : fieldReturn)
            fieldOffsets.add(cursor.getColumnIndex(f));
    }

    //I want some how on each new read item it invokes a delegate or something so its on demand
    //Then a function to just read all
    //Now lets use reflection or sometning to read ?


    public <T extends ICursorReader> List<T> read(Class<T> clazz) {
        Field[] fields = new Field[fieldReturn.size()];
        Integer[] indexes = fieldOffsets.toArray(new Integer[0]);

        for(int i = 0; i < fieldReturn.size(); i++) {
            fields[i] = clazz.getDeclaredField(fieldReturn.get(i));
        }

        for(String f : fieldReturn) {
            fields[i] =
        }
    }*/
//}
