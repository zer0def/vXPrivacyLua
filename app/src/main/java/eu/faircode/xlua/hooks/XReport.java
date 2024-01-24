package eu.faircode.xlua.hooks;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.objects.IDBSerial;
import eu.faircode.xlua.api.objects.xlua.hook.GroupDatabaseEntry;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.api.xlua.XSettingsDatabase;

public class XReport implements IDBSerial {
    public String hookId;
    public String packageName;
    public Integer uid;
    public String event;
    public Long time;
    public Bundle data;

    private Boolean notify = null;
    private Integer userid = null;
    private Integer restricted = null;

    public XReport() { }
    public XReport(Bundle b) { fromBundle(b); }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(hookId != null) b.putString("hook", hookId);
        if(packageName != null) b.putString("packageName", packageName);
        if(uid != null) b.putInt("uid", uid);
        if(event != null) b.putString("event", event);
        if(time != null) b.putLong("time", time);
        if(data != null) b.putBundle("data", data);
        return b;
    }

    public void fromBundle(Bundle b) {
        hookId = b.getString("hook");
        packageName = b.getString("packageName");
        uid = b.getInt("uid");
        event = b.getString("event");
        time = b.getLong("time");
        data = b.getBundle("data");
    }

    public GroupDatabaseEntry createGroupObject(xHook hook, long used) { return new GroupDatabaseEntry(packageName, uid, hook.getGroup(), used); }

    public int getRestricted() {
        if(restricted == null)
            restricted = data.getInt("restricted", 0);

        return restricted;
    }

    public int getUserId() {
        if(userid == null)
            userid = XUtil.getUserId(uid);

        return userid;
    }

    public boolean getNotify(XDataBase db) {
        if(notify == null)
            notify = Boolean.parseBoolean(XSettingsDatabase.getSettingValue(db, getUserId(), packageName, "notify"));

        return notify;
    }

    public DatabaseQuerySnake generateQuery() {
        return DatabaseQuerySnake.create()
                .whereColumns("package", "uid", "hook")
                .whereColumnValues(packageName, Integer.toString(uid), hookId);
    }

    public String getFullException() {
        StringBuilder b = new StringBuilder();
        for (String key : data.keySet()) {
            b.append(' ');
            b.append(key);
            b.append('=');
            Object value = data.get(key);
            b.append(value == null ? "null" : value.toString());
        }

        return b.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return "Hook " + hookId + " pkg=" + packageName + ":" + uid + " event=" + event;
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        if(event.equals("install"))
            cv.put("installed", time);
        else if(event.equals("use")) {
            cv.put("used", time);
            cv.put("restricted", data.getInt("restricted", 0));
        }

        if (data.containsKey("exception"))
            cv.put("exception", data.getString("exception"));
        if (data.containsKey("old"))
            cv.put("old", data.getString("old"));
        if (data.containsKey("new"))
            cv.put("new", data.getString("new"));

        return cv;
    }

    @Override
    public void fromCursor(Cursor cursor) {

    }
}
