package eu.faircode.xlua.api;

import android.content.Context;
import android.database.Cursor;
import android.os.Process;

import java.util.Map;

import eu.faircode.xlua.utilities.BundleUtil;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.database.DatabaseQuerySnake;

import eu.faircode.xlua.utilities.CursorUtil;

/*public class XSettingsProxy {
    //Boolean
    public static boolean getSettingBoolean(Context context, String category, String name) { return getSettingBoolean(context, XUtil.getUserId(Process.myUid()), category, name); }
    public static void putSettingBoolean(Context context, String category, String name, boolean value) { putSetting(context, category, name, Boolean.toString(value)); }
    public static boolean getSettingBoolean(Context context, int user, String category, String name) { return Boolean.parseBoolean(getSetting(context, user, category, name)); }

    //Setting Value
    public static String getSetting(Context context, String category, String name) { return getSetting(context, XUtil.getUserId(Process.myUid()), category, name); }
    public static String getSetting(Context context, int user, String category, String name) {
        XSettingIO setting = new XSettingIO(user, category, name);
        return BundleUtil.readString(
                XLuaCallHandler.invokeCall(context, "getSetting", setting.toBundle()), "value", "false");
    }

    public static void putSetting(Context context, XSettingIO setting) { putSetting(context, setting.userId, setting.category, setting.name, setting.value); }
    public static void putSetting(Context context, String category, String name, String value) { putSetting(context, XUtil.getUserId(Process.myUid()), category, name, value); }
    public static void putSetting(Context context, int userId, String category, String name, String value) {
        XSettingIO setting = new XSettingIO(userId, category, name, value);
        XLuaCallHandler.invokeCall(context, "putSetting", setting.toBundle());
    }

    public static Map<String, String> getSettings(Context context, int uid) { return getAppSettings(context, uid, "global"); }
    public static Map<String, String> getAppSettings(Context context, int uid, String packageName) {
        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create()
                .whereColumn("pkg", packageName)
                .whereColumn("uid", uid);
        //Our handler Provider function will get "pkg" and "uid" as args to use
        Cursor c = XLuaQueryHandler.invokeQuery(
                context,
                "getSettings",
                snake.getSelectionCompareValues(),
                snake.getSelectionArgs());

        return CursorUtil.toDictionary(c, true);
    }
}*/
