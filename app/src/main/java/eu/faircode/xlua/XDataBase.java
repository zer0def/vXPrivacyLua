package eu.faircode.xlua;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.DatabasePathUtil;
import eu.faircode.xlua.utilities.StringUtil;
import eu.faircode.xlua.rootbox.xFileUtils;

public class XDataBase {
    private static final String TAG = "XLua.Database.SqliteWrapper";

    private String path;
    private String name;
    private File dbFile;

    public ReentrantReadWriteLock dbLock = new ReentrantReadWriteLock(true);
    private SQLiteDatabase db = null;

    public XDataBase(String dbname, Context context) {
        this(dbname, context, true);
    }

    public XDataBase(String dbname, Context context, boolean setPerms) {
        this(dbname, context, setPerms, false);
    }

    public XDataBase(String dbname, Context context, boolean setPerms, boolean newDir) {
        if(!dbname.endsWith(".db"))
            dbname += ".db";

        name = dbname;

        Log.w(TAG, "Is New Dir Flag");
        if(!DatabasePathUtil.ensureDirectoryChange(context)) {
            Log.w(TAG, "Ensured Failed");
            path = DatabasePathUtil.getOriginalDataLocationString(context);
        }else {
            Log.w(TAG, "Ensured");
            path = DatabasePathUtil.getDatabaseDirectory(context).getAbsolutePath();
        }

        dbFile = new File(path + File.separator + name);
        xFileUtils.chown(dbFile.getAbsolutePath(), Process.SYSTEM_UID, Process.SYSTEM_UID);
        Log.i(TAG, "DB File=" + dbFile.toString());
        if(setPerms)
            setPermissions(dbFile);
    }

    public boolean open() {
        try {
            if(dbFile == null)
                return  false;

            if(db == null) {
                db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                Log.i(TAG, "Database file=" + dbFile);
            }

            return  true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to open DB: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String tableName) { return delete(tableName, null, null); }
    public boolean delete(String tableName, String selectionArgs, String[] compareValues) {
        try {
            long rows = db.delete(tableName, selectionArgs, compareValues);
            if(rows < 0) {
                Log.e(TAG, "Failed to delete I think ? Row count=" + rows);
                return false;
            }

            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to Delete Item from Table [" + tableName + "] from DB [" + db + "] ");
            return false;
        }
    }

    public boolean update(String tableName, ContentValues values, DatabaseQuerySnake queryFilter) {
        try {
            long rows = db.updateWithOnConflict(tableName, values, queryFilter.getSelectionArgs(), queryFilter.getSelectionCompareValues(), SQLiteDatabase.CONFLICT_REPLACE);
            //if(rows < 0) {
            if(rows != 1){
                Log.e(TAG, "Failed to Update Data into Table:" + tableName);
                return false;
            }

            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to Add / Update Row in Table=" + tableName + "\n" + e.getMessage());
            return false;
        }
    }

    public boolean insert(String tableName, ContentValues values)  {
        try {
            long rows = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            if (rows < 0) {
                Log.e(TAG, "Failed to Insert Data into Table:" + tableName);
                return false;
            }

            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to Add / Insert Row in Table=" + tableName + "\n" + e.getMessage());
            return false;
        }
    }

    public void writeLock() {
        dbLock.writeLock().lock();
    }

    public void writeUnlock() {
        dbLock.writeLock().unlock();
    }

    public void readLock() {
        dbLock.readLock().lock();
    }

    public void readUnlock() {
        dbLock.readLock().unlock();
    }

    public boolean beginTransaction() {
        return beginTransaction(false);
    }

    public boolean beginTransaction(boolean writeLock) {
        try {
            if(!isOpen(true))
                return false;

            if(writeLock) writeLock();

            db.beginTransaction();
            return true;
        }catch (Exception e) {
            if(writeLock) writeUnlock();
            Log.e(TAG, "Begin Transaction Error: " + e + "\n" + Log.getStackTraceString(e));
            return false;
        }
    }

    public void setTransactionSuccessful(){
        try {
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.e(TAG, "Failed to set Transcation Successful\n" + e.getMessage());
        }
    }

    public boolean endTransaction() {
        return endTransaction(false, false);
    }

    public boolean endTransaction(boolean writeUnlock, boolean wasSuccessful) {
        try {
            if(!isOpen(false))
                return false;

            if(wasSuccessful) setTransactionSuccessful();
            db.endTransaction();
            if(writeUnlock) writeUnlock();
            return true;
        }catch (Exception e) {
            if(writeUnlock) writeUnlock();
            Log.e(TAG, "Failed to End Transaction: " + e.getMessage());
            return false;
        }
    }

    public boolean close() {
        try {
            if(dbFile == null)
                return  false;

            if(db != null) {
                db.close();
                Log.i(TAG, "Database file closed=" + dbFile);
            }

            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to close DB: " + e.getMessage());
            return false;
        }
    }

    public boolean isOpen() {
        return isOpen(false);
    }

    public boolean exists() {
        try {
            return dbFile.exists();
        }catch (Exception e) {
            Log.e(TAG, "Failed to check if DB File exists assuming it does not... \n" + e.getMessage());
            return false;
        }
    }
    public boolean isOpen(boolean open_if_not) {
        if(db == null || !db.isOpen()) {
            if(open_if_not)
                return open();
            else
                return false;
        }

        return true;
    }

    public boolean createTable(Map<String, String> columns, String name) {
        if(!isOpen(true))
            return false;

        if(name == null || name == " "){
            Log.e(TAG, "[createTable] Not a valid Table Name");
            return false;
        }

        String qry = dynamicCreateQuery(columns, name);
        Log.i(TAG, "QUERY=" + qry);

        try {
            db.execSQL(qry);
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to create Table: props >> " + e.getMessage());
            return false;
        }
    }

    public boolean dropTable(String tableName) {
        if(!isOpen(true))
            return false;

        try{
            String query = "DROP TABLE IF EXISTS " + tableName;
            db.execSQL(query);
            return true;
        }catch (Exception e){
            Log.e(TAG, "Failed to drop Table: " + tableName);
            return false;
        }
    }

    public boolean tableIsEmpty(String tableName) { return tableEntries(tableName) < 1; }

    public int tableEntries(String tableName) {
        if(!isOpen(true))
            return -1;

        if(!hasTable(tableName))
            return 0;

        int count = 0;
        try {
            String query = "SELECT COUNT(*) FROM " + tableName;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst())
                count = cursor.getInt(0);

            cursor.close();
        }catch (Exception e) {
            Log.e(TAG, "Failed to get Table Item Count.. " + e.getMessage());
        }finally {
            Log.i(TAG, " table entries in " + tableName + "  size=" + count);
            return count;
        }
    }

    public boolean hasTable(String tableName) {
        if(!isOpen(true))
            return false;

        try {
            String qry = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name=?";
            Cursor cursor = db.rawQuery(qry, new String[] { tableName });
            if (!cursor.moveToFirst()) {
                //cursor.close();
                CursorUtil.closeCursor(cursor);
                return false;
            }
            int count = cursor.getInt(0);
            //cursor.close();
            CursorUtil.closeCursor(cursor);
            return count > 0;
        }catch (Exception e) {
            Log.e(TAG, "Failed to check for DB Table: " + e.getMessage());
            return false;
        }
    }

    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public  SQLiteDatabase getDatabase() {
        return db;
    }

    private void setPermissions(File dbFile) {
        if(!XposedUtil.isVirtualXposed()) {
            File dbpfile = dbFile.getParentFile();
            Log.i(TAG, "mkdirs::" + dbpfile.getPath() + " " + dbpfile.getName());

            dbpfile.mkdirs();

            this.open();

            Log.i(TAG, "Setting File Permissions (0770) SYSTEM_UID For XLUA Directory");

            // Set database file permissions
            // Owner: rwx (system)
            // Group: rwx (system)
            // World: ---
            XUtil.setPermissions(dbpfile.getAbsolutePath(), 0770, Process.SYSTEM_UID, Process.SYSTEM_UID);
            File[] files = dbpfile.listFiles();
            if (files != null)
                for (File file : files)
                    XUtil.setPermissions(file.getAbsolutePath(), 0770, Process.SYSTEM_UID, Process.SYSTEM_UID);
        }
    }

    public static void setPerms(File directoryOrFile) {
        //if(!xUnsafeApi.isSafe(directoryOrFile))
        //    return;

        Log.i(TAG, "Setting File Permissions (0770) SYSTEM_UID For XLUA Directory for UID: " + Process.SYSTEM_UID);

        //Class<?> fileUtils = Class.forName("android.os.FileUtils");
        // Set database file permissions
        // Owner: rwx (system)
        // Group: rwx (system)
        // World: ---
        //Process.myUid()
        XUtil.setPermissions(directoryOrFile.getAbsolutePath(), 0770, Process.SYSTEM_UID, Process.SYSTEM_UID);
        File[] files = directoryOrFile.listFiles();
        if (files != null)
            for (File file : files)
                XUtil.setPermissions(file.getAbsolutePath(), 0770, Process.SYSTEM_UID, Process.SYSTEM_UID);

        Log.i(TAG, "Finished setting permissions for: " + directoryOrFile.getPath());
    }

    public static boolean isReady(XDataBase database) {
        if(database == null) {
            Log.e(TAG, "[mock.db] null Database entry...");
            return false;
        }

        if (!database.exists() || !database.isOpen(true)) {
            Log.e(TAG, "[mock.db] failed to init...");
            return false;
        }

        return true;
    }

    private static String dynamicCreateQuery(Map<String, String> colms, String tableName) {
        String top = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
        StringBuilder mid = new StringBuilder();

        int i = 1;
        int sz = colms.size();
        for(Map.Entry<String, String> r : colms.entrySet()) {
            String l = r.getKey() + " " + r.getValue();
            mid.append(l);
            if(i == 1) mid.append(" PRIMARY KEY");
            if(sz != i)  mid.append(",");
            i++;
        }

        String fullQuery = top + mid + ");";
        Log.i(TAG, "query=" + fullQuery);
        return fullQuery;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " path=" + getPath() + " isOpen=" + isOpen(false);
    }
}
