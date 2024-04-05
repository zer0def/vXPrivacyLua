package eu.faircode.xlua.api.xmock.query;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.properties.MockPropProvider;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.standard.QueryCommandHandler;
import eu.faircode.xlua.api.standard.UserIdentityPacket;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetMockPropMapsCommand extends QueryCommandHandler {
    @SuppressWarnings("unused")
    public GetMockPropMapsCommand() { this(false); }
    public GetMockPropMapsCommand(boolean marshall) {
        this.marshall = marshall;
        this.name = marshall ? "getMockPropMaps2" : "getMockPropMaps";
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        return CursorUtil.toMatrixCursor(
                MockPropProvider.getMockPropMaps(
                        commandData.getContext(),
                        commandData.getDatabase()), marshall, 0);
    }

    public static Cursor invoke(Context context, boolean marshall) {
        return XProxyContent.mockQuery(
                context,
                marshall ? "getMockPropMaps2" : "getMockPropMaps");
    }
}
