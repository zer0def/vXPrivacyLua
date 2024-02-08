package eu.faircode.xlua.api.xmock.xquery;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.xmock.XMockPhoneDatabase;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetMockConfigsCommand extends QueryCommandHandler {
    public static GetMockConfigsCommand create(boolean marshall) { return new GetMockConfigsCommand(marshall); };

    private boolean marshall;
    public GetMockConfigsCommand(boolean marshall) {
        this.name = marshall ? "getMockConfigs2" : "getMockConfigs";
        this.marshall = marshall;
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        Log.i("XLua.getMockConfigsCommand", "Getting mock Configs");
        throwOnPermissionCheck(commandData.getContext());
        return CursorUtil.toMatrixCursor(
                XMockPhoneDatabase.getMockConfigs(
                        commandData.getContext(),
                        commandData.getDatabase()),
                marshall, 0);
    }

    public static Cursor invoke(Context context, boolean marshall) {
        return XProxyContent.mockQuery(
                context,
                marshall ? "getMockConfigs2" : "getMockConfigs");
    }
}
