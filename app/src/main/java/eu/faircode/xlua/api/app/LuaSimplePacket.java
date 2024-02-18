package eu.faircode.xlua.api.app;

import android.os.Bundle;
import android.os.Parcel;

import eu.faircode.xlua.api.standard.UserIdentityPacket;
import eu.faircode.xlua.utilities.BundleUtil;

public class LuaSimplePacket extends UserIdentityPacket {
    public static LuaSimplePacket create(Integer user) { return new LuaSimplePacket(user, null, CODE_NULL_EMPTY, null, null); }
    public static LuaSimplePacket create(Integer user, String category, Boolean kill) { return new LuaSimplePacket(user, category, CODE_NULL_EMPTY, kill, null); }
    public static LuaSimplePacket create(Integer user, String category, Boolean kill, Boolean flag) { return new LuaSimplePacket(user, category, null, kill, flag); }
    public static LuaSimplePacket create(Integer user, String category, Boolean kill, Integer code) { return new LuaSimplePacket(user, category, code, kill, null); }

    public static final int CODE_RESERVE_FULL_DATA = 0x0;
    public static final int CODE_DELETE_FULL_DATA = 0x1;

    public Boolean flag;

    public LuaSimplePacket() { setUseUserIdentity(true); }
    public LuaSimplePacket(Integer user, String category, Integer code, Boolean kill, Boolean flag) {
        this();
        setUser(user);
        setCategory(category);
        setCode(code);
        setKill(kill);
        setFlag(flag);
    }

    public Boolean getFlag() { return this.flag; }
    public LuaSimplePacket setFlag(Boolean flag) { if(flag != null) this.flag = flag; return this; }

    public boolean isDeleteFullData() { return isCode(CODE_DELETE_FULL_DATA) || (flag != null && flag); }

    @Override
    public Bundle toBundle() {
        Bundle b = writePacketHeaderBundle(super.toBundle());
        if(flag != null) b.putBoolean("settings", flag);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        if(b != null) {
            super.fromBundle(b);
            readPacketHeaderBundle(b);
            this.flag = BundleUtil.readBoolean(b, "settings");
        }
    }

    public static int getCodeForFullData(boolean deleteFullData) { return deleteFullData ? CODE_DELETE_FULL_DATA : CODE_RESERVE_FULL_DATA; }
}
