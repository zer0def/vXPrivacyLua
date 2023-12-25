package eu.faircode.xlua;

import org.json.JSONArray;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipFile;

public class XMockFileUtils {
    public static HashMap<String, String> cpuMaps = new HashMap<>();


    //private static volatile List<XMockFile> files = new ArrayList<>();

    public void InitCache() {
        if(cpuMaps == null || cpuMaps.isEmpty()) {

        }
    }

    public static FileDescriptor createMockFileCpuinfo(String cpuId) throws IOException {
        //if file is created in a folder that the caller app has access too
        File temp = File.createTempFile("temp", null);
        FileOutputStream fos = new FileOutputStream(temp, false);
        OutputStreamWriter osw = new OutputStreamWriter(fos);

        String contents = "EMPTY";
        if(cpuMaps.containsKey(cpuId))
            contents = cpuMaps.get(cpuId);

        osw.write(contents);
        osw.flush();
        osw.close();
        return null;
    }

    public static List<XMockFile> getFromAssets() {

        /*ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(apk);
            ZipEntry zipEntry = zipFile.getEntry("assets/hooks.json");
            if (zipEntry == null)
                throw new IllegalArgumentException("assets/hooks.json not found in " + apk);

            InputStream is = null;
            try {
                is = zipFile.getInputStream(zipEntry);
                String json = new Scanner(is).useDelimiter("\\A").next();
                ArrayList<XHook> hooks = new ArrayList<>();
                JSONArray jarray = new JSONArray(json);
                for (int i = 0; i < jarray.length(); i++) {
                    XHook hook = XHook.fromJSONObject(jarray.getJSONObject(i));
                    //hook.builtin = true;
                    //if(hook.builtin == false)

                    // Link script
                    if (hook.luaScript.startsWith("@")) {
                        ZipEntry luaEntry = zipFile.getEntry("assets/" + hook.luaScript.substring(1) + ".lua");
                        if (luaEntry == null)
                            throw new IllegalArgumentException(hook.luaScript + " not found for " + hook.getId());
                        else {
                            InputStream lis = null;
                            try {
                                lis = zipFile.getInputStream(luaEntry);
                                hook.luaScript = new Scanner(lis).useDelimiter("\\A").next();
                            } finally {
                                if (lis != null)
                                    try {
                                        lis.close();
                                    } catch (IOException ignored) {
                                    }
                            }
                        }
                    }

                    hooks.add(hook);
                }

                return hooks;
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
            }
        } finally {
            if (zipFile != null)
                try {
                    zipFile.close();
                } catch (IOException ignored) {
                }
        }*/
        return null;
    }


}
