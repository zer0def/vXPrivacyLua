package eu.faircode.xlua.rootbox;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SuperTestSite {
    private static final String TAG = "XLua.SuperTestSite";

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
                    Method dSys = XReflectUtils.getMethodFor(defSys, "getFileSystem");
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
            Field fsField = XReflectUtils.getFieldFor(Field.class, "fs", true);
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

}
