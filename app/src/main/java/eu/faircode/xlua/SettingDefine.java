package eu.faircode.xlua;

import android.os.Parcel;
import android.os.Parcelable;

public class SettingDefine implements Parcelable {
    protected String name;
    protected String description;
    protected String value;

    public SettingDefine(Parcel p) { }


    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<SettingDefine> CREATOR = new Parcelable.Creator<SettingDefine>() {
        @Override
        public SettingDefine createFromParcel(Parcel source) {
            return new SettingDefine(source);
        }

        @Override
        public SettingDefine[] newArray(int size) {
            return new SettingDefine[size];
        }
    };

}
