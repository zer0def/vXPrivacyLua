package eu.faircode.xlua.utilities;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.XposedUtil;
import eu.faircode.xlua.rootbox.xFileUtils;

public class DatabasePathUtil {
    private static final String TAG = "XLua.DatabasePathUtil";
    //private static String lastKnownPath = null;
    //private static final String NEW_PATH = "/da"
    //make a function to ensure not alot of copies ? find what has all the files

    public static boolean ensureDirectoryChange(Context context) {
        if(context != null && XposedUtil.isVirtualXposed())
            return true;

        Log.i(TAG, "ensureDirectoryChange");
        String originalDirectory = getOriginalDataLocationString(null);
        File oDir = new File(originalDirectory);
        Log.i(TAG, "Ensuring Directory does not exist: " + originalDirectory);

        File nDir = getDatabaseDirectory(context);
        if (nDir == null) {
            String newDir = getDefaultLocationString(null) + "-" + UUID.randomUUID().toString();
            Log.i(TAG, "New Directory! =" + newDir);
            nDir = new File(newDir);
        }

        if (!xFileUtils.copyDirectories(oDir, nDir, true, Process.SYSTEM_UID)) {
            Log.e(TAG, "Failed to Copy over the Database Files to the new Directory! new dir=" + nDir.getAbsolutePath());
            xFileUtils.forceDelete(nDir, true, Process.SYSTEM_UID);
            return false;
        }

        if(!xFileUtils.forceDelete(oDir, true, Process.SYSTEM_UID)) {
            Log.e(TAG, "Failed to Delete Old Directory=" + oDir.getAbsolutePath());
        }

        return true;
    }

    public static File getDatabaseDirectory(Context context) {
        if (context != null && XposedUtil.isVirtualXposed())
            return context.getFilesDir();

        return xFileUtils.getFirstDirectoryNameLike(Environment.getDataDirectory() + File.separator + "system", "xplex-");
    }

    public static String getOriginalDataLocationString(Context context) {
        if (context != null && XposedUtil.isVirtualXposed()) {
            return context.getFilesDir().getPath();
        } else {
            return Environment.getDataDirectory() + File.separator +
                    "system" + File.separator +
                    "xlua";
        }
    }

    public static String getDefaultLocationString(Context context) {
        if (context != null && XposedUtil.isVirtualXposed()) {
            return context.getFilesDir().getPath();
        } else {
            return Environment.getDataDirectory() + File.separator +
                    "system" + File.separator +
                    "xplex";
        }
    }
}
