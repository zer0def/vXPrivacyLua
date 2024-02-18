package eu.faircode.xlua.api.hook;

import android.os.Parcel;

public class XLuaHookAssets extends XLuaHook {
    public XLuaHookAssets() { }
    public XLuaHookAssets(Parcel in) { fromParcel(in); }
    public XLuaHookAssets(HookDatabaseEntry hookDb) { fromBundle(hookDb.toBundle()); }

    public void setIsBuiltIn(Boolean isBuiltin) { if(isBuiltin != null) this.builtin = isBuiltin; }
    public void setLuaScript(String luaScript) { if(luaScript != null) this.luaScript = luaScript; }

}
