package eu.faircode.xlua;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import eu.faircode.xlua.utilities.ReflectUtil;

/*public class XMonitorApp {
    private static final String TAG = "XLua.XMonitorApp";

    public String key;
    public XApp app;

    //private HashMap<String, String[]> _classes
    public List<Class<?>> gpuClassess = new ArrayList<>();

    public void hookGpuTwo(final XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            XposedBridge.hookAllMethods(GL10.class, "glGetString", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(TAG, "THIS IS A GL10 INVOKE FOR GLGETSTRING V2");
                    //super.afterHookedMethod(param);
                }
            });

            Log.i(TAG, "HOOKED GPU 1");
        }catch (Exception ex) {
            Log.e(TAG, "Failed to Hook V2: " + ex);
        }
    }

    public void hookGpuOne(final  XC_LoadPackage.LoadPackageParam lpparam) {
        key = lpparam.packageName;//for now

        try {
            XposedHelpers.findAndHookMethod(GL10.class, "glGetString", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(TAG, "THIS IS A GL10 INVOKE FOR GLGETSTRING");
                    //super.afterHookedMethod(param);
                }
            });

            Log.i(TAG, "HOOKED GPU 1");
        }catch (Exception ex) {
            Log.e(TAG, "Failed to Hook: " + ex);
        }

        //XposedHelpers.
        //Class<?> at = Class.forName(tiramisu ? "android.app.Instrumentation" : "android.app.LoadedApk", false, lpparam.classLoader);

        XposedBridge.hookAllMethods(ClassLoader.class, "loadClass", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object res = param.getResult();
                if(res != null) {
                    Class<?> clzz = (Class<?>)res;
                    if(ReflectUtil.extendsGpuClass(clzz)) {
                        gpuClassess.add(clzz);
                        Log.i(TAG, "Found GPU Class! " + clzz.getName());
                    }
                }

                //super.afterHookedMethod(param);
            }
        });
    }
}*/
