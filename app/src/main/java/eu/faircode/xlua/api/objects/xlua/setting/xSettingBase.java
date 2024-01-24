package eu.faircode.xlua.api.objects.xlua.setting;

import androidx.annotation.NonNull;

public class xSettingBase {
    protected Integer user;
    protected String category;
    protected String name;
    protected String value;

    public xSettingBase() { }
    public xSettingBase(Integer user, String category, String name) { init(user, category, name, null); }
    public xSettingBase(Integer user, String category, String name, String value) { init(user, category, name, value); }

    private void init(Integer user, String category, String name, String value) {
        setUser(user);
        setCategory(category);
        setName(name);
        setValue(value);
    }

    public Integer getUser() { return this.user; }
    public String getCategory() { return this.category; }
    public String getName() { return this.name; }
    public String getValue() { return this.value; }

    public xSettingBase setUser(Integer user) {
        if(user != null) this.user = user;
        return this;
    }

    public xSettingBase setCategory(String category) {
        if(category != null) this.category = category;
        return this;
    }

    public xSettingBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public xSettingBase setValue(String value) {
        if(value != null) this.value = value;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(user != null) {
            sb.append("user=");
            sb.append(user);
        }

        if(category != null) {
            sb.append(" category=");
            sb.append(category);
        }

        if(name != null) {
            sb.append(" name=");
            sb.append(name);
        }

        if(value != null) {
            sb.append(" value=");
            sb.append(value);
        }

        return sb.toString();
    }
}
