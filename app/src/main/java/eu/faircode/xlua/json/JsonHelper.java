package eu.faircode.xlua.json;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.database.IDatabaseHelper;

public class JsonHelper {
    private static final String TAG = "XLua.JsonHelper";

    //TBH We only need two reader types
    //One for hooks (since it can be defined multiple times)
    //One for single JSON files like MAPS, PROPS etc ones that have one file

    public static <T extends IJsonHelper> List<T> findJsonElementsFromAssets(
            String apk, String jsonName, boolean stopOnFirst, Class<T> typeClass) {

        if(!jsonName.endsWith(".json"))
            jsonName += ".json";

        Log.i(TAG, "Reading JSON Elements [" + apk + "][" + jsonName + "]");

        List<T> list = new ArrayList<>();
        ZipFile zipFile = null;

        try {
            zipFile = new ZipFile(apk);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if(!entry.isDirectory() && name.startsWith("assets/") && name.endsWith(jsonName)) {
                    Log.i(TAG, "Found JSON: " + name);
                    List<T> items = parseJson(entry, zipFile, jsonName, typeClass);
                    if(!items.isEmpty())
                        list.addAll(items);

                    if(stopOnFirst)
                        break;
                }
            }

        }catch (Exception e) {
            Log.e(TAG, "Failed to Read JSON Elements::\n" + e + "\n" + Log.getStackTraceString(e));
            return list;
        }finally {
            if(zipFile != null) try { zipFile.close(); }catch (Exception ex) { }
        }

        Log.i(TAG, "Finished Parsing JSON Objects from Assets. Count=" + list.size() + " JSON File Name=" + jsonName);
        return list;
    }

    //Lets avoid reading hooks with this FOR NOW
    //Just Read elements that dont need links to it
    public static <T extends IJsonHelper> List<T>
        parseJson(
                ZipEntry entry, ZipFile zipFile, String jsonName, Class<T> typeClass) {

        List<T> list = new ArrayList<>();
        String entryName = entry.getName();
        String entryPath = entryName.substring(0, entryName.length() - jsonName.length() - 1);
        //boolean isBuiltIn = entryName.endsWith("assets/hooks.json");

        Log.i(TAG, "Parsing JSON. Path=" + entryPath + " Name=" + entryName);

        InputStream is = null;
        try {
            is = zipFile.getInputStream(entry);
            String json = new Scanner(is).useDelimiter("\\A").next();
            JSONArray jarray = new JSONArray(json);
            for(int i = 0; i < jarray.length(); i++) {
                T item = typeClass.newInstance();
                item.fromJSONAssets(jarray.getJSONObject(i), entryPath);
                list.add(item);
            }

        }catch (Exception e) {
            Log.e(TAG, "Failed to parse JSON File::\n" + e + "\n" + Log.getStackTraceString(e));
            return list;
        }finally {
            if(is != null) try { is.close(); }catch (Exception ex) { }
        }

        Log.i(TAG, "Finished Parsing JSON, Count=" + list.size() + " File=" + jsonName);
        return list;
    }
}
