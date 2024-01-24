package eu.faircode.xlua.api.objects.xlua.app;

import android.database.Cursor;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.faircode.xlua.utilities.CursorUtil;

public class xAppConversions {
    public static Collection<xApp> fromCursor(Cursor cursor, boolean marshall, boolean close) {
        Collection<xApp> apps = new ArrayList<>();
        if(cursor == null) return apps;

        try {
            if(marshall) {
                while (cursor.moveToNext()) {
                    byte[] marshaled = cursor.getBlob(0);
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(marshaled, 0, marshaled.length);
                    parcel.setDataPosition(0);
                    xApp app = xApp.CREATOR.createFromParcel(parcel);
                    parcel.recycle();
                    apps.add(app);
                }
            }else {

            }
        }finally {
            CursorUtil.closeCursor(cursor);
        }

        return apps;
    }
}
