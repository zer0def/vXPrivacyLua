/*
    This file is part of XPrivacyLua.

    XPrivacyLua is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    XPrivacyLua is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with XPrivacyLua.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2017-2019 Marcel Bokhorst (M66B)
 */

package eu.faircode.xlua;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import eu.faircode.xlua.hooks.XHookUtil;

/*public class XHook {
    private final static String TAG = "XLua.XHook";
    //we can jsut do a key exchange system

    protected boolean builtin = false;
    protected String collection;
    protected String group;
    protected String name;
    protected String author;
    protected int version = 0;
    protected String description;

    protected String className;
    protected String resolvedClassName = null;
    protected String methodName;
    protected String[] parameterTypes;
    protected String returnType;

    protected int minSdk;
    protected int maxSdk;
    protected int minApk;
    protected int maxApk;
    protected String[] excludePackages;
    protected boolean enabled;
    protected boolean optional;
    protected boolean usage;
    protected boolean notify;

    protected String luaScript;

    protected String[] settings;

    public final static int FLAG_WITH_LUA = 2; // =PARCELABLE_ELIDE_DUPLICATES

    protected XHook() { }
    protected XHook(Parcel in) {
        this.builtin = (in.readByte() != 0);
        this.collection = in.readString();
        this.group = in.readString();
        this.name = in.readString();
        this.author = in.readString();
        this.version = in.readInt();
        this.description = in.readString();
        this.className = in.readString();
        this.resolvedClassName = in.readString();
        this.methodName = in.readString();
        this.parameterTypes = in.createStringArray();
        this.returnType = in.readString();
        this.minSdk = in.readInt();
        this.maxSdk = in.readInt();
        this.minApk = in.readInt();
        this.maxApk = in.readInt();
        this.excludePackages = in.createStringArray();
        this.enabled = (in.readByte() != 0);
        this.optional = (in.readByte() != 0);
        this.usage = (in.readByte() != 0);
        this.notify = (in.readByte() != 0);
        this.luaScript = in.readString();
        this.settings = in.createStringArray();
    }

    public String getId() {
        return this.collection + "." + this.name;
    }

    public boolean isBuiltin() {
        return this.builtin;
    }

    //do note reason most likely not being exposed , aka private is to prevent the Script
    //From modifying the actual Hook it self ?
    public void setIsBuiltIn(boolean isBuiltIn) { this.builtin = isBuiltIn; }

    @SuppressWarnings("unused")
    public String getCollection() {
        return this.collection;
    }

    public String getGroup() {
        return this.group;
    }

    public String getName() {
        return this.name;
    }

    @SuppressWarnings("unused")
    public String getAuthor() {
        return this.author;
    }

    @SuppressWarnings("unused")
    public String getDescription() {
        return this.description;
    }

    @SuppressWarnings("unused")
    public String getClassName() {
        return this.className;
    }

    public String getResolvedClassName() { return (this.resolvedClassName == null ? this.className : this.resolvedClassName); }

    public String getMethodName() {
        return this.methodName;
    }

    public String[] getParameterTypes() {
        return this.parameterTypes;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public boolean isAvailable(String packageName, List<String> collection) {
        if (!collection.contains(this.collection))
            return false;

        if (!this.enabled)
            return false;

        if (Build.VERSION.SDK_INT < this.minSdk || Build.VERSION.SDK_INT > this.maxSdk)
            return false;

        if (packageName == null)
            return true;

        if (this.excludePackages == null)
            return true;

        boolean included = true;
        for (String excluded : this.excludePackages)
            if (Pattern.matches(excluded, packageName)) {
                included = false;
                break;
            }
        return included;
    }

    public boolean isAvailable(int versionCode) { return (versionCode >= this.minApk && versionCode <= maxApk); }

    public boolean isOptional() {
        return this.optional;
    }

    public boolean doUsage() {
        return this.usage;
    }

    public boolean doNotify() {
        return this.notify;
    }

    public String getLuaScript() {
        return this.luaScript;
    }

    public void setLuaScript(String contents) { this.luaScript = contents; }

    public void resolveClassName(Context context) {
        String rName = XHookUtil.resolveClassName(context, this.className);
        if(rName == null)
            return;
        this.resolvedClassName = rName;
    }

    public static ArrayList<XHookIO> readHooks(Context context, String apk) throws IOException, JSONException { return XHookUtil.readHooksEx(context, apk); }

    public void validate() {
        if (TextUtils.isEmpty(this.collection))
            throw new IllegalArgumentException("collection missing");
        if (TextUtils.isEmpty(this.group))
            throw new IllegalArgumentException("group missing");
        if (TextUtils.isEmpty(this.name))
            throw new IllegalArgumentException("name missing");
        if (TextUtils.isEmpty(this.author))
            throw new IllegalArgumentException("author missing");
        if (TextUtils.isEmpty(this.className))
            throw new IllegalArgumentException("class name missing");
        if (parameterTypes == null)
            throw new IllegalArgumentException("parameter types missing");
        if (TextUtils.isEmpty(this.luaScript))
            throw new IllegalArgumentException("Lua script missing");
    }

    @Override
    public String toString() {
        return this.getId() + "@" + this.className + ":" + this.methodName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XHook))
            return false;
        XHook other = (XHook) obj;
        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public static class Table {
        public static final String name = "hook";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("id", "TEXT");
            put("definition", "TEXT");
        }};
    }
}*/
