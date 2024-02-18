package eu.faircode.xlua.api.app;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.hook.assignment.XLuaAssignmentBase;
import eu.faircode.xlua.api.standard.interfaces.IListener;

public class XLuaAppBase {
    protected String packageName;
    protected Integer uid;
    protected Integer icon;
    protected String label;
    protected Boolean enabled;
    protected Boolean persistent;
    protected Boolean system;
    protected Boolean forceStop = true;
    protected List<LuaAssignment> assignments;
    //secret key implement here

    private IListener listener = null;

    public XLuaAppBase() { }

    public String getPackageName() { return this.packageName; }
    public XLuaAppBase setPackageName(String packageName) {
        if(packageName != null) this.packageName = packageName;
        return this;
    }

    public Integer getUid() { return this.uid; }
    public XLuaAppBase setUid(Integer uid) {
        if(uid != null) this.uid = uid;
        return this;
    }

    public Integer getIcon() { return this.icon; }
    public XLuaAppBase setIcon(Integer icon) {
        if(icon != null) this.icon = icon;
        return this;
    }

    public String getLabel() { return this.label; }
    public XLuaAppBase setLabel(String label) {
        if(label != null) this.label = label;
        return this;
    }

    public Boolean isEnabled() { return this.enabled; }
    public XLuaAppBase setEnabled(Boolean enabled) {
        if(enabled != null) this.enabled = enabled;
        return this;
    }

    public Boolean isPersistent() { return this.persistent; }
    public XLuaAppBase setPersistent(Boolean persistent) {
        if(persistent != null) this.persistent = persistent;
        return this;
    }

    public Boolean isSystem() { return this.system; }
    public XLuaAppBase setSystem(Boolean system) {
        if(system != null) this.system = system;
        return this;
    }

    public Boolean getForceStop() { return this.forceStop; }
    public XLuaAppBase setForceStop(Boolean forceStop) {
        if(forceStop != null) this.forceStop = forceStop;
        return this;
    }

    public Collection<LuaAssignment> getAssignments() { return this.assignments; }
    public Collection<LuaAssignment> getAssignments(String group) {
        if (group == null)
            return assignments;

        Collection<LuaAssignment> filtered = new ArrayList<>();
        for (LuaAssignment assignment : assignments)
            if (group.equals(assignment.getHook().getGroup()))
                filtered.add(assignment);

        return filtered;
    }

    public LuaAssignment getAssignmentAt(int index) {
        if(this.assignments.size() <= index)
            return null;

        return this.assignments.get(index);
    }

    public int assignmentIndex(LuaAssignment assignment) {
        if(assignment != null)
            return this.assignments.indexOf(assignment);

        return -1;
    }

    public boolean hasAssignment(XLuaAssignmentBase assignment) {
        return this.assignments.contains(assignment);
    }

    public XLuaAppBase removeAssignment(LuaAssignment assignment) {
        if(assignment != null) this.assignments.remove(assignment);
        return this;
    }

    public XLuaAppBase addAssignment(LuaAssignment assignment) {
        if(assignment != null) this.assignments.add(assignment);
        return this;
    }

    public XLuaAppBase setAssignments(Collection<LuaAssignment> assignments) {
        if(assignments != null) this.assignments = new ArrayList<>(assignments);
        return this;
    }

    public void setListener(IListener listener) {
        this.listener = listener;
    }
    public void notifyAssign(Context context, String groupName, boolean assign) {
        if (this.listener != null)
            this.listener.onAssign(context, groupName, assign);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XLuaAppBase))
            return false;
        XLuaAppBase other = (XLuaAppBase) obj;
        return (this.packageName.equals(other.packageName) && this.uid == other.uid);
    }

    @Override
    public int hashCode() {
        return this.packageName.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "pkg=" + packageName + " uid=" + uid;
    }
}
