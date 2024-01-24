package eu.faircode.xlua;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;

public class XSecurity {
    public static final String TAG = "XLua.XSecurity";

    public static Uri getURI() {
        if (XposedUtil.isVirtualXposed())
            return Uri.parse("content://eu.faircode.xlua.vxp/");
        else
            return Settings.System.CONTENT_URI;
    }

    public static void checkCaller(Context context) throws SecurityException {
        Log.i(TAG, "Checking Caller");
        //This will check caller to make sure its either 'SYSTEM' , Current App or 'PRO COMPONENT'
        //Does not Modify Anything, just checks caller makes sure its not an imposter
        int current_id = XUtil.getAppId(Binder.getCallingUid());
        Log.i(TAG, "Caller=" + current_id);

        //Old Identity obj
        long oIdentity = Binder.clearCallingIdentity();

        try {
            if(current_id == Process.SYSTEM_UID) {
                Log.i(TAG, "Caller has SYSTEMUID");
                return;
            }

            PackageManager pm = context.getPackageManager();
            int uid = pm.getApplicationInfo(BuildConfig.APPLICATION_ID, 0).uid;
            if (pm.checkSignatures(current_id, uid) == PackageManager.SIGNATURE_MATCH) {
                Log.i(TAG, "Caller is Main Application");
                return;
            }

            String[] cPackage = pm.getPackagesForUid(current_id);
            if(cPackage.length > 0) {
                String name = cPackage[0];
                String print = XUtil.getSha1FingerprintString(context, name);

                Log.i(TAG, "Caller Requesting Fingerprint=" + print + "=[" + name + "]");

                Resources resources = pm.getResourcesForApplication(BuildConfig.APPLICATION_ID);
                String allow = resources.getString(R.string.pro_fingerprint);

                Log.i(TAG, "allowed=" + allow);

                if (allow.equals(print)) {
                    Log.i(TAG, "Is allowed , welcome ObbedCode");
                    return;
                }

                if(print.equals("1062ae961d78854f6c9cd872c4388232849c799")) {
                    Log.i(TAG, "Is allowed original");
                    return;
                }

                //if(allow.equals("ObbedCode")) {
                //    Log.i(TAG, "Temp Debug Symbol. Welcome ObbedCode");
                //    return;
                //}

                //if(print.equals(allow)){
                //    Log.i(TAG, "Caller Signature Verified! " + allow);
                //    return;
                //}
            }

            Log.e(TAG, "Signature error cuid=" + current_id);
            throw new SecurityException("Signature error cuid=" + current_id);
        } catch (Throwable ex) {
            Log.e(TAG, "Call Error: " + ex.getMessage());
            throw new SecurityException(ex);
        } finally {
            //If all Checks pass then restore the caller
            Binder.restoreCallingIdentity(oIdentity);
        }
    }
}
