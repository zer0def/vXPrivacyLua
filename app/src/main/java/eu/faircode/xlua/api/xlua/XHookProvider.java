package eu.faircode.xlua.api.xlua;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.robv.android.xposed.XposedBridge;
import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XGlobalCore;
import eu.faircode.xlua.XUiGroup;
import eu.faircode.xlua.api.XLuaCallApi;

import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.api.xlua.xcall.GetVersionCommand;

public class XHookProvider {
    private static final String TAG = "XLua.XHookProvider";

    public static List<String> getCollections(XDataBase db, int userId) {
        String value = XSettingsDatabase.getSettingValue(db, userId, "global", "collection");
        if(value == null) {
            List<String> r2 = new ArrayList<>();
            r2.add("Privacy");//PrivacyEx ??
            return r2;
        }


        List<String> result = new ArrayList<>();
        Collections.addAll(result, value.split(","));
        return result;
    }

    public static boolean putHook(Context context, String id, String definition, XDataBase database) throws Throwable {
        if (id == null) {
            Log.e("XHookIO.Convert", "ID Missing from Hook!");
            return false;
        }

        xHook hook = null;
        if(definition != null) {
            hook = new xHook();
            hook.fromJSONObject(new JSONObject(definition));
        }

        if(hook != null) {
            hook.validate();
            if(!id.equals(hook.getId())) {
                Log.e(TAG, "ID Mismatch: Given=" + id + "  Parsed=" + hook.getId());
                return false;
            }
        }

        if(!XGlobalCore.updateHookCache(context, hook, id)) {
            Log.e(TAG, "Failed at Updating Hook Cache, id=" + id);
            return false;
        }

        return XHookDatabase.updateHook(database, hook, id);
    }

    public static boolean isAvailable(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0);
            Bundle b = GetVersionCommand.invoke(context);
            //return XLuaCallApi.getVersion(context) == pi.versionCode;
            return (b != null && pi.versionCode == b.getInt("version"));
        } catch (Throwable ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            XposedBridge.log(ex);
            return false;
        }
    }

    public static List<XUiGroup> getUiGroups(Context context) {
        List<String> groups = XLuaCallApi.getGroups(context);
        List<XUiGroup> uiGroups = new ArrayList<>();

        if(groups == null)
            return uiGroups;

        Resources res = context.getResources();
        for(String name : groups) {
            String g = name.toLowerCase().replaceAll("[^a-z]", "_");
            int id = res.getIdentifier("group_" + g, "string", context.getPackageName());

            XUiGroup group = new XUiGroup();
            group.name = name;
            group.title = (id > 0 ? res.getString(id) : name);
            uiGroups.add(group);
        }

        return uiGroups;
    }
}
