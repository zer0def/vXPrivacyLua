package eu.faircode.xlua.api.objects.xlua.packets;

import android.os.Bundle;
import android.os.Parcel;

import eu.faircode.xlua.api.objects.ISerial;

public class AppPacket implements ISerial {
    public String packageName;
    public Integer uid;
    public Boolean kill;
    public Boolean settings;

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(packageName != null) b.putString("packageName", packageName);
        if(uid != null) b.putInt("uid", uid);
        if(kill != null) b.putBoolean("kill", kill);
        if(settings != null) b.putBoolean("settings", settings);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        this.packageName = b.getString("packageName");
        this.uid = b.getInt("uid");
        this.kill = b.getBoolean("kill", false);
        this.settings = b.getBoolean("settings", false); //or set it to true ?????
    }

    @Override
    public void fromParcel(Parcel in) { }

    @Override
    public void writeToParcel(Parcel dest, int flags) { }
}
