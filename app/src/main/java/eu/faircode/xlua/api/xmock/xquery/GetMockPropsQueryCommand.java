package eu.faircode.xlua.api.xmock.xquery;

import android.database.Cursor;

import eu.faircode.xlua.api.objects.QueryCommandHandler;
import eu.faircode.xlua.api.objects.QueryPacket;

public class GetMockPropsQueryCommand extends QueryCommandHandler {

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        return null;
    }
}
