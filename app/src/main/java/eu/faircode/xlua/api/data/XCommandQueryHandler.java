package eu.faircode.xlua.api.data;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import eu.faircode.xlua.XSecurity;

/*public abstract class XCommandQueryHandler {
    public String name;
    public String parent;
    public boolean requiresPermissionCheck;

    public XCommandQueryHandler() { }
    public XCommandQueryHandler(String name, boolean requirePermissionCheck) {
        this.name = name;
        this.requiresPermissionCheck = requirePermissionCheck;
    }

    public abstract Cursor handle(XQueryData commandData) throws Throwable;

    public void throwOnPermissionCheck(Context context) { if(!requiresPermissionCheck) XSecurity.checkCaller(context); }
    public String getName() { return name; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof String))
            return false;
        String other = (String) obj;
        return this.getName().equals(other);
    }
}*/
