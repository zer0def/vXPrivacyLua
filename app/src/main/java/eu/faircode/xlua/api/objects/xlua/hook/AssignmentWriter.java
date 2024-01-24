package eu.faircode.xlua.api.objects.xlua.hook;

import android.os.Parcel;

public class AssignmentWriter extends Assignment {
    public AssignmentWriter() { }
    public AssignmentWriter(Parcel p) { super(p); }
    public AssignmentWriter(xHook hook) { super(hook); }

    public AssignmentWriter setHook(xHook hook) {
        if(hook != null) this.hook = hook;
        return this;
    }

    public AssignmentWriter setInstalled(Long installed) {
        if(installed != null) this.installed = installed;
        return this;
    }

    public AssignmentWriter setUsed(Long used) {
        if(used != null) this.used = used;
        return this;
    }

    public AssignmentWriter setRestricted(Boolean restricted) {
        if(restricted != null) this.restricted = restricted;
        return this;
    }

    public AssignmentWriter setException(String exception) {
        if(exception != null) this.exception = exception;
        return this;
    }
}
