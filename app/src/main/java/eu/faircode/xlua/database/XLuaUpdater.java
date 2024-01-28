package eu.faircode.xlua.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;

public class XLuaUpdater {
    private static final String  TAG = "XLua.XLuaUpdater";

    public static void checkForUpdate(XDataBase db) throws Throwable {
        // Build database file
       /* File dbFile;
        if (XposedUtil.isVirtualXposed())
            dbFile = new File(context.getFilesDir(), "xlua.db");
        else {
            dbFile = new File(
                    Environment.getDataDirectory() + File.separator +
                            "system" + File.separator +
                            "xlua" + File.separator +
                            "xlua.db");
            dbFile.getParentFile().mkdirs();
        }

        // Open database
        SQLiteDatabase _db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        Log.i(TAG, "Database file=" + dbFile);

        if (!XposedUtil.isVirtualXposed()) {
            // Set database file permissions
            // Owner: rwx (system)
            // Group: rwx (system)
            // World: ---
            XUtil.setPermissions(dbFile.getParentFile().getAbsolutePath(), 0770, Process.SYSTEM_UID, Process.SYSTEM_UID);
            File[] files = dbFile.getParentFile().listFiles();
            if (files != null)
                for (File file : files)
                    XUtil.setPermissions(file.getAbsolutePath(), 0770, Process.SYSTEM_UID, Process.SYSTEM_UID);
        }

        dbLock.writeLock().lock();*/
        SQLiteDatabase _db = db.getDatabase();
        //db.writeLock();

        //int v = _db.getVersion();
        //Log.i(TAG, "DB VERSION=" + v);

        try {
            // Upgrade database if needed
            if (_db.needUpgrade(1)) {
                Log.i(TAG, "Database upgrade version 1");
                _db.beginTransaction();
                try {
                    _db.execSQL("CREATE TABLE assignment (package TEXT NOT NULL, uid INTEGER NOT NULL, hook TEXT NOT NULL, installed INTEGER, used INTEGER, restricted INTEGER, exception TEXT)");
                    _db.execSQL("CREATE UNIQUE INDEX idx_assignment ON assignment(package, uid, hook)");

                    _db.execSQL("CREATE TABLE setting (user INTEGER, category TEXT NOT NULL, name TEXT NOT NULL, value TEXT)");
                    _db.execSQL("CREATE UNIQUE INDEX idx_setting ON setting(user, category, name)");

                    _db.setVersion(1);
                    _db.setTransactionSuccessful();
                } finally {
                    _db.endTransaction();
                }
            }

            if (_db.needUpgrade(2)) {
                Log.i(TAG, "Database upgrade version 2");
                _db.beginTransaction();
                try {
                    _db.execSQL("CREATE TABLE hook (id TEXT NOT NULL, definition TEXT NOT NULL)");
                    _db.execSQL("CREATE UNIQUE INDEX idx_hook ON hook(id, definition)");

                    _db.setVersion(2);
                    _db.setTransactionSuccessful();
                } finally {
                    _db.endTransaction();
                }
            }

            if (_db.needUpgrade(3)) {
                Log.i(TAG, "Database upgrade version 3");
                _db.beginTransaction();
                try {
                    _db.execSQL("ALTER TABLE assignment ADD COLUMN old TEXT");
                    _db.execSQL("ALTER TABLE assignment ADD COLUMN new TEXT");
                    _db.execSQL("CREATE INDEX idx_assignment_used ON assignment(used)");

                    _db.setVersion(3);
                    _db.setTransactionSuccessful();
                } finally {
                    _db.endTransaction();
                }
            }

            if (_db.needUpgrade(4)) {
                Log.i(TAG, "Database upgrade version 4");
                _db.beginTransaction();
                try {
                    Map<String, xHook> tmp = new HashMap<>();
                    Cursor cursor = null;
                    try {
                        cursor = _db.query("hook", null,
                                null, null,
                                null, null, null);
                        int colDefinition = cursor.getColumnIndex("definition");
                        while (cursor.moveToNext()) {
                            String definition = cursor.getString(colDefinition);
                            xHook hook = new xHook();
                            hook.fromJSONObject(new JSONObject(definition));
                            tmp.put(hook.getId(), hook);
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    Log.i(TAG, "Converting definitions=" + tmp.size());

                    _db.execSQL("DROP INDEX idx_hook");
                    _db.execSQL("DELETE FROM hook");
                    _db.execSQL("CREATE UNIQUE INDEX idx_hook ON hook(id)");

                    for (String id : tmp.keySet()) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", id);
                        cv.put("definition", tmp.get(id).toJSON());
                        long rows = _db.insertWithOnConflict("hook", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                        if (rows < 0)
                            throw new Throwable("Error inserting hook");
                    }


                    _db.setVersion(4);
                    _db.setTransactionSuccessful();
                } finally {
                    _db.endTransaction();
                }
            }

            if (_db.needUpgrade(5)) {
                Log.i(TAG, "Database upgrade version 5");
                _db.beginTransaction();
                try {
                    _db.execSQL("CREATE TABLE `group` (package TEXT NOT NULL, uid INTEGER NOT NULL, name TEXT NOT NULL, used INTEGER)");
                    _db.execSQL("CREATE UNIQUE INDEX idx_group ON `group`(package, uid, name)");

                    _db.setVersion(5);
                    _db.setTransactionSuccessful();
                } finally {
                    _db.endTransaction();
                }
            }

            Log.i(TAG, "Renaming Hooks");

            //deleteHook(_db, "Privacy.ContentResolver/query1");
            //deleteHook(_db, "Privacy.ContentResolver/query16");
            //deleteHook(_db, "Privacy.ContentResolver/query26");
            renameHook(_db, "TelephonyManager/getDeviceId", "TelephonyManager.getDeviceId");
            renameHook(_db, "TelephonyManager/getDeviceId/slot", "TelephonyManager.getDeviceId/slot");
            renameHook(_db, "TelephonyManager/getGroupIdLevel1", "TelephonyManager.getGroupIdLevel1");
            renameHook(_db, "TelephonyManager/getImei", "TelephonyManager.getImei");
            renameHook(_db, "TelephonyManager/getImei/slot", "TelephonyManager.getImei/slot");
            renameHook(_db, "TelephonyManager/getLine1Number", "TelephonyManager.getLine1Number");
            renameHook(_db, "TelephonyManager/getMeid", "TelephonyManager.getMeid");
            renameHook(_db, "TelephonyManager/getMeid/slot", "TelephonyManager.getMeid/slot");
            renameHook(_db, "TelephonyManager/getNetworkSpecifier", "TelephonyManager.getNetworkSpecifier");
            renameHook(_db, "TelephonyManager/getSimSerialNumber", "TelephonyManager.getSimSerialNumber");
            renameHook(_db, "TelephonyManager/getSubscriberId", "TelephonyManager.getSubscriberId");
            renameHook(_db, "TelephonyManager/getVoiceMailAlphaTag", "TelephonyManager.getVoiceMailAlphaTag");
            renameHook(_db, "TelephonyManager/getVoiceMailNumber", "TelephonyManager.getVoiceMailNumber");
            renameHook(_db, "Settings.Secure.getString", "Settings.Secure.getString/android_id");
            renameHook(_db, "SystemProperties.get", "SystemProperties.get/serial");
            renameHook(_db, "SystemProperties.get/default", "SystemProperties.get.default/serial");

            Log.i(TAG, "Database version=" + _db.getVersion());

            // Reset usage data
            ContentValues cv = new ContentValues();
            cv.put("installed", -1);
            cv.putNull("exception");
            long rows = _db.update("assignment", cv, null, null);
            Log.i(TAG, "Reset assigned hook data count=" + rows);

            //return _db;
        } catch (Throwable ex) {
            Log.i(TAG, "DB EXCEPTION FUCK=" + ex + "\n" + Log.getStackTraceString(ex));
            //_db.close();
            throw ex;
        } finally {
            //dbLock.writeLock().unlock();

            //db.writeUnlock(); first lock is the bug
        }
    }

    private static void renameHook(SQLiteDatabase _db, String oldId, String newId) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("hook", newId);
            long rows = _db.update("assignment", cv, "hook = ?", new String[]{oldId});
            Log.i(TAG, "Renamed hook " + oldId + " into " + newId + " rows=" + rows);
        } catch (Throwable ex) {
            Log.i(TAG, "Renamed hook " + oldId + " into " + newId + " ex=" + ex.getMessage());
        }
    }
}
