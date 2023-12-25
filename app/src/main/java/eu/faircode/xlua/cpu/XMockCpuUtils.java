package eu.faircode.xlua.cpu;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import eu.faircode.xlua.utilities.FileUtil;

//Make this more global , Take in contents then do work
//No need for specific params just CONTENTS
public class XMockCpuUtils {
    private static final String TAG = "XLua.XMockCpuUtils";

    public static FileDescriptor generateFakeFileDescriptor(XMockCpuIO map) {
        Log.i(TAG, "MOCK FileDescriptor For: " + map);
        return FileUtil.generateFakeFileDescriptor(map.contents);
    }

    public static File generateFakeFile(XMockCpuIO map) {
        Log.i(TAG, "MOCK File For: " + map);
        return FileUtil.generateFakeFile(map.contents);
    }
}
