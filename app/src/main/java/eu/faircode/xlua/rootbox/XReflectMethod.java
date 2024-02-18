package eu.faircode.xlua.rootbox;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class XReflectMethod {
    private static final String TAG = "XLua.rootbox.xReflectMethod";
    private boolean exists = false;

    private final Method method;
    private final String classPath;
    private final String methodName;

    private boolean isStatic;
    private boolean isNative;
    private boolean isInstance;

    public XReflectMethod(Class<?> clzz, String methodName) {
        this.classPath = clzz.getName();
        this.methodName = methodName;

        this.method = XReflectUtils.getMethodFor(clzz, methodName);
        if(this.method == null) {
            Log.e(TAG, "Failed to get Method: " + methodName + " => " + classPath);
            return;
        }

        this.exists = true;
    }

    public XReflectMethod(String classPath, String methodName) {
        this.classPath = classPath;
        this.methodName = methodName;

        this.method = XReflectUtils.getMethodFor(classPath, methodName);
        if(this.method == null) {
            Log.e(TAG, "Failed to get Method: " + methodName + " => " + classPath);
            return;
        }

        this.exists = true;
    }

    public XReflectMethod(String classPath, String methodName, Class<?>... params) {
        //check caller ?
        this.classPath = classPath;
        this.methodName = methodName;

        this.method = XReflectUtils.getMethodFor(classPath, methodName, params);

        if(this.method == null) {
            Log.e(TAG, "Failed to get Method: " + methodName + " => " + classPath);
            return;
        }

        this.exists = true;
    }

    private void Init() {
        if(method == null)
            return;

        int mods = method.getModifiers();
        isStatic = Modifier.isStatic(mods);
        isInstance = !isStatic;
        isNative = Modifier.isNative(mods);
    }

    public boolean exists() { return exists; }
    public boolean isStatic() { return isStatic; }
    public boolean isInstance() { return isInstance; }
    public boolean isNative() { return isNative; }

    public Object instanceInvoke(Object instance, Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, args);
    }

    public Object tryInstanceInvoke(Object instance, Object... args) {
        try {
            return instanceInvoke(instance, args);
        }catch (Exception e) {
            Log.e(TAG, "Failed to instance invoke: " + methodName + " => " + classPath);
            return null;
        }
    }

    public Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(null, args);
    }

    public Object tryInvoke(Object... args) {
        try {
             return invoke(args);
        }catch (Exception e) {
            Log.e(TAG, "Failed to invoke: " + methodName + " => " + classPath);
            return null;
        }
    }
}
