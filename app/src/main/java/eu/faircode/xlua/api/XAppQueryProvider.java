package eu.faircode.xlua.api;

import android.content.Context;
import android.database.Cursor;
import android.os.Binder;

import java.util.Map;


import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.utilities.CursorUtil;

/*public class XAppQueryProvider {
    private static final String TAG = "XLua.XAppQueryProvider";

    public static Cursor getApps(Context context, boolean marshall, XDataBase db) throws Throwable {
        Map<String, XAppIO> apps =
                XAppProvider.getApps(context, XUtil.getUserId(Binder.getCallingUid()), db, true, true);
        return CursorUtil.toMatrixCursor(
                apps.values(),
                marshall,
                0);
    }
}*/
