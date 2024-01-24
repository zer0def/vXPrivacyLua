package eu.faircode.xlua.api.xmock.xcall.packets;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;


//import eu.faircode.xlua.api.data.ICallCommand;

/*public class MockPropPacket implements ICallCommand, IDatabaseHelper {
    public String name;
    public String value;
    public String defaultValue;
    public Boolean enabled;

    public Boolean update;

    public MockPropPacket() { }
    public MockPropPacket(Bundle b)  { fromBundle(b); }
    public MockPropPacket(XMockProp prop) { fromObjectBase(prop); }
    public MockPropPacket(String name, String value, String defaultValue, Boolean enabled) {
        init(name, value, defaultValue, enabled, null);
    }

    public MockPropPacket(String name, String value, String defaultValue, Boolean enabled, Boolean update) {
        init(name, value, defaultValue, enabled, update);
    }

    private void init(String name, String value, String defaultValue, Boolean enabled, Boolean update) {
        setName(name);
        setValue(value);
        setDefaultValue(defaultValue);
        setEnabled(enabled);
        setUpdate(update);
    }

    public MockPropPacket setName(String name) {
        if(name != null) this.name = name;
        return this;
    }

    public MockPropPacket setValue(String value) {
        if(value != null) this.value = value;
        return this;
    }

    public MockPropPacket setDefaultValue(String value) {
        if(value != null) this.defaultValue = value;
        return this;
    }

    public MockPropPacket setEnabled(Boolean enabled) {
        if(enabled != null) this.enabled = enabled;
        return this;
    }

    public MockPropPacket setUpdate(Boolean update) {
        if(update != null) this.update = update;
        return this;
    }

    public XMockPropIO toObjectBase() { return new XMockPropIO(this.name, this.value, this.defaultValue, this.enabled); }
    public void fromObjectBase(XMockProp prop) {
        this.name = prop.getName();
        this.value = prop.getMockValue();
        this.defaultValue = prop.getDefaultValue();
        this.enabled = prop.getIsEnabled();
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        if(name != null) b.putString("name", name);
        if(value != null) b.putString("mockValue", value);
        if(defaultValue != null) b.putString("defaultValue", defaultValue);
        if(enabled != null) b.putBoolean("enabled", enabled);
        return  b;
    }

    @Override
    public void fromBundle(Bundle b) {
        this.name = b.getString("name");
        this.value = b.getString("mockValue");
        this.defaultValue = b.getString("defaultValue");
        this.enabled = b.getBoolean("enabled");
    }

    @Override
    public void readFromCursor(Cursor cursor) {

    }

    @Override
    public ContentValues createContentValues() {
        ContentValues cv = new ContentValues();
        if(name != null) cv.put("name", name);
        if(value != null) cv.put("mockValue", value);
        if(defaultValue != null) cv.put("defaultValue", defaultValue);
        if(enabled != null) cv.put("enabled", enabled);
        return cv;
    }

    public static class Convert {
        public static MockPropPacket fromObjectBase(XMockProp prop) {
            return new MockPropPacket(prop);
        }
    }
}*/
