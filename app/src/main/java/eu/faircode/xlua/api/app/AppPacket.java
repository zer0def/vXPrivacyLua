package eu.faircode.xlua.api.app;

import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.api.xstandard.UserIdentityPacket;

public class AppPacket  extends UserIdentityPacket {
    public static AppPacket create(AppGeneric application) { return new AppPacket(application, true, true); }
    public static AppPacket create(AppGeneric application, boolean initForceKill) {  return new AppPacket(application, initForceKill, true); }
    public static AppPacket create(AppGeneric application, boolean initForceKill, boolean initSettings) {  return new AppPacket(application, initForceKill, initSettings); }

    public static AppPacket create(String packageName) { return new AppPacket(GLOBAL_USER, packageName, true, true); }
    public static AppPacket create(String packageName, boolean initForceKill) { return new AppPacket(GLOBAL_USER, packageName, initForceKill, true); }
    public static AppPacket create(String packageName, boolean initForceKill, boolean initSettings) { return new AppPacket(GLOBAL_USER, packageName, initForceKill, initSettings); }
    public static AppPacket create(int userId, String packageName, boolean initForceKill, boolean initSettings) { return new AppPacket(userId, packageName, initForceKill, initSettings); }

    public static final int CODE_GET_EMPTY = 0x0;
    public static final int CODE_INIT_SETTINGS = 0x1;
    public static final int CODE_INIT_FORCE_STOP = 0x2;
    public static final int CODE_INIT_STOP_AND_SETTINGS = 0x3;

    public boolean isInitSettings() { return isCodes(CODE_INIT_SETTINGS, CODE_INIT_STOP_AND_SETTINGS); }
    public boolean isInitForceStop() { return isCodes(CODE_INIT_STOP_AND_SETTINGS, CODE_INIT_FORCE_STOP); }

    public AppPacket() { setUseUserIdentity(true); }
    public AppPacket(AppGeneric application, boolean initForceStop, boolean initSettings) { this(application.getUid(), application.getPackageName(), initForceStop, initSettings); }
    public AppPacket(int userId, String packageName, boolean initForceStop, boolean initSettings) {
        this();
        setUser(userId);
        setCategory(packageName);
        setCode(getCode(initForceStop, initSettings));
    }

    @Override
    public Bundle toBundle() { return writePacketHeaderBundle(super.toBundle()); }

    @Override
    public void fromBundle(Bundle b) {
        super.fromBundle(b);
        super.readPacketHeaderBundle(b);
    }

    public static int getCode(boolean initForceStop, boolean initSettings) {
        return !initForceStop && !initSettings ? CODE_GET_EMPTY :
                initForceStop && !initSettings ? CODE_INIT_FORCE_STOP :
                        !initForceStop ? CODE_INIT_SETTINGS :
                                CODE_INIT_STOP_AND_SETTINGS;
    }
}
