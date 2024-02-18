package eu.faircode.xlua.api.hook;

public abstract class HookDatabaseBase {
    protected String id;
    protected String definition;

    public HookDatabaseBase() { }
    public HookDatabaseBase(String id, String definition) {
        this.id  = id;
        this.definition = definition;
    }

    public HookDatabaseBase setId(String id) {
        if(id != null) this.id = id;
        return this;
    }

    public HookDatabaseBase setDefinition(String definition) {
        if(definition != null) this.definition = definition;
        return this;
    }

    public String getId() { return this.id; }
    public String getDefinition() { return this.definition; }
}
