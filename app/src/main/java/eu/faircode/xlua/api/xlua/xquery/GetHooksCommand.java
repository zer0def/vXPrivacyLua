package eu.faircode.xlua.api.xlua.xquery;

import android.content.Context;
import android.database.Cursor;

import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetHooksCommand extends QueryCommandHandler {
    public static GetHooksCommand create(boolean marshall) { return new GetHooksCommand(marshall); };

    private boolean marshall;
    public GetHooksCommand(boolean marshall) {
        name = marshall ? "getHooks" : "getHooks2";
        this.marshall = marshall;
        requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        String[] selection = commandData.getSelection();
        boolean all = (selection != null && selection.length == 1 && "all".equals(selection[0]));
        return CursorUtil.toMatrixCursor(
                XGlobalCore.getHooks(commandData.getDatabase(), all),
                marshall,
                xHook.FLAG_WITH_LUA);
    }

    public static Cursor invoke(Context context) { return XProxyContent.luaQuery(context, "getHooks"); }
    public static Cursor invokeEx(Context context) { return XProxyContent.luaQuery(context, "getHooks2"); }
}
