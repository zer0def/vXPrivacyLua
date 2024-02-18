package eu.faircode.xlua.api.standard;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XSecurity;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.api.standard.interfaces.ISecurityObject;
import eu.faircode.xlua.utilities.DatabasePathUtil;
import eu.faircode.xlua.utilities.ReflectUtil;

public abstract class QueryCommandHandler implements ISecurityObject {
    protected String name;
    protected boolean requiresPermissionCheck;
    protected boolean marshall;
    protected boolean requiresSingleThread = false;

    public QueryCommandHandler() { }
    public QueryCommandHandler(String name, boolean requirePermissionCheck) {
        this.name = name;
        this.requiresPermissionCheck = requirePermissionCheck;
    }

    public abstract Cursor handle(QueryPacket commandData) throws Throwable;

    @Override
    public void throwOnPermissionCheck(Context context) { XSecurity.checkCaller(context); }

    @Override
    public boolean requiresCheck() { return requiresPermissionCheck; }

    public String getName() { return name; }
    public boolean isMarshall() { return marshall; }

    public boolean isRequiresSingleThread() { return this.requiresSingleThread; }

    public void setAsMarshallCommand() {
        name += "2";
        marshall = true;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof String))
            return false;
        String other = (String) obj;
        return this.getName().equals(other);
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append(" name=")
                .append(name)
                .append(" requires permission check=")
                .append(requiresPermissionCheck)
                .append(" requires single thread=")
                .append(requiresSingleThread)
                .append(" marshall=")
                .append(marshall).toString();
    }
}
