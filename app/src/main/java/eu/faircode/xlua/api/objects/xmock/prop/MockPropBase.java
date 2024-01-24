package eu.faircode.xlua.api.objects.xmock.prop;


public abstract class MockPropBase {
    protected String name;
    protected String value;
    protected String defaultValue;
    protected Boolean enabled;

    public MockPropBase() { }
    public MockPropBase(String name, String value) { init(name, value, null, null); }
    public MockPropBase(String name, Boolean enabled) { init(name, null, null, enabled); }
    public MockPropBase(String name, String value, Boolean enabled) { init(name, value, null, enabled); }
    public MockPropBase(String name, String value, String defaultValue, Boolean enabled) { init(name, value, defaultValue, enabled); }

    private void init(String name, String value, String defaultValue, Boolean enabled) {
        setName(name);
        setValue(value);
        setDefaultValue(defaultValue);
        setEnabled(enabled);
    }

    public MockPropBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public MockPropBase setValue(String value) {
        if(value != null) this.value = value;
        return this;
    }

    public MockPropBase setDefaultValue(String defaultValue) {
        if(defaultValue != null) this.defaultValue = defaultValue;
        return this;
    }

    public MockPropBase setEnabled(Boolean enabled) {
        if(enabled != null) this.enabled = enabled;
        return this;
    }

    public String getName() {
        return this.name;
    }
    public String getValue() {
        return this.value;
    }
    public String getDefaultValue() {
        return this.defaultValue;
    }
    public Boolean isEnabled() { return this.enabled; }

    @Override
    public int hashCode() { return this.getName().hashCode(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(name != null) {
            sb.append("name=");
            sb.append(name);
        }

        if(value != null) {
            sb.append(" value=");
            sb.append(value);
        }

        if(defaultValue != null) {
            sb.append(" defValue=");
            sb.append(defaultValue);
        }

        if(enabled != null) {
            sb.append(" enabled=");
            sb.append(enabled);
        }

        return sb.toString();
        //return this.getName() + "::" + this.value + "::" + this.defaultValue + "::" + this.enabled;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MockPropBase))
            return false;
        MockPropBase other = (MockPropBase) obj;
        return this.getName().equals(other.getName());
    }
}