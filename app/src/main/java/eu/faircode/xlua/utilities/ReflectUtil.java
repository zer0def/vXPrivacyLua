package eu.faircode.xlua.utilities;

import android.content.pm.PackageManager;
import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES31;
import android.opengl.GLES32;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class ReflectUtil {
    private static final String TAG = "XLua.ReflectUtil";
    private static final String GL_PATTEN = ".*\\.GL\\d{2}$";
    private static final String GLES_PATTERN = ".*\\.GLES\\d{2}$";


    public static String logStack() {
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement e : new Exception().getStackTrace()) {
            sb.append(e.getClassName());
            sb.append("::");
            sb.append(e.getMethodName());
            sb.append("\n");
        }

        return sb.toString();
    }

    public static boolean extendsGpuClass(Class<?> clazz) {
        Class<?> superClass = clazz.getSuperclass();
        if(superClass == null)
            return false;

        String name = superClass.getName();
        if(!name.startsWith("android.opengl."))
            return false;


        Log.i(TAG, "Is Opengl Class: " + name);

        return name.endsWith("GL") ||
                name.endsWith("GLES") ||
                name.matches(GL_PATTEN) ||
                name.matches(GLES_PATTERN);

        /*return clazz.isAssignableFrom(GL10.class) ||
                clazz.isAssignableFrom(GL11.class) ||
                clazz.isAssignableFrom(GLES10.class) ||
                clazz.isAssignableFrom(GLES11.class) ||
                clazz.isAssignableFrom(GLES20.class) ||
                clazz.isAssignableFrom(GLES30.class) ||
                clazz.isAssignableFrom(GLES31.class) ||
                clazz.isAssignableFrom(GLES32.class) ||
                clazz.isAssignableFrom(GLES11.class);*/
    }

    public static Class<?> resolveClass(String name, ClassLoader loader) throws ClassNotFoundException {
        if ("boolean".equals(name))
            return boolean.class;
        else if ("byte".equals(name))
            return byte.class;
        else if ("char".equals(name))
            return char.class;
        else if ("short".equals(name))
            return short.class;
        else if ("int".equals(name))
            return int.class;
        else if ("long".equals(name))
            return long.class;
        else if ("float".equals(name))
            return float.class;
        else if ("double".equals(name))
            return double.class;

        else if ("boolean[]".equals(name))
            return boolean[].class;
        else if ("byte[]".equals(name))
            return byte[].class;
        else if ("char[]".equals(name))
            return char[].class;
        else if ("short[]".equals(name))
            return short[].class;
        else if ("int[]".equals(name))
            return int[].class;
        else if ("long[]".equals(name))
            return long[].class;
        else if ("float[]".equals(name))
            return float[].class;
        else if ("double[]".equals(name))
            return double[].class;

        else if ("void".equals(name))
            return Void.TYPE;

        else
            return Class.forName(name, false, loader);
    }

    public static Field resolveField(Class<?> cls, String name, Class<?> type) throws NoSuchFieldException {
        try {
            Class<?> c = cls;
            while (c != null && !c.equals(Object.class))
                try {
                    Field field = c.getDeclaredField(name);
                    if (!field.getType().equals(type))
                        throw new NoSuchFieldException();
                    return field;
                } catch (NoSuchFieldException ex) {
                    for (Field field : c.getDeclaredFields()) {
                        if (!name.equals(field.getName()))
                            continue;

                        if (!field.getType().equals(type))
                            continue;

                        Log.i(TAG, "Resolved field=" + field);
                        return field;
                    }
                }
            throw new NoSuchFieldException(name);
        } catch (NoSuchFieldException ex) {
            Class<?> c = cls;
            while (c != null && !c.equals(Object.class)) {
                Log.i(TAG, c.toString());
                for (Method method : c.getDeclaredMethods())
                    Log.i(TAG, "- " + method.toString());
                c = c.getSuperclass();
            }
            throw ex;
        }
    }

    public static Member resolveMember(Class<?> cls, String name, Class<?>[] params) throws NoSuchMethodException {
        boolean exists = false;
        try {
            Class<?> c = cls;
            while (c != null && !c.equals(Object.class))
                try {
                    if (name == null)
                        return c.getDeclaredConstructor(params);
                    else
                        return c.getDeclaredMethod(name, params);
                } catch (NoSuchMethodException ex) {
                    for (Member member : name == null ? c.getDeclaredConstructors() : c.getDeclaredMethods()) {
                        if (name != null && !name.equals(member.getName()))
                            continue;

                        exists = true;

                        Class<?>[] mparams = (name == null
                                ? ((Constructor) member).getParameterTypes()
                                : ((Method) member).getParameterTypes());

                        if (mparams.length != params.length)
                            continue;

                        boolean same = true;
                        for (int i = 0; i < mparams.length; i++) {
                            if (!mparams[i].isAssignableFrom(params[i])) {
                                same = false;
                                break;
                            }
                        }
                        if (!same)
                            continue;

                        Log.i(TAG, "Resolved member=" + member);
                        return member;
                    }
                    c = c.getSuperclass();
                    if (c == null)
                        throw ex;
                }
            throw new NoSuchMethodException(name);
        } catch (NoSuchMethodException ex) {
            Class<?> c = cls;
            while (c != null && !c.equals(Object.class)) {
                Log.i(TAG, c.toString());
                for (Member member : name == null ? c.getDeclaredConstructors() : c.getDeclaredMethods())
                    if (!exists || name == null || name.equals(member.getName()))
                        Log.i(TAG, "    " + member.toString());
                c = c.getSuperclass();
            }
            throw ex;
        }
    }




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
