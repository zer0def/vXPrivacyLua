package eu.faircode.xlua.rootbox;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class xFileCommands {
    private static final String TAG = "XLua.xFileCommands";

    public static String getChmodCommand(String path, int mode, boolean recursive) {
        StringBuilder sb = new StringBuilder();
        sb.append("chmod ");

        if(recursive)
            sb.append("-R ");

        sb.append(mode);
        sb.append("'");
        sb.append(path);
        sb.append("'");
        return sb.toString();
    }

    public static String getChownCommand(String path, int ownerUid, int groupUid, boolean recursive) {
        StringBuilder sb = new StringBuilder();
        sb.append("chown ");
        if(recursive)
            sb.append("-R ");

        sb.append(ownerUid);
        sb.append(":");
        sb.append(groupUid);
        sb.append(" '");
        sb.append(path);
        sb.append("'");
        return sb.toString();
    }

    public static String getShredCommand(String path, boolean recursive) {
        StringBuilder sb = new StringBuilder();
        sb.append("shred ");
        if(recursive)
            sb.append("-R ");

        sb.append("'");
        sb.append(path);
        sb.append("'");
        return sb.toString();
    }

    public static String getRMCommand(String path, boolean recursive) {
        StringBuilder sb = new StringBuilder();
        sb.append("rm ");
        if(recursive)
            sb.append("-R ");

        sb.append("'");
        sb.append(path);
        sb.append("'");
        return sb.toString();
    }
}
