package eu.faircode.xlua.utilities;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import java.lang.reflect.Field;

public class LuaLongUtil {
    private static final String TAG = "XLua.LuaLongUtil";

    /**
     * Write a Long Value From String to a Bundle
     * This is to Help work with LUA not Supporting LONG / BIGINT
     *
     * @param bundle Bundle Instance Write Long To
     * @param key Bundle Field/key Name Holding Long Value
     * @param longValue Value of Long represented as a String , will be Converted back to a LONG
     */
    public static void bundlePutLong(Bundle bundle, String key, String longValue) {
        try {
            bundle.putLong(key, Long.parseLong(longValue));
        }catch (Exception e) {
            Log.e(TAG, "Failed to Parcel Write Long LUA: \n" + e + "\n" + Log.getStackTraceString(e));
        }
    }

    /**
     * Get LONG Value from bundle as String
     * This is to Help work with LUA not Supporting LONG / BIGINT
     *
     * @param bundle bundle Instance read LONG Value from
     * @param key Bundle Field/key Name Holding Long Value
     * @return Returns the Long Value as a String, if Failed return "0"
     */
    public static String bundleGetLong(Bundle bundle, String key) {
        try {
            return Long.toString(bundle.getLong(key));
        }catch (Exception e) {
            Log.e(TAG, "Failed to Read LONG LUA: \n" + e + "\n" + Log.getStackTraceString(e));
            return "0";
        }
    }

    /**
     * Get LONG Value from Parcel as String
     * This is to Help work with LUA not Supporting LONG / BIGINT
     *
     * @param parcel Parcel Instance read LONG Value from
     * @return Returns the Long Value as a String, if Failed return "0"
     */
    public static String parcelReadLong(Parcel parcel) {
        try {
            return Long.toString(parcel.readLong());
        } catch (Exception e) {
            Log.e(TAG, "Failed to Read LONG LUA: \n" + e + "\n" + Log.getStackTraceString(e));
            return "0";
        }
    }

    /**
     * Write a Long Value From String to a Parcel
     * This is to Help work with LUA not Supporting LONG / BIGINT
     *
     * @param parcel Parcel to Write Long Value to
     * @param longValue Value of Long represented as a String , will be Converted back to a LONG
     */
    public static void parcelWriteLong(Parcel parcel, String longValue) {
        try {
            parcel.writeLong(Long.parseLong(longValue));
        }catch (Exception e) {
            Log.e(TAG, "Failed to Parcel Write Long LUA: \n" + e + "\n" + Log.getStackTraceString(e));
        }
    }

    /**
     * Dynamically using Reflection Gets a Field of Long Type
     * This is to Help work with LUA not Supporting LONG / BIGINT
     *
     * @param instance Instance of the Object you want to get the Long Field From
     * @param fieldName Field that has Long Value
     * @return Returns the Long Value that the Field is Storing, if Failed then return 0
     */
    public static Long getFieldValue(Object instance, String fieldName) {
        if (instance == null || !StringUtil.isValidString(fieldName)) {
            Log.e(TAG, "Instance and field name must not be null or empty");
            return (long)0;
        }

        try {
            // Get the class of the instance
            Class<?> clazz = instance.getClass();
            // Access the field
            Field field = clazz.getDeclaredField(fieldName);
            // Ensure the field is accessible
            try {
                field.setAccessible(true);
            }catch (Exception e) { }

            // Check if the field is of type Long
            if (field.getType() != Long.class && field.getType() != long.class) {
                Log.e(TAG, "Field '" + fieldName + "' is not of type Long");
            }

            // Return the value of the field
            return (Long) field.get(instance);
        } catch (NoSuchFieldException e) {
            Log.e(TAG, "Field '" + fieldName + "' not found", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG,"Failed to access field '" + fieldName + "'", e);
        }

        return (long)0;
    }

    /**
     * Dynamically using Reflection Set a Field Long Value
     * This is to Help work with LUA not Supporting LONG / BIGINT
     *
     * @param instance Instance of the Object you want to get the Long Field From
     * @param fieldName Field that has Long Value
     * @param newValue Value to Set in the Long Field as String will be Converted to LONG
     */
    public static void setLongFieldValue(Object instance, String fieldName, String newValue) {
        try {
            setLongFieldValue(instance, fieldName, Long.parseLong(newValue));
        }catch (Exception e){
            Log.e(TAG, "Failed to Convert Value to LONG! \n" + e + "\n" + Log.getStackTraceString(e));
        }
    }

    /**
     * Dynamically using Reflection Set a Field Long Value
     * This is to Help work with LUA not Supporting LONG / BIGINT
     *
     * @param instance Instance of the Object you want to get the Long Field From
     * @param fieldName Field that has Long Value
     * @param newValue Value to Set in the Long Field
     */
    public static void setLongFieldValue(Object instance, String fieldName, Long newValue) {
        if (instance == null || fieldName == null || fieldName.isEmpty()) {
            Log.e(TAG, "Instance and field name must not be null or empty");
        }

        try {
            // Get the class of the instance
            Class<?> clazz = instance.getClass();
            // Access the field
            Field field = clazz.getDeclaredField(fieldName);
            // Ensure the field is accessible
            try {
                field.setAccessible(true);
            }catch (Exception e) { }

            // Check if the field is of type Long
            if (field.getType() != Long.class && field.getType() != long.class) {
                Log.e(TAG, "Field '" + fieldName + "' is not of type Long");
            }

            // Set the new value of the field
            field.set(instance, newValue);
        } catch (NoSuchFieldException e) {
            Log.e(TAG,"Field '" + fieldName + "' not found", e);
        } catch (IllegalAccessException e) {
            Log.e(TAG,"Failed to access field '" + fieldName + "'", e);
        }
    }
}
