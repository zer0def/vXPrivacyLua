package eu.faircode.xlua.api.hook;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import eu.faircode.xlua.api.xstandard.interfaces.IJsonSerial;
import eu.faircode.xlua.utilities.JSONUtil;

public class LuaHookUpdate implements IJsonSerial {
    protected String newName;
    protected String oldName;
    protected String description;
    protected String extra;

    public String getNewId() { return this.newName; }
    public String getOldId() { return this.oldName; }
    public String getDescription() { return this.description; }
    public String getExtra() { return this.extra; }
    public LuaHookUpdate() {  }

    @Override
    public ContentValues createContentValues() { return null; }

    @Override
    public List<ContentValues> createContentValuesList() { return null; }

    @Override
    public void fromContentValuesList(List<ContentValues> contentValues) { }

    @Override
    public void fromContentValues(ContentValues contentValue) { }

    @Override
    public void fromCursor(Cursor cursor) { }

    @Override
    public Bundle toBundle() { return null; }

    @Override
    public void fromBundle(Bundle bundle) { }

    @Override
    public void fromParcel(Parcel in) { }

    @Override
    public void writeToParcel(Parcel dest, int flags) { }

    @Override
    public String toJSON() throws JSONException { return toJSONObject().toString(2); }

    @Override
    public JSONObject toJSONObject() throws JSONException {
        JSONObject jRoot = new JSONObject();
        if(this.oldName != null) jRoot.put("old", this.oldName);
        if(this.newName != null) jRoot.put("new", this.newName);
        if(this.description != null) jRoot.put("description", this.description);
        if(this.extra != null) jRoot.put("extra", this.extra);
        return jRoot;
    }

    @Override
    public void fromJSONObject(JSONObject obj) throws JSONException {
        if(obj != null) {
            this.oldName = JSONUtil.getString(obj, "old");
            this.newName = JSONUtil.getString(obj, "new");
            this.description = JSONUtil.getString(obj, "description");
            this.extra = JSONUtil.getString(obj, "extra");
        }
    }
}
