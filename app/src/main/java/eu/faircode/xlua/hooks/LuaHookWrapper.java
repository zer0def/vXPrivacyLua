package eu.faircode.xlua.hooks;

import android.content.Context;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

import de.robv.android.xposed.XC_MethodHook;
import eu.faircode.xlua.api.objects.xlua.hook.xHook;
import eu.faircode.xlua.api.objects.xlua.setting.xSettingConversions;
import eu.faircode.xlua.XParam;

public class LuaHookWrapper {
    private static final String TAG = "XLua.LuaFieldHook";

    public final Globals globals;
    public final LuaClosure closure;
    public final LuaValue func;
    public final LuaValue[] args;

    public final boolean isMemberOrMethod;

    public LuaHookWrapper(
            Context context,
            xHook hook,
            Map<String, String> settings,
            Prototype compiledScript,
            String function,
            XC_MethodHook.MethodHookParam param,
            Globals globals) {

        isMemberOrMethod = true;

        //Thread thread = Thread.currentThread();
        //if (!threadGlobals.containsKey(thread))
        //    threadGlobals.put(thread, XHookUtil.getGlobals(context, hook, settings));

        //Globals globals = threadGlobals.get(thread);
        this.globals = globals;
        // Define functions
        closure = new LuaClosure(compiledScript, globals);
        closure.call();

        // Check if function exists
        func = globals.get(function);
        //if(!isValid()) return;
        //if (func.isnil())
        //    return;

        // Build arguments
        args = new LuaValue[]{
                CoerceJavaToLua.coerce(hook),
                //Create XPARAM here
                CoerceJavaToLua.coerce(new XParam(context, param, settings))
        };
    }

    public LuaHookWrapper(Context context, xHook hook, Map<String, String> settings, Prototype compiledScript, Field field) {
        isMemberOrMethod = false;
        // Initialize Lua runtime
        globals = XHookUtil.getGlobals(context, hook, settings);
        closure = new LuaClosure(compiledScript, globals);
        closure.call();

        func = globals.get("after");
        //if(!isValid()) return;

        args = new LuaValue[]{
                CoerceJavaToLua.coerce(hook),
                CoerceJavaToLua.coerce(new XParam(context, field, settings))
        };
    }

    public static LuaHookWrapper createMember(Context context, xHook hook, Map<String, String> settings, Prototype compiledScript, String function, XC_MethodHook.MethodHookParam param, Globals globals) {
        return new LuaHookWrapper(context, hook, settings, compiledScript, function, param, globals);
    }

    public static LuaHookWrapper createField(Context context, xHook hook, Map<String, String> settings, Prototype compiledScript, Field field) {
        return new LuaHookWrapper(context, hook, settings, compiledScript, field);
    }

    public Varargs invoke() {
        if(!isValid()) return null;
        return func.invoke(args);
    }

    public boolean isValid() {
        return func != null && !func.isnil();
    }
}
