package eu.faircode.xlua.api;

import android.content.Context;
import android.util.Log;

import java.util.Collection;

import eu.faircode.xlua.api.objects.xmock.phone.MockConfigConversions;
import eu.faircode.xlua.api.objects.xmock.phone.MockPhoneConfig;
import eu.faircode.xlua.api.xmock.xquery.GetMockConfigsCommand;

public class XMockQueryApi {
    public static Collection<MockPhoneConfig> getConfigs(Context context, boolean marshall) {
        Log.i("XLua.XMockQueryApi", "Getting the Configs");
        return MockConfigConversions.configsFromCursor(
                GetMockConfigsCommand.invoke(context, marshall),
                marshall,
                true);
    }
}
