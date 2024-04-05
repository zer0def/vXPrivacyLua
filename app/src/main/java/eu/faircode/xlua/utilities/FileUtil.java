package eu.faircode.xlua.utilities;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.logger.XLog;


public class FileUtil {
    private static final String TAG = "XLua.FileUtil";

    public static int getDescriptorNumber(FileDescriptor fd) {
        try {
            Method intMethod = fd.getClass().getDeclaredMethod("getInt$");
            int fId = (int) intMethod.invoke(fd);
            return fId;
            //res = Integer.toString(fId);
        } catch (Throwable e) {
            XLog.e("Failed to get File Descriptor Path.", e);
            return 0;
        }
    }

    //       String[] ss = new String[] {"fpc1020", "fpc_fpc1020", "fpc1020", "fpc_fpc1020", "fpc1020", "fp_fpc1020", "fpc1020", "fpc,fpc1020", "fpc1020", "fp_fpc1155", "fpc1145", "fpc1145","fpc1145_device", "fingerprint_fpc", "goodix_fp",  "goodix_fp", "0.goodix_fp", "fpc1145",  "0.fpc1145", "fpc1020",  "fingerprint", "silead_fp", "0.silead_fp", "cdfinger_fp",  "silead_fp", "goodix_fp", "goodix_fingerprint", "cdfinger_fp", "cdfinger_fingerprint", "goodix_fp", "goodix,fingerprint", "fpc1020","fingerprint_fpc", "goodix_fp","fingerprint_goodix", "qbt1000","qbt2000",  "elan",  "elan","elan_elan_fp", "esfp0", "egistec_fp" };
    public static boolean isDeviceDriver(String filePath) {
        if(filePath.contains("sys/devices/system/cpu/"))
            return false;

        ///sys/bus/platform/driver
        if(filePath.contains("/sys/bus/platform/drivers") ||
                filePath.contains("/sys/devices/soc") ||
                filePath.contains("/sys/class/fingerprint") ||
                filePath.contains("/sys/class/camera") ||
                filePath.contains("/sys/android_camera") ||
                filePath.contains("/sys/project_info") ||
                filePath.contains("/vendor/etc") ||
                filePath.contains("/sys/block/sda/device/model") ||
                filePath.contains("/sys/class/block") ||
                filePath.contains("/proc/scsi") ||
                filePath.contains("/proc/bus/input") ||
                filePath.contains("/sys/class/sensors") ||
                filePath.contains("/sys/bus/spi/drivers") ||
                filePath.contains("/sys/class/fingerprint/fingerprint/name")) {

            Log.i(TAG, "PATH_FOUND=" + filePath);
            return true;
        }

        return false;
    }

    public static List<File> filterFileList(List<File> files) {
        List<File> fs = new ArrayList<>();
        for(File f : files)
            if(!FileUtil.isDeviceDriver(f.getPath()))
                fs.add(f);

        Log.i(TAG, "SZ=" + files.size() + "  >> FSZA=" + fs.size());

        if(fs.size() == files.size())
            return files;

        return fs.isEmpty() ? null : fs;
    }

    public static File[] filterFileArray(File[] files) {
        List<File> fs = new ArrayList<>();
        for(File f : files)
            if(!FileUtil.isDeviceDriver(f.getPath()))
                fs.add(f);

        Log.i(TAG, "SZ=" + files.length + "  >> FSZA=" + fs.size());


        if(fs.size() == files.length)
            return files;

        return fs.isEmpty() ? null : fs.toArray(new File[fs.size()]);
    }


    public static List<String> filterList(List<String> files) {
        List<String> fs = new ArrayList<>();
        for(String s : files)
            if(!FileUtil.isDeviceDriver(s))
                fs.add(s);

        Log.i(TAG, "SZ=" + files.size() + "  >> FSZA=" + fs.size());

        if(fs.size() == files.size())
            return files;

        return fs.isEmpty() ? null : fs;
    }

    public static String[] filterArray(String[] files) {
        List<String> fs = new ArrayList<>();
        for(String s : files)
            if(!FileUtil.isDeviceDriver(s))
                fs.add(s);

        Log.i(TAG, "SZ=" + files.length + "  >> FSZA=" + fs.size());

        if(fs.size() == files.length)
            return files;

        return fs.isEmpty() ? null : fs.toArray(new String[fs.size()]);
    }

    public static FileDescriptor generateFakeFileDescriptor(String contents) {
        Log.i(TAG, "Generating Fake File Descriptor...");
        File mockFile = generateFakeFile(contents);
        if(mockFile == null) {
            Log.e(TAG, "MOCK File is Null :( ");
            return null;
        }

        try {
            ParcelFileDescriptor pFileDescriptor = ParcelFileDescriptor.open(mockFile, ParcelFileDescriptor.MODE_READ_ONLY);//0x10000000
            Log.i(TAG, "MOCK File Opened [ParcelFileDescriptor]");
            return pFileDescriptor.getFileDescriptor();
        }catch (Exception e) {
            Log.e(TAG, "Failed to Create Fake File Descriptor!!" + e + "\n" + Log.getStackTraceString(e));
            return null;
        }
    }


    public static File generateFakeFile(String contents) {
        Log.i(TAG, "Generating Fake File...");
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            File temp = File.createTempFile("temp", null);
            Log.i(TAG, "Created MOCK File in temp: " + temp.getPath() + " Name=" + temp.getName());

            fos = new FileOutputStream(temp, false);
            osw = new OutputStreamWriter(fos);

            Log.i(TAG, "Created MOCK File Streams! Writing Contents Size=" + contents.length());

            osw.write(contents);
            return temp;
        }catch (Exception e) {
            Log.e(TAG, "Failed to Create Fake File!!\n" + e + "\n" + Log.getStackTraceString(e));
            return null;
        }finally {
            if(osw != null) try { osw.flush(); osw.close(); } catch (Exception swallow) {  }
            if(fos != null) try { fos.close(); } catch (Exception swallow) {  }
        }
    }

    public static void printContents(String file) {
        Log.i(TAG, "Printing File Contents: " + file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i(TAG, line);
            }
        } catch (Exception e) {
            Log.e(TAG,  "Error Printing File Contents\n" +  e + "\n" + Log.getStackTraceString(e));
        }
    }
}
