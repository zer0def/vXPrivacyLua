package eu.faircode.xlua.api.objects.xmock.phone;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.utilities.CursorUtil;

public class MockPhoneConversions {
    private static final String TAG = "XLua.MockPhoneConversions";

    public static Map<String, String> convertJsonToMap(String jsonData) {
        Map<String, String> settings = new HashMap<>();
        try {
            JSONArray jArray = new JSONArray(jsonData);
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                if(jObject == null)
                    continue;

                String name = jObject.getString("name");
                String value = jObject.getString("value");
                settings.put(name, value);
            }

            Log.i(TAG, "phone config settings size=" + settings.size());
            return settings;
        }catch (Exception e) {
            Log.e(TAG, "Failed to read Config Settings! " + e);
            return settings;
        }
    }

    public static JSONArray convertMapToJson(Map<String, String> settings) {
        JSONArray jArray = new JSONArray();

        for(Map.Entry<String, String> r : settings.entrySet()) {
            JSONObject jObject = new JSONObject();
            try {
                jObject.put("name", r.getKey());
                jObject.put("value", r.getValue());
            }catch (Exception e) {
                Log.e(TAG, "Failed to write JSON element: name=" + r.getKey() + " value=" + r.getValue() + "\n" + e);
            }
        }
        return jArray;
    }

    public static void writeSettingsToBundle(Bundle b, Map<String, String> settings) {
        String[] names = new String[settings.size()];
        String[] values = new String[settings.size()];

        int i = 0;
        for(Map.Entry<String, String> r : settings.entrySet()) {
            names[i] = r.getKey();
            values[i] = r.getValue();
            i++;
        }

        b.putStringArray("names", names);
        b.putStringArray("values", values);
    }

    public static Map<String, String> readSettingsFromBundle(Bundle b) {
        Map<String, String> settings = new HashMap<>();
        String[] names = b.getStringArray("names");
        String[] values = b.getStringArray("values");
        if(names == null || values == null)
            return settings;

        for(int i = 0; i < names.length; i++)
            settings.put(names[i], values[i]);

        return settings;
    }

    public static Collection<MockPhone> phoneConfigsFromCursor(Cursor cursor, boolean marshall, boolean close) {
        Collection<MockPhone> ps = new ArrayList<>();
        try {
            if(marshall) {
                while (cursor != null && cursor.moveToNext()) {
                    byte[] marshaled = cursor.getBlob(0);
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(marshaled, 0, marshaled.length);
                    parcel.setDataPosition(0);
                    MockPhone config = MockPhone.CREATOR.createFromParcel(parcel);
                    parcel.recycle();
                    ps.add(config);
                }
            }else {

            }
        }finally {
            if(close) CursorUtil.closeCursor(cursor);
        }

        return ps;
    }

    public static Collection<MockCarrier> phoneCarriersFromCursor(Cursor cursor, boolean marshall, boolean close) {
        Collection<MockCarrier> ps = new ArrayList<>();
        try {
            if(marshall) {
                while (cursor != null && cursor.moveToNext()) {
                    byte[] marshaled = cursor.getBlob(0);
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(marshaled, 0, marshaled.length);
                    parcel.setDataPosition(0);
                    MockCarrier config = MockCarrier.CREATOR.createFromParcel(parcel);
                    parcel.recycle();
                    ps.add(config);
                }
            }else {

            }
        }finally {
            if(close) CursorUtil.closeCursor(cursor);
        }

        return ps;
    }

    public static Collection<MockUniqueId> phoneUniqueIdsFromCursor(Cursor cursor, boolean marshall, boolean close) {
        Collection<MockUniqueId> ps = new ArrayList<>();
        try {
            if(marshall) {
                while (cursor != null && cursor.moveToNext()) {
                    byte[] marshaled = cursor.getBlob(0);
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(marshaled, 0, marshaled.length);
                    parcel.setDataPosition(0);
                    MockUniqueId config = MockUniqueId.CREATOR.createFromParcel(parcel);
                    parcel.recycle();
                    ps.add(config);
                }
            }else {

            }
        }finally {
            if(close) CursorUtil.closeCursor(cursor);
        }

        return ps;
    }
}
