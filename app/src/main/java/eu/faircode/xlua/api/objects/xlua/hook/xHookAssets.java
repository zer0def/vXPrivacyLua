package eu.faircode.xlua.api.objects.xlua.hook;

import android.os.Parcel;

public class xHookAssets extends xHook {
    public xHookAssets() { }
    public xHookAssets(Parcel in) { fromParcel(in); }
    public xHookAssets(HookDatabaseEntry hookDb) { fromBundle(hookDb.toBundle()); }

    public void setIsBuiltIn(Boolean isBuiltin) { if(isBuiltin != null) this.builtin = isBuiltin; }
    public void setLuaScript(String luaScript) { if(luaScript != null) this.luaScript = luaScript; }

}
