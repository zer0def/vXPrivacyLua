package eu.faircode.xlua.api.standard.command;

import android.content.Context;

import androidx.annotation.NonNull;

import eu.faircode.xlua.XDatabase;

public class BridgePacket {
    protected String commandPrefix;
    protected String method;
    protected Context context;
    protected XDatabase db;
    protected String packageName;

    public BridgePacket() { }
    public BridgePacket(String commandPrefix, String method, Context context, XDatabase db, String packageName) {
        this.commandPrefix = commandPrefix;
        this.method = method;
        this.context = context;
        this.db = db;
        this.packageName = packageName;
        //Is VXP if package is null ?
        //Also secret key ?
    }

    public String getCommandPrefix() { return this.commandPrefix; }
    public String getMethod() { return this.method; }
    public Context getContext() { return this.context; }
    public XDatabase getDatabase() { return this.db; }
    public String getPackageName() { return this.packageName; }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(" command prefix=")
                .append(commandPrefix)
                .append(" method=")
                .append(method)
                .append(" package=")
                .append(packageName)
                .append(" db=")
                .append(db).toString();
    }
}
