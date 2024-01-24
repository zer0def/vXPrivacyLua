package eu.faircode.xlua.api.xlua.xcall.packets;

import android.os.Bundle;


/*public class HookPacket implements ICallCommand {
    public String id;
    public String definition;

    public HookPacket() { }
    public HookPacket(Bundle b) { fromBundle(b); }
    public HookPacket(String id, String definition) {
        this.id = id;
        this.definition = definition;
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(id != null) b.putString("id", id);
        if(definition != null) b.putString("definition", definition);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        if(b != null) {
            id = b.getString("id");
            definition = b.getString("definition");
        }
    }

    public static HookPacket createFromBundle(Bundle b) { return new HookPacket(b); }
    public static HookPacket create(String id, String definition) {
        return new HookPacket(id, definition);
    }
}*/
