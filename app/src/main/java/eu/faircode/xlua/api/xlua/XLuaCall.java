package eu.faircode.xlua.api.xlua;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.hook.LuaHookPacket;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.standard.UserIdentityPacket;
import eu.faircode.xlua.api.xlua.call.AssignHooksCommand;
import eu.faircode.xlua.api.xlua.call.ClearAppCommand;
import eu.faircode.xlua.api.xlua.call.ClearDataCommand;
import eu.faircode.xlua.api.xlua.call.GetGroupsCommand;
import eu.faircode.xlua.api.xlua.call.GetSettingCommand;
import eu.faircode.xlua.api.xlua.call.GetVersionCommand;
import eu.faircode.xlua.api.xlua.call.InitAppCommand;
import eu.faircode.xlua.api.xlua.call.PutHookCommand;
import eu.faircode.xlua.api.xlua.call.PutSettingCommand;
import eu.faircode.xlua.api.xlua.call.ReportCommand;
import eu.faircode.xlua.api.app.LuaSimplePacket;
import eu.faircode.xlua.api.hook.assignment.LuaAssignmentPacket;
import eu.faircode.xlua.api.xmock.call.PutMockSettingCommand;
import eu.faircode.xlua.hooks.XReport;
import eu.faircode.xlua.utilities.BundleUtil;

public class XLuaCall {
    private static final String TAG = "XLua.XLuaCallApi";

    public static XResult assignHooks(Context context, Integer user, String packageName, List<String> hookIds, Boolean delete) { return assignHooks(context, user, packageName, hookIds, delete, false); }
    public static XResult assignHooks(Context context, Integer user, String packageName, List<String> hookIds, Boolean delete, Boolean kill) { return XResult.from(AssignHooksCommand.invoke(context, LuaAssignmentPacket.create(user, packageName, hookIds, delete, kill))); }
    public static XResult assignHooks(Context context, LuaAssignmentPacket packet) { return XResult.from(AssignHooksCommand.invoke(context, packet)); }

    public static XResult initApp(Context context, Integer user, String packageName) { return initApp(context, user, packageName, false); }
    public static XResult initApp(Context context, Integer user, String packageName, Boolean kill) { return XResult.from(InitAppCommand.invoke(context, LuaSimplePacket.create(user, packageName, kill))); }

    public static XResult clearData(Context context, Integer user) { return XResult.from(ClearDataCommand.invoke(context, LuaSimplePacket.create(user))); }

    public static XResult clearApp(Context context, Integer user, String packageName) { return clearApp(context, user, packageName, false, false); }
    public static XResult clearApp(Context context, Integer user, String packageName, Boolean deleteFullData) { return clearApp(context, user, packageName, false, deleteFullData); }
    public static XResult clearApp(Context context, Integer user, String packageName, Boolean kill, Boolean deleteFullData) { return XResult.from(ClearAppCommand.invoke(context, LuaSimplePacket.create(user, packageName, kill, LuaSimplePacket.getCodeForFullData(deleteFullData)))); }
    public static XResult clearApp(Context context, LuaSimplePacket packet) { return XResult.from(ClearAppCommand.invoke(context, packet)); }


    public static List<String> getGroups(Context context) {
        return BundleUtil.readStringList(
                GetGroupsCommand.invoke(context),
                "groups",
                true);
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
    public static int getVersion(Context context) { return BundleUtil.readInteger(GetVersionCommand.invoke(context), "version");}

    public static XResult putHook(Context context, String id, String definition) { return putHook(context, LuaHookPacket.create(id, definition)); }
    public static XResult putHook(Context context, LuaHookPacket packet) { return XResult.from(PutHookCommand.invoke(context, packet)); }

    public static boolean getSettingBoolean(Context context, String name) { return getSettingBoolean(context, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE, name); }
    public static boolean getSettingBoolean(Context context, Integer user, String category, String name) { return Boolean.parseBoolean(getSettingValue(context, user, category, name)); }

    public static String getSettingValue(Context context, String name) { return getSettingValue(context, name, false); }
    public static String getSettingValue(Context context, String name, boolean getValueOrDefault) { return getSettingValue(context, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE, name, getValueOrDefault); }
    public static String getSettingValue(Context context, Integer user, String category, String name) { return getSettingValue(context, user, category, name, false); }
    public static String getSettingValue(Context context, Integer user, String category, String name, boolean getValueOrDefault) { return BundleUtil.readString(GetSettingCommand.invoke(context, LuaSettingPacket.create(user, category, name, null, LuaSettingPacket.getCodeForGetValue(getValueOrDefault))), "value"); }

    public static XResult deleteSetting(Context context, String settingName) { return deleteSetting(context, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE, settingName); }
    public static XResult deleteSetting(Context context, Integer user, String category, String settingName) { return deleteSetting(context, user, category, settingName, false); }
    public static XResult deleteSetting(Context context, Integer user, String category, String settingName, Boolean kill) { return putSetting(context, user, category, settingName, null, kill); }

    public static XResult putSettingBoolean(Context context, String settingName, boolean value) { return putSettingBoolean(context, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE, settingName, value, false);  }
    public static XResult putSettingBoolean(Context context, Integer user, String category, String settingName, boolean value) { return putSettingBoolean(context, user, category, settingName, value, false); }
    public static XResult putSettingBoolean(Context context, Integer user, String category, String settingName, boolean value, Boolean kill) { return putSetting(context, user, category, settingName, Boolean.toString(value), kill); }

    public static XResult putSetting(Context context, String settingName) { return putSetting(context, settingName, null); }
    public static XResult putSetting(Context context, String settingName, String value) { return putSetting(context, UserIdentityPacket.GLOBAL_USER, UserIdentityPacket.GLOBAL_NAMESPACE, settingName, value, false);}
    public static XResult putSetting(Context context, Integer user, String category, String settingName) { return putSetting(context, user, category, settingName, null); }
    public static XResult putSetting(Context context, Integer user, String category, String settingName, String value) { return putSetting(context, user, category, settingName, value, false); }
    public static XResult putSetting(Context context, Integer user, String category, String settingName, String value, Boolean kill) { return sendSetting(context, LuaSettingPacket.create(user, category, settingName, value, LuaSettingPacket.getCodeFromValue(value), kill)); }


    public static XResult sendSetting(Context context, LuaSettingPacket packet) { return XResult.from(PutSettingCommand.invoke(context, packet)); }
    public static XResult sendMockSetting(Context context, LuaSettingPacket packet) { return XResult.from(PutMockSettingCommand.invoke(context, packet)); }
}
