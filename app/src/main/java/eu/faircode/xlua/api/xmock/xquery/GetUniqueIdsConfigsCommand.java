package eu.faircode.xlua.api.xmock.xquery;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import eu.faircode.xlua.api.XMockCallApi;
import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;
import eu.faircode.xlua.api.xmock.XMockPhoneDatabase;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetUniqueIdsConfigsCommand extends QueryCommandHandler {
    public static GetUniqueIdsConfigsCommand create(boolean marshall) { return new GetUniqueIdsConfigsCommand(marshall); };

    private boolean marshall;
    public GetUniqueIdsConfigsCommand(boolean marshall) {
        this.name = marshall ? "getUniqueIds2" : "getUniqueIds";
        this.marshall = marshall;
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        return CursorUtil.toMatrixCursor(
                XMockPhoneDatabase.getMockUniqueIDs(
                        commandData.getContext(),
                        commandData.getDatabase()),
                marshall, 0);
    }

    public static Cursor invoke(Context context, boolean marshall) {
        return XProxyContent.mockQuery(
                context,
                marshall ? "getUniqueIds2" : "getUniqueIds");
    }
}
