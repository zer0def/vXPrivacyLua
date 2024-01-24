package eu.faircode.xlua;

import android.os.StrictMode;

public class XPolicy {
    private StrictMode.ThreadPolicy originalPolicy;

    public XPolicy() {
        originalPolicy = StrictMode.getThreadPolicy();
    }

    public void allowRW() {
        StrictMode.allowThreadDiskReads();
        StrictMode.allowThreadDiskWrites();
    }

    public void revert() {
        StrictMode.setThreadPolicy(originalPolicy);
    }

    public static XPolicy policyAllowRW() {
        XPolicy policy = new XPolicy();
        policy.allowRW();
        return policy;
    }
}
