package eu.faircode.xlua.api.xmock.query;

import android.content.Context;
import android.database.Cursor;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.QueryCommandHandler;
import eu.faircode.xlua.api.standard.UserIdentityPacket;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetMockSettingsCommand extends QueryCommandHandler {

    @SuppressWarnings("unused")
    public GetMockSettingsCommand() { this(false); }
    public GetMockSettingsCommand(boolean marshall) {
        this.marshall = marshall;
        this.name = marshall ? "getMockSettings2" : "getMockSettings";
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        LuaSettingPacket packet = commandData.readFullPacketFrom(LuaSettingPacket.class, UserIdentityPacket.USER_QUERY_PACKET_ONE);
        if(packet == null)
            return null;

        packet.resolveUserID();
        packet.ensureCode(LuaSettingPacket.CODE_GET_MODIFIED);
        switch (packet.getCode()) {
            case LuaSettingPacket.CODE_GET_ALL:
                return CursorUtil.toMatrixCursor(LuaSettingsDatabase.getAllSettings(
                        commandData.getContext(), commandData.getDatabase(), packet), marshall, 0);
            case LuaSettingPacket.CODE_GET_MODIFIED:
                return CursorUtil.toMatrixCursor(LuaSettingsDatabase.getSettings(
                        commandData.getDatabase(), packet), marshall, 0);
        }

        return null;
    }

    public static Cursor invoke(Context context, boolean marshall, LuaSettingPacket packet) {
        return XProxyContent.luaQuery(
                context,
                marshall ? "getMockSettings2" : "getMockSettings",
                packet.generateSelectionArgsQuery(UserIdentityPacket.USER_QUERY_PACKET_ONE));
    }
}