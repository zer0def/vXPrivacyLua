package eu.faircode.xlua.api.hook.assignment;

import android.os.Parcel;

import eu.faircode.xlua.api.hook.XLuaHook;

public class LuaAssignmentWriter extends LuaAssignment {
    public LuaAssignmentWriter() { }
    public LuaAssignmentWriter(Parcel p) { super(p); }
    public LuaAssignmentWriter(XLuaHook hook) { super(hook); }

    public LuaAssignmentWriter setHook(XLuaHook hook) {
        if(hook != null) this.hook = hook;
        return this;
    }

    public LuaAssignmentWriter setInstalled(Long installed) {
        if(installed != null) this.installed = installed;
        return this;
    }

    public LuaAssignmentWriter setUsed(Long used) {
        if(used != null) this.used = used;
        return this;
    }

    public LuaAssignmentWriter setRestricted(Boolean restricted) {
        if(restricted != null) this.restricted = restricted;
        return this;
    }

    public LuaAssignmentWriter setException(String exception) {
        if(exception != null) this.exception = exception;
        return this;
    }
}
