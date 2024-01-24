package eu.faircode.xlua.utilities;

import android.os.Parcel;

public class ParcelUtil {
    public static void writeBool(Parcel in, Boolean bResult) {
        if(bResult == null) {
            //?
        }else {
            in.writeByte(bResult ? (byte)1 : (byte)0);
        }
    }

    public static Boolean readBool(Parcel in) {
        return in.readByte() == 1;
    }
}
