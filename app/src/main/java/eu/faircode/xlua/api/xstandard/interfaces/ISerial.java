package eu.faircode.xlua.api.xstandard.interfaces;

import android.os.Bundle;
import android.os.Parcel;

public interface ISerial {
    Bundle toBundle();
    void fromBundle(Bundle bundle);
    void fromParcel(Parcel in);
    void writeToParcel(Parcel dest, int flags);
}
