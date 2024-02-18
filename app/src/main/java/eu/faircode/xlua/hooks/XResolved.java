package eu.faircode.xlua.hooks;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import eu.faircode.xlua.utilities.ReflectUtil;

public class XResolved {
    private static final String TAG = "XLua.XResolved";

    public final Class<?> clazz;
    public final String methodName;
    public final Class<?>[] paramTypes;
    public final Class<?> returnType;

    public void throwIfMismatchReturn(Member member) throws Throwable {
        throwIfMismatchReturn(((Method)member).getReturnType());
    }

    public void throwIfMismatchReturn(Class<?> compareType) throws Throwable {
        if(!isConstructor() && !returnTypeIsValid(compareType))
            throw new Throwable("Invalid return type " + compareType + " got " + returnType);
    }

    public boolean returnTypeIsValid(Class<?> compareType) {
        if(returnType == null && compareType == null)
            return true;

        return compareType.isAssignableFrom(returnType);
    }

    public boolean isConstructor() {
        return methodName == null;
    }

    public boolean isField() {
        return XHookUtil.isField(methodName);
    }

    public Member tryGetMember() {
        try {
            return getMember();
        }catch (NoSuchMethodException e) {
            String c = clazz != null ? clazz.getName() : " null ";
            Log.e(TAG, "Failed to resolve Method! " + " class=" + c  + " method=" + methodName + " e=\n" + e + "\n" + Log.getStackTraceString(e));
            return null;
        }
    }

    public Member getMember() throws NoSuchMethodException {
        return ReflectUtil.resolveMember(clazz, methodName, paramTypes);
    }

    public Field tryGetField(boolean setAccessible) {
        try {
            return getField(setAccessible);
        }catch (NoSuchFieldException e) {
            Log.e(TAG, "Failed to resolve Field! " + e + "\n" + Log.getStackTraceString(e));
            return null;
        }
    }

    public Field getField(boolean setAccessible) throws NoSuchFieldException {
        Field field = ReflectUtil.resolveField(clazz, methodName.substring(1), returnType);
        if(setAccessible) field.setAccessible(true);
        return field;
    }

    public XResolved(Class<?> clazz, String methodName, Class<?>[] paramTypes, Class<?> returnType) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.returnType = returnType;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("class=");
        b.append(clazz.toString());
        if(!isField()) {
            b.append(" member=");
            b.append(isConstructor() ? "constructor" : methodName);

            if(paramTypes != null) {
                int ln = paramTypes.length;
                if(ln > 0) {
                    b.append(" params=(");
                    int nd = ln - 1;
                    for(int i = 0; i < ln; i++) {
                        b.append(paramTypes[i].getName());
                        if(i != nd) {
                            b.append(",");
                        }else {
                            b.append(")");
                        }
                    }
                }
            }
        }

        if(returnType != null) {
            b.append(" return=");
            b.append(returnType.getName());
        }

        return b.toString();
    }
}
