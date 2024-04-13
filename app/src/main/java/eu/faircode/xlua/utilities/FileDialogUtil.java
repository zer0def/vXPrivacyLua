package eu.faircode.xlua.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import eu.faircode.xlua.AdapterConfig;
import eu.faircode.xlua.api.configs.MockConfig;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.logger.XLog;

public class FileDialogUtil {
    private static final String TAG = "XLua.FileDialogUtil";

    public static MockConfig readConfig(Context context, Uri selectedFileUri) {
        String contents = readAllFile(context, selectedFileUri);
        try {
            MockConfig config = new MockConfig();
            config.fromJSONObject(new JSONObject(contents));
            return config;
        }catch (JSONException e) {
            XLog.e("Failed to read config", e, true);
            return null;
        }
    }

    public static String readAllFile(Context context, Uri selectedFileUri) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = context.getContentResolver().openInputStream(selectedFileUri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append('\n');

        } catch (IOException e) {
            XLog.e("Error Reading File: " + selectedFileUri.getPath(), e, true);
        }

        return sb.toString();
    }

    public static boolean saveConfigSettings(Context context, Uri selectedFileUri, AdapterConfig config) {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(context, selectedFileUri);
        if (pickedDir != null) {
            String fName = config.getConfigName();
            if(fName == null || fName.isEmpty())
                return false;

            DocumentFile newFile = pickedDir.createFile("application/json", fName + ".json");
            if (newFile != null) {
                try (OutputStream out = context.getContentResolver().openOutputStream(newFile.getUri())) {
                    List<LuaSettingExtended> settings = config.getEnabledSettings();
                    if(settings == null || settings.isEmpty())
                        throw new IOException("Settings is Empty");

                    MockConfig mockConfig = new MockConfig();
                    mockConfig.setName(fName);
                    mockConfig.setSettings(settings);

                    byte[] bys = mockConfig.toJSON().getBytes();
                    if(out == null) {
                        XLog.e("Out stream is NULL...", new Throwable(), true);
                        return false;
                    }

                    out.write(bys);
                    XLog.i("Config File written successfully: " + fName);
                    return true;
                } catch (IOException e) {
                    XLog.e("Error writing to config file: " + fName, e, true);
                }catch (JSONException e) {
                    XLog.e("Failed to Read Data from Config: " + fName, e, true);
                }
            }
        }

        return false;
    }
}
