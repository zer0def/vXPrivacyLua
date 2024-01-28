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
import eu.faircode.xlua.rootbox.xUnsafeApi;

public class DatabasePathUtil {
    private static final String TAG = "XLua.DatabasePathUtil";
    //private static String lastKnownPath = null;
    //private static final String NEW_PATH = "/da"
    //make a function to ensure not alot of copies ? find what has all the files

    public static boolean ensureDirectoryChangeEx(Context context) {
        if (context != null && XposedUtil.isVirtualXposed())
            return true;

        Log.i(TAG, "ensureDirectoryChangeEx");
        String originalDirectory = getOriginalDataLocationString(null);
        Log.i(TAG, "Ensuring Directory does not exist: " + originalDirectory);

        File oDir = new File(originalDirectory);
        if(oDir.exists()) {
            Log.i(TAG, "Found the original Directory updating... " + originalDirectory);
            Log.i(TAG, "Do note please as the USER assure there are no 'new' dirs except for the ones made by this application!");

            if (getDatabaseDirectoryEx(context) == null) {
                    String newDir = Environment.getDataDirectory() +
                            File.separator +
                            "system" +
                            File.separator +
                            "xplex-" + UUID.randomUUID().toString();//"hjfhJKHSkjhsjj";//UUID.randomUUID().toString();

                    Log.i(TAG, "New Directory! =" + newDir);

                    File nDir = new File(newDir);

                if (!xUnsafeApi.copyDirectoryTo(oDir, nDir, true, true)) {
                    Log.e(TAG, "Failed to Copy over the Database Files to the new Directory! new dir=" + newDir);
                    return false;
                }
            }

            if (!xUnsafeApi.delete(oDir, true)) {
                Log.w(TAG, "canRead=" + oDir.canRead() + "  canWrite=" + oDir.canWrite());
                xUnsafeApi.setPermissionsNuke(oDir, xUnsafeApi.CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, true);
                Log.w(TAG, "canRead=" + oDir.canRead() + "  canWrite=" + oDir.canWrite());
                Log.w(TAG, "Odd it failed to delete the old directory but copied over the filed so when you get time delete it pls! " + originalDirectory);
                xUnsafeApi.deleteDirectory(oDir, true);

                //if(!xUnsafeApi.delete(oDir, true)) {
                //    Log.w(TAG, "Odd it failed to delete the old directory but copied over the filed so when you get time delete it pls AGAINNNNN! " + originalDirectory);
                //}
            }
        } else {
            if (getDatabaseDirectoryEx(context) == null) {
                String newDir = Environment.getDataDirectory() +
                        File.separator +
                        "system" +
                        File.separator +
                        "xplex-" + "hjfhJKHSkjhsjj";//UUID.randomUUID().toString();

                Log.i(TAG, "New Directory! =" + newDir);

                File nDir = new File(newDir);
                xUnsafeApi.mkdirs(nDir, true);
            }
        }

        return true;
    }

    public static File getDatabaseDirectoryEx(Context context) {
        if (context != null && XposedUtil.isVirtualXposed())
            return context.getFilesDir();

        return xUnsafeApi.getFirstDirectoryNameLike(Environment.getDataDirectory() + File.separator + "system", "xplex-");
    }

    public static String ensureDirectoryChange() {
        Log.i(TAG, "ensureDirectoryChange");
        String originalDirectory = getOriginalDataLocationString(null);
        Log.i(TAG, "Ensuring Directory does not exist: " + originalDirectory);

        try {
            File oDir = new File(originalDirectory);
            if(oDir.exists() && oDir.isDirectory()) {
                Log.i(TAG, "Found the original Directory updating... " + originalDirectory);
                //XDataBase.setPerms(oDir);
                Log.i(TAG, "Do note please as the USER assure there are no 'new' dirs except for the ones made by this application!");

                //if(lastKnownPath != null) {
                //    Log.w(TAG, "Last known path is not know, that means we have already init we just failed to delete old files: " + lastKnownPath);
                //}

                String newDir = Environment.getDataDirectory() +
                        File.separator +
                        "system" +
                        File.separator +
                        "xplex-" + UUID.randomUUID().toString();
                        // UUID.randomUUID().toString();;

                Log.i(TAG, "New Directory! =" + newDir);

                File nDir = new File(newDir);
                if(!nDir.mkdirs()) {
                    Log.e(TAG, "Failed to make Directory for: " + nDir.getPath());
                }else {
                    Log.i(TAG, "Created (" + nDir.getPath() + ")");
                    //XDataBase.setPerms(nDir);
                    File[] oFiles = oDir.listFiles();
                    if(oFiles == null || oFiles.length < 1) {
                        Log.e(TAG, "Getting files from Old Directory failed or it null!");
                        Log.w(TAG, "We will assume its empty and can be deleted!");
                        //Assume it was just empty and the database now will need to be init in the new folder, so return the new folder!
                    }else {
                        for(File f : oFiles) {
                            String newPath = newDir + File.separator + f.getName();
                            File tFile = new File(newPath);
                            //tFile.
                            if(!f.renameTo(tFile)) {
                                if(!tFile.exists()) {
                                    Log.e(TAG, "Failed to Move / Rename file a:" + f.getPath() + " b:" + tFile.getPath());
                                    Log.i(TAG, "Deleted Failed Created Directory ? ... " + nukeDirectory(nDir));
                                    return "wait-for-reboot";//assume we cant move the files so it may need a reboot ? im not sure :P rip
                                }
                            }
                        }
                    }

                    if(!nukeDirectory(oDir)) {
                        Log.e(TAG, "Since nuking the old DIR failed , we will cache in Folder to prevent multiple folder creations");
                        if(oDir.exists() && new File(oDir.getPath() + File.separator + "xlua.db").exists()) {
                            Log.w(TAG, "Nuking the new Directory since failed to delete older one and its contents, status=" + nukeDirectory(nDir));
                            return "wait-for-reboot";
                        }
                    }

                    XDataBase.setPerms(nDir);
                    return newDir;
                }
            }

            Log.i(TAG, "Directory (" + originalDirectory + ") Does not exist!");
            return null;
        }catch (Exception e) {
            Log.e(TAG, "Failed to ensure! " + e + "\n" + Log.getStackTraceString(e));
            return null;
        }
    }


    public static String getDatabaseDirectory(Context context) {
        Log.i(TAG, "Resolving the Directory to the Database Files!");
        if (context != null && XposedUtil.isVirtualXposed())
            return context.getFilesDir().getPath();
        else {
            String newDir = ensureDirectoryChange();
            if(newDir == null) {
                Log.w(TAG, "Either New Directory was already created and needs to be found and or was failed to move / create");
            }
            else if(newDir.equals("wait-for-reboot")) {
                Log.w(TAG, "I could not copy the files over, perhaps restart the device ?");
                return getOriginalDataLocationString(null);
            } else {
                Log.i(TAG, "New Directory=" + newDir);
                return newDir;
            }

            try {
                String baseDir = Environment.getDataDirectory() + File.separator + "system" + File.separator;
                File bFile = new File(baseDir);
                if(bFile.exists() && bFile.isDirectory()) {
                    File[] fs = bFile.listFiles();
                    if(fs == null || fs.length < 1) {
                        Log.e(TAG, "Files/Directories in Directory (" + baseDir + ") returned null / empty");
                    }else {
                        for(File f : fs) {
                            if(f.isDirectory() && f.getName().toLowerCase().startsWith("xplex-")) {
                                Log.i(TAG, "Found XPL-EX Directory!");
                                return f.getPath();
                            }
                        }
                    }
                }else
                    Log.e(TAG, "Failed to access/ find Data Directory, " + baseDir);

                return getOriginalDataLocationString(null);
            }catch (SecurityException se) {
                Log.e(TAG, "Security Exception make sure you are ROOT Process! " + se + "\n" + Log.getStackTraceString(se));
                return getOriginalDataLocationString(null);
            }
            catch (Exception e) {
                return getOriginalDataLocationString(null);
            }
        }
    }

    public static boolean nukeDirectory(File dir) {
        try {
            if(!dir.exists() || !dir.isDirectory())
                return true;

            Log.i(TAG , "Nuking: " + dir.getPath());

            //XDataBase.setPerms(dir);//incase
            File[] fls = dir.listFiles();
            if(fls != null && !(fls.length < 1)) {
                for(File f : fls) {
                    try {
                        Log.i(TAG, "File (" + f.getPath() + ") Deleted ? " + f.delete());
                    }catch (Exception ee) {
                        Log.e(TAG, "Failed to delete file: " + f.getPath() + "\n" + ee);
                    }
                }
            }

            boolean del = dir.delete();
            Log.i(TAG, "Directory (" + dir.getPath() + ") Deleted ? " + del);
            return del;
        }catch (Exception e) {
            Log.e(TAG, "Failed to Nuke Directory ! " + e + "\n" + Log.getStackTraceString(e));
            return false;
        }
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
