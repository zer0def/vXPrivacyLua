package eu.faircode.xlua.hooks;

import android.content.Context;
import android.util.Log;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import eu.faircode.xlua.XParam;

public class LuaHook extends VarArgFunction {
    private static final String TAG = "XLua.LuaHook";
    private Context context;
    private Map<String, String> settings;
    private Map<String, Integer> propSettings;
    private Map<String, String> propMaps;
    private String key;

    LuaHook(Context context, Map<String, String> settings, Map<String, Integer> propSettings, Map<String, String> propMaps, String key) {
        this.context = context;
        this.settings = settings;
        this.propSettings = propSettings;
        this.propMaps = propMaps;
        this.key = key;
    }

    @Override
    public Varargs invoke(final Varargs args) {
        Class<?> cls = args.arg(1).checkuserdata().getClass();
        String m = args.arg(2).checkjstring();
        args.arg(3).checkfunction();
        //Log.i(TAG, "Dynamic hook " + cls.getName() + "." + m);
        final LuaValue fun = args.arg(3);
        final List<LuaValue> xargs = new ArrayList<>();
        for (int i = 4; i <= args.narg(); i++)
            xargs.add(args.arg(i));

        XposedBridge.hookAllMethods(cls, m, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                execute("before", param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                execute("after", param);
            }

            private void execute(String when, MethodHookParam param) {
                //I dont think this shit is invoked..
                //Log.i(TAG, "Dynamic invoke " + param.method);
                List<LuaValue> values = new ArrayList<>();
                values.add(LuaValue.valueOf(when));
                values.add(CoerceJavaToLua.coerce(new XParam(context, param, settings, propSettings, propMaps, key)));
                for (int i = 0; i < xargs.size(); i++)
                    values.add(xargs.get(i));
                fun.invoke(values.toArray(new LuaValue[0]));
            }
        });

        return LuaValue.NIL;
    }
}