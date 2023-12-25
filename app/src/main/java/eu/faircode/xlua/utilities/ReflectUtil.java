package eu.faircode.xlua.utilities;

import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
    private static final String TAG = "XLua.ReflectUtil";

    /**
     * Creates a Array using Reflection
     *
     * @param className Full Class Path + Name of Array Element Type
     * @param size The Size of the Array
     * @return A Object of the created Array
     */
    public static Object createArray(String className, int size) { return createArray(getClassType(className), size); }

    /**
     * Creates a Array using Reflection
     *
     * @param classType Class Type of the Array Element
     * @param size The Size of the Array
     * @return A Object of the created Array
     */
    public static Object createArray(Class<?> classType, int size) {
        try {
            if(classType == null)
                return null;

            //Integer.TYPE;
            return Array.newInstance(classType, size);
        }catch (Exception e) {
            Log.e(TAG, "Failed to Create Reflect Array! \n" + e + "\n" + Log.getStackTraceString(e));
            return null;
        }
    }

    /**
     * Dynamically Get Class Type
     *
     * @param className Full Class Path + Name of Array Element Type
     * @return Returns a Class Type used for Reflection
     */
    public static Class<?> getClassType(String className) {
        try {
            return Class.forName(className);
        }catch (Exception e) {
            Log.e(TAG, "Failed to Grab Class Type! \n" + e + "\n" + Log.getStackTraceString(e));
            return null;
        }
    }

    /**
     * Dynamically Check if a Java Method Exists
     * Works on Static, Private, Public, Instance
     *
     * @param className Full Class Path + Name of Array Element Type
     * @param methodName Method to Check if Exists
     * @return Returns True if Method Exists, False if Not
     */
    public static boolean javaMethodExists(String className, String methodName) {
        try {
            // Load the class
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods)
                if (method.getName().equals(methodName))
                    return true; // Method found
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Class not found: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "An error occurred: " + e.getMessage());
        }
        return false; // Method not found
    }
}
