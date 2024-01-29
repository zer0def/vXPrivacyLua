package eu.faircode.xlua.rootbox;

import android.os.Environment;
import android.os.Process;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import eu.faircode.xlua.utilities.DatabasePathUtil;

public class xFileUtils {
    private static final String TAG = "XLua.xFileUtils";

    public static final int CHMOD_ALL = 0x777;          //User, Groups, Owner can Read, Write, Execute
    public static final int CHMOD_ALL_RX = 0x775;       //User, Groups, Owner can Read Execute
    public static final int CHMOD_OWNER_ALL = 0770;     //Owner only can Read, Write, Execute

    public static boolean copyDirectories(File aFrom, File bTo, boolean setPermissions, int userId) {
        if(!aFrom.exists() || !aFrom.isDirectory())
            return false;

        if(!bTo.exists() && mkdirs(bTo)) {
            Log.e(TAG, "ERROR COPYING Failed to create Directory: (" + bTo.getAbsolutePath() + ") setPermissions=" + setPermissions);
            return false;
        }

        String toDir = bTo.getAbsolutePath();
        File[] fs = aFrom.listFiles();
        int fCount = 0;
        if(fs != null) {
            fCount++;
            for(File f : fs) {
                String pTo = toDir + File.separator + f.getName();
                File fTo = new File(pTo);

                if(f.isFile()) {
                    if(!fTo.exists()) {
                        if(!createFile(fTo)) {
                            Log.e(TAG, "Failed to create copy file from (" + f.getAbsolutePath() + ") to (" + fTo.getAbsolutePath() + ")");
                            continue;
                        }

                        if(!copy(f, fTo, setPermissions, userId)) {
                            Log.e(TAG, "Failed to copy file from (" + f.getAbsolutePath() + ") to (" + fTo.getAbsolutePath() + ")");
                            continue;
                        }

                        Log.i(TAG, "Copied File (" + f.getAbsolutePath() + ") to (" + fTo.getAbsolutePath() + ")");
                    }
                }else {
                    mkdirs(fTo);
                    if(!copyDirectories(f, fTo, setPermissions, userId)) {
                        Log.e(TAG, "Failed to copy Directory (" + f.getAbsolutePath() + ") to (" + fTo.getAbsolutePath() + ")");
                    }
                }
            }
        }

        if(setPermissions)
            setPermissions(bTo, CHMOD_OWNER_ALL, userId, userId);

        File[] fsNew = bTo.listFiles();
        return fsNew == null ? bTo.exists() : fsNew.length == fCount;
    }

    public static boolean copy(File aFrom, File bTo, boolean setPermissions, int userId) {
        return copyFile(aFrom, bTo, setPermissions, userId) > 0;
    }

    public static long copyFile(File aFrom, File bTo, boolean setPermissions, int userId) {
        if(!aFrom.isFile() || !bTo.isFile())
            return 0;

        if(!isSafeDirectory(aFrom) || !isSafeDirectory(bTo))
            return 0;

        if(!mkdirs(bTo)) {
            Log.e(TAG, "Failed to [copy] from: " + aFrom.getAbsolutePath() + " to:" + bTo.getAbsolutePath() + " could not create the path to");
            return 0;
        }

        if(setPermissions) {
            setPermissions(aFrom, CHMOD_ALL, userId, userId);
            setPermissions(bTo, CHMOD_ALL, userId, userId);
        }

        try {
            Log.i(TAG, "Copying File (from) " + aFrom.getAbsolutePath() + " (to) " + bTo.getAbsolutePath() + " (setPerms)=" + setPermissions);
            Method cpTo = xReflectUtils.getMethodFor("android.os.FileUtils", "copy", File.class, File.class);
            if(cpTo == null) {
                Log.e(TAG, "Failed to get reflect [copy]");
                return -2;
            }

            Log.i(TAG, "Found the [copy] method from FileUtils");

            long res = (long)cpTo.invoke(null, aFrom, bTo);
            if(res < 1)
                throw new Exception("Failed copy internal");

            Log.i(TAG, "COPIED (" + res + ") bytes of data from (" + aFrom.getAbsolutePath() + ") to (" + bTo.getAbsolutePath()+ ")");
            return res;
        }catch (Exception e) {
            Log.e(TAG, "Failed to [copy] from: " + aFrom.getAbsolutePath() + " to:" + bTo.getAbsolutePath() + "\n" + e);
            return -1;
        }
    }

    public static boolean createFile(File file) {
        if(!isSafeDirectory(file))
            return false;

        try {
            return file.createNewFile();
        }catch (Exception e) {
            Log.e(TAG, "Failed to Create File: " + file.getAbsolutePath());
            return false;
        }
    }

    public static boolean mkdirs(File path) {
        if(path.exists())
            return true;

        try {
            return path.mkdirs();
        }catch (Exception e) {
            Log.e(TAG, "Failed to create Directory: (" + path.getAbsolutePath() + ")\n" + e);
            return false;
        }
    }

    public static boolean forceDelete(File path, boolean setPermissions, int userId) {
        if(!path.exists()) return true;
        if(!isSafeDirectory(path)) return false;

        if(path.isDirectory()) {
            if (setPermissions)
                setPermissions(path, CHMOD_ALL, userId, userId);

            File[] fs = path.listFiles();
            if(fs != null) {
                for(File fil : fs) {
                    if(fil.isFile()) {
                        if(!deleteEx(fil)) {
                            Log.e(TAG, "ERROR DELETING: (" + fil.getAbsolutePath() + ")");
                        }
                    }else {
                        forceDelete(fil, setPermissions, userId);
                    }
                }
            }

            return deleteEx(path);
        }else {
            if (setPermissions)
                setPermissions(path, CHMOD_ALL, userId, userId);

            return deleteEx(path);
        }
    }

    public static void setPermissions(File path, int mode, int ownerUid, int groupUid) {
        if(!path.exists() || !isSafeDirectory(path))
            return;

        String pathPath = path.getAbsolutePath();
        if(path.isDirectory()) {
            chownEx(pathPath, ownerUid, groupUid, true);
            chmodEx(pathPath, mode, true);

            File[] fs = path.listFiles();
            if(fs != null) {
                for(File fil : fs) {
                    setPermissions(fil, mode, ownerUid, groupUid);
                }
            }

            setAllPermissionsManaged(path);
            chmodEx(pathPath, mode, true);
        } else {
            chownEx(pathPath, ownerUid, groupUid, false);
            chmodEx(pathPath, mode, false);
            setAllPermissionsManaged(path);
            chownEx(pathPath, ownerUid, groupUid, false);
        }
    }

    public static boolean deleteEx(File path) {
        if(path.exists()) {
            boolean recursive = path.isDirectory();
            String pth = path.getAbsolutePath();
            delete(path);
            if(!path.exists())
                return true;

            rm(pth);
            if(!path.exists())
                return true;

            rmEXEC(pth, recursive);
            if(!path.exists())
                return true;

            shredEXEC(pth, recursive);
            return path.exists();
        }

        return true;
    }

    public static void chmodEx(String path, int mode, boolean recursive) {
        chmod(path, mode);
        chmodEXEC(path, mode, false);
    }

    public static void chownEx(String path, int ownerUid, int groupUid, boolean recursive) {
        chown(path, ownerUid, groupUid);
        chownEXEC(path, ownerUid, groupUid, recursive);
    }

    public static boolean setAllPermissionsManaged(File path) {
        if(!setAllPermissionsManaged(path, false))
            return setAllPermissionsManaged(path, true);

        return true;
    }

    public static boolean setAllPermissionsManaged(File path, boolean ownerOnly) {
        if(!isSafeDirectory(path))
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

    public static boolean delete(File file) {
        if(!isSafeDirectory(file)) return false;
        try {
            return file.delete();
        }catch (Exception e) {
            Log.e(TAG, "DELETE failed on object (" + file.getAbsolutePath() + ")");
            return false;
        }
    }

    public static boolean shredEXEC(String path, boolean recursive) {
        if(!isSafeDirectory(path)) return false;
        xProcessResult res = xProcessResult.exec(xFileCommands.getShredCommand(path, recursive));
        return new File(path).exists();
    }

    public static boolean rmEXEC(String path, boolean recursive) {
        if(!isSafeDirectory(path)) return false;
        xProcessResult res = xProcessResult.exec(xFileCommands.getRMCommand(path, recursive));
        return new File(path).exists();
    }

    public static boolean rm(String path) {
        if(!isSafeDirectory(path)) return false;
        try {
            Os.remove(path);
            return new File(path).exists();
        }catch (ErrnoException e) {
            Log.e(TAG, "RM failed on file (" + path + ")\n" + e);
            return false;
        }
    }

    public static boolean chownEXEC(String path, int ownerUid, int groupUid, boolean recursive) {
        if(!isSafeDirectory(path)) return false;
        //if ownerUid >= 0 ?
        //if(ownerUid >= 0 || groupUid >= 0) {
        //}
        xProcessResult res = xProcessResult.exec(xFileCommands.getChownCommand(path, ownerUid, groupUid, recursive));
        return !res.isOuterException();
    }

    public static boolean chown(String path, int ownerUid, int groupUid) {
        if(!isSafeDirectory(path)) return false;
        try {
            Os.chown(path, ownerUid, groupUid);
            return true;
        }catch (ErrnoException e) {
            Log.e(TAG, "CHOWN failed on file (" + path + ") ownerUid=" + ownerUid + " groupUid=" + groupUid + "\n" + e);
            return false;
        }
    }

    public static boolean chmodEXEC(String path, int mode, boolean recursive) {
        if(!isSafeDirectory(path)) return false;
        xProcessResult res = xProcessResult.exec(xFileCommands.getChmodCommand(path, mode, recursive));
        return !res.isOuterException();
    }

    public static boolean chmod(String path, int mode) {
        if(!isSafeDirectory(path)) return false;
        try {
            Os.chmod(path, mode);
            return true;
        }catch (ErrnoException e) {
            Log.e(TAG, "CHMOD fails on file (" + path + ") mode=" + mode + "\n" + e);
            return false;
        }
    }

    public static boolean isSafeDirectory(File f) {
        if(!f.isDirectory()) f = f.getParentFile();
        if(f == null) return false;
        return isSafeDirectory(f.getAbsolutePath());
    }

    public static boolean isSafeDirectory(String directory) {
        if(directory == null ||
                directory.equals(Environment.getDataDirectory().getAbsolutePath()) ||
                directory.equals(Environment.getDataDirectory() + File.separator + "system")) {
            Log.e(TAG, "Directory is not safe to work with: " + directory);
            return false;
        }

        return true;
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
