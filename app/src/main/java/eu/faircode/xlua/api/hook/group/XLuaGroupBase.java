package eu.faircode.xlua.api.hook.group;

import androidx.annotation.NonNull;

public class XLuaGroupBase {
    protected String packageName;
    protected Integer uid;
    protected String name;
    protected Long used;

    public XLuaGroupBase() { }
    public XLuaGroupBase(String packageName, Integer userId, String name, Long used) {
        setPackageName(packageName);
        setUid(userId);
        setName(name);
        setUsed(used);
    }

    public String getPackageName() { return this.packageName; }
    public XLuaGroupBase setPackageName(String packageName) {
        if(packageName != null) this.packageName = packageName;
        return this;
    }

    public Integer getUid() { return this.uid; }
    public XLuaGroupBase setUid(Integer uid) {
        if(uid != null) this.uid = uid;
        return this;
    }

    public String getName() { return this.name; }
    public XLuaGroupBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public Long getUsed() { return this.used; }
    public XLuaGroupBase setUsed(Long used) {
        if(used != null) this.used = used;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "pkg=" + packageName + " uid=" + uid + " name=" + name + " used=" + used;
    }
}
