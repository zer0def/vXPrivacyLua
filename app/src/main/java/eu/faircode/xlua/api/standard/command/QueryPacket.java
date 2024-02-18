package eu.faircode.xlua.api.standard.command;

import android.content.Context;
import android.util.Log;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.standard.interfaces.IBridgePacketContext;
import eu.faircode.xlua.api.standard.interfaces.IUserPacket;

public class QueryPacket extends BridgePacket implements IBridgePacketContext {
    private static final String TAG = "XLua.QueryPacket";

    protected final String[] selection;
    public QueryPacket(Context context, String commandPrefix, String method, String[] selection, XDatabase db) { this(context, commandPrefix, method, selection, db, null); }
    public QueryPacket(Context context, String commandPrefix, String method, String[] selection, XDatabase db, String packageName) {
        super(commandPrefix, method, context, db, packageName);
        this.selection = selection;
    }

    public String[] getSelection() { return this.selection; }
    public <T extends IUserPacket> T readFullPacketFrom(Class<T> clazz, int flags) {
        try {
            T itm = clazz.newInstance();
            itm.readSelectionArgsFromQuery(selection, flags);
            return itm;
        }catch (Exception e) {
            Log.e(TAG, "Failed to read Selections args! flag=" + flags + " class type=" + clazz.getName() + " " + this + "\ne=" + e);
            return null;
        }
    }
}
