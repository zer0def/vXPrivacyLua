package eu.faircode.xlua.api.objects.xlua.hook;

//import eu.faircode.xlua.XAssignment;

public class AssignmentBase {
    protected xHook hook;
    protected long installed = -1;
    protected long used = -1;
    protected boolean restricted = false;
    protected String exception;

    public AssignmentBase() { }
    public AssignmentBase(xHook hook) {
        this.hook = hook;
    }

    public xHook getHook() { return this.hook; }
    public long getInstalled() { return this.installed; }
    public long getUsed() { return this.used; }
    public boolean getRestricted() { return this.restricted; }
    public String getException() { return this.exception; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AssignmentBase))
            return false;
        AssignmentBase other = (AssignmentBase) obj;
        return this.hook.getId().equals(other.hook.getId());
    }

    @Override
    public int hashCode() {
        return this.hook.getId().hashCode();
    }
}
