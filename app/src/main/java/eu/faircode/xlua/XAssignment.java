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

import android.os.Parcel;

import java.util.LinkedHashMap;

/*class XAssignment  {
    public XHookIO hook;
    public long installed = -1;
    public long used = -1;
    public boolean restricted = false;
    public String exception;

    public XAssignment() { }
    public XAssignment(XHookIO hook) {
        this.hook = hook;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XAssignment))
            return false;
        XAssignment other = (XAssignment) obj;
        return this.hook.getId().equals(other.hook.getId());
    }

    @Override
    public int hashCode() {
        return this.hook.getId().hashCode();
    }

    protected XAssignment(Parcel in) {
        this.hook = in.readParcelable(XHook.class.getClassLoader());
        this.installed = in.readLong();
        this.used = in.readLong();
        this.restricted = (in.readByte() != 0);
        this.exception = in.readString();
    }

    public static class Table {
        public static final String name = "assignment";
        public static final LinkedHashMap<String, String> columns = new LinkedHashMap<String, String>() {{
            put("package", "TEXT");
            put("uid", "INTEGER");
            put("hook", "TEXT");
            put("installed", "INTEGER");
            put("used", "INTEGER");
            put("restricted", "INTEGER");
            put("exception", "TEXT");
            put("old", "TEXT");
            put("new", "TEXT");
        }};
    }
}*/
