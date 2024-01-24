package eu.faircode.xlua.api.objects.xlua.packets;

import android.os.Bundle;
import android.os.Parcel;

import androidx.annotation.NonNull;

import eu.faircode.xlua.api.objects.xlua.setting.xSetting;

public class SettingPacket extends xSetting {
    protected Boolean kill;

    public SettingPacket() { }
    public SettingPacket(Bundle b) { fromBundle(b); }
    public SettingPacket(Parcel in) { super(in); }
    public SettingPacket(Integer user, String category, String name) { super(user, category, name); }
    public SettingPacket(Integer user, String category, String name, String value) { super(user, category, name, value); }
    public SettingPacket(Integer user, String category, String name, String value, Boolean kill) {
        super(user, category, name, value);
        setKill(kill);
    }

    public SettingPacket setKill(Boolean kill) {
        if(kill != null) this.kill = kill;
        return this;
    }

    public Boolean getKill() { return this.kill; }

    @Override
    public Bundle toBundle() {
        Bundle b = super.toBundle();
        if(kill != null) b.putBoolean("kill", kill);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        super.fromBundle(b);
        if(b != null) {
            if(b.containsKey("kill")) kill = b.getBoolean("kill");
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if(kill != null) {
            sb.append("\nkill=");
            sb.append(kill);
        }

        return sb.toString();
    }
}