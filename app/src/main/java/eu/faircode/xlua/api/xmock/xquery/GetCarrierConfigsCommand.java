package eu.faircode.xlua.api.xmock.xquery;

import android.content.Context;
import android.database.Cursor;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.xmock.XMockPhoneDatabase;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetCarrierConfigsCommand extends QueryCommandHandler {
    public static GetCarrierConfigsCommand create(boolean marshall) { return new GetCarrierConfigsCommand(marshall); };

    private boolean marshall;
    public GetCarrierConfigsCommand(boolean marshall) {
        this.name = marshall ? "getCarrierConfigs2" : "getCarrierConfigs";
        this.marshall = marshall;
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        return CursorUtil.toMatrixCursor(
                XMockPhoneDatabase.getMockCarriers(
                        commandData.getContext(),
                        commandData.getDatabase()),
                marshall, 0);
    }

    public static Cursor invoke(Context context, boolean marshall) {
        return XProxyContent.mockQuery(
                context,
                marshall ? "getCarrierConfigs2" : "getCarrierConfigs");
    }
}
