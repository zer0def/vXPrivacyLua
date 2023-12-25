package eu.faircode.xlua.cpu;

import android.content.Context;
import android.os.Parcel;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.json.JsonHelper;

public class XMockCpu {
    private static final String TAG = "XLua.XMockCpu";

    public String name;
    public String model;
    public String manufacturer;
    public String contents;
    public boolean selected;

    public XMockCpu() { }
    public XMockCpu(String name, String model, String manufacturer, String contents) {
        this.name = name;
        this.model = model;
        this.manufacturer = manufacturer;
        this.contents = contents;
        this.selected = false;
    }

    public XMockCpu(String name, String model, String manufacturer, String contents, boolean selected) {
        this.name = name;
        this.model = model;
        this.manufacturer = manufacturer;
        this.contents = contents;
        this.selected = selected;
    }

    protected XMockCpu(Parcel in) {
        this.name = in.readString();
        this.model = in.readString();
        this.manufacturer = in.readString();
        this.contents = in.readString();
        this.selected =  (in.readByte() != 0);
    }

    @Override
    public int hashCode() { return this.name.hashCode(); }

    @Override
    public String toString() { return this.name + "@" + this.manufacturer + ":" + this.model + ":" + this.selected; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XMockCpu))
            return false;
        XMockCpu other = (XMockCpu) obj;
        return this.name.equals(other.name) || this.model.equals(other.model);
    }

    public static List<XMockCpuIO> readCpuMaps(Context context) throws IOException, JSONException {
        return JsonHelper.findJsonElementsFromAssets(XUtil.getApk(context), "cpumaps.json", true, XMockCpuIO.class);
    }

    static class Table {
        public static final String name = "cpumaps";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT");
            put("model", "TEXT");
            put("manufacturer", "TEXT");
            put("contents", "TEXT");
            put("selected", "BOOLEAN");
        }};
    }
}
