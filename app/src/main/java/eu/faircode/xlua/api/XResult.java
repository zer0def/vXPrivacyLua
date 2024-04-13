package eu.faircode.xlua.api;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import eu.faircode.xlua.DebugUtil;
import eu.faircode.xlua.utilities.DateTimeUtil;
import eu.faircode.xlua.utilities.StringUtil;

public class XResult {
    private static final String TAG = "XLua.XResult";

    public static final int STATUS_FAILED = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_UNKNOWN = 2;
    public static final int STATUS_NULL = 3;

    //Honestly we can make this into something more than simple yes no or null responses :P
    //We can wrap bundle returns with this as well as a Read<T> func
    public static XResult from(Bundle bundle) { return new XResult(bundle); }
    public static XResult from(Bundle bundle, String methodName) { return new XResult(bundle, methodName); }
    public static XResult from(Bundle bundle, String methodName, String extra) { return new XResult(bundle, methodName, extra); }

    public static XResult create() { return new XResult().setCode(STATUS_UNKNOWN); }
    public static XResult from(boolean status) { return from(status ? STATUS_SUCCESS : STATUS_FAILED, null, null, null, null); }
    public static XResult from(boolean status, String method) { return from(status ? STATUS_SUCCESS : STATUS_FAILED, method, null, null, null); }
    public static XResult from(boolean status, String method, String message) { return from(status ? STATUS_SUCCESS : STATUS_FAILED, method, message, null, null); }
    public static XResult from(boolean status, String method, String message, String error) { return from(status ? STATUS_SUCCESS : STATUS_FAILED, method, message, error, null); }
    public static XResult from(int code, String method, String message, String error, String extra) {
        return new XResult(code, message, method, error, extra);
    }

    public static void logError(String tag, XResult res, String message) {
        if(res != null) {
            res.appendErrorMessage(message, tag);
        }else if(StringUtil.isValidString(tag)) {
            Log.e(TAG, message);
        }
    }

    public static XResult fromInvalidPacket(String commandName, Class<?> clazz) {
        return XResult.create()
                .setMethodName(commandName)
                .setFailed("Reading Packet Data Failed! (Packet is NULL). Packet Class=" + clazz.getName());
    }

    public static Bundle generate(boolean status, String message) { return generate(status, message, null, null); }
    public static Bundle generate(boolean status, String message, String method) { return generate(status, message, method, null); }
    public static Bundle generate(boolean status, String message, String method, String error) { return generate(status ? STATUS_SUCCESS : STATUS_FAILED, message, method, error); }
    public static Bundle generate(int code, String method, String message, String error) {
        Bundle b = new Bundle();
        b.putInt("result", code);

        if(StringUtil.isValidString(message))
           b.putString("message", message);

        if(StringUtil.isValidString(error))
            b.putString("error", error);

        if(StringUtil.isValidString(method))
            b.putString("method", method);

        return b;
    }

    public static XResult combine(XResult a, XResult b) {
        if(a == null && b != null) return b;
        if(b == null && a != null) return a;
        if(a == null && b == null) return XResult.create().setFailed("No Message");

        XResult res = XResult.create();
        res.methodName = new StringBuilder()
                .append(" (a)=")
                .append(a.methodName)
                .append(" (b)=")
                .append(b.methodName).toString();

        res.extra = new StringBuilder()
                .append(" (a)=")
                .append(a.extra)
                .append(" (b)=")
                .append(b.extra).toString();

        res.message = new StringBuilder()
                .append(" (a)=")
                .append(a.message)
                .append(" (b)=")
                .append(b.message).toString();

        if(a.succeeded() && b.succeeded())
            return res.setSucceeded();
        else if(a.failed() && b.failed())
            return res.setFailed("(a) & (b) Task Failed!\n" + new StringBuilder()
                    .append(" (a)=")
                    .append(a.errorMessage)
                    .append(" (b)=")
                    .append(b.errorMessage).toString());
        else {
            if(a.failed())
                return res.setFailed("(a) task failed! " + a.errorMessage);
            else return res.setFailed("(b) task failed! " + b.errorMessage);
        }
    }

    private String message;
    private StringBuilder errorMessage = new StringBuilder();
    private String methodName;
    private String extra;
    private int code;
    private String rawMessage;

    private StringBuilder logs = new StringBuilder();

    public XResult() { }

    public XResult(int code) { this(code, null, null, null, null); }
    public XResult(int code, String message) { this(code, message, null, null, null); }
    public XResult(int code, String message, String method) { this(code, message, method, null, null); }
    public XResult(int code, String message, String method, String error) { this(code, message, method, error, null); }
    public XResult(int code, String message, String method, String error, String extra) {
        this.code = code;
        this.message = message;
        this.methodName = method;
        this.errorMessage.append(error);
        this.extra = extra;
    }

    public XResult(Bundle b) { this(b, null, null); }
    public XResult(Bundle b, String methodName) { this(b, methodName, null); }
    public XResult(Bundle b, String methodName, String extra) {
        this.methodName = methodName;
        this.extra = extra;
        intMessage(b);
        rawMessage = toString();
        if(DebugUtil.isDebug())
            Log.i(TAG, rawMessage);
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putInt("result", code);

        if(StringUtil.isValidString(message))
            b.putString("message", message);

        if(StringUtil.isValidString(methodName))
            b.putString("method", methodName);

        if(errorMessage.length() > 0)
            b.putString("error", errorMessage.toString());

        if(StringUtil.isValidString(extra))
            b.putString("extra", extra);

        return b;
    }

    private void intMessage(Bundle b) {
        if(b == null) {
            code = STATUS_NULL;
            return;
        }

        if(b.containsKey("result")) {
            Object res =  b.get("result");
            String s = String.valueOf(res);
            try {
                int i = Integer.parseInt(s);
                if(i == STATUS_FAILED)
                    code = STATUS_FAILED;
                else if(i == STATUS_SUCCESS)
                    code = STATUS_SUCCESS;
                else if(i == STATUS_UNKNOWN)
                    code = STATUS_UNKNOWN;
                else code = i;
            }catch (Exception e) {
                Log.w(TAG, "Data is not of Integer return: " + s);
                if(s.equalsIgnoreCase("false"))
                    code = STATUS_FAILED;
                else if(s.equalsIgnoreCase("true"))
                    code = STATUS_SUCCESS;
                else {
                    code = STATUS_UNKNOWN;
                    message += s;
                }
            }
        }

        if(b.containsKey("message")) {
            Object res =  b.get("message");
            message += String.valueOf(res);
        }

        if(b.containsKey("error") || b.containsKey("exception")) {
            Object res = null;
            if(b.containsKey("error")) res = b.get("error");
            else res = b.get("exception");
            errorMessage.append(String.valueOf(res));
        }

        if(b.containsKey("method")) {
            Object met = b.get("method");
            methodName = String.valueOf(met);
        }

        if(b.containsKey("extra")) {
            Object met = b.get("extra");
            methodName = String.valueOf(met);
        }

        //if()
        //If message is null get
    }

    public String getFullMessage() { return rawMessage; }

    public boolean succeeded() { return code == STATUS_SUCCESS; }
    public XResult setSucceeded() { return setSucceeded(null); }
    public XResult setSucceeded(String message) {
        this.code = STATUS_SUCCESS;
        if(StringUtil.isValidString(message))
            this.message = message;

        return this;
    }

    public XResult log(String message, String tag) {
        if(message != null) {
            this.logs.append(message);
            if(tag != null)
                Log.i(tag, message);
        }
        return this;
    }

    public XResult log(String message) {
        if(message != null)
            this.logs.append(message);

        return this;
    }

    public boolean failed () { return code == STATUS_FAILED; }
    public XResult setFailed() { return setFailed(null); }
    public XResult setFailed(String message) {
        this.code = STATUS_FAILED;
        if(StringUtil.isValidString(message))
            this.errorMessage.append(message);

        return this;
    }

    public boolean unknown() { return code == STATUS_UNKNOWN; }
    public XResult setUnknown() {
        this.code = STATUS_UNKNOWN;
        return this;
    }

    public XResult setResult(boolean resultStatus) {
        if(resultStatus) code = STATUS_SUCCESS;
        else code = STATUS_FAILED;
        return this;
    }

    public int getCode() { return code; }
    public XResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMethodName() { return methodName; }
    public XResult setMethodName(String name) {
        this.methodName = name;
        return this;
    }

    public String getExtra() { return extra; }
    public XResult setExtra(String extra) {
        this.extra = extra;
        return this;
    }

    public String getErrorMessage() { return errorMessage.toString(); }
    public XResult appendErrorMessage(String message) { return appendErrorMessage(message, null); }
    public XResult appendErrorMessage(String message, String tag) {
        if(StringUtil.isValidString(tag)) {
            Log.e(tag, message);
            errorMessage.append(tag);
        }

        errorMessage.append(message);
        errorMessage.append("\n");
        return this;
    }

    public String getMessage() { return message; }
    public XResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public XResult writeErrorIf(boolean ifCondition, String message) {
        if(!ifCondition) this.errorMessage.append(message);
        return this;
    }

    public XResult writeIf(boolean ifCondition, String message) {
        if(!ifCondition) this.message = message;
        return this;
    }

    public String getResultMessage() {
        StringBuilder sb = new StringBuilder();

        if(code == STATUS_NULL)
            sb.append("Returned NULL but should have completed :P (ignore this message)");
        else if(code == STATUS_FAILED) {
            sb.append("Failed to execute command! ");
            if(errorMessage != null)
                sb.append(" error=").append(errorMessage);
            else if(extra != null)
                sb.append(" extra=").append(extra);
        }
        else if(code == STATUS_UNKNOWN)
            sb.append("Command execution status Unknown ?");
        else if(code == STATUS_SUCCESS)
            sb.append("Command executed successfully!");
        else if(code > STATUS_NULL) {
            sb.append("code=(");
            sb.append(code);
            sb.append(")");
        }

        return sb.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("result:\n").append(getResultMessage()).append("\n")
                .append("error:\n").append(errorMessage).append("\n")
                .append("message:\n").append(message).append("\n")
                .append("method:\n").append(methodName).append("\n")
                .append("extra:\n").append(extra)
                .toString();
    }
}
