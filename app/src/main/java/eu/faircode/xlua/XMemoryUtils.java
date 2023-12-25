package eu.faircode.xlua;

import android.app.ActivityManager;
import android.util.Log;

public class XMemoryUtils {
    private static final String TAG = "XLua.XMemoryUtils";

    public static ActivityManager.MemoryInfo getMemory(int totalMemoryInGB, int availableMemoryInGB) {
        ActivityManager.MemoryInfo m = new ActivityManager.MemoryInfo();
        populateMemoryInfo(m, totalMemoryInGB, availableMemoryInGB);
        return m;
    }

    public static void populateMemoryInfo(ActivityManager.MemoryInfo memoryInfo, int totalMemoryInGB, int availableMemoryInGB) {
        if(totalMemoryInGB < 1){
            Log.e(TAG,"Invalid Memory Setting in GB:" + totalMemoryInGB);
            return;
        }

        // 1 gigabyte (GB) is equal to 1,073,741,824 bytes
        long totalBytes = (long) totalMemoryInGB * 1073741824L;
        long availableBytes = (long) availableMemoryInGB * 1073741824L;

        if (availableBytes > totalBytes) {
            Log.e(TAG, "Available memory cannot be greater than total memory.");
        }

        if (totalBytes < 0 || availableBytes < 0) {
            Log.e(TAG, "Memory values must be non-negative");
            return;
        }

        // Set available memory
        memoryInfo.availMem = availableBytes;
        // Set total memory
        memoryInfo.totalMem = totalBytes;
        // Set threshold (for example, setting it to a quarter of the total memory)
        memoryInfo.threshold = totalBytes / 4;
        // Set lowMemory (you can modify this logic as per your requirement)
        memoryInfo.lowMemory = memoryInfo.availMem <= memoryInfo.threshold;
    }
}
