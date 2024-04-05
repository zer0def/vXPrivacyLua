package eu.faircode.xlua.api.xmock.query;

import android.content.Context;
import android.database.Cursor;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.standard.QueryCommandHandler;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;
import eu.faircode.xlua.api.useragent.MockUserAgentDatabaseManager;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetMockAgentsCommand extends QueryCommandHandler {

    @SuppressWarnings("unused")
    public GetMockAgentsCommand() { this(false); }

    @SuppressWarnings("unused")
    public GetMockAgentsCommand(boolean marshall) {
        this.marshall = marshall;
        this.name = marshall ? "getMockAgents2" : "getMockAgents";
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        String[] selection = commandData.getSelection();
        if(selection != null && selection.length > 0) {
            String dev = selection[0];
            if(dev == null || dev.isEmpty() || !MockUserAgentDatabaseManager.DEVICES.contains(dev)) return null;
            return CursorUtil.toMatrixCursor(
                    MockUserAgentDatabaseManager.getUserAgentGroup(
                            commandData.getContext(),
                            commandData.getDatabase(), dev), marshall, 0);
        } return null;
    }

    public static Cursor invoke(Context context, String device, boolean marshall) {
        if(!MockUserAgentDatabaseManager.DEVICES.contains(device)) {
            XLog.e("Device being passed to the User Agent Database dosnt exist... " + device);
            return null;
        }

        return XProxyContent.mockQuery(
                context,
                marshall ? "getMockAgents2" : "getMockAgents",
                SqlQuerySnake
                        .create()
                        .whereColumn("device", device));
    }
}
