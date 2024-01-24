package eu.faircode.xlua.api.objects.xlua.hook;

import android.database.Cursor;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;

import eu.faircode.xlua.utilities.CursorUtil;


public class xHookConversions {
    public static Collection<xHook> fromCursor(Cursor cursor, boolean marshall, boolean close) {
        Collection<xHook> ps = new ArrayList<>();
        try {
            if(marshall) {
                while (cursor != null && cursor.moveToNext()) {
                    byte[] marshaled = cursor.getBlob(0);
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(marshaled, 0, marshaled.length);
                    parcel.setDataPosition(0);
                    xHook hook = xHook.CREATOR.createFromParcel(parcel);
                    parcel.recycle();
                    ps.add(hook);
                }
            }else {

            }
        }finally {
            if(close) CursorUtil.closeCursor(cursor);
        }

        return ps;
    }
}
