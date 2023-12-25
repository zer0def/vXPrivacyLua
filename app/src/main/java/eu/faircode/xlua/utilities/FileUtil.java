package eu.faircode.xlua.utilities;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import eu.faircode.xlua.cpu.XMockCpuIO;

public class FileUtil {
    private static final String TAG = "XLua.FileUtil";

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
