package eu.faircode.xlua.utilities;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellUtils {
    private static final String TAG = "XLua.ShellUtils";
    public static final List<String> USELESS_COMMANDS = Arrays.asList("cd", "echo", "ls", "grep", "nano", "su", "sudo", "cat", "tail", "more", "head", "less", "shred", "awk", "sed", "tree", "cut", "paste", "sort", "uniq", "wc", "tr", "fmt", "pr", "join", "tac", "bat", "strings", "xxd", "hexdump", "diff", "comm", "fold");


    public static ProcessBuilder getEchoProcessBuilder(String command) {
        ProcessBuilder builder = new ProcessBuilder();
        String[] cmdline = { "sh", "-c", "echo " + command };
        builder.command(cmdline);
        return builder;
    }

    public static Process echo(String command) {
        try {
            if(command == null) {
                Log.e(TAG, "Command passed to the echo was null filling in...");
                command = "Hello";
            }

            String[] cmdline = { "sh", "-c", "echo " + command};
            return Runtime.getRuntime().exec(cmdline);
        }catch (Exception e) {
            Log.e(TAG, "Failed to start Dummy Process: " + e);
            return null;
        }
    }
}
