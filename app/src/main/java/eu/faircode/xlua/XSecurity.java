package eu.faircode.xlua;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Process;
import android.provider.Settings;
import android.system.Os;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import eu.faircode.xlua.rootbox.xReflectUtils;

public class XSecurity {
    public static final String TAG = "XLua.XSecurity";





    public static void TestFunctions() {
        //            Class<?> fileUtils = Class.forName("android.os.FileUtils");
        String ioBridge = "libcore.io.IoBridge";
        String lCore = "libcore.io.Libcore";
        String fOs = "libcore.io.ForwardingOs";
        String lioUtils = "libcore.io.IoUtils";
        String bOs = "libcore.io.BlockGuardOs";
        String oss = "libcore.io.Os";
        String lin = "libcore.io.Linux";
        String oss2 = "android.system.Os";
        String fUtils = "android.os.FileUtils";

        String defSys = "java.io.DefaultSystem";

        if(classExistsAndFunction(defSys, null)) {
            if(classExistsAndFunction(defSys, "getFileSystem")) {
                try {
                    Log.i(TAG, "Getting [getFileSystem]");
                    Method dSys = xReflectUtils.getMethodFor(defSys, "getFileSystem");
                    Log.i(TAG, "Got method [getFileSystem]");

                    Log.i(TAG, "Invoking [getFileSystem]");
                    Object uFS = dSys.invoke(null);
                    Log.i(TAG, "Invoked [getFileSystem]");

                    Log.i(TAG, "Getting [setPermission]");
                    Method setPermsOne = uFS.getClass().getDeclaredMethod("setPermission", File.class, int.class, boolean.class, boolean.class);
                    Log.i(TAG, "Got [setPermission] form [" + uFS.getClass().getName() + "]:FileSystem");

                    Log.i(TAG, "Getting [setPermission0]");
                    Method setPermsTwo = uFS.getClass().getDeclaredMethod("setPermission0", File.class, int.class, boolean.class, boolean.class);
                    Log.i(TAG, "Got [setPermission0] from [" + uFS.getClass().getName() + "]:FileSystem");
                }catch (Exception e) {
                    Log.e(TAG, "Failed doing super cool direct file shit....");
                }
            }
        }else {
            Log.i(TAG, "Getting 'fs' field in 'java.io.File");
            Field fsField = xReflectUtils.getFieldFor(Field.class, "fs", true);
            if(fsField == null) {
                Log.e(TAG, "Failed to get 'fs' field for 'java.io.File'");
            }else {
                try {
                    Object vFs = fsField.get(null);
                    if(vFs == null) {
                        Log.e(TAG, "Failed to get 'fs' Field Value is null ");
                    }else {
                        try {
                            Log.i(TAG, "Getting [setPermission]");
                            Method setPermsOne = vFs.getClass().getDeclaredMethod("setPermission", File.class, int.class, boolean.class, boolean.class);
                            Log.i(TAG, "Got [setPermission] form [" + vFs.getClass().getName() + "]:FileSystem");

                            Log.i(TAG, "Getting [setPermission0]");
                            Method setPermsTwo = vFs.getClass().getDeclaredMethod("setPermission0", File.class, int.class, boolean.class, boolean.class);
                            Log.i(TAG, "Got [setPermission0] from [" + vFs.getClass().getName() + "]:FileSystem");
                        }catch (Exception ee) {
                            Log.e(TAG, "Failed to get [setPermission] function: " + ee);
                        }
                    }
                }catch (Exception e) {
                    Log.e(TAG, "Failed to get 'fs' Field Value: " + e);
                }
            }
        }


        classExistsAndFunction(ioBridge, null );
        if(classExistsAndFunction(fOs, null )) {
            classExistsAndFunction(fOs, "chown", String.class, int.class, int.class);
            classExistsAndFunction(fOs, "chmod", String.class, int.class);

            classExistsAndFunction(fOs, "fchown", FileDescriptor.class, int.class, int.class);
            classExistsAndFunction(fOs, "fchmod", FileDescriptor.class, int.class);
        }

        if(classExistsAndFunction(lioUtils, null )) {
            classExistsAndFunction(lioUtils, "setFdOwner", FileDescriptor.class, Object.class);
            classExistsAndFunction(lioUtils, "acquireRawFd", FileDescriptor.class);
        }

        if(classExistsAndFunction(bOs, null)) {
            classExistsAndFunction(bOs, "fchmod", FileDescriptor.class, int.class);
            classExistsAndFunction(bOs, "fchown", FileDescriptor.class, int.class, int.class);
            classExistsAndFunction(bOs, "rename", String.class, String.class);
            classExistsAndFunction(bOs, "lchown", String.class, int.class, int.class);
            classExistsAndFunction(bOs, "chmod", String.class, int.class);
            classExistsAndFunction(bOs, "chown", String.class, int.class, int.class);
        }

        if(classExistsAndFunction(lCore, null)) {
            classExistsAndFunction(lCore, "getOs");
        }

        if(classExistsAndFunction(oss, null)) {
            classExistsAndFunction(oss, "fchmod", FileDescriptor.class, int.class);
            classExistsAndFunction(oss, "fchown", FileDescriptor.class, int.class, int.class);
            classExistsAndFunction(oss, "chmod", String.class, int.class);
            classExistsAndFunction(oss, "chown", String.class, int.class, int.class);
        }

        if(classExistsAndFunction(lin, null)) {
            classExistsAndFunction(lin, "fchmod", FileDescriptor.class, int.class);
            classExistsAndFunction(lin, "fchown", FileDescriptor.class, int.class, int.class);
            classExistsAndFunction(lin, "chmod", String.class, int.class);
            classExistsAndFunction(lin, "chown", String.class, int.class, int.class);
        }

        if(classExistsAndFunction(oss2, null)) {
            classExistsAndFunction(oss2, "fchmod", FileDescriptor.class, int.class);
            classExistsAndFunction(oss2, "fchown", FileDescriptor.class, int.class, int.class);
            classExistsAndFunction(oss2, "chmod", String.class, int.class);
            classExistsAndFunction(oss2, "chown", String.class, int.class, int.class);
        }

        if(classExistsAndFunction(fUtils, null)) {
            https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/android/os/FileUtils.java;l=98?q=android.os.FileUti&sq=
            classExistsAndFunction(fUtils, "copyPermissions", File.class, File.class);
            classExistsAndFunction(fUtils, "copyFileOrThrow", File.class, File.class);
            classExistsAndFunction(fUtils, "copy", File.class, File.class);
            classExistsAndFunction(fUtils, "createDir", File.class);
            classExistsAndFunction(fUtils, "createDir", File.class, String.class);
            //    public static @Nullable File createDir(File baseDir, String name) {
            //copyFileOrThrow
        }


        //"name": "CPU/IoUtils.setFdOwner",
        //        "author": "OBC",
        //        "className": "libcore.io.IoUtils",
        //        "methodName": "setFdOwner",
    }


    public static boolean classExistsAndFunction(String path, String function, Class<?>... params) {
        try {
            Log.i(TAG, "Checking Class & Func => " + path + "  => " + function);

            Class<?> cTarget = Class.forName(path);

            Log.i(TAG, "Class exists: " + path);

            if(function != null) {
                try {
                    Method mTarget = cTarget.getMethod(function, params);
                    Log.i(TAG, "Seems like Method Exists: " + function);

                    //Class<?> fileUtils = Class.forName("android.os.FileUtils");
                    //Method setPermissions = fileUtils
                    //        .getMethod("setPermissions", String.class, int.class, int.class, int.class);
                    Log.i("XLua.MethodClassTest", "CLASS EXISTS: " + path + "  WITH METHOD: " + function);
                }catch (Exception ie) {
                    Method m = cTarget.getDeclaredMethod(function, params);
                    Log.i("XLua.MethodClassTest", "CLASS EXISTS: " + path + "  WITH INSTANCE METHOD: " + function);
                }

            }else {
                Log.i("XLua.MethodClassTest", "CLASS EXISTS: " + path);
            }

            return true;
        }catch (Exception e) {
            Log.e(TAG, "Class does not exist or something... " + path + " => " + function + "\n" + e);
            return false;
        }
    }


    public static void setPerms(File directoryOrFile) {
        setPerms(directoryOrFile, 0770);
    }

    public static void setPerms(File directoryOrFile, int mode) {
        Log.i(TAG, "Setting File Permissions (" + mode + ") SYSTEM_UID For XLUA Directory for UID: " + Process.SYSTEM_UID);

        // Set database file permissions
        // Owner: rwx (system)
        // Group: rwx (system)
        // World: ---
        //Process.myUid()
        XUtil.setPermissions(directoryOrFile.getAbsolutePath(), mode, Process.SYSTEM_UID, Process.SYSTEM_UID);
        File[] files = directoryOrFile.listFiles();
        if (files != null)
            for (File file : files)
                XUtil.setPermissions(file.getAbsolutePath(), mode, Process.SYSTEM_UID, Process.SYSTEM_UID);

        Log.i(TAG, "Finished setting permissions for: " + directoryOrFile.getPath());
    }


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
