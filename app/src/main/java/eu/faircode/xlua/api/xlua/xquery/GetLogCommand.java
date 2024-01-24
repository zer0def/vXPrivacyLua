package eu.faircode.xlua.api.xlua.xquery;

import android.database.Cursor;
import android.os.Binder;
import android.os.Process;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.objects.xlua.hook.Assignment;
import eu.faircode.xlua.database.DatabaseQuerySnake;

public class GetLogCommand extends QueryCommandHandler {
    public static GetLogCommand create() { return new GetLogCommand(); };

    public GetLogCommand() {
        name = "getLog";
        requiresPermissionCheck = true;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());

        XDataBase db = commandData.getDatabase();
        if (commandData.getSelection() != null)
            throw new IllegalArgumentException("selection invalid");

        int userid = XUtil.getUserId(Binder.getCallingUid());
        int start = XUtil.getUserUid(userid, 0);
        int end = XUtil.getUserUid(userid, Process.LAST_APPLICATION_UID);

        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create(db, Assignment.Table.name)
                .onlyReturnColumns("package", "uid", "hook", "used", "old", "new")
                .whereColumn("restricted", "1")
                .whereColumn("uid", start, ">=")
                .whereColumn("uid", end, "<=")
                .orderBy("used DESC");

        db.readLock();
        Cursor c = snake.query();
        snake.clean(null);
        db.readUnlock();
        return c;
    }
}
