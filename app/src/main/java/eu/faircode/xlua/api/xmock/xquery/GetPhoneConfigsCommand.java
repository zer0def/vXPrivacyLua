package eu.faircode.xlua.api.xmock.xquery;

import android.content.Context;
import android.database.Cursor;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.xmock.XMockPhoneDatabase;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetPhoneConfigsCommand extends QueryCommandHandler {
    public static GetPhoneConfigsCommand create(boolean marshall) { return new GetPhoneConfigsCommand(marshall); };

    private boolean marshall;
    public GetPhoneConfigsCommand(boolean marshall) {
        this.name = marshall ? "getPhoneConfigs2" : "getPhoneConfigs";
        this.marshall = marshall;
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        return CursorUtil.toMatrixCursor(
                XMockPhoneDatabase.getMockPhones(
                        commandData.getContext(),
                        commandData.getDatabase()),
                marshall, 0);
    }

    public static Cursor invoke(Context context, boolean marshall) {
        return XProxyContent.mockQuery(
                context,
                marshall ? "getPhoneConfigs2" : "getPhoneConfigs");
    }
}
