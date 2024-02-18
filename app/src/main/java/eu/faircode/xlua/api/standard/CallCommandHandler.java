package eu.faircode.xlua.api.standard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XSecurity;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.standard.interfaces.ISecurityObject;

public abstract class CallCommandHandler implements ISecurityObject {
    protected String name;
    protected boolean requiresPermissionCheck;
    protected boolean requiresSingleThread = false;

    public CallCommandHandler() { }
    public CallCommandHandler(String name, boolean requirePermissionCheck) {
        this.name = name;
        this.requiresPermissionCheck = requirePermissionCheck;
    }

    public abstract Bundle handle(CallPacket rawData) throws Throwable;

    @Override
    public void throwOnPermissionCheck(Context context) { XSecurity.checkCaller(context); }

    @Override
    public boolean requiresCheck() { return requiresPermissionCheck; }

    public boolean isRequiresSingleThread() { return this.requiresSingleThread; }

    public String getName() { return name; }

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
                .append(requiresSingleThread).toString();
    }
}
