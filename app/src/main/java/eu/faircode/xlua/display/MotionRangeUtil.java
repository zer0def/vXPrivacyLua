package eu.faircode.xlua.display;

import android.util.Log;
import android.view.InputDevice;
import android.view.InputDevice;
import android.view.MotionEvent;

import java.lang.reflect.Constructor;

public class MotionRangeUtil {
    private static final String TAG = "XLua.MotionRangeUtil";
    public static InputDevice.MotionRange createXAxis(int height) { return  createMotionRange(0, height, MotionEvent.AXIS_X); }

    public static InputDevice.MotionRange createYAxis(int width) { return  createMotionRange(0, width, MotionEvent.AXIS_Y); }

    public static InputDevice.MotionRange createMotionRange(float min, float max, int axis) {
        try {
            Constructor<InputDevice.MotionRange> constructor = InputDevice.MotionRange.class.getDeclaredConstructor(int.class, int.class, float.class, float.class, float.class, float.class, float.class);
            constructor.setAccessible(true);

            // Assuming some default values for parameters we don't have
            return constructor.newInstance(axis, InputDevice.SOURCE_ANY, min, max, 0.0f, 0.0f, 1.0f);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "Error creating MotionRange Object:\n" + e + "\n" + Log.getStackTraceString(e));
            return null;
        }
    }

}

