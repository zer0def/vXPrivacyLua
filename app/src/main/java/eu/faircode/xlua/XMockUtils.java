package eu.faircode.xlua;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class XMockUtils {
    private static final String TAG = "XLua.XMockUtils";
    public static final String NOT_BLACKLISTED = "NotBlacklisted";

    public static boolean isPropVxpOrLua(String propName) {
        return propName.equals("exp") || propName.equals("vxp") || propName.equals(".lua");
    }

    public static String filterProperty(String propertyName, List<XMockPropIO> props) {
        for(XMockPropIO prop : props) {
            //if(prop.name.equals(propertyName) && prop.enabled) {
            if(propertyName.contains(prop.name) && prop.enabled) {
                Log.i(TAG, "Mocking Property::" + propertyName);
                return prop.mockValue;
            }
        }

        return  NOT_BLACKLISTED;
    }

    public static Process createMockProcess(String[] commands) throws  Exception {
        return  createMockProcess(TextUtils.join(" ", commands));
    }

    public static Process createMockProcess(String command) throws Exception {
        Log.i(TAG, "[Uber] Mocking Command: " + command);

        /*String low = command.toLowerCase();
        for(Map.Entry<String, String> entry : XBlackLists.commands.entrySet()) {
            if(low.contains(entry.getKey())) {
                Log.i(TAG, "[Uber] Found Blacklisted Command: " + entry.getKey());
                return Runtime.getRuntime().exec(new String[] { "sh", "-c", entry.getValue()});
            }
        }*/

        //return Runtime.getRuntime().exec(command);
        return null;
    }
}
