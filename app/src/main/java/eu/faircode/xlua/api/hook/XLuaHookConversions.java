package eu.faircode.xlua.api.hook;

import android.database.Cursor;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;

import eu.faircode.xlua.utilities.CursorUtil;


public class XLuaHookConversions {
    public static Collection<XLuaHook> fromCursor(Cursor cursor, boolean marshall, boolean close) {
        Collection<XLuaHook> ps = new ArrayList<>();
        try {
            if(marshall) {
                while (cursor != null && cursor.moveToNext()) {
                    byte[] marshaled = cursor.getBlob(0);
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(marshaled, 0, marshaled.length);
                    parcel.setDataPosition(0);
                    XLuaHook hook = XLuaHook.CREATOR.createFromParcel(parcel);
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
