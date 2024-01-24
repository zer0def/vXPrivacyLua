package eu.faircode.xlua.api.xmock.xcall.packets;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

//import eu.faircode.xlua.XMockPropIO;
//import eu.faircode.xlua.api.data.ICallCommand;

/*public class MockPropsPacketBuilder implements ICallCommand {
    public String[] names;
    public String[] values;
    public String[] defaultValues;
    public boolean[] enabled;

    public MockPropsPacketBuilder() { }
    public MockPropsPacketBuilder(String[] names, String[] values, String[] defaultValues, boolean[] enabled) {
        this.names = names;
        this.values = values;
        this.defaultValues = defaultValues;
        this.enabled = enabled;
    }

    public List<MockPropPacket> toListPacket() {
        List<MockPropPacket> props = new ArrayList<>();
        for(int i = 0; i < names.length; i++) {
            MockPropPacket prop = new MockPropPacket();
            prop.name = names[i];
            prop.value = names[i];
            prop.defaultValue = defaultValues[i];
            prop.enabled = enabled[i];
            props.add(prop);
        }

        return props;
    }

    public void fromListPacket(List<MockPropPacket> props) {
        names = new String[props.size()];
        values = new String[props.size()];
        defaultValues = new String[props.size()];
        enabled = new boolean[props.size()];
        for(int i = 0; i < props.size(); i++) {
            MockPropPacket prop = props.get(i);
            names[i] = prop.name;
            values[i] = prop.value;
            defaultValues[i] = prop.defaultValue;
            enabled[i] = prop.enabled;
        }
    }

    public void fromList(List<XMockPropIO> props) {
        names = new String[props.size()];
        values = new String[props.size()];
        defaultValues = new String[props.size()];
        enabled = new boolean[props.size()];
        for(int i = 0; i < props.size(); i++) {
            XMockPropIO prop = props.get(i);
            names[i] = prop.getName();
            values[i] = prop.getMockValue();
            defaultValues[i] = prop.getDefaultValue();
            enabled[i] = prop.getIsEnabled();
        }
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(names != null) b.putStringArray("names", names);
        if(values != null) b.putStringArray("values", values);
        if(defaultValues != null) b.putStringArray("defaultValues", defaultValues);
        if(enabled != null) b.putBooleanArray("enabledValues", enabled);
        return b;
    }

    @Override
    public void fromBundle(Bundle b) {
        this.names = b.getStringArray("names");
        this.values = b.getStringArray("values");
        this.defaultValues = b.getStringArray("defaultValues");
        this.enabled = b.getBooleanArray("enabledValues");
    }

    public static class Convert {

    }
}*/
