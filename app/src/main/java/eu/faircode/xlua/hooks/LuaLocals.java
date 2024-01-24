package eu.faircode.xlua.hooks;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class LuaLocals extends Globals {
    LuaLocals(Globals globals) {
        this.presize(globals.length(), 0);
        Varargs entry = globals.next(LuaValue.NIL);
        while (!entry.arg1().isnil()) {
            LuaValue key = entry.arg1();
            LuaValue value = entry.arg(2);
            super.rawset(key, value);
            entry = globals.next(entry.arg1());
        }
    }

    @Override
    public void set(int key, LuaValue value) {
        if (value.isfunction())
            super.set(key, value);
        else
            error("Globals not allowed: set " + value);
    }

    @Override
    public void rawset(int key, LuaValue value) {
        if (value.isfunction())
            super.rawset(key, value);
        else
            error("Globals not allowed: rawset " + value);
    }

    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (value.isfunction())
            super.rawset(key, value);
        else
            error("Globals not allowed: " + key + "=" + value);
    }
}