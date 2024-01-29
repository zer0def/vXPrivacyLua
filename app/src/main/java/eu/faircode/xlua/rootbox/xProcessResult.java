package eu.faircode.xlua.rootbox;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class xProcessResult {
    private static final String TAG = "XLua.xProcessResult";

    private boolean outException = true;
    private String resultOutput;
    private String errorOutput;
    private String command;

    public String getResultOutput() { return resultOutput; }
    public String getErrorOutput() { return errorOutput; }
    public String getCommand() { return command; }
    public boolean isOuterException() { return outException; }

    public static xProcessResult exec(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.wait();
            return new xProcessResult(p, command);
        }catch (Exception e) {
            Log.e(TAG, "Failed to execute command: " + command);
            return new xProcessResult();
        }
    }

    public xProcessResult() { }
    public xProcessResult(Process process, String command) { this(process.getInputStream(), process.getErrorStream(), process.exitValue(), command); }
    public xProcessResult(InputStream resultOutput, InputStream errorOutput, int exitValue, String command) {
        if(!(resultOutput == null && errorOutput == null)) {
            outException = false;

            StringBuilder inpSb = new StringBuilder();
            inpSb.append("exec[");
            inpSb.append(command);
            inpSb.append("] ");
            inpSb.append("exitValue=");
            inpSb.append(exitValue);
            inpSb.append("\n");

            String lineOne = inpSb.toString();
            String str = null;
            if(resultOutput != null) {
                StringBuilder resultSb = new StringBuilder();
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(resultOutput));
                try {
                    while ((str = stdInput.readLine()) != null) {
                        resultSb.append(str);
                        resultSb.append("\n");
                    }

                    this.resultOutput = "RESULT=\n" + lineOne + "\n" + resultSb;
                    Log.i(TAG, this.resultOutput);
                }catch (Exception e) {
                    Log.e(TAG, "Failed to read result: " + command + " \n" + e);
                }
            }

            if(errorOutput != null) {
                StringBuilder errorSb = new StringBuilder();
                // Get the standard error (error output of the command)
                BufferedReader stdError = new BufferedReader(new InputStreamReader(errorOutput));
                try {
                    while ((str = stdError.readLine()) != null) {
                        errorSb.append(str);
                        errorSb.append("\n");
                    }

                    this.errorOutput = "ERROR=\n" + lineOne + "\n" + errorSb;
                    Log.i(TAG, this.errorOutput);
                }catch (Exception e) {
                    Log.e(TAG, "Failed to read error: " + command + " \n" + e);
                }
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return command + "\n" + resultOutput + "\n" + errorOutput;
    }
}
