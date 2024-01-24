package eu.faircode.xlua.utilities;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.faircode.xlua.api.objects.ISerial;

public class BundleUtil {
    public static Boolean readBoolean(Bundle b, String keyName) { return readBoolean(b, keyName, null); }
    public static Boolean readBoolean(Bundle b, String keyName,  Boolean defaultValue) {
        if(!b.containsKey(keyName))
            return defaultValue;

        return b.getBoolean(keyName);
    }

    public static <T extends ISerial> Bundle createFromISerial(T obj, boolean returnFalseIfNull) {
        if(obj == null)
            return returnFalseIfNull ? createResultStatus(false) : new Bundle();

        return obj.toBundle();
    }

    public static Bundle createFromStringArray(String keyName, List<String> lst) { return createFromStringArray(keyName, lst.toArray(new String[0])); }
    public static Bundle createFromStringArray(String keyName, String[] arr) {
        Bundle b = new Bundle();
        b.putStringArray(keyName, arr);
        return b;
    }

    public static Bundle createSingleLong(String keyName, long value) {
        Bundle b = new Bundle();
        b.putLong(keyName, value);
        return b;
    }

    public static Bundle createSingleInt(String keyName, int value) {
        Bundle b = new Bundle();
        b.putInt(keyName, value);
        return b;
    }

    public static Bundle createSingleString(String keyName, String value) { return createSingleString(keyName, value, true); }
    public static Bundle createSingleString(String keyName, String value, boolean ignoreNullValue) {
        Bundle b = new Bundle();
        if(!ignoreNullValue && value == null) b.putString(keyName, "null");
        else b.putString(keyName, value);
        return b;
    }

    public static String[] readStringArray(Bundle bundle, String keyName) {
        if(bundle == null || !bundle.containsKey(keyName))
            return new String[] { };

        return bundle.getStringArray(keyName);
    }


    public static List<String> readStringList(Bundle bundle, String keyName) { return readStringList(bundle, keyName, false); }
    public static List<String> readStringList(Bundle bundle, String keyName, boolean fromArray) {
        List<String> elements = new ArrayList<>();
        if(bundle == null || !bundle.containsKey(keyName))
            return elements;

        if(fromArray) {
            String[] arr = bundle.getStringArray(keyName);
            if(arr != null) Collections.addAll(elements, arr);
        }else {
            List<String> copy = bundle.getStringArrayList(keyName);
            if(copy != null) return copy;
        }

        return elements;
    }

    public static boolean readResultStatus(Bundle bundle) {
        if(bundle == null) return false;
        return bundle.getBoolean("result", false);
    }

    public static String readString(Bundle bundle, String keyName) { return readString(bundle, keyName, null); }
    public static String readString(Bundle bundle, String keyName, String defaultValue) {
        if(bundle == null) return defaultValue;
        if(!bundle.containsKey(keyName)) return defaultValue;
        return bundle.getString(keyName, defaultValue);
    }

    public static int readInt(Bundle bundle, String keyName) { return readInt(bundle, keyName, -5); }
    public static int readInt(Bundle bundle, String keyName, int defaultValue) {
        if(bundle == null) return defaultValue;
        return bundle.getInt(keyName, defaultValue);
    }

    public static Bundle createResultStatus(boolean result) {
        Bundle b = new Bundle();
        b.putInt("result", result ? 0 : -1);
        return b;
    }
}
