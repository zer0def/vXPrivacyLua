package eu.faircode.xlua.database;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.DebugUtil;
import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.loggers.DatabaseQueryLogger;

public class DatabaseQuerySnake extends DatabaseQueryBuilder {
    private static final String TAG = "XLua.DatabaseQuerySnake";

    private boolean canCompile = true;
    private Exception error = null;

    public static DatabaseQuerySnake create() { return new DatabaseQuerySnake();}
    public static DatabaseQuerySnake create(String tableName) { return new DatabaseQuerySnake(null, tableName); }
    public static DatabaseQuerySnake create(XDataBase db, String tableName) {
        return new DatabaseQuerySnake(db, tableName);
    }

    public DatabaseQuerySnake() { super(); }
    public DatabaseQuerySnake(XDataBase db, String tableName) { super(db, tableName); }

    public DatabaseQuerySnake setSymbol(String symbol, String compareValue) {
        setSymbol(symbol, compareValue);
        return this;
    }

    public DatabaseQuerySnake setSymbol(String symbol) {
        internal_setSymbol(symbol);
        return this;
    }

    public DatabaseQuerySnake useOr(boolean useOr) {
        internal_useOr(useOr);
        return this;
    }

    public DatabaseQuerySnake whereColumns(String... columnNames) {
        internal_whereColumnsEquals(columnNames);
        return this;
    }

    public DatabaseQuerySnake whereColumnValues(String... values) {
        internal_anchorValuesWithFields(values);
        return this;
    }

    public DatabaseQuerySnake whereColumn(String columnName, String value) {
        internal_whereColumnBinds(columnName, value);
        return this;
    }

    public DatabaseQuerySnake whereColumn(String columnName, int value) {
        internal_whereColumnBinds(columnName, Integer.toString(value), null);
        return this;
    }

    public DatabaseQuerySnake whereColumn(String columnName, int value, String symbol) {
        internal_whereColumnBinds(columnName, Integer.toString(value), symbol);
        return this;
    }

    public DatabaseQuerySnake whereColumn(String columnName, String value, String symbol) {
        internal_whereColumnBinds(columnName, value, symbol);
        return this;
    }

    public DatabaseQuerySnake onlyReturnColumn(String columnName) {
        internal_onlyReturnColumn(columnName);
        return this;
    }

    public DatabaseQuerySnake onlyReturnColumns(String... fields) {
        internal_onlyReturnColumns(fields);
        return this;
    }

    public DatabaseQuerySnake orderBy(String orderByOrFieldName) {
        internal_orderBy(orderByOrFieldName);
        return this;
    }

    public XDataBase getDatabase() { return db; }
    public String getOrderBy() { return orderOrFieldName; }

    public List<String> getOnlyReturn() {
        for(String s : onlyReturn)
            Log.i(TAG, "onlyReturn=" + s);

        return onlyReturn;
    }

    public boolean threwError() { return error != null; }
    public Exception getError() { return error; }

    public Collection<String> queryAsStringList(String columnReturn, boolean cleanUpAfter) {
        if(!canCompile) return null;
        canCompile = false;

        prepareReturn(columnReturn);

        db.readLock();
        Cursor c = query();
        Collection<String> list = new ArrayList<>();
        try {
            if(c != null) {
                int ix = c.getColumnIndex(columnReturn);
                if(ix == -1) {
                    Log.e(TAG, "Database [" + db + "] Table [" + tableName + "] Field Name [" + columnReturn + "] Does not exist in the table or cursor enum ??");
                    return list;
                }

                int typeCode = c.getType(ix);
                if (typeCode == Cursor.FIELD_TYPE_NULL) {
                    Log.e(TAG, "Database [" + db + "] Table [" + tableName + "] Field Name [" + columnReturn + "] Type is null ??");
                    return list;
                }

                if(!c.moveToFirst()) {
                    Log.e(TAG, "Database [" + db + "] Table [" + tableName + "] Field Name [" + columnReturn + "] Failed to get First Element ??");
                    return list;
                }

                switch (typeCode) {
                    case Cursor.FIELD_TYPE_STRING:
                        do { list.add(c.getString(ix)); } while (c.moveToNext());
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        do { list.add(Integer.toString(c.getInt(ix))); } while (c.moveToNext());
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        do { list.add(Float.toString(c.getFloat(ix))); } while (c.moveToNext());
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        //Add
                        break;
                        //fix add long , double , short support
                }
            }

            return list;
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to query Cursor as String List! From DB [" + db + "] from Table [" + tableName + "]\n" + e + "\n" + Log.getStackTraceString(e));
            return list;
        } finally {
            db.readUnlock();
            if(cleanUpAfter)
                clean(c);
        }
    }

    public long queryGetFirstLong(String columnReturn, boolean cleanUpAfter) {
        if(!canCompile) return 0;
        canCompile = false;

        prepareReturn(columnReturn);
        db.readLock();
        Cursor c = query();
        try {
            if(c != null) {
                if (c.moveToFirst())
                    return c.getLong(0);
            }
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to query Cursor as Long! From DB [" + db + "] from Table [" + tableName + "]\n" + e + "\n" + Log.getStackTraceString(e));
        }finally {
            db.readUnlock();
            if(cleanUpAfter)
                clean(c);
        }

        return 0;
    }

    public <T extends IDBSerial> T queryGetFirstAs(Class<T> typeClass, boolean cleanUpAfter) {
        if(!canCompile) return null;
        canCompile = false;

        T item = null;
        Cursor c = null;
        try {
            // Create a new instance of T, we create it first to init a default so its never 'null' assuming the typeClass can be constructed ()
            item = typeClass.newInstance();
            db.readLock();
            c = query();
            if(c != null) {
                if (c.moveToFirst()) {
                    item.fromCursor(c);             // Read data from cursor
                    return item;
                }
            }
        }
        catch (InstantiationException ie) {
            error = ie;
            Log.e(TAG, "Your object is messed up via constructor not my fault...");
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to query Cursor! From DB [" + db + "] from Table [" + tableName + "]\n" + e + "\n" + Log.getStackTraceString(e));
        } finally {
            db.readUnlock();
            if(cleanUpAfter)
                clean(c);
        }

        return item;
    }

    public <T extends IDBSerial> Collection<T> queryAs(Class<T> typeClass) { return queryAs(typeClass, false); }
    public <T extends IDBSerial> Collection<T> queryAs(Class<T> typeClass, boolean cleanUpAfter) {
        if(!canCompile) return null;
        canCompile = false;

        db.readLock();
        Cursor c = query();
        Collection<T> items = new ArrayList<>();
        try {
            if(c != null) {
                if (c.moveToFirst()) {
                    do {
                        T item = typeClass.newInstance();   // Create a new instance of T
                        item.fromCursor(c);             // Read data from cursor
                        items.add(item);
                    } while (c.moveToNext());
                }
            }

            return items;
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to query Cursor! From DB [" + db + "] from Table [" + tableName + "]\n" + e + "\n" + Log.getStackTraceString(e));
            return items;
        } finally {
            db.readUnlock();
            if(cleanUpAfter)
                clean(c);
        }
    }

    public <T extends IDBSerial> Collection<T> queryAsData(Class<T> typeClass, boolean cleanUpAfter)  {
        if(!canCompile) return null;
        canCompile = false;

        Collection<T> items = new ArrayList<>();
        db.readLock();
        Cursor c = query();
        try {
            T reader = typeClass.newInstance();
            reader.fromCursor(c);
            return items;
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to Query from DB [" + db + "] from Table [" + tableName + "] with Selection Args [" + selectionArgsBuilder + "]\n" + e + "\n" + Log.getStackTraceString(e));
            return items;
        }finally {
            db.readUnlock();
            if(cleanUpAfter)
                clean(c);
        }
    }

    public <T extends IDBSerial> Collection<T> queryAll(Class<T> typeClass) { return queryAll(typeClass, false); }
    public <T extends IDBSerial> Collection<T> queryAll(Class<T> typeClass, boolean cleanUpAfter) {
        if(!canCompile) return null;
        canCompile = false;

        Collection<T> items = new ArrayList<>();
        Cursor c = null;
        try {
            String[] columns = onlyReturn.isEmpty() ? null : onlyReturn.toArray(new String[0]);
            db.readLock();
            c = db.getDatabase().query(
                    tableName,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    orderOrFieldName);

            while (c.moveToNext()) {
                T item = typeClass.newInstance();   // Create a new instance of T
                item.fromCursor(c);             // Read data from cursor
                items.add(item);
            }

            return items;
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to Query from DB [" + db + "] from Table [" + tableName + "] with Selection Args [" + selectionArgsBuilder + "]\n" + e + "\n" + Log.getStackTraceString(e));
            return items;
        }finally {
            db.readUnlock();
            if(cleanUpAfter)
                clean(c);
        }
    }

    public <T extends IDBSerial> Collection<T> queryAllEx(Class<T> typeClass) { return queryAll(typeClass, false); }
    public <T extends IDBSerial> Collection<T> queryAllEx(Class<T> typeClass, boolean cleanUpAfter) {
        if(!canCompile) return null;
        canCompile = false;

        Collection<T> items = new ArrayList<>();
        Cursor c = null;
        try {
            db.readLock();
            c = db.getDatabase().rawQuery("SELECT * FROM " + tableName, null);
            while (c.moveToNext()) {
                T item = typeClass.newInstance();   // Create a new instance of T
                item.fromCursor(c);             // Read data from cursor
                items.add(item);
            }

            return items;
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to Query from DB [" + db + "] from Table [" + tableName + "] with Selection Args [" + selectionArgsBuilder + "]\n" + e + "\n" + Log.getStackTraceString(e));
            return items;
        }finally {
            db.writeUnlock();
            if(cleanUpAfter)
                clean(c);
        }
    }

    public Cursor query() {
        if(DebugUtil.isDebug())
            DatabaseQueryLogger.logSnakeSnapshot(this, true, true);

        Cursor c = null;
        try {
            String[] columns = onlyReturn.isEmpty() ? null : onlyReturn.toArray(new String[0]);
            if(columns != null) {
                Log.i(TAG, "columns[0]onlyReturn=" + columns[0]);
            }

            Log.i(TAG, "query,  table=" + tableName);

            c = db.getDatabase().query(
                    tableName,
                    columns,
                    selectionArgsBuilder.toString(),
                    compareValues.toArray(new String[0]),
                    null,
                    null,
                    orderOrFieldName);
        }catch (Exception e) {
            error = e;
            Log.e(TAG, "Failed to Query from DB [" + db + "] from Table [" + tableName + "] with Selection Args [" + selectionArgsBuilder + "]\n" + e + "\n" + Log.getStackTraceString(e));
        }

        return c;
    }

    public void clean(Cursor c) {
        compareValues.clear();
        onlyReturn.clear();
        if(c != null) {
            try {
                c.close();
            }catch (Exception e) {
                Log.e(TAG, "Failed to Close Cursor ? \n" + e + "\n" + Log.getStackTraceString(e));
            }
        }
    }

    private void prepareReturn(String fieldReturn) {
        if(onlyReturn.size() > 0 && !onlyReturn.contains(fieldReturn))
            onlyReturn.clear();

        if(!onlyReturn.contains(fieldReturn))
            onlyReturn.add(fieldReturn);
    }
}
