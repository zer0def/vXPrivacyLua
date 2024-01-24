package eu.faircode.xlua.database.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/*public class assigned_hook_data implements IDataFace {
    private int hookId_column_index;
    private int used_column_index;
    private Cursor cursor;
    private Boolean started;

    private String hookId;
    private String used;

    public String getHookId() {
        return hookId;
    }

    public String getUsed() {
        return used;
    }

    public assigned_hook_data(String hookId, String used) {
        this.hookId = hookId;
        this.used = used;
    }

    public assigned_hook_data(Cursor cursor) {
        initCursor(cursor);
    }

    @Override
    public void initCursor(Cursor cursor) {
        this.hookId_column_index = cursor.getColumnIndex("hook");
        this.used_column_index = cursor.getColumnIndex("used");
        this.cursor = cursor;
        this.started = false;
    }

    @Override
    public List<IDataFace> readAll() {
        List<IDataFace> data = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                data.add(new assigned_hook_data(cursor.getString(hookId_column_index), cursor.getString(used_column_index)));
            }while (cursor.moveToNext());
        }

        return data;

        //if(started) {
        //    do {
        //        data.add(new assigned_hook_data(cursor.getString(hookId_column_index), cursor.getString(used_column_index)));
        //    }while (cursor.moveToNext());
        //}
    }

    @Override
    public assigned_hook_data thisAs() {
        return this;
    }

    @Override
    public assigned_hook_data readNext() {
        if(!started) {
            if(!cursor.moveToFirst())
                return null;

            started = true;
        }
        else {
            if(!cursor.moveToNext())
                return null;
        }

        return new assigned_hook_data(cursor.getString(hookId_column_index), cursor.getString(used_column_index));
    }
}*/
