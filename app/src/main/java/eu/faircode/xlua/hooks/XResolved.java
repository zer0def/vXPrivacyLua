package eu.faircode.xlua.hooks;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.utilities.ReflectUtil;

public class XResolved {
    //private static final String TAG = "XLua.XResolved";
    public final Class<?> clazz;
    public final String methodName;
    public final Class<?>[] paramTypes;
    public final Class<?> returnType;

    public void throwIfMismatchReturn(Member member) throws Throwable {
        if(isConstructor()) return;
        throwIfMismatchReturn(((Method)member).getReturnType());
    }

    public void throwIfMismatchReturn(Class<?> compareType) throws Throwable {
        if(!isConstructor() && !returnTypeIsValid(compareType))
            throw new Throwable("Invalid return type " + compareType + " got needed: " + returnType);
    }

    public boolean hasMismatchReturn(Member member) throws Throwable {
        if(isConstructor()) return false;
        try {
            return hasMismatchReturn(((Method)member).getReturnType());
        }catch (Exception e) {
            XLog.e("Failed to check Type if MisMatch...", e);
            return true;
        }
    }

    public boolean hasMismatchReturn(Class<?> compareType) throws Throwable {
        if(!isConstructor() && !returnTypeIsValid(compareType)) {
            XLog.e("Invalid return type " + compareType + " got needed: " + returnType);
            return true;
        } return false;
    }

    public boolean returnTypeIsValid(Class<?> compareType) {
        if(ReflectUtil.isReturnTypeNullOrVoid(compareType) && ReflectUtil.isReturnTypeNullOrVoid(returnType))
            return true;

        if(ReflectUtil.isReturnTypeNullOrVoid(compareType) || ReflectUtil.isReturnTypeNullOrVoid(returnType))
            return false;

        return compareType.isAssignableFrom(returnType);
    }

    public boolean isConstructor() {
        return methodName == null || TextUtils.isEmpty(methodName);
    }
    public boolean isField() {
        return XHookUtil.isField(methodName);
    }

    public Member tryGetMember() {
        try {
            return getMember();
        }catch (NoSuchMethodException e) {
            XLog.e("Failed to resolve Member: " + this , e, true);
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
            XLog.e("Failed to Resolve Filed: " + this, e, true);
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
        if(clazz != null) {
            b.append("class=");
            b.append(clazz.getName());
        }

        if(methodName != null) {
            b.append(" member=");
            b.append(isConstructor() ? "constructor" : methodName);
        }

        if(returnType != null) {
            b.append(" return=");
            b.append(returnType.getName());
        }

        if(!isField()) {
            if(paramTypes != null) {
                int ln = paramTypes.length;
                if(ln > 0) {
                    b.append("\nparams=(");
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

        return b.toString();
    }
}
