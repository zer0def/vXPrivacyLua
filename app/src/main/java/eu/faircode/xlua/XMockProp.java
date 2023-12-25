package eu.faircode.xlua;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMockProp {
    private final static String TAG = "XLua.XMockProp";
    String name;
    String mockValue;
    String defaultValue;
    boolean enabled;

    public String getName() { return name; }
    public String getMockValue() { return mockValue; }
    public String getDefaultValue() { return defaultValue; }
    public boolean getIsEnabled() { return enabled; }

    public XMockProp(String name, String defaultValue) {
        this.name = name;
        this.mockValue = defaultValue;
        this.defaultValue = defaultValue;
        this.enabled = true;//default
    }

    public XMockProp(String name, String mockValue, String defaultValue) {
        this.name = name;
        this.mockValue = mockValue;
        this.defaultValue = defaultValue;
        this.enabled = true;//default
    }

    public XMockProp(String name, String mockValue, String defaultValue, boolean enabled) {
        this.name = name;
        this.mockValue = mockValue;
        this.defaultValue = defaultValue;
        this.enabled = enabled;//default
    }

    protected XMockProp() { }
    protected XMockProp(Parcel in) {
        this.name = in.readString();
        this.mockValue = in.readString();
        this.defaultValue = in.readString();
        this.enabled =  (in.readByte() != 0);
    }

    @Override
    public int hashCode() { return this.getName().hashCode(); }

    @Override
    public String toString() { return this.getName() + "::" + this.mockValue + "::" + this.defaultValue + "::" + this.enabled; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XMockProp))
            return false;
        XMockProp other = (XMockProp) obj;
        return this.getName().equals(other.getName());
    }

    static class Table {
        public static final String name = "props";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("name", "TEXT");
            put("mockValue", "TEXT");
            put("defaultValue", "TEXT");
            put("enabled", "BOOLEAN");//change this to INTEGER ?
        }};
    }
}
