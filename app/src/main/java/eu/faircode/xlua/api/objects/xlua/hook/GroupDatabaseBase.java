package eu.faircode.xlua.api.objects.xlua.hook;

import androidx.annotation.NonNull;

public class GroupDatabaseBase {
    protected String packageName;
    protected Integer uid;
    protected String name;
    protected Long used;

    public GroupDatabaseBase() { }
    public GroupDatabaseBase(String packageName, Integer userId, String name, Long used) {
        setPackageName(packageName);
        setUid(userId);
        setName(name);
        setUsed(used);
    }

    public String getPackageName() { return this.packageName; }
    public GroupDatabaseBase setPackageName(String packageName) {
        if(packageName != null) this.packageName = packageName;
        return this;
    }

    public Integer getUid() { return this.uid; }
    public GroupDatabaseBase setUid(Integer uid) {
        if(uid != null) this.uid = uid;
        return this;
    }

    public String getName() { return this.name; }
    public GroupDatabaseBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public Long getUsed() { return this.used; }
    public GroupDatabaseBase setUsed(Long used) {
        if(used != null) this.used = used;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "pkg=" + packageName + " uid=" + uid + " name=" + name + " used=" + used;
    }
}
