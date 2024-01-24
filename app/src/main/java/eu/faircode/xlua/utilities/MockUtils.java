package eu.faircode.xlua.utilities;

import android.text.TextUtils;
import android.util.Log;

import java.util.Collection;

import eu.faircode.xlua.api.objects.xmock.prop.MockProp;

public class MockUtils {
    private static final String TAG = "XLua.XMockUtils";
    public static final String NOT_BLACKLISTED = "NotBlacklisted";

    public static boolean isPropVxpOrLua(String propName) {
        return propName.equals("exp") || propName.equals("vxp") || propName.equals(".lua");
    }

    public static String filterProperty(String propertyName, Collection<MockProp> props) {
        for(MockProp prop : props) {
            //if(prop.name.equals(propertyName) && prop.enabled) {
            if(propertyName.contains(prop.getName()) && prop.isEnabled()) {
                Log.i(TAG, "Mocking Property::" + propertyName);
                return prop.getValue();
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
