package eu.faircode.xlua.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvidenceUtil {
    //public static List<String> BAD

    //List of bad dev ids / serial
    public static final List<String> DEFECT_IDS = Arrays.asList("9774d56d682e549c", "unknown", "000000000000000");

    public static final List<String> SU_PATHS = Arrays.asList("/system/app/superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su");
    public static final List<String> ROOT_PACKAGES = Arrays.asList("io.github.vvb2060.magisk", "io.github.vvb2060.magisk.lit", "com.noshufou.android.su", "com.noshufou.android.su.elite", "eu.chainfire.supersu", "com.koushikdutta.superuser", "com.thirdparty.superuser", "com.yellowes.su", "com.topjohnwu.magisk", "io.github.huskydg.magisk");
    public static final List<String> BAD_APPS = Arrays.asList("com.koushikdutta.rommanager", "com.dimonvideo.luckypatcher", "com.chelpus.lackypatch", "com.ramdroid.appquarantine", "eu.faircode.xlua", "org.lsposed.manager", "com.tsng.hidemyapplist", "rikka.appops", "com.guoshi.httpcanary", "com.httpcanary.pro", "com.aistra.hail", "github.tornaco.android.thanos.pro", "com.mrchandler.disableprox", "org.adaway", "dev.ukanth.ufirewall.donate", "more.shizuku.privileged.api", "ru.bluecat.android.xposed.mods.appsettings", "com.qingyu.rm", "lozn.hookui", "me.jsonet.jshook", "com.zhenxi.jnitrace", "com.zhenxi.fundex2", "com.zhenxi.funelf", "cn.wq.myandroidtools", "ccc71.at.free", "com.sanmer.mrepo", "com.fox2code.mmm", "me.rhunk.snapenhance", "eu.faircode.xlua.pro", "com.berdik.letmedowngrade", "biz.bokhorst.xprivacy", "cn.qssq666.systool", "com.wind.cotter", "player.normal.np", "lozn.godhand");
    public static final List<String> CLOAK_APPS = Arrays.asList("com.koushikdutta.rommanager", "com.dimonvideo.luckypatcher", "com.chelpus.lackypatch", "com.ramdroid.appquarantine");

    public static final List<String> SU_MANAGERS = Arrays.asList("busybox", "su", "magisk");
    public static final List<String> SU_PATHS_EX = Arrays.asList("/data/local/", "/data/local/bin/", "/data/local/xbin/", "/sbin/", "/su/bin/", "/system/bin/", "/system/bin/.ext/", "/system/bin/failsafe/", "/system/sd/xbin/", "/system/usr/we-need-root/", "/system/xbin/", "/system/xbin/daemonsu/", "/system/etc/init.d/99SuperSUDaemon/", "/system/bin/.ext/.su/", "/system/etc/.has_su_daemon/", "/system/etc/.installed_su_daemon/", "/cache/", "/data/", "/dev/");

    public static final List<String> NON_WRITABLE_DIRS = Arrays.asList("/system", "/system/bin", "/system/sbin", "/system/xbin", "/vendor/bin", "/sbin", "/etc");

    public static final List<String> EMULATOR_APPS = Arrays.asList("com.bignox.appcenter", "com.bluestacks.settings", "com.bluestacks.filemanager", "com.genymotion.superuser", "org.greatfruit.andy.ime", "com.kaopu001.tiantianserver", "com.tiantian.ime", "com.microvirt.installer", "com.android.ld.appstore", "com.ldmnq.launcher3", "com.jide.Appstore");


    public static final List<String> EMULATOR_FILES_RC = Arrays.asList("init.android_x86.rc", "ueventd.android_x86.rc", "fstab.android_x86", "x86.prop", "ueventd.ttVM_x86.rc", "init.ttVM_x86.rc", "fstab.ttVM_x86", "fstab.vbox86", "init.vbox86.rc", "ueventd.vbox86.rc", "ueventd.android_x86_64.rc", "init.android_x86_64.rc", "fstab.goldfish", "init.goldfish.rc", "init.superuser.rc");
    public static final List<String> EMULATOR_SYS_FILES = Arrays.asList("/system/lib/libc_malloc_debug_qemu.so", "/sys/qemu_trace", "/system/bin/qemu-props");
    public static final List<String> EMULATOR_DEV_FILES = Arrays.asList("/dev/socket/qemud", "/dev/qemu_pipe");
    public static final List<String> EMULATOR_FILES = Arrays.asList("init.ranchu.rc", "init.remixos.rc", "init.andy.rc", "ueventd.andy.rc", "bin/genybaseband", "bin/genymotion-vbox-sf", "ueventd.nox.rc", "init.nox.rc", "/system/bin/noxd");

    public static final List<String> EMULATOR_MANUFACTURER_NAMES = Arrays.asList("unknown", "Genymotion", "AndyOS");
    public static final List<String> EMULATOR_BRAND_NAMES = Arrays.asList("generic", "generic_x86", "Android", "AndyOS");
    public static final List<String> EMULATOR_DEVICE_NAMES = Arrays.asList("AndyOSX", "Droid4X", "generic", "generic_x86", "vbox86p");
    public static final List<String> EMULATOR_HARDWARE_NAMES = Arrays.asList("goldfish", "vbox86", "andy", "ranchu", "ttVM_x86", "android_x86");
    public static final List<String> EMULATOR_MODEL_NAMES = Arrays.asList("sdk", "google_sdk", "Android SDK built for x86", "generic");
    public static final List<String> EMULATOR_PRODUCT_NAMES = Arrays.asList("vbox86p", "Genymotion", "Driod4X", "AndyOSX", "remixemu");

    //public static final String BAD_FILE = "superUserApk";//suFileName
    public static final Map<String, String> DANGEROUS_PROPERTIES = new HashMap<String, String>() {{
            put("[ro.debuggable]", "[1]");
            put("[ro.secure]", "[0]");
        }
    };

    public static final String BAD_TAGS = "test-keys";
    public static final String BAD_IP = "0.0.0.0";
    public static final String BAD_NAME = "goldfish";
    public static final String EMULATOR_SHARE_FOLDER = "windows/BstSharedFolder";
    public static final String BAD_PRODUCT_NAME_PATTERN = ".*_?sdk_?.*";//str.matches

    public static final int FILTER_EMULATOR = 0x1;
    public static final int FILTER_ROOT = 0x2;
    public static final int FILTER_EMULATOR_ROOT = 0x3;

    public static File[] fileArray(File[] files, int code) {
        if(files == null || files.length == 0) return files;
        List<File> nFile = fileList(Arrays.asList(files), code);
        return nFile.toArray(new File[0]);
    }

    public static List<File> fileList(List<File> files, int code) {
        if(files == null || files.isEmpty()) return files;
        List<File> nFiles = new ArrayList<>();
        for(File f : files)
            if(!file(f, code))
                nFiles.add(f);

        return nFiles;
    }

    public static String[] stringArray(String[] files, int code) {
        if(files == null || files.length == 0) return files;
        List<String> nFile = stringList(Arrays.asList(files), code);
        return nFile.toArray(new String[0]);
    }

    public static List<String> stringList(List<String> files, int code) {
        if(files == null || files.isEmpty()) return files;
        List<String> nFiles = new ArrayList<>();
        for(String f : files) {
            if(!f.contains("/"))
                if(!file(null, f, code))
                    nFiles.add(f);
            else {
                if(!file(f, code))
                    nFiles.add(f);
            }
        } return nFiles;
    }

    public static boolean packageName(String packageName, int code) {
        String pLow = packageName.toLowerCase();
        if(code == FILTER_ROOT || code == FILTER_EMULATOR_ROOT) {
            if(ROOT_PACKAGES.contains(pLow) || BAD_APPS.contains(pLow) || CLOAK_APPS.contains(pLow))
                return true;
        }
        if(code == FILTER_EMULATOR || code == FILTER_EMULATOR_ROOT) {
            if(EMULATOR_APPS.contains(pLow))
                return true;
        } return false;
    }

    public static boolean file(String file, int code) { return file(new File(file), code); }
    public static boolean file(File file,  int code) { return file(file.getAbsolutePath(), file.getName(), code); }
    public static boolean file(String fileFull, String fileName, int code) {
        String ffLow = fileFull == null ? fileName : fileFull.toLowerCase();
        String fnLow = fileName.toLowerCase();
        if(code == FILTER_EMULATOR || code == FILTER_EMULATOR_ROOT) {
            for(String f : EMULATOR_SYS_FILES)
                if(ffLow.startsWith(f))
                    return true;

            for(String f : EMULATOR_DEV_FILES)
                if(ffLow.startsWith(f))
                    return true;

            for(String f : EMULATOR_FILES_RC)
                if(ffLow.contains(f.toLowerCase()))
                    return true;

            for(String f : EMULATOR_FILES)
                if(ffLow.contains(f.toLowerCase()))
                    return true;

            if(EMULATOR_APPS.contains(fnLow))
                return true;

            if(ffLow.contains(EMULATOR_SHARE_FOLDER.toLowerCase()))
                return true;
        }

        if(code == FILTER_ROOT || code == FILTER_EMULATOR_ROOT) {
            for(String f : SU_PATHS)
                if(f.equalsIgnoreCase(fileFull))
                    return true;
            //if(SU_MANAGERS.contains(fnLow)) {
            //    for(String f : SU_PATHS_EX) {
            //        if(ffLow.contains(f))
            //            return true;
            //    }
            //}
            if(SU_MANAGERS.contains(fnLow))
                return true;

            if(ROOT_PACKAGES.contains(ffLow) || CLOAK_APPS.contains(ffLow) || BAD_APPS.contains(ffLow))
                return true;
        }

        return false;
    }


    /*public static File[] filterFileArrayForEvidence(File[] files) {
        if(files == null || files.length == 0) return files;
        List<File> fNew = new ArrayList<>();
        for(File f : files)
            if(!fileContainsEvidence(f))
                fNew.add(f);

        return fNew.toArray(new File[0]);
    }

    public static List<File> filterFileListForEvidence(List<File> files) {
        if(files == null || files.isEmpty()) return files;
        List<File> fNew = new ArrayList<>();
        for(File f : files) {
            if(!fileContainsEvidence(f))
                fNew.add(f);
        }

        return fNew;
    }

    public static boolean fileContainsEvidence(String file) { return fileContainsEvidence(new File(file)); }
    public static boolean fileContainsEvidence(File file) {
        if(file == null) return false;
        String fName = file.getName().toLowerCase();
        String fFull = file.getAbsolutePath().toLowerCase();
        return !SU_PATHS.contains(fFull) && !ROOT_PACKAGES.contains(fName) && !BAD_APPS.contains(fName) && !CLOAK_APPS.contains(fName);
    }

    public static String[] filterArrayForEvidence(String[] arr) {
        if(arr == null || arr.length == 0) return arr;
        List<String> newList = new ArrayList<>();
        for (String s : arr)
            if(!containsEvidence(s.toLowerCase()))
                newList.add(s);

        return newList.toArray(new String[0]);
    }

    public static List<String> filterListForEvidence(List<String> lst) {
        if(lst == null || lst.isEmpty()) return lst;
        List<String> newList = new ArrayList<>();
        for (String s : lst) {
            if(!containsEvidence(s.toLowerCase()))
                newList.add(s);
        }

        return newList;
    }

    public static boolean containsEvidence(String s) { return SU_PATHS.contains(s) || ROOT_PACKAGES.contains(s) || BAD_APPS.contains(s) || CLOAK_APPS.contains(s); }*/
}
