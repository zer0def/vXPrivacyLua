package eu.faircode.xlua.rootbox;

import android.os.Environment;
import android.os.Process;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.attribute.UserPrincipal;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.utilities.DatabasePathUtil;

public class xUnsafeApi {
    private static final String TAG = "XLua.UnsafeApiEx";
    //https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/os/StrictMode.java
    //onPathAccess (interesting ?)


    public static final int CHMOD_ALL = 0x777;          //User, Groups, Owner can Read, Write, Execute
    public static final int CHMOD_ALL_RX = 0x775;       //User, Groups, Owner can Read Execute
    public static final int CHMOD_OWNER_ALL = 0770;    //Owner only can Read, Write, Execute

    public static boolean setPermissionsNuke(
            File path,
            int mode,
            int ownerUid,
            int groupUid,
            boolean setAllManagedIfFail,
            boolean subDirAndAllFiles) {

        if(!isSafe(path))
            return false;

        try {
            if(subDirAndAllFiles) {
                File dir = path.isDirectory() ? path : path.getParentFile();
                if(!isSafe(dir))
                    return false;

                Log.i(TAG, "Setting permissions for (" + dir.getAbsolutePath() + ") mode=" + mode + " ownerUid=" + ownerUid + " groupUid=" + groupUid);


                if(!setPermissions(dir, mode, ownerUid, groupUid) && setAllManagedIfFail) {
                    Log.i(TAG, "Setting ALL permissions for (" + dir.getAbsolutePath() + ") status=" + setPermissionsAllManaged(dir));
                    chown(dir.getAbsolutePath(), ownerUid, groupUid);
                }

                File[] files = dir.listFiles();
                if(files != null && files.length > 0) {
                    Log.i(TAG, "Files Count for Nuke=" + files.length);
                    for(File f : files) {
                        if(f.isFile()) {
                            Log.i(TAG, "Setting Permissions for (" + f.getAbsolutePath() + ") mode=" + mode);
                            if(!setPermissions(f, mode, ownerUid, groupUid)) {
                                Log.e(TAG, "Failed to set Permissions for(" + f.getAbsolutePath() + ") mode=" + mode + " ownerUid=" + ownerUid + " groupUid=" + groupUid);
                                if(setAllManagedIfFail) {
                                    Log.i(TAG, "Set ALL permissions for (" + f.getAbsolutePath() + ") status=" + setPermissionsAllManaged(f));
                                    chown(f.getAbsolutePath(), ownerUid, groupUid);
                                }
                            }
                        }else {
                            //setPermissionsNuke(f, mode, ownerUid, groupUid, setAllManagedIfFail, true);
                        }
                    }
                }

                //return dir.isDirectory();
                return access(dir.getAbsolutePath(), mode);
            }else {
                if(!setPermissions(path, mode, ownerUid, groupUid) && setAllManagedIfFail) {
                    Log.i(TAG, "Set ALL permissions for (" + path.getAbsolutePath() + ") status=" + setPermissionsAllManaged(path));
                    chown(path.getAbsolutePath(), ownerUid, groupUid);
                }

                return access(path.getAbsolutePath(), mode);
            }
        }catch (Exception e) {
            Log.e(TAG, "Failed to NUKE Directory with Permissions: " + path.getAbsolutePath() + " mode=" + mode + " ownerUid=" + ownerUid + " groupUid=" + groupUid);
            return false;
        }
    }

    public static void setPermsOld(File directoryOrFile, int mode) {
        if(!isSafe(directoryOrFile))
            return;

        Log.i(TAG, "Setting File Permissions (" + mode + ") SYSTEM_UID For XLUA Directory for UID: " + Process.SYSTEM_UID);

        // Set database file permissions
        // Owner: rwx (system)
        // Group: rwx (system)
        // World: ---
        //Process.myUid()
        XUtil.setPermissions(directoryOrFile.getAbsolutePath(), mode, Process.SYSTEM_UID, Process.SYSTEM_UID);
        File[] files = directoryOrFile.listFiles();
        if (files != null)
            for (File file : files)
                XUtil.setPermissions(file.getAbsolutePath(), mode, Process.SYSTEM_UID, Process.SYSTEM_UID);

        Log.i(TAG, "Finished setting permissions for: " + directoryOrFile.getPath());
    }

    public static boolean renameTo(File from, File to) {
        try {
            return from.renameTo(to);
        }catch (Exception e) {
            Log.e(TAG, "Failed to [move/rename] from: " + from.getAbsolutePath() + " to:" + to.getAbsolutePath() + "\n" + e);
            return false;
        }
    }

    public static boolean isSafe(String f) {
        if(f == null || f.equals(Environment.getDataDirectory().getAbsolutePath()) || f.equals(Environment.getDataDirectory() + File.separator + "system")) {
            Log.e(TAG, "Directory is not safe to work with: " + f);
            return false;
        }

        return true;
    }

    public static boolean isSafe(File f) {
        if(f == null || f.getAbsolutePath().equals(Environment.getDataDirectory().getAbsolutePath()) ||
                f.getAbsolutePath().equals(Environment.getDataDirectory() + File.separator + "system")) {

            Log.e(TAG, "Directory is not safe to work with: " + f.getAbsolutePath());
            return false;
        }

        Log.i(TAG, "Directory is safe: " + f.getAbsolutePath());
        return true;
    }

    public static int initCounter = 0;

    public static boolean copyDirectoryTo(File fromDir, File toDir, boolean setPerms, boolean deleteIfFail) {
        if(!fromDir.isDirectory()) {
            Log.i(TAG, "FROM DIR IS NOT A DIRECTORY (" + fromDir.getAbsolutePath() + " isDir=" + fromDir.isDirectory() + ")");
            return false;
        }

        if(toDir.exists() && !toDir.isDirectory()) {
            Log.i(TAG, "TO DIR IS NOT A DIRECTORY (" + toDir.getAbsolutePath() + " isDir=" + toDir.isDirectory() + ")");
            return false;
        }

        if(!isSafe(fromDir) || !isSafe(toDir))
            return false;

        try {
            Log.i(TAG, "Moving Directory (from)=" + fromDir.getAbsolutePath() + " (to)=" + toDir.getAbsolutePath() + " setPerms=" + setPerms);

            if(!mkdirs(toDir, false)) {
                Log.e(TAG, "Failed to copy Directory from (" + fromDir.getAbsolutePath() + ") to (" + toDir.getAbsolutePath() + ") , could not create to directory");
                return false;
            }

            if(setPerms) {
                setPermissionsNuke(fromDir, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, true);
                setPermissionsNuke(toDir, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, true);
            }

            Log.i(TAG, "[" + fromDir.getAbsolutePath() + "] canRead=" + fromDir.canRead() + " [" + toDir.getAbsolutePath() + "] canWrite=" + toDir.canWrite());
            //Set toDir permissions ????
            File[] fs = fromDir.listFiles();
            if(fs == null || fs.length < 1) {
                Log.e(TAG, "Could not Find any files to copy over ? (from)=" + fromDir.getAbsolutePath() + " (to)=" + toDir.getAbsolutePath());
                return false;
            }

            chown(toDir.getAbsolutePath(), Process.SYSTEM_UID, Process.SYSTEM_UID);
            chmod(toDir.getAbsolutePath(), CHMOD_OWNER_ALL);
            setPermissionsAllManaged(toDir, true);

            String toDirName = toDir.getAbsolutePath();
            for (File f : fs) {
                File toF = new File(toDirName + File.separator + f.getName());
                toF.createNewFile();
                setPermissionsNuke(toF, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, false);
                long st = copy(f, toF, false);
                Log.i(TAG, "Copying (" + f.getAbsolutePath() + " canRead=" + f.canRead() + ") to (" + toDirName + " canWrite=" + toDir.canWrite() + ") status=" + st);
                if(st < 1) {
                    Log.e(TAG, "Failed to copy (" + f.getAbsolutePath() + ") to (" + toF.getAbsolutePath() + ")");
                    if(deleteIfFail) {
                        delete(toDir, false);
                        return false;
                    }
                }
            }

        }catch (Exception e) {
            Log.e(TAG, "Failed Moving Directories (from)=" + fromDir.getAbsolutePath() + " (to)=" + toDir.getAbsolutePath() + "\n" + e);
            return false;
        }

        return true;
    }

    public static boolean copyTo(File from, File to, boolean setPerms) {
        return copy(from, to, setPerms) > 0;
    }

    public static long copy(File from, File to, boolean setPerms) {

        if(!from.isFile() || !to.isFile())
            return 0;

        if(!isSafe(from) || !isSafe(to))
            return 0;

        if(!mkdirs(to, false)) {
            Log.e(TAG, "Failed to [copy] from: " + from.getAbsolutePath() + " to:" + to.getAbsolutePath() + " coult not create the path to");
            return 0;
        }

        try {
            if(setPerms) {
                setPermissionsNuke(from, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, false);
                setPermissionsNuke(to, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, false);
                //return copy(from, to, false);
            }

            Log.i(TAG, "Copying File (from) " + from.getAbsolutePath() + " (to) " + to.getAbsolutePath() + " (setPerms)=" + setPerms);
            Method cpTo = xReflectUtils.getMethodFor("android.os.FileUtils", "copy", File.class, File.class);
            if(cpTo == null) {
                Log.e(TAG, "Failed to get reflect [copy]");
                return -2;
            }

            Log.i(TAG, "Found the [copy] method from FileUtils");

            long res = (long)cpTo.invoke(null, from, to);
            if(res < 1)
                throw new Exception("Failed copy internal");

            Log.i(TAG, "COPIED (" + res + ") bytes of data from (" + from.getAbsolutePath() + ") to (" + to.getAbsolutePath()+ ")");
            return res;
        }catch (Exception e) {
            Log.e(TAG, "Failed to [copy] from: " + from.getAbsolutePath() + " to:" + to.getAbsolutePath() + "\n" + e);
            return -1;
        }
    }

    public static boolean mkdirs(File path, boolean setPerms) {

        //only set perms IF not root base
        //if(setPerms && path.getAbsolutePath().equals(Environment.getDataDirectory() + File.separator + "system"))
        //    setPerms = false;//like this kinda incase prevent bad shit

        try {
            File dir = path.isFile() ? path.getParentFile() : path;
            if(!isSafe(dir))
                return false;

            if(dir == null)
                return false;

            if(!dir.exists()) {
                if(!dir.mkdirs())
                    throw new Exception("Failed to create Directory");
            }
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to create dir: " + path.getAbsolutePath() + "\n" + e);
            return false;
        }
    }

    public static boolean access(String path, int mode) {
        try {
            Os.access(path, mode);
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Failed to access: " + path + " mode=" + mode + "\n" + e);
            return false;
        }
    }

    public static boolean setPermissions(File path, int mode, int ownerUid, int groupUid) {
        if(!isSafe(path))
            return false;

        if(ownerUid >= 0 || groupUid >= 0)
            chown(path.getAbsolutePath(), ownerUid, groupUid);

        if(!chmod(path.getAbsolutePath(), mode))
            return false;

        if(ownerUid >= 0 || groupUid >= 0)
            return chown(path.getAbsolutePath(), ownerUid, groupUid);

        return true;
    }

    public static boolean setPermissionsAllManaged(File path) {
        if(!isSafe(path))
            return false;

        if(!setPermissionsAllManaged(path, true))
            return setPermissionsAllManaged(path, false);

        return true;
    }

    public static boolean setPermissionsAllManaged(File path, boolean ownerOnly) {
        if(!isSafe(path))
            return false;

        try {
            if(!path.setReadable(true, ownerOnly)) {
                Log.e(TAG, "Failed to set READ on: " + path.getAbsolutePath());
                return false;
            }

            if(!path.setWritable(true, ownerOnly)) {
                Log.e(TAG, "Failed to set WRITE on: " + path.getAbsolutePath());
                return false;
            }

            if(!path.setExecutable(true, ownerOnly)) {
                Log.e(TAG, "Failed to set EXECUTE on: " + path.getAbsolutePath());
                return false;
            }

            Log.i(TAG, "RWX set for File: " + path.getAbsolutePath());
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Error setting RWX on file: " + path.getAbsolutePath());
            return false;
        }
    }

    public static boolean chmod(String path, int mode) {

        if(!isSafe(path))
            return false;

        try {
            Os.chmod(path, mode);
            Log.i(TAG, "CHMOD (" + path + ") mode=" + mode);
            return true;
        }catch (ErrnoException e) {
            Log.e(TAG, "Failed to CHMOD (" + path + ") mode=" + mode + "\n" + e);
            return false;
        }
    }

    public static boolean chown(String path, int ownerUid, int groupUid) {
        if(!isSafe(path))
            return false;

        try {
            if(ownerUid >= 0 || groupUid >= 0) {
                Os.chown(path, ownerUid, groupUid);
                Log.i(TAG, "CHOWN (" + path + ")  ownerUid=" + ownerUid + " groupUid=" + groupUid);
                return true;
            }

            Log.w(TAG, "Did not CHOWN since Owner and Group Flag is not set: " + path);
            return false;
        }catch (ErrnoException e) {
            Log.e(TAG, "Failed to CHOWN (" + path + ") ownerUid=" + ownerUid + " groupUid=" + groupUid + "\n" + e);
            return false;
        }
    }

    public static boolean deleteDirectory(File path, boolean setPerms) {
        if(!isSafe(path))
            return false;

        if(!path.exists())
            return true;

        if(setPerms)
            setPermissionsNuke(path, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, true);

        try {
            String command = "rm -r '" + path.getAbsolutePath() + "'";
            java.lang.Process p = Runtime.getRuntime().exec(command);
            //InputStream isE = p.getErrorStream();
            //OutputStream osE =  p.getOutputStream();

            ///InputStreamReader strm = new InputStreamReader(isE);
            //BufferedReader ss = new BufferedReader(isE);


            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            /*BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));



            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            p.waitFor();

            Log.w(TAG, "OUTPUT: " + output.toString());*/

            //return output.toString();

            //debug

            // Get the standard output (output of the command)
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // Get the standard error (error output of the command)
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // Read the output from the command
            //System.out.println("Standard output of the command:\n");
            Log.i(TAG, "STANDARD OUTPUT");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                //System.out.println(s);
                Log.w(TAG, "OUTPUT=" + s);
            }

            // Read any errors from the attempted command
            //System.out.println("Standard error of the command (if any):\n");
            Log.i(TAG, "STANDARD OUTPUT ERROR");
            while ((s = stdError.readLine()) != null) {
                //System.out.println(s);
                Log.w(TAG, "ERROR OUTPUT=" + s);
            }

            return true;
        }catch (IOException e) {
            Log.e(TAG, "Failed to Delete Directory: " + e);
            return false;
        }
        /* catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException: " + e);
            //throw new RuntimeException(e);
        }*/
    }

    public static boolean delete(File path, boolean setPerms) {
        if(!isSafe(path))
            return false;

        if(!path.exists())
            return true;

        if(path.isFile()) {
            try {
                if(setPerms)
                    setPermissionsNuke(path, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, false);

                if(!path.delete())
                    throw new Exception("Failed to delete file...");

                return true;
            }catch (Exception e) {
                Log.e(TAG, "Failed to delete File: " + path.getAbsolutePath() + "\n" + e);
                return false;
            }
        }else {
            try {
                if(setPerms)
                    setPermissionsNuke(path, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, true);

                Log.i(TAG , "Deleting (" + path.getAbsolutePath() + ") canRead=" + path.canRead() + " canWrite=" + path.canWrite());

                File[] fs = path.listFiles();
                if(fs != null) {
                    for(File f : fs) {
                        Log.i(TAG , "Deleting (" + f.getAbsolutePath() + ") canRead=" + f.canRead() + " canWrite=" + f.canWrite());
                        delete(f, true);
                    }
                }

                //if(setPerms) {
                //    setPermissionsNuke(path, CHMOD_OWNER_ALL, Process.SYSTEM_UID, Process.SYSTEM_UID, true, true);
                //    return delete(path, false);
                //}

                return path.delete();
            }catch (Exception e) {
                Log.e(TAG, "Failed to Delete Directory: " + path.getAbsolutePath() + "\n" + e);
                return false;
            }
        }
    }

    public static File getFirstDirectoryNameLike(String baseDir, String likeHas) {
        File f = getFirstDirectoryNameLike(new File(baseDir), likeHas);
        if(f == null)
            return new File(DatabasePathUtil.getOriginalDataLocationString(null));

        return f;
    }

    public static File getFirstDirectoryNameLike(File baseDir, String likeHas) {
        try {
            if(baseDir.isDirectory() && baseDir.exists()) {
                File[] fs = baseDir.listFiles();
                if(fs == null || fs.length < 1) {
                    Log.e(TAG, "Failed to find Directory like: " + likeHas + " from: " + baseDir.getAbsolutePath());
                    return null;
                }

                for (File fl : fs) {
                    if (fl.isDirectory() && fl.getAbsolutePath().contains(likeHas)) {
                        Log.i(TAG, "Found Like Directory: " + fl.getAbsolutePath());
                        return fl;
                    }
                }
            }

            Log.e(TAG, "Failed to find Directory like: " + likeHas + " from: " + baseDir.getAbsolutePath());
            return  null;
        }catch (Exception e) {
            Log.e(TAG, "Failed to enum DIRS in (" + baseDir + ") for (" + likeHas + ")");
            return null;
        }
    }
}
