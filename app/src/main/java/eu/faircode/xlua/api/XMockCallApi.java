package eu.faircode.xlua.api;

import android.content.Context;
import android.os.Bundle;

import java.util.Collection;

import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpuConversions;
import eu.faircode.xlua.api.objects.xmock.packets.MockPropPacket;
import eu.faircode.xlua.api.objects.xmock.prop.MockProp;
import eu.faircode.xlua.api.objects.xmock.prop.MockPropConversions;
import eu.faircode.xlua.api.xmock.xcall.GetMockCpuCommand;
import eu.faircode.xlua.api.xmock.xcall.GetMockCpusCommand;
import eu.faircode.xlua.api.xmock.xcall.GetMockPropCommand;
import eu.faircode.xlua.api.xmock.xcall.GetMockPropsCommand;
import eu.faircode.xlua.api.xmock.xcall.PutMockCpuCommand;
import eu.faircode.xlua.api.xmock.xcall.PutMockPropCommand;
import eu.faircode.xlua.api.xmock.xcall.PutMockPropsCommand;
import eu.faircode.xlua.utilities.BundleUtil;

public class XMockCallApi {
    public static Collection<MockProp> getMockProps(Context context) {
        return MockPropConversions.fromBundleArray(
                GetMockPropsCommand.invoke(context));
    }

    public static MockProp getMockProp(Context context, String propName) {
        if(propName == null) return null;
        return MockPropConversions.fromBundle(
                GetMockPropCommand.invoke(
                        context,
                        propName));
    }

    public static Collection<MockCpu> getCpuMaps(Context context) {
        return MockCpuConversions.fromBundleArray(
                GetMockCpusCommand.invoke(context));
    }

    public static MockCpu getSelectedMockCpu(Context context) {
        return MockCpuConversions.fromBundle(
                GetMockCpuCommand.invoke(context));
    }

    public static boolean putMockCpu(Context context, MockCpu mockCpu) {
        return BundleUtil.readResultStatus(
                PutMockCpuCommand.invoke(context, mockCpu));
    }

    public static boolean updateProp(Context context, String name, String value) { return BundleUtil.readResultStatus(PutMockPropCommand.invokeUpdate(context, name, value)); }
    public static boolean updateProp(Context context, String name, String value, boolean enabled) { return BundleUtil.readResultStatus(PutMockPropCommand.invokeUpdate(context, name, value, enabled)); }

    public static boolean putMockProp(Context context, MockPropPacket packet) { return BundleUtil.readResultStatus(PutMockPropCommand.invoke(context, packet)); }
    public static boolean putMockProp(
            Context context,
            String name,
            String value,
            String defaultValue,
            boolean enabled) {
        return BundleUtil.readResultStatus(PutMockPropCommand.invokeInsert(context, name, value, defaultValue, enabled));
    }

    public static boolean putMockProps(Context context, Collection<MockProp> props) {
        return BundleUtil.readResultStatus(PutMockPropsCommand.invoke(
                context,
                props));
    }
}
