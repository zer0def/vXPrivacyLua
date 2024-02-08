package eu.faircode.xlua.api.xmock;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.Map;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.objects.ISettingsConfig;
import eu.faircode.xlua.api.objects.xlua.packets.SettingPacket;
import eu.faircode.xlua.api.objects.xlua.setting.xSetting;
import eu.faircode.xlua.api.xlua.XSettingsDatabase;
import eu.faircode.xlua.utilities.StringUtil;

public class XMockPhoneProvider {
    private static final String TAG = "XLua.XMockPhoneProvider";

    public static boolean applySettingsConfig(Context context, ISettingsConfig config, String packageName, XDataBase db) { return applySettingsConfig(context, config, packageName, XUtil.getUserId(Process.myUid()), db); }
    public static boolean applySettingsConfig(Context context, ISettingsConfig config, String packageName, int userId, XDataBase db)  {
        if(config == null)
            return false;

        Map<String, String> settings = config.getSettings();
        if(settings == null || settings.size() < 1)
            return false;

        Log.i(TAG, "setting config name=" + config.getName() + " settings size=" + settings.size());
        int inserted = 0;
        int failed = 0;
        try {
            if(!db.beginTransaction(true)) {
                Log.e(TAG, "Failed to being Transaction on DB: " + db + " for applying settings config");
                return false;
            }

            for(Map.Entry<String, String> r : settings.entrySet()) {
                String n = r.getKey();
                String v = r.getValue();
                if(!StringUtil.isValidString(v))
                    continue;

                xSetting setting = new SettingPacket(userId, packageName, n, v);
                if(!db.insert(xSetting.Table.name, setting.createContentValues())) {
                    Log.e(TAG, "Failed to insert setting: name=" + n + " value=" + v);
                    failed++;
                }else {
                    inserted++;
                }
            }

            db.setTransactionSuccessful();
            return true;
        }finally {
            Log.i(TAG, "Inserted settings successfully=" + inserted + " failed=" + failed + " config name=" + config.getName());
            db.endTransaction(true, false);
        }
    }
}
