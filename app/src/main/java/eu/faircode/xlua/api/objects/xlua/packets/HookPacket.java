package eu.faircode.xlua.api.objects.xlua.packets;

import eu.faircode.xlua.api.objects.xlua.hook.HookDatabaseEntry;

public class HookPacket extends HookDatabaseEntry {
    public HookPacket() { }
    public HookPacket(String id, String definition) { super(id, definition); }
}
