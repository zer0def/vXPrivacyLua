package eu.faircode.xlua.api.xlua.query;

import android.content.Context;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;

import java.util.Map;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.standard.QueryCommandHandler;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.api.xlua.provider.XLuaAppProvider;
import eu.faircode.xlua.utilities.CursorUtil;

import eu.faircode.xlua.api.app.XLuaApp;


public class GetAppsCommand extends QueryCommandHandler {
    @SuppressWarnings("unused")
    public GetAppsCommand() { this(false); }
    public GetAppsCommand(boolean marshall) {
        name = marshall ? "getApps2" : "getApps";
        this.marshall = marshall;
        requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        Map<String, XLuaApp> apps =
                XLuaAppProvider.getApps(
                        commandData.getContext(),
                        commandData.getDatabase(),
                        XUtil.getUserId(Binder.getCallingUid()),
                        true,
                        true);
        return CursorUtil.toMatrixCursor(
                apps.values(),
                marshall,
                0);
    }

    public static Cursor invoke(Context context, boolean marshall) {
        return XProxyContent.luaQuery(
                context,
                marshall ? "getApps2" : "getApps");
    }
}
