package eu.faircode.xlua.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.json.IJsonHelper;
import eu.faircode.xlua.json.JsonHelper;

public class DatabaseHelper {
    private static String TAG = "XLua.DatabaseHelper";

    public static <T extends IJsonHelper>
        boolean updateItems(
                XDataBase database,
                String tableName,
                List<T> items) {

        if(!XDataBase.isReady(database)) {
            Log.e(TAG, "Database is not Ready...");
            return false;
        }

        Log.i(TAG, "Updating Database [" + database.getName() + "] Entries=" + items.size() + " in Table [" + tableName + "]");

        try {
            if(!database.beginTransaction()) {
                Log.e(TAG, "[" + database.getName() + "] Failed to [beginTransaction][updateItems]");
                return false;
            }

            if(!database.hasTable(tableName)) {
                Log.e(TAG, "Database [" + database.getName() + "] Is missing Table [" + tableName + "] Make sure your pre-init Table First [updateItems]");
                return false;
            }

            for(T item : items) {
                if(!database.insertOrUpdate(tableName, item.createContentValues()))
                    Log.e(TAG, "[" + database.getName() + "] Failed to [insertOrUpdate] Item=[[" + item + "]] in Table [" + tableName + "]");
            }

            database.setTransactionSuccessful();
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to Update Database [" + database.getName() + "] Entries=" + items.size() + " in Table [" + tableName + "]\n" + e + "\n" + Log.getStackTraceString(e));
            return false;
        }finally {
            database.endTransaction();
            database.writeUnlock();
        }
    }

    public static <T extends  IJsonHelper>
        boolean updateItem(
                XDataBase database,
                String tableName,
                T item) {

        if(!XDataBase.isReady(database)) {
            Log.e(TAG, "Database is not Ready...");
            return false;
        }

        Log.i(TAG, "Updating Database [" + database.getName() + "] Entry [[" + item + "]]" + " in Table [" + tableName + "]");

        //We synchronized on a Higher level
        //These functions just use the DB Locks , not synchronized lock
        database.writeLock();
        try {
            if(!database.beginTransaction()) {
                Log.e(TAG, "[" + database.getName() + "] Failed to [beginTransaction][updateItem]");
                return false;
            }

            if(!database.hasTable(tableName)) {
                Log.e(TAG, "Database [" + database.getName() + "] Is missing Table [" + tableName + "] Make sure your pre-init Table First [updateItem]");
                return false;
            }

            Log.i(TAG, "[" + database.getName() + "][" + tableName + "] Inserting Database Item::" + item);

            if(!database.insertOrUpdate(tableName, item.createContentValues())) {
                Log.e(TAG, "[" + database.getName() + "] Failed to [insertOrUpdate] [" + item + "] Into Table [" + tableName + "]");
                return false;
            }

            Log.i(TAG, "Finished Inserting Item Into Table! " + item);
            database.setTransactionSuccessful();
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to Update Database Item. Database=" + database.getName() + " Table=" + tableName + " Item=" + item + "\n" + e + "\n" + Log.getStackTraceString(e));
            return false;
        }finally {
            database.endTransaction();
            database.writeUnlock();
        }
    }

    public static <T extends IJsonHelper>
        boolean prepareTableIfMissingOrInvalidCount(
            Context context,
            XDataBase database,
            String tableName,
            HashMap<String, String> columns,
            String jsonName,
            boolean stopOnFirstJson,
            Class<T> typeClass,
            int itemCheckCount) {

        //Do note as I feared
        //This wont will if in different app context
        //so we cant store them in .json ?
        //Also make check to try to avoid reading .json file when in different context at all costs

        if(!XDataBase.isReady(database)) {
            Log.e(TAG, "Database is not Ready...");
            return false;
        }

        if(!database.hasTable(tableName) || (itemCheckCount > 0 && database.tableEntries(tableName) < itemCheckCount))  {
            Log.w(TAG, "Table [" + tableName + "] in Database [" + database.getName() + "] Is either NULL or Not a Valid Count fixing...");
            List<T> itms = initDatabse(context, database, tableName, columns, jsonName, stopOnFirstJson, typeClass, itemCheckCount);
            //Maybe return itms ? and the caller can cache it in if needed?
            //Tho then they wont be able to tell if table is valid as if Table Exists and Entries Count is Valid then it will NOT initDatabase there for will return empty
            if(itms == null || itms.isEmpty()) {
                Log.e(TAG, "Failed to Prepare Table: [" + tableName + "] in Database [" + database.getName() + "]. init Came back with EMPTY or NULL Entries");
                return false;
            }
        }

        if(!database.hasTable(tableName)) {
            Log.e(TAG, "Failed to Find Table: [" + tableName + "] in Database: [" + database.getName() + "] (I tried creating it....)");
            return false;
        }

        if(itemCheckCount > 0) {
            int dbTableCount = database.tableEntries(tableName);
            if(dbTableCount < itemCheckCount) {
                Log.e(TAG, "Failed to Prepare Table: [" + tableName + "] in Database [" + database.getName() + "], Count is Not adding up... Needed=" + itemCheckCount + " Given=" + dbTableCount);
                return false;
            }
        }

        Log.i(TAG, "Database: [" + database.getName() + "] Table [" + tableName + "] Is Ready to Go !!!");
        return true;
    }

    public static <T extends IJsonHelper>
        List<T> initDatabse(
                Context context,
                XDataBase database,
                String tableName,
                HashMap<String, String> columns,
                String jsonName,
                boolean stopOnFirstJson,
                Class<T> typeClass,
                int itemCheckCount) {

        List<T> list = new ArrayList<>();

        if(!XDataBase.isReady(database)) {
            Log.e(TAG, "Database is not Ready...");
            return list;
        }

        Log.i(TAG, "Init Database: " + database.getName());

        database.writeLock();
        database.readLock();

        try {
            if (!database.hasTable(tableName) || database.tableEntries(tableName) == 0) {
                Log.w(TAG, "[" + database.getName() + "] Table [" + tableName + "] Is NULL or EMPTY Fixing...");

                if(!database.createTable(columns, tableName)) {
                    Log.e(TAG, "[" + database.getName() + "] Failed to create table [" + tableName + "]");
                    return list;
                }

                for(T item : JsonHelper.findJsonElementsFromAssets(
                        XUtil.getApk(context), jsonName, stopOnFirstJson, typeClass)) {

                    if (!database.insertOrUpdate(tableName, item.createContentValues())) {
                        Log.e(TAG, "[" + database.getName() + "] Failed to insertOrUpdate [" + item + "] into [" + tableName + "] Table");
                        continue;
                    }

                    list.add(item);
                }
            }else {
                list = getFromDatabase(database, tableName, typeClass);
                if(list.size() < itemCheckCount && itemCheckCount > 0) {
                    Log.w(TAG, "[" + database.getName() + "] [" + tableName + "] entries count [" + list.size() + "] is less than [" + itemCheckCount + "] , fixing...");

                    List<T> newItems = new ArrayList<>();

                    for(T item : JsonHelper.findJsonElementsFromAssets(
                            XUtil.getApk(context), jsonName, stopOnFirstJson, typeClass)) {

                        boolean found = false;
                        for(T dbItem : list) {
                            if(dbItem.equals(item)) {
                                found = true;
                                break;
                            }
                        }

                        if(!found) {
                            Log.w(TAG, "[" + database.getName() + "] Table [" + tableName + "] Is Missing Entry:: " + item);

                            if (!database.insertOrUpdate(tableName, item.createContentValues())) {
                                Log.e(TAG, "[" + database.getName() + "] Failed to insertOrUpdate [[" + item + "]] into [" + tableName + "] Table");
                                continue;
                            }

                            newItems.add(item);
                        }
                    }

                    Log.i(TAG, "Finished Loading Missing Items:: Added=" + newItems.size());
                    list.addAll(newItems);
                }
            }

            Log.i(TAG, "Finished Loading Database Items Count=" + list.size());
            return list;
        }catch (Exception e) {
            Log.e(TAG, "[" + database.getName() + "][" + tableName + "] Internal Database Init ERROR::\n" + e + Log.getStackTraceString(e));
            return list;
        }finally {
            database.writeUnlock();
            database.readUnlock();
        }
    }

    public static <T extends IDatabaseHelper> List<T> getFromDatabase(XDataBase database, String tableName, Class<T> typeClass) {
        Log.i(TAG, "Grabbing Items from Table [" + tableName + "] From Database [" + database.getName() + "]");
        List<T> list = new ArrayList<>();
        try {
            Cursor cursor = database.getDatabase().rawQuery("SELECT * FROM " + tableName, null);
            if (cursor.moveToFirst()) {
                do {
                    T item = typeClass.newInstance(); // Create a new instance of T
                    item.readFromCursor(cursor); // Read data from cursor
                    list.add(item);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading from database: " + e.getMessage());
        }

        Log.i(TAG, "Total Items in Database=" + list.size());
        return list;
    }
}
