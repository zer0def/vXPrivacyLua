package eu.faircode.xlua.api.xstandard;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import eu.faircode.xlua.api.xstandard.interfaces.IJsonSerial;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.utilities.StringUtil;

public class JsonHelper {
    private static final String ERROR_JSON_HOOKS = "Parsing HOOKS.JSON using the (non hook) JSON Parser... please use the correct JSON Parser avoid this one for Hooks! ";
    private static final String BLACKLISTED_JSON = "hooks.json";

    /**
     * Read a Collection of IJsonSerial Elements from Json file in Apk assets
     * <p>
     * Will search for the Target Json File, then once found it will Parse the Json Contents into a Collection of IJsonSerial Elements.
     * Ensure the Target Class Object to parse each Json Element into has a No Argument Constructor and overrides "fromJSONObject" function from IJsonSerial.
     * </p>
     * @param apk Base PackageName to search for Json File
     * @param jsonName Name of target Json to Search For
     * @param stopOnFirst Stop when on the first occurrence of Target Json
     * @param typeClass Class (inherits IJsonSerial) to Read each Json Element as
     * @return Collection of IJsonSerial Elements that were Parsed from the Json File
     */
    public static <T extends IJsonSerial> Collection<T> findJsonElementsFromAssets(String apk, String jsonName, boolean stopOnFirst, Class<T> typeClass) {
        Collection<T> list = new ArrayList<>();
        jsonName = StringUtil.trimEnsureEnd(jsonName, ".json");
        if(!StringUtil.isValidString(jsonName)) {
            XLog.e("Parameter Target Json was Null or Empty... ");
            return list;
        }

        String logEnd = " Json Name=" + jsonName + " Apk=" + apk + " Stop On First Json=" + stopOnFirst + " Class Name=" + typeClass.getName();
        XLog.i("Searching for Json File in Assets: " + logEnd);
        if(jsonName.equalsIgnoreCase(BLACKLISTED_JSON)) XLog.w(ERROR_JSON_HOOKS + logEnd);

        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(apk);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if(!entry.isDirectory() && name.startsWith("assets/") && name.endsWith(jsonName)) {
                    XLog.i("Found Target Json: " + jsonName + "  Full asset File path=" + name);
                    Collection<T> items = parseJson(entry, zipFile, jsonName, typeClass);
                    if(!items.isEmpty()) list.addAll(items);
                    if(stopOnFirst) break;
                }
            }
        }catch (Exception e) {
            XLog.e("Failed searching for Target Json: " + logEnd, e, true);
            return list;
        }finally { if(zipFile != null) try { zipFile.close(); } catch (Exception ignored) { } }
        XLog.i("Finished Searching & Parsing Json File! Elements Parsed Count=" + list.size() + logEnd);
        return list;
    }

    /**
     * Read a Collection of IJsonSerial Elements from a Found Json File in Apk assets
     * <p>
     * Parse a ZipFile that is suppose to represent the Target / Found Json File.
     * Pass off the results of the targeted Json Files or File to then Parse Json Contents into a Collection of IJsonSerial
     * Ensure the Target Class Object to parse each Json Element into has a No Argument Constructor and overrides "fromJSONObject" function from IJsonSerial.
     * </p>
     * @param entry Entry to the root Zip file
     * @param zipFile ZipFile that represents Json file to be Parsed into IJsonSerial Elements
     * @param jsonName Name of target Json that you will be parsing
     * @param typeClass Class (inherits IJsonSerial) to Read each Json Element as
     * @return Collection of IJsonSerial Elements that were Parsed from the Json File
     */
    public static <T extends IJsonSerial> Collection<T> parseJson(ZipEntry entry, ZipFile zipFile, String jsonName, Class<T> typeClass) {
        Collection<T> list = new ArrayList<>();
        jsonName = StringUtil.trimEnsureEnd(jsonName, ".json");
        if(!StringUtil.isValidString(jsonName)) {
            XLog.e("Parameter Target Json was Null or Empty... ");
            return list;
        }

        String entryName = entry.getName();
        String entryPath = entryName.substring(0, entryName.length() - jsonName.length() - 1);

        String logEnd = " Path=" + entryPath + " Json Name=" + jsonName + " Class Name=" + typeClass.getName();
        XLog.i("Parsing Json From Assets:" + logEnd);
        if(jsonName.equalsIgnoreCase(BLACKLISTED_JSON)) XLog.w(ERROR_JSON_HOOKS + logEnd);

        InputStream is = null;
        try {
            is = zipFile.getInputStream(entry);
            String json = new Scanner(is).useDelimiter("\\A").next();
            JSONArray jArray = new JSONArray(json);
            for (int i = 0; i < jArray.length(); i++) {
                T item = typeClass.newInstance();
                item.fromJSONObject(jArray.getJSONObject(i));
                list.add(item);
            }
        }catch (JSONException je) {
            XLog.e("Failed to Parse/Read Json Element to IJsonSerial Object. Function [fromJSONObject] failed for IJsonSerial class=" + typeClass.getName() + logEnd, je, true);
            return list;
        }catch (IllegalAccessException ie) {
            XLog.e("Failed to Construct IJsonSerial class=" + typeClass.getName() + logEnd, ie, true);
            return list;
        }catch (Exception e) {
            XLog.e("Failed to parse Json From Assets!" + logEnd, e, true);
            return list;
        }finally { if(is != null) try { is.close(); } catch (Exception ignored) { }}

        XLog.i("Finished Parsing JSON From Assets! Elements Parsed Count=" + list.size() + logEnd);
        return list;
    }
}
