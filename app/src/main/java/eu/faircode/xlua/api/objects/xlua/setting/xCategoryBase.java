package eu.faircode.xlua.api.objects.xlua.setting;

import androidx.annotation.NonNull;

public class xCategoryBase {
    protected Integer userId;
    protected String name;

    public xCategoryBase() { }
    public xCategoryBase(Integer userId, String name) {
        setUserId(userId);
        setName(name);
    }

    public Integer getUserId() { return this.userId; }
    public xCategoryBase setUserId(Integer userId) {
        if(userId != null) this.userId = userId;
        return this;
    }

    public String getName() { return this.name; }
    public xCategoryBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(userId != null) {
            sb.append("user=");
            sb.append(userId);
        }

        if(name != null) {
            sb.append(" name=");
            sb.append(name);
        }

        return sb.toString();
    }
}
