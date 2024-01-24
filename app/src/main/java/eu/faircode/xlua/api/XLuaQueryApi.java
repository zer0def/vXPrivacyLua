package eu.faircode.xlua.api;

import android.content.Context;
import android.database.Cursor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.api.objects.xlua.app.xApp;
import eu.faircode.xlua.api.objects.xlua.app.xAppConversions;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.api.objects.xlua.hook.xHookConversions;
import eu.faircode.xlua.api.xlua.xquery.GetAppsCommand;
import eu.faircode.xlua.api.xlua.xquery.GetAssignedHooksCommand;
import eu.faircode.xlua.api.xlua.xquery.GetHooksCommand;
import eu.faircode.xlua.api.xlua.xquery.GetSettingsCommand;
import eu.faircode.xlua.utilities.CursorUtil;

public class XLuaQueryApi {
    public static Collection<xApp> getApps(Context context, boolean marshall) {
        return xAppConversions.fromCursor(
                !marshall ?
                GetAppsCommand.invoke(context) : GetAppsCommand.invokeEx(context), marshall, true);
    }

    public static Collection<xHook> getHooks(Context context, boolean marshall) {
        return xHookConversions.fromCursor(
                !marshall ?
                GetHooksCommand.invoke(context) : GetHooksCommand.invokeEx(context), marshall, true);
    }

    public static Map<String, String> getGlobalSettings(Context context, int uid) { return getSettings(context, "global", uid); }
    public static Map<String, String> getSettings(Context context, String packageOrName, int uid) {
        final Map<String, String> settings = new HashMap<>();
        Cursor c = GetSettingsCommand.invoke(context, packageOrName, uid);

        try {
            while (c != null && c.moveToNext())
                settings.put(c.getString(0), c.getString(1));
        }finally {
            //if(c != null) c.close();
            CursorUtil.closeCursor(c);
        }

        return settings;
    }

    public static Collection<xHook> getAssignments(Context context, String packageName, int uid, boolean marshall) {
        return xHookConversions.fromCursor(
                !marshall ?
                GetAssignedHooksCommand.invoke(context, packageName, uid) :
                GetAssignedHooksCommand.invokeEx(context, packageName, uid),
                marshall, true);
    }
}
