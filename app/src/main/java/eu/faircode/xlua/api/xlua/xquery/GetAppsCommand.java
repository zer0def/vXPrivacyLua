package eu.faircode.xlua.api.xlua.xquery;

import android.content.Context;
import android.database.Cursor;
import android.os.Binder;

import java.util.Map;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.xlua.XAppProvider;
import eu.faircode.xlua.utilities.CursorUtil;

import eu.faircode.xlua.api.objects.xlua.app.xApp;
import eu.faircode.xlua.api.objects.xlua.app.xAppConversions;


public class GetAppsCommand extends QueryCommandHandler {
    public static GetAppsCommand create(boolean marshall) { return new GetAppsCommand(marshall); };

    private boolean marshall;
    public GetAppsCommand(boolean marshall) {
        name = marshall ? "getApps" : "getApps2";
        this.marshall = marshall;
        requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        Map<String, xApp> apps =
                XAppProvider.getApps(
                        commandData.getContext(),
                        XUtil.getUserId(Binder.getCallingUid()),
                        commandData.getDatabase(),
                        true,
                        true);
        return CursorUtil.toMatrixCursor(
                apps.values(),
                marshall,
                0);
    }

    public static Cursor invoke(Context context) { return XProxyContent.luaQuery(context, "getApps"); }
    public static Cursor invokeEx(Context context) { return XProxyContent.luaQuery(context, "getApps2"); }
}
