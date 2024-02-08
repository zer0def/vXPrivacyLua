package eu.faircode.xlua.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.api.objects.IJsonSerial;
import eu.faircode.xlua.utilities.CollectionUtil;

public class DatabaseHelperEx {
    private static final String TAG = "XLua.DatabaseHelper";
    private static final String TAG_initDatabase = "initDatabase";
    private static final String TAG_prepareTable = "initDatabase";
    private static final String TAG_InsertUpdate = "updateOrInsertItem";
    private static final String TAG_InsertUpdateS = "insertOrUpdateItems";
    private static final String TAG_DELETE_ITEM = "deleteItem";

    private static final String ERROR_PREPARE = "Database not Prepared!";
    private static final String ERROR_READY = "Database not Ready!";
    private static final String ERROR_TRANSACTION = "Failed to begin Database Transaction";
    private static final String ERROR_TABLE = "Table does not Exist/ Could not Create!";
    private static final String ERROR_INSERT = "Failed to Insert Item";
    private static final String ERROR_UPDATE = "Failed to Update Item";
    private static final String ERROR_DELETE = "Failed to Delete Database Table Item";

    public static boolean deleteItem(
            DatabaseQuerySnake query) {

        return deleteItem(query.db, query.tableName, query.getSelectionCompareValues(), query.getSelectionArgs());
    }

    public static boolean deleteItem(
            XDataBase db,
            DatabaseQuerySnake query) {

        return deleteItem(db, query.tableName, query.getSelectionCompareValues(), query.getSelectionArgs());
    }

    public static boolean deleteItem(
            XDataBase db,
            String tableName,
            DatabaseQuerySnake query) {

        return deleteItem(db, tableName, query.getSelectionCompareValues(), query.getSelectionArgs());
    }

    public static boolean deleteItem(
            XDataBase db,
            String tableName,
            String[] selectionArgs,
            String argValues) {

        try {
            if(!db.beginTransaction(true)) {
                error(tableName, db, TAG_DELETE_ITEM, ERROR_TRANSACTION);
                return false;
            }

            if(!db.hasTable(tableName)) {
                error(tableName, db, TAG_DELETE_ITEM, ERROR_TABLE);
                return false;
            }

            if(!db.delete(tableName, argValues, selectionArgs)) {
                error(tableName, db, TAG_DELETE_ITEM, ERROR_DELETE);
                return false;
            }

            db.setTransactionSuccessful();
            return true;
        }finally {
            db.endTransaction(true, false);
        }
    }

    public static <T extends IDBSerial>
        boolean updateItems(
                XDataBase db,
                String tableName,
                Collection<T> items,
                DatabaseQuerySnake query) {

            return insertOrUpdateItems(db, tableName, items, query, true);
    }

    public static <T extends IDBSerial>
        boolean updateItems(
                XDataBase db,
                String tableName,
                Collection<T> items,
                DatabaseQuerySnake query,
                boolean prepareResult) {

        return insertOrUpdateItems(db, tableName, items, query, prepareResult);
    }

    public static <T extends IDBSerial>
        boolean insertItems(
                XDataBase db,
                String tableName,
                Collection<T> items) {

        return insertOrUpdateItems(db, tableName, items, null, true);
    }

    public static <T extends IDBSerial>
        boolean insertItems(
                XDataBase db,
                String tableName,
                Collection<T> items,
                boolean prepareResult) {

        return insertOrUpdateItems(db, tableName, items, null, prepareResult);
    }

    public static <T extends IDBSerial>
        boolean insertOrUpdateItems(
                XDataBase db,
                String tableName,
                Collection<T> items,
                DatabaseQuerySnake query,
                boolean prepareResult) {

        //Updating wont work for now
        //need to make a 'query' for all the objects
        if(!prepareResult) {
            error(tableName, db, TAG_InsertUpdateS, ERROR_PREPARE);
            return false;
        }

        if(!XDataBase.isReady(db)) {
            error(tableName, db, TAG_InsertUpdateS, ERROR_READY);
            return false;
        }

        info(tableName, db, TAG_InsertUpdateS, "Updating Database items count=" + items.size());

        try {
            if(!db.beginTransaction(true)) {
                error(tableName, db, TAG_InsertUpdateS, ERROR_TRANSACTION);
                return false;
            }

            if(!db.hasTable(tableName)) {
                error(tableName, db, TAG_InsertUpdateS, ERROR_TABLE);
                return false;
            }

            if(query != null) {
                for(T item : items) {
                    if(!db.update(tableName, item.createContentValues(), query))
                        error(tableName, db, TAG_InsertUpdateS, ERROR_UPDATE + " item=" + item);
                }
            }else {
                for(T item : items) {
                    if(!db.insert(tableName, item.createContentValues()))
                        error(tableName, db, TAG_InsertUpdateS, ERROR_INSERT + " item=" + item);
                }
            }

            db.setTransactionSuccessful();
            return true;
        }finally {
            db.endTransaction(true, false);
        }
    }

    public static <T extends IDBSerial>
        boolean updateItem(
                DatabaseQuerySnake snake,
                T item) {

        return updateOrInsertItem(snake.db, snake.tableName, snake, item, true);
    }

    public static <T extends IDBSerial>
        boolean updateItem(
                XDataBase database,
                DatabaseQuerySnake snake,
                T item) {

        return updateOrInsertItem(database, snake.tableName, snake, item, true);
    }

    public static <T extends IDBSerial>
        boolean updateItem(
                XDataBase database,
                String tableName,
                DatabaseQuerySnake snake,
                T item) {

        return updateOrInsertItem(database, tableName, snake, item, true);
    }

    public static <T extends IDBSerial>
        boolean updateItem(
                XDataBase database,
                String tableName,
                DatabaseQuerySnake snake,
                T item,
                boolean prepareResult) {

        return updateOrInsertItem(database, tableName, snake, item, prepareResult);
    }

    public static <T extends IDBSerial>
        boolean insertItem(
                XDataBase db,
                String tableName,
                T item) {
        return updateOrInsertItem(db, tableName,null, item, true);
    }

    public static <T extends IDBSerial>
        boolean insertItem(
                XDataBase db,
                String tableName,
                T item,
                boolean prepareResult) {
        return updateOrInsertItem(db, tableName, null, item, prepareResult);
    }

    public static <T extends IDBSerial>
        boolean updateOrInsertItem(
                XDataBase db,
                String tableName,
                DatabaseQuerySnake query,
                T item,
                boolean prepareResult) {

        if(!prepareResult) {
            error(tableName, db, TAG_InsertUpdate, ERROR_PREPARE);
            return false;
        }

        if(!XDataBase.isReady(db)) {
            error(tableName, db, TAG_InsertUpdate, ERROR_READY);
            return false;
        }

        info(tableName, db, TAG_InsertUpdate, "Updating Database Item=" + item);
        try {
            if(!db.beginTransaction(true)) {
                error(tableName, db, TAG_InsertUpdate, ERROR_TRANSACTION);
                return false;
            }

            if(!db.hasTable(tableName)) {
                error(tableName, db, TAG_InsertUpdate, ERROR_TABLE);
                return false;
            }

            if(query == null) {
                info(tableName, db, TAG_InsertUpdate, "Inserting item=" + item);
                if(!db.insert(tableName, item.createContentValues())) {
                    error(tableName, db, TAG_InsertUpdate, ERROR_INSERT + " item=" + item);
                    return false;
                }

                info(tableName, db, TAG_InsertUpdate, "Inserted item=" + item);
            }else {
                info(tableName, db, TAG_InsertUpdate, "Updating item=" + item);
                if(db.update(tableName, item.createContentValues(), query)) {
                    error(tableName, db, TAG_InsertUpdate, ERROR_UPDATE + " item=" + item);
                    return false;
                }

                info(tableName, db, TAG_InsertUpdate, "Updated item=" + item);
            }

            db.setTransactionSuccessful();
            return true;
        }finally {
            db.endTransaction(true, false);
        }
    }

    public static <T extends IJsonSerial>
        boolean prepareTableIfMissingOrInvalidCount (
                Context context,
                XDataBase db,
                String tableName,
                Map<String, String> columns,
                Class<T> typeClass) {
        return prepareTableIfMissingOrInvalidCount(context, db, tableName, columns, null, false, typeClass, 0);
    }

    public static <T extends IJsonSerial>
        boolean prepareTableIfMissingOrInvalidCount(
                Context context,
                XDataBase db,
                String tableName,
                Map<String, String> columns,
                String jsonName,
                boolean stopOnFirstJson,
                Class<T> typeClass,
                int itemCheckCount) {

        //Do note this will not work in HOOK context
        if(!XDataBase.isReady(db)) {
            error(tableName, db, TAG_prepareTable, ERROR_READY);
            return false;
        }

        if(jsonName == null || itemCheckCount < 1) {
            if(db.hasTable(tableName)) {
                info(tableName, db, TAG_prepareTable, "Table does not Require Default Values & Exist !");
                return false;
            }

            if(!db.createTable(columns, tableName)) {
                error(tableName, db, TAG_prepareTable, ERROR_TABLE);
                return false;
            }

            return db.hasTable(tableName);
        }

        if(itemCheckCount > 1 && db.tableEntries(tableName) < itemCheckCount ||  !db.hasTable(tableName)) {
            warning(tableName, db, TAG_prepareTable, "Table is Empty or NULL Fixing to=" + itemCheckCount);
            Collection<T> items = initDatabase(
                    context,
                    db,
                    tableName,
                    columns,
                    jsonName, stopOnFirstJson, typeClass, itemCheckCount);

            if(!CollectionUtil.isValid(items)) {
                error(tableName, db, TAG_prepareTable, "Failed to grab Database Items");
                return false;
            }
        }

        if(!db.hasTable(tableName)) {
            error(tableName, db, TAG_prepareTable, ERROR_TABLE);
            return false;
        }

        if(itemCheckCount > 1) {
            int itemCount = db.tableEntries(tableName);
            if(itemCount < itemCheckCount) {
                error(tableName, db, TAG_prepareTable, "Size is off, table size=" + itemCount + " hardcoded size=" + itemCheckCount);
                return false;
            }
        }

        info(tableName, db, TAG_prepareTable, "Its Ready to Go!");
        return true;
    }

    public static  <T extends IJsonSerial> Collection<T> initDatabase(
            Context context,
            XDataBase db,
            String tableName,
            Map<String, String> columns,
            String jsonName,
            boolean stopOnFirstJson,
            Class<T> typeClass,
            int itemCheckCount) {

        Collection<T> items = new ArrayList<>();

        Log.i(TAG, "init tableName=" + tableName + " count=" + itemCheckCount + " db=" + db);

        if(!XDataBase.isReady(db)) {
            error(tableName, db, TAG_initDatabase, ERROR_READY);
            return items;
        }

        Log.i(TAG, "Database is read=" + db + " table=" + tableName);
        //MAKE sure it goes (writeLock, readLock) when reversed it tends to generate unpleasing results
        //read.unlock()
        //If the number of readers is now zero then the lock is made available for write lock attempts.
        //If the current thread does not hold this lock then IllegalMonitorStateException is thrown.
        if(!db.beginTransaction(true)) {
            error(tableName, db, TAG_initDatabase, ERROR_TRANSACTION);
            return items;
         }

        db.readLock();

        try {
            if(!db.hasTable(tableName) || db.tableIsEmpty(tableName)) {
                warning(tableName, db, TAG_initDatabase, "Table is Empty or NULL");

                if(!db.createTable(columns, tableName)) {
                    error(tableName, db, TAG_initDatabase, ERROR_TABLE);
                    return items;
                }

                for(T item : JsonHelper.findJsonElementsFromAssets(
                        XUtil.getApk(context),
                        jsonName,
                        stopOnFirstJson,
                        typeClass
                )) {
                    if(!db.insert(tableName, item.createContentValues())) {
                        error(tableName, db, TAG_initDatabase, ERROR_INSERT + " item=" + item);
                        continue;
                    }

                    items.add(item);
                }
            }else {
                items = getFromDatabase(db, tableName, typeClass);
                info(tableName, db, TAG_initDatabase, "itemCheckCount=" + itemCheckCount + " size=" + items.size());
                if(itemCheckCount > 0 && items.size() < itemCheckCount) {
                    error(tableName, db, TAG_initDatabase, "Size is off, table size=" + items.size() + " hardcoded size=" + itemCheckCount);

                    Collection<T> newItems = new ArrayList<>();

                    for(T item : JsonHelper.findJsonElementsFromAssets(
                            XUtil.getApk(context),
                            jsonName,
                            stopOnFirstJson,
                            typeClass
                    )) {
                        boolean found = false;
                        for(T dItem : items) {
                            if(dItem.equals(item)) {
                                found = true;
                                break;
                            }
                        }

                        if(!found) {
                            warning(tableName, db, TAG_initDatabase, "Missing Table Item=" + item);
                            if(!db.insert(tableName, item.createContentValues())) {
                                error(tableName, db, TAG_initDatabase, ERROR_INSERT + " item=" + item);
                                continue;
                            }

                            newItems.add(item);
                        }
                    }

                    items.addAll(newItems);
                }
            }

            if(!CollectionUtil.isValid(items))
                error(tableName, db, TAG_initDatabase, "Returning EMPTY List from Database Entries!");

            info(tableName, db, TAG_initDatabase, "Finished Loading Database Items Count=" + items.size());
            db.setTransactionSuccessful();
            return items;
        }catch (Exception e) {
            error(tableName, db, TAG_initDatabase, e);
            return items;
        }finally {
            info(tableName, db, TAG_initDatabase, "Items Count=" + items.size());
            db.endTransaction(true, false);
            db.readUnlock();
        }
    }

    public static <T extends IDBSerial> Collection<T> getFromDatabase(
            XDataBase db,
            String tableName,
            Class<T> typeClass) {

        Collection<T> items = new ArrayList<>();
        Log.e(TAG, "[getFromDatabase] table=" + tableName + " db=" + db);
        try {
            Cursor c= db.getDatabase().rawQuery("SELECT * FROM " + tableName, null);
            while (c.moveToNext()) {
                T item = typeClass.newInstance();
                item.fromCursor(c);
                items.add(item);
            }
        }catch (Exception e) {
            Log.e(TAG, "[getFromDatabase] error , table=" + tableName + " db=" + db);
        }

        Log.i(TAG, "items in db=" + items.size());
        return items;
    }

    private static void info(String tableName, XDataBase db, String methodName, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("PROGRESS\n");
        doLog(sb, tableName, db, methodName, message);
        Log.i(TAG, sb.toString());
    }

    private static void warning(String tableName, XDataBase db, String methodName, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("WARNING\n");
        doLog(sb, tableName, db, methodName, message);
        Log.w(TAG, sb.toString());
    }

    private static void error(String tableName, XDataBase db, String methodName, String message) { error(tableName, db, methodName, new Exception(message)); }
    private static void error(String tableName, XDataBase db, String methodName, Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("ERROR");
        doLog(sb, tableName, db, methodName, e == null ? "nil" : e.getMessage());
        if(e != null) {
            sb.append("\nStack=\n");
            sb.append(Log.getStackTraceString(e));
        }

        Log.e(TAG, sb.toString());
    }

    private static void doLog(StringBuilder sb, String tableName, XDataBase db, String methodName, String message) {
        sb.append("Database=");
        sb.append(db);
        sb.append(" table=");
        sb.append(tableName);
        if (methodName != null) {
            sb.append("\nmethod=");
            sb.append(methodName);
        }

        if (message != null) {
            sb.append("\nmessage=");
            sb.append(message);
        }
    }
}
