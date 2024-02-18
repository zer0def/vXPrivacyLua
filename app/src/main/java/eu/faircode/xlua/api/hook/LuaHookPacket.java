package eu.faircode.xlua.api.hook;

public class LuaHookPacket extends HookDatabaseEntry {
    public static LuaHookPacket create(String id, String definition) { return new LuaHookPacket(id, definition); }

    public LuaHookPacket() { }
    public LuaHookPacket(String id, String definition) { super(id, definition); }
}
