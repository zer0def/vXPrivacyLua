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
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eu.faircode.xlua.api.objects.xlua.hook.Assignment;

/*public class XApp {
    public String packageName;
    public int uid;
    public int icon;
    public String label;
    public boolean enabled;
    public boolean persistent;
    public boolean system;
    public boolean forceStop = true;
    public List<Assignment> assignments;

    public XApp() { }
    public XApp(Parcel in) {
        this.packageName = in.readString();
        this.uid = in.readInt();
        this.icon = in.readInt();
        this.label = in.readString();
        this.enabled = (in.readByte() != 0);
        this.persistent = (in.readByte() != 0);
        this.system = (in.readByte() != 0);
        this.forceStop = (in.readByte() != 0);
        this.assignments = in.createTypedArrayList(Assignment.CREATOR);
    }

    public List<Assignment> getAssignments(String group) {
        if (group == null)
            return assignments;

        List<Assignment> filtered = new ArrayList<>();
        for (Assignment assignment : assignments)
            if (group.equals(assignment.getHook().getGroup()))
                filtered.add(assignment);

        return filtered;
    }

    private IListener listener = null;

    void setListener(IListener listener) {
        this.listener = listener;
    }

    void notifyAssign(Context context, String groupName, boolean assign) {
        if (this.listener != null)
            this.listener.onAssign(context, groupName, assign);
    }

    public interface IListener {
        void onAssign(Context context, String groupName, boolean assign);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XApp))
            return false;
        XApp other = (XApp) obj;
        return (this.packageName.equals(other.packageName) && this.uid == other.uid);
    }

    @Override
    public int hashCode() {
        return this.packageName.hashCode();
    }
}*/
