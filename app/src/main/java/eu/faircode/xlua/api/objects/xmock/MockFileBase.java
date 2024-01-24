package eu.faircode.xlua.api.objects.xmock;



public class MockFileBase {
    protected String name;
    protected String contents;

    public MockFileBase() { }
    public MockFileBase(String name, String contents) {
        setName(name);
        setContents(contents);
    }

    public String getName() { return this.name; }
    public MockFileBase setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public String getContents() { return this.contents; }
    public MockFileBase setContents(String contents) {
        if(contents != null) this.contents = contents;
        return this;
    }

    @Override
    public int hashCode() { return this.name.hashCode(); }

    @Override
    public String toString() { return this.name; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MockFileBase))
            return false;
        MockFileBase other = (MockFileBase) obj;
        return this.name.equals(other.name);
    }
}
