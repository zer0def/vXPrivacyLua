package eu.faircode.xlua;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

//import eu.faircode.xlua.database.IDatabaseHelper;

/*public class XSettingIO extends XSetting implements Parcelable, IDatabaseHelper {
    public XSettingIO(Bundle bundle) { readFromBundle(bundle); }
    public XSettingIO() { }
    public XSettingIO(Parcel in) { super(in); }

    public XSettingIO(int userId, String category, String name) { super(userId, category, name); }
    public XSettingIO(int userId, String category, String name, String value) {
        super(userId, category, name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.category);
        dest.writeString(this.name);
        dest.writeString(this.value);
    }

    public Bundle toBundle() { return XSettingIO.Convert.toBundle(this); }
    public void readFromBundle(Bundle bundle) {
        userId = bundle.getInt("user");
        category = bundle.getString("category");
        name = bundle.getString("name");
        value = bundle.getString("value");
        kill = bundle.getBoolean("kill", false);
    }

    public void writeToBundle(Bundle bundle) {
        bundle.putInt("user", userId);
        bundle.putString("category", category);
        bundle.putString("name", name);

        if(value != null)
            bundle.putString("value", value);
        if(kill != null)
            bundle.putBoolean("kill", kill);
    }

    public void readFromCursor(Cursor cursor) {
        //Make into a function
        int u = cursor.getColumnIndex("user");
        this.userId = u == -1 ? 0 : cursor.getInt(u);

        int c = cursor.getColumnIndex("category");
        this.category = c == - 1 ? null : cursor.getString(c);

        int n = cursor.getColumnIndex("name");
        this.name = n == -1 ? null : cursor.getString(n);

        int v = cursor.getColumnIndex("value");
        this.value = v == -1 ? null : cursor.getString(v);
    }

    @Override
    public ContentValues createContentValues() {
        ContentValues v = new ContentValues();
        v.put("user", userId);
        v.put("category", category);
        v.put("name", name);
        v.put("value", value);
        return v;
    }

    public static final Parcelable.Creator<XSettingIO> CREATOR = new Parcelable.Creator<XSettingIO>() {
        @Override
        public XSettingIO createFromParcel(Parcel source) {
            return new XSettingIO(source);
        }

        @Override
        public XSettingIO[] newArray(int size) {
            return new XSettingIO[size];
        }
    };

    public static class Convert {
        public static XSettingIO fromBundle(Bundle extras) {
            XSettingIO setting = new XSettingIO();
            setting.readFromBundle(extras);
            return setting;
        }

        public static Bundle toBundle(XSettingIO setting) {
            Bundle b = new Bundle();
            setting.writeToBundle(b);
            return b;
        }
    }
}*/
