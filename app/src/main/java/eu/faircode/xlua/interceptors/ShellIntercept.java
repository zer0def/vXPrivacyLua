package eu.faircode.xlua.interceptors;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.interceptors.shell.CommandInterceptor;
import eu.faircode.xlua.interceptors.shell.handlers.GetPropIntercept;
import eu.faircode.xlua.interceptors.shell.ShellInterceptionResult;
import eu.faircode.xlua.interceptors.shell.handlers.LogcatIntercept;
import eu.faircode.xlua.interceptors.shell.handlers.MemInfoIntercept;
import eu.faircode.xlua.interceptors.shell.handlers.SuIntercept;
import eu.faircode.xlua.interceptors.shell.handlers.UnameIntercept;

public class ShellIntercept {
    private static final String TAG = "XLua.ShellIntercept";
    private static final List<CommandInterceptor> interceptors = new ArrayList<>();

    public static ShellInterceptionResult intercept(ShellInterceptionResult results) {
        try {
            if(results.isValueValid()) {
                Log.i(TAG, "Checking command: " + results.getOriginalValue());
                for(CommandInterceptor interceptor : getInterceptors()) {
                    if(interceptor.containsCommand(results.getOriginalValue())) {
                        if(interceptor.interceptCommand(results)) {
                            Log.w(TAG, "Malicious command! " + results.getOriginalValue());
                            return results;
                        }
                    }
                }
            }
        }catch (Exception e) { Log.e(TAG, "Error with Interceptor Core: e=" + e); }
        return results;
    }

    public static List<CommandInterceptor> getInterceptors() {
        if(interceptors.isEmpty()) {
            interceptors.add(new GetPropIntercept());
            interceptors.add(new MemInfoIntercept());
            interceptors.add(new SuIntercept());
            interceptors.add(new UnameIntercept());
            interceptors.add(new LogcatIntercept());
        }

        return interceptors;
    }
}
