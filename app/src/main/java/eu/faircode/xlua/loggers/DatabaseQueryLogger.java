package eu.faircode.xlua.loggers;

import android.util.Log;

import java.util.List;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.database.DatabaseQuerySnake;

public class DatabaseQueryLogger {
    public static void logSnakeSnapshot(DatabaseQuerySnake snake, boolean logStack, boolean logTableEntries) {
        String TAG = "XLua.DatabaseQuerySnake.Log";
        StringBuilder log = new StringBuilder();

        List<String> onlyReturn = snake.getOnlyReturn();
        log.append("[1] onlyReturn=");
        LogHelper.writeStringList(log, onlyReturn);
        log.append("\n");

        log.append("[2] selectFields=");
        log.append(snake.getSelectionArgs());
        log.append("\n");

        String[] selectValues = snake.getSelectionCompareValues();
        log.append("[3] selectValues=");
        LogHelper.writeStringArray(log, selectValues);
        log.append("\n");

        log.append("[4] orderField=");
        log.append(snake.getOrderBy());
        log.append("\n");

        XDataBase db = snake.getDatabase();
        log.append("[5] db=");
        log.append(db);
        if(db != null){
            log.append("DB Version=");
            log.append(db.getDatabase().getVersion());
        }
        log.append("\n");

        log.append("[6] Table=");
        log.append(snake.getTableName());
        if(db != null && logTableEntries) {
            log.append("  Table Entries Count=");
            log.append(db.tableEntries(snake.getTableName()));
        }

        log.append("\n");

        //Binder.getCallingUid();
        if(logStack) {
            log.append("[7] Stack=\n");
            for(StackTraceElement e : Thread.currentThread().getStackTrace()) {
                log.append(e.getClassName());
                log.append("::");
                log.append(e.getMethodName());
                log.append("\n");
            }
        }

        Log.i(TAG, log.toString());
    }
}
