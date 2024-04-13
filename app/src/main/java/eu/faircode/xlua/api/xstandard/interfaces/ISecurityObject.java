package eu.faircode.xlua.api.xstandard.interfaces;

import android.content.Context;

public interface ISecurityObject {
    boolean isRequiresSingleThread();
    boolean requiresCheck();
    void throwOnPermissionCheck(Context context);
}
