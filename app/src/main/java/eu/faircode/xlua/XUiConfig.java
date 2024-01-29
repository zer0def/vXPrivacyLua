package eu.faircode.xlua;

public class XUiConfig {
    public String name;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XUiConfig))
            return false;
        XUiConfig other = (XUiConfig) obj;
        return this.name.equals(other.name);
    }
}
