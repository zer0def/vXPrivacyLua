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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import eu.faircode.xlua.api.hook.LuaHooksGroup;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.hook.XLuaHook;

import eu.faircode.xlua.api.app.XLuaApp;
import eu.faircode.xlua.utilities.SettingUtil;


public class AdapterGroup extends RecyclerView.Adapter<AdapterGroup.ViewHolder> {
    private static final String TAG = "XLua.Group";

    private XLuaApp app;
    private List<LuaHooksGroup> groups = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
        final View itemView;
        final ImageView ivException;
        final ImageView ivInstalled;
        final TextView tvUsed;
        final TextView tvGroup;
        final AppCompatCheckBox cbAssigned;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            ivException = itemView.findViewById(R.id.ivException);
            ivInstalled = itemView.findViewById(R.id.ivInstalled);
            tvUsed = itemView.findViewById(R.id.tvUsed);
            tvGroup = itemView.findViewById(R.id.tvGroup);
            cbAssigned = itemView.findViewById(R.id.cbAssigned);
        }

        private void wire() {
            ivException.setOnClickListener(this);
            tvGroup.setOnClickListener(this);
            cbAssigned.setOnCheckedChangeListener(this);
        }

        private void unWire() {
            ivException.setOnClickListener(null);
            tvGroup.setOnClickListener(null);
            cbAssigned.setOnCheckedChangeListener(null);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            LuaHooksGroup group = groups.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.ivException:
                    StringBuilder sb = new StringBuilder();
                    for (LuaAssignment assignment : app.getAssignments(group.name))
                        if (assignment.getHook().getGroup().equals(group.name))
                            if (assignment.getException() != null) {
                                sb.append("<b>");
                                sb.append(Html.escapeHtml(assignment.getHook().getId()));
                                sb.append("</b><br><br>");
                                for (String line : assignment.getException().split("\n")) {
                                    sb.append(Html.escapeHtml(line));
                                    sb.append("<br>");
                                }
                                sb.append("<br><br>");
                            }

                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    View alert = inflater.inflate(R.layout.exception, null, false);
                    TextView tvException = alert.findViewById(R.id.tvException);
                    tvException.setText(Html.fromHtml(sb.toString()));

                    new AlertDialog.Builder(view.getContext())
                            .setView(alert)
                            .create()
                            .show();
                    break;

                case R.id.tvGroup:
                    cbAssigned.setChecked(!cbAssigned.isChecked());//Invoke the onCheck
                    break;
            }
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            final LuaHooksGroup group = groups.get(getAdapterPosition());
            switch (compoundButton.getId()) {
                case R.id.cbAssigned:
                    app.notifyAssign(compoundButton.getContext(), group.name, checked);
                    break;
            }
        }
    }

    AdapterGroup() {
        setHasStableIds(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    void set(XLuaApp app, List<XLuaHook> hooks, Context context) {
        this.app = app;

        Map<String, LuaHooksGroup> map = new HashMap<>();
        for (XLuaHook hook : hooks) {
            LuaHooksGroup group;
            if (map.containsKey(hook.getGroup()))
                group = map.get(hook.getGroup());
            else {
                group = new LuaHooksGroup();

                Resources resources = context.getResources();
                String name = hook.getGroup().toLowerCase().replaceAll("[^a-z]", "_");
                group.id = resources.getIdentifier("group_" + name, "string", context.getPackageName());
                group.name = hook.getGroup();
                group.title = (group.id > 0 ? resources.getString(group.id) : hook.getGroup());

                map.put(hook.getGroup(), group);
            }
            group.hooks.add(hook);
        }

        for (String groupId : map.keySet()) {
            for (LuaAssignment assignment : app.getAssignments())
                if (assignment.getHook().getGroup().equals(groupId)) {
                    LuaHooksGroup group = map.get(groupId);
                    if (assignment.getException() != null)
                        group.exception = true;
                    if (assignment.getInstalled() >= 0)
                        group.installed++;
                    if (assignment.getHook().isOptional())
                        group.optional++;
                    if (assignment.getRestricted())
                        group.used = Math.max(group.used, assignment.getUsed());
                    group.assigned++;
                }
        }

        this.groups = new ArrayList<>(map.values());

        final Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.SECONDARY); // Case insensitive, process accents etc
        Collections.sort(this.groups, new Comparator<LuaHooksGroup>() {
            @Override
            public int compare(LuaHooksGroup group1, LuaHooksGroup group2) {
                return collator.compare(group1.title, group2.title);
            }
        });

        notifyDataSetChanged();//Invoke to Update the UI
    }

    @Override
    public long getItemId(int position) {
        return groups.get(position).id;
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.group, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unWire();
        LuaHooksGroup group = groups.get(position);

        // Get localized group name
        Context context = holder.itemView.getContext();
        Resources resources = holder.itemView.getContext().getResources();

        holder.ivException.setVisibility(group.hasException() ? View.VISIBLE : View.GONE);
        holder.ivInstalled.setVisibility(group.hasInstalled() ? View.VISIBLE : View.GONE);
        holder.ivInstalled.setAlpha(group.allInstalled() ? 1.0f : 0.5f);
        holder.tvUsed.setVisibility(group.lastUsed() < 0 ? View.GONE : View.VISIBLE);
        holder.tvUsed.setText(DateUtils.formatDateTime(context, group.lastUsed(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_ALL));
        //holder.tvGroup.setText(group.title);
        holder.tvGroup.setText(SettingUtil.cleanSettingName(group.title));
        holder.cbAssigned.setChecked(group.hasAssigned());
        holder.cbAssigned.setButtonTintList(ColorStateList.valueOf(resources.getColor(
                group.allAssigned() ? R.color.colorAccent : android.R.color.darker_gray, null)));

        holder.wire();
    }
}
