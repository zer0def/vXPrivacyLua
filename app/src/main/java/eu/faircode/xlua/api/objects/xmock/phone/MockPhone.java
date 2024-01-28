package eu.faircode.xlua.api.objects.xmock.phone;

import android.content.Context;

import java.util.List;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.xlua.XAppProvider;
import eu.faircode.xlua.api.xlua.XSettingsDatabase;
import eu.faircode.xlua.api.objects.xlua.setting.xSetting;

public class MockPhone {
    protected String name;
    protected String model;
    protected String manufacturer;
    protected String carrier;
    protected List<MockSetting> settings;

    public boolean setSettings(Context context, String packageName, XDataBase db) {
        for(MockSetting s : settings) {
            xSetting setting = new xSetting(0, packageName, s.Name, s.Value);
            XSettingsDatabase.putSetting(context,db, setting);
        }

        //XAppProvider.forceStop(context, packageName);
        return true;
    }
}
