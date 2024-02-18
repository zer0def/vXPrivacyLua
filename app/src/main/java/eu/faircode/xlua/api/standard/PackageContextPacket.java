package eu.faircode.xlua.api.standard;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;
import eu.faircode.xlua.api.standard.interfaces.IDBQuery;
import eu.faircode.xlua.api.standard.interfaces.IJsonSerial;
import eu.faircode.xlua.api.standard.interfaces.IPacket;
import eu.faircode.xlua.utilities.BundleUtil;
import eu.faircode.xlua.utilities.ContentValuesUtil;
import eu.faircode.xlua.utilities.CursorUtil;
import eu.faircode.xlua.utilities.StringUtil;

//Hmm we can actually cook something with this :P
//We can use this as some optional field
//For now lets not get too carried away
//This is a mess but a PoC for later to work on for now dont get carried away
//If we dont want it as a 'field' we can just Inherit this for any object that requires package context
/*public class PackageContextPacket implements IJsonSerial, IDBQuery, IPacket {
    public static final int USER_PACKET_ONE = 0x0;
    public static final int USER_PACKET_TWO = 0x1;

    public static PackageContextPacket create() { return new PackageContextPacket(LuaSettingsDatabase.GLOBAL_USER, LuaSettingsDatabase.GLOBAL_NAMESPACE); }
    public static PackageContextPacket create(int user, String packageName) {  return new PackageContextPacket(user, packageName); }

    protected int user;
    protected String packageName;
    protected SqlQuerySnake query;

    public PackageContextPacket(int user, String packageName) {
        //Do not use with LUA core since it used 'category' instead of 'packageName'
        //Once we remove the 'pro' app for sure we can do migration
        this.user = user;
        this.packageName = packageName;
        this.query = SqlQuerySnake.create()
                .whereColumn("user", user)
                .whereColumn("packageName", packageName);
    }

    @Override
    public Bundle toBundle() { return null; }

    @Override
    public void fromBundle(Bundle b) {
        this.user = BundleUtil.readInteger(b, "user", LuaSettingsDatabase.GLOBAL_USER);
        this.packageName = BundleUtil.readString(b, "packageName", LuaSettingsDatabase.GLOBAL_NAMESPACE);
    }

    @Override
    public ContentValues createContentValues() { return null; }

    @Override
    public void fromContentValues(ContentValues contentValue) {
        this.user = ContentValuesUtil.getInteger(contentValue, "user", LuaSettingsDatabase.GLOBAL_USER);
        this.packageName = ContentValuesUtil.getString(contentValue, "packageName", LuaSettingsDatabase.GLOBAL_NAMESPACE);
    }

    @Override
    public List<ContentValues> createContentValuesList() { return null;  }

    @Override
    public void fromContentValuesList(List<ContentValues> contentValues) { }

    @Override
    public void fromCursor(Cursor cursor) {
        this.user = CursorUtil.getInteger(cursor, "user", LuaSettingsDatabase.GLOBAL_USER);
        this.packageName = CursorUtil.getString(cursor, "packageName", LuaSettingsDatabase.GLOBAL_NAMESPACE);
    }

    @Override
    public void fromParcel(Parcel in) {
        this.user = in.readInt();
        this.packageName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user);
        dest.writeString(this.packageName);
    }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException { return null; }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException { }

    @Override
    public SqlQuerySnake createQuery(XDatabase db) { return query.setDatabase(db); }

    @Override
    public int getSecretKey() { return 0; }

    @Override
    public void readSelectionArgs(String[] selection, int flags) {
        ///hmmm maybe i dunno this bind this too much and if missing something then issue :P
        //Lets just create a packet
        if(selection != null && selection.length > 0) {
            if(flags == USER_PACKET_ONE) {
                this.user = StringUtil.toInteger(selection[0], LuaSettingsDatabase.GLOBAL_USER);
                if(selection.length > 1)
                    this.packageName = selection[1];
            }else if(flags == USER_PACKET_TWO) {
                this.packageName = selection[0];
                if(selection.length > 1)
                    this.user =  StringUtil.toInteger(selection[1], LuaSettingsDatabase.GLOBAL_USER);
            }
        }
    }

}*/
