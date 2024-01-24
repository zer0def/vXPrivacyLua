package eu.faircode.xlua.api.objects;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.Callable;

import eu.faircode.xlua.XSecurity;

public abstract class CallCommandHandler {
    public String name;
    public String parent;
    public boolean requiresPermissionCheck;

    public CallCommandHandler() { }
    public CallCommandHandler(String name, boolean requirePermissionCheck) {
        Log.i("XLua.Command Constructor", " name=" + name);
        this.name = name;
        this.requiresPermissionCheck = requirePermissionCheck;
    }

    public abstract Bundle handle(CallPacket rawData) throws Throwable;

    public void throwOnPermissionCheck(Context context) {
        Log.i("XLua.Command Handler", " command=" + this.name);
        if(!requiresPermissionCheck) XSecurity.checkCaller(context);
    }
    public String getName() { return name; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof String))
            return false;
        String other = (String) obj;
        return this.getName().equals(other);
    }
}
