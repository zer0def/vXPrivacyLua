package eu.faircode.xlua;

import android.os.Bundle;
import android.os.Parcel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*public class XSetting {
    //private static Map<Integer, XCategory> settings = new HashMap<>();

    public int userId;
    public String category;
    public String name;

    public String value = null;
    public Boolean kill = null;

    public XSetting(int userId, String category, String name) {
        this.userId = userId;
        this.category = category;
        this.name = name;
    }

    public XSetting(int userId, String category, String name, String value) {
        this.userId = userId;
        this.category = category;
        this.name = name;
        this.value = value;
    }

    protected XSetting() { }
    protected XSetting(Parcel in) {
        this.userId = in.readInt();
        this.name = in.readString();
        this.category = in.readString();
        this.value = in.readString();
    }

    public static class Table {
        public static final String name = "setting";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("user", "INTEGER");
            put("category", "TEXT");
            put("name", "TEXT");
            put("value", "TEXT");
        }};
    }
}*/
