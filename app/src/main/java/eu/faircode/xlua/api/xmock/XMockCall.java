package eu.faircode.xlua.api.xmock;

import android.content.Context;

import java.util.Collection;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.configs.MockConfigPacket;
import eu.faircode.xlua.api.cpu.MockCpu;
import eu.faircode.xlua.api.cpu.MockCpuConversions;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.xmock.call.GetMockCpuCommand;
import eu.faircode.xlua.api.xmock.call.GetMockCpusCommand;
import eu.faircode.xlua.api.xmock.call.PutMockConfigCommand;
import eu.faircode.xlua.api.xmock.call.PutMockCpuCommand;
import eu.faircode.xlua.api.xmock.call.PutMockPropCommand;

public class XMockCall {
    private static final String TAG = "XLua.XMockCall";

    /*public static String getPropertyValue(Context context, String propertyName) { return getPropertyValue(context, propertyName, "Global"); }
    public static String getPropertyValue(Context context, String propertyName, String packageName) { return getPropertyValue(context, propertyName, packageName, 0); }
    public static String getPropertyValue(Context context, String propertyName, String packageName, int uid) {
        XMockProp packet = new XMockProp(propertyName, uid, packageName);
        Bundle bundle = GetMockPropValueCommand.invoke(context, packet);
        if(bundle == null) {
            if(DebugUtil.isDebug())
                Log.e(TAG, "Bundle from [getPropertyValue] is null: " + packet);

            return MockUtils.NOT_BLACKLISTED;
        }

        return new XMockProp(bundle).getValue();
    }*/

    public static XResult putMockConfig(Context context, MockConfigPacket packet) { return XResult.from(PutMockConfigCommand.invoke(context, packet)); }
    public static XResult putMockProp(Context context, MockPropPacket packet) { return XResult.from(PutMockPropCommand.invoke(context, packet)); }

    public static Collection<MockCpu> getCpuMaps(Context context) { return MockCpuConversions.fromBundleArray(GetMockCpusCommand.invoke(context)); }
    public static MockCpu getSelectedMockCpu(Context context) { return MockCpuConversions.fromBundle(GetMockCpuCommand.invoke(context)); }
    public static XResult putMockCpu(Context context, MockCpu mockCpu) { return XResult.from(PutMockCpuCommand.invoke(context, mockCpu)); }
}
