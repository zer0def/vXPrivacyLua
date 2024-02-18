package eu.faircode.xlua.utilities;

import android.text.TextUtils;
import android.util.Log;

import java.util.Collection;


public class MockUtils {
    private static final String TAG = "XLua.XMockUtils";
    public static final String NOT_BLACKLISTED = "NotBlacklisted";
    public static final String HIDE_PROPERTY = "<<*hide*>>";

    public static boolean isPropVxpOrLua(String propName) {
        boolean r = propName.equalsIgnoreCase("exp") ||
                        propName.equalsIgnoreCase("vxp") ||
                        propName.equalsIgnoreCase(".lua");
        if(!r)
            Log.w(TAG, "Property [" + propName + "] is a Whitelisted Prop to avoid Overflow/Recursion");

        return r;
    }
}
