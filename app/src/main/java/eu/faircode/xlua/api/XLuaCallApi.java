package eu.faircode.xlua.api;

import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XUtil;

import eu.faircode.xlua.api.objects.xlua.packets.HookPacket;
import eu.faircode.xlua.api.xlua.xcall.AssignHooksCommand;
import eu.faircode.xlua.api.xlua.xcall.ClearAppCommand;
import eu.faircode.xlua.api.xlua.xcall.ClearDataCommand;
import eu.faircode.xlua.api.xlua.xcall.GetGroupsCommand;
import eu.faircode.xlua.api.xlua.xcall.GetSettingCommand;
import eu.faircode.xlua.api.xlua.xcall.GetVersionCommand;
import eu.faircode.xlua.api.xlua.xcall.InitAppCommand;
import eu.faircode.xlua.api.xlua.xcall.PutHookCommand;
import eu.faircode.xlua.api.xlua.xcall.PutSettingCommand;
import eu.faircode.xlua.api.xlua.xcall.ReportCommand;
import eu.faircode.xlua.api.objects.xlua.packets.AppPacket;
import eu.faircode.xlua.api.objects.xlua.packets.AssignmentPacket;
import eu.faircode.xlua.api.objects.xlua.packets.SettingPacket;
import eu.faircode.xlua.hooks.XReport;
import eu.faircode.xlua.utilities.BundleUtil;

public class XLuaCallApi {
    private static final String TAG = "XLua.XLuaCallApi";

    public static boolean assignHooks(Context context, AssignmentPacket packet) { return BundleUtil.readResultStatus(AssignHooksCommand.invoke(context, packet)); }
    public static boolean assignHooks(Context context, List<String> hookIds, String packageName, int uid, Boolean delete, Boolean kill) {
        return BundleUtil.readResultStatus(
                AssignHooksCommand.invoke(
                        context,
                        hookIds,
                        packageName,
                        uid,
                        delete,
                        kill));
    }

    public static boolean initApp(Context context, String packageName, Integer userId) { return initApp(context, packageName, userId, false); }
    public static boolean initApp(Context context, String packageName, Integer userId, Boolean kill) {
        return BundleUtil.readResultStatus(
                InitAppCommand.invoke(
                        context,
                        packageName,
                        userId,
                        kill));
    }

    public static boolean clearData(Context context, int userId) { return BundleUtil.readResultStatus(ClearDataCommand.invoke(context, userId)); }

    public static boolean clearApp(Context context, AppPacket packet) { return BundleUtil.readResultStatus(ClearAppCommand.invoke(context, packet)); }
    public static boolean clearApp(Context context, String packageName, int uid) { return clearApp(context, packageName, uid, false); }
    public static boolean clearApp(Context context, String packageName, int uid, boolean full) {
        return BundleUtil.readResultStatus(
                ClearAppCommand.invoke(
                        context,
                        packageName,
                        uid,
                        full));
    }

    public static List<String> getGroups(Context context) {
        return BundleUtil.readStringList(
                GetGroupsCommand.invoke(context),
                "groups",
                true);
    }

    public static SettingPacket getSetting(Context context, Integer userId, String category, String name) {
        SettingPacket setting = new SettingPacket(userId, category, name);
        setting.fromBundle(GetSettingCommand.invoke(context, setting));
        return setting;
    }

    public static boolean getSettingBoolean(Context context, String name) { return getSettingBoolean(context, "global", name); }
    public static boolean getSettingBoolean(Context context, String category, String name) { return getSettingBoolean(context, XUtil.getUserId(Process.myUid()), category, name); }
    public static boolean getSettingBoolean(Context context, Integer user, String category, String name) { return Boolean.parseBoolean(getSettingValue(context, user, category, name)); }
    public static boolean getSettingBoolean(Context context, Integer user, String name) { return Boolean.parseBoolean(getSettingValue(context, user, "global", name)); }

    public static String getSettingValue(Context context, String name) { return getSettingValue(context,"global", name); }
    public static String getSettingValue(Context context, String category, String name) { return getSettingValue(context, XUtil.getUserId(Process.myUid()), category, name); }
    public static String getSettingValue(Context context, Integer userId, String category, String name) {
        if(BuildConfig.DEBUG)
            Log.i(TAG, "[getSettingValue] user=" + userId + "  category=" + category + "  name=" + name);

        return BundleUtil.readString(GetSettingCommand.invoke(
                context,
                userId,
                category,
                name,
                null), "value");
    }

    public static List<String> getCollections(Context context) {
        List<String> collections = new ArrayList<>();
        String collectionValue = getSettingValue(context, "collection");
        if(collectionValue == null || collectionValue.isEmpty()) {
            collections.add("Privacy");
            collections.add("PrivacyEx");
            //Should not happen
        }
        else if(!collectionValue.contains(",")) collections.add(collectionValue);
        else Collections.addAll(collections, collectionValue.split(","));

        return collections;
    }

    public static String getTheme(Context context) {
        String theme = getSettingValue(context, "theme");
        if(theme == null)
            theme = "dark";

        return theme;
    }

    public static boolean report(Context context, XReport report) { return BundleUtil.readResultStatus(ReportCommand.invoke(context, report)); }
    public static int getVersion(Context context) { return BundleUtil.readInt(GetVersionCommand.invoke(context), "version");}

    public static boolean putHook(Context context, String id, String definition) { return BundleUtil.readResultStatus(PutHookCommand.invoke(context, id, definition)); }
    public static boolean putHook(Context context, HookPacket packet) { return BundleUtil.readResultStatus(PutHookCommand.invoke(context, packet)); }

    public static boolean putSettingBoolean(Context context, String name, Boolean value) { return putSetting(context, "global", name, Boolean.toString(value)); }
    public static boolean putSettingBoolean(Context context, String category, String name, Boolean value) { return putSetting(context, category, name, Boolean.toString(value)); }
    public static boolean putSettingBoolean(Context context, Integer userId, String category, String name, Boolean value) { return putSetting(context, userId, category, name, Boolean.toString(value)); }

    public static boolean putSetting(Context context, SettingPacket packet) { return BundleUtil.readResultStatus(PutSettingCommand.invoke(context, packet)); }
    public static boolean putSetting(Context context, String name, String value) { return putSetting(context, XUtil.getUserId(Process.myUid()), "global", name, value); }
    public static boolean putSetting(Context context, String category, String name, String value) { return putSetting(context, XUtil.getUserId(Process.myUid()), category, name, value); }
    public static boolean putSetting(Context context, Integer userId, String category, String name, String value) {
        return BundleUtil.readResultStatus(
                PutSettingCommand.invoke(context, userId, category, name, value));
    }

    public static boolean putSetting(Context context, Integer userId, String category, String name, String value, Boolean kill) {
        return BundleUtil.readResultStatus(
                PutSettingCommand.invoke(context, userId, category, name, value, kill));
    }
}
