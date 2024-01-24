package eu.faircode.xlua.api.objects.xlua.packets;

import android.os.Bundle;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.api.objects.ISerial;

//Seperate communication ? like this ?
//Have packets for each similar to a C2 server

//Have like toReadItems or something read the independant items from it or something
//Example XAssignmentIO
//Have even maybe post call ? then way for return ? ...
//Again these classes can specifically be for communiction within the Proxy/Gum/Content Resolver
//Have even maybe a "needsCheckPermissions" then checks caller if specific com requires
//Possibly if possible have key communication shit ? prevent detection via Content Resolver
//For the "invokeCall" function this can be passed ?

//Hmm when dynamically made ? or something have within here a "Handle" function to now Handle the command on the other side ????

public class AssignmentPacket implements ISerial {
    public List<String> hookIds;
    public String packageName;
    public Integer uid;
    public Boolean delete;
    public Boolean kill;

    public AssignmentPacket() { }
    public AssignmentPacket(Bundle b) { fromBundle(b); }
    public AssignmentPacket(List<String> hookIds, String packageName, Integer uid, Boolean delete, Boolean kill) {
        this.hookIds = hookIds;
        this.packageName = packageName;
        this.uid = uid;
        this.delete = delete;
        this.kill = kill;
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(hookIds != null) b.putStringArrayList("hooks", (ArrayList<String>) hookIds);
        if(packageName != null) b.putString("packageName", packageName);
        if(uid != null) b.putInt("uid", uid);
        if(delete != null) b.putBoolean("delete", delete);
        if(kill != null) b.putBoolean("kill", kill);//default value this ???
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        if(b != null) {
            hookIds = b.getStringArrayList("hooks");
            packageName = b.getString("packageName");
            uid = b.getInt("uid");
            delete = b.getBoolean("delete");
            //kill = b.getBoolean("kill", false);
            if(b.containsKey("kill"))
                kill = b.getBoolean("kill"); //should we set ?, shouldnt matter if other side recives it will be default false then either way :P
        }
    }

    @Override
    public void fromParcel(Parcel in) { }

    @Override
    public void writeToParcel(Parcel dest, int flags) { }

    public static AssignmentPacket createFromBundle(Bundle b) { return new AssignmentPacket(b); }
    public static AssignmentPacket create(List<String> hookIds, String packageName, Integer uid, Boolean delete, Boolean kill) {
        return new AssignmentPacket(hookIds, packageName, uid, delete, kill);
    }
}
