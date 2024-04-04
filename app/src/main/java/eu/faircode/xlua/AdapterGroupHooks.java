package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import eu.faircode.xlua.api.app.XLuaApp;
import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.properties.MockPropGroupHolder;
import eu.faircode.xlua.utilities.SettingUtil;
import eu.faircode.xlua.utilities.ViewUtil;

public class AdapterGroupHooks extends RecyclerView.Adapter<AdapterGroupHooks.ViewHolder> {
    private static final String TAG = "XLua.AdapterGroupHooks";

    private XLuaApp app;
    private List<Group> groups = new ArrayList<>();
    private final List<Group> filtered = new ArrayList<>();
    private final HashMap<String, Boolean> expanded = new HashMap<>();

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        final View view;
        final TextView tvGroupName;
        final CheckBox cbGroup;
        final ImageView ivExpander;

        final RecyclerView rvGroups;
        final AdapterHook adapterHook;

        ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            this.tvGroupName = itemView.findViewById(R.id.tvHookGroupName);
            this.cbGroup = itemView.findViewById(R.id.cbHookGroup);
            this.rvGroups = itemView.findViewById(R.id.rvHookGroup);
            this.ivExpander = itemView.findViewById(R.id.ivExpanderGroup);

            //init RV
            rvGroups.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext());
            llm.setAutoMeasureEnabled(true);
            rvGroups.setLayoutManager(llm);
            adapterHook = new AdapterHook();
            rvGroups.setAdapter(adapterHook);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void unWire() {
            this.ivExpander.setOnClickListener(null);
            this.tvGroupName.setOnClickListener(null);
            this.cbGroup.setOnCheckedChangeListener(null);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void wire() {
            this.ivExpander.setOnClickListener(this);
            this.tvGroupName.setOnClickListener(this);
            this.cbGroup.setOnCheckedChangeListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            int code = view.getId();
            Log.i(TAG, "onClick=" + code);

            final Group group = filtered.get(getAdapterPosition());
            final String name = group.name;

            switch (code) {
                case R.id.itemViewGroupHooks:
                case R.id.tvHookGroupName:
                case R.id.ivExpanderGroup:
                    ViewUtil.internalUpdateExpanded(expanded, name);
                    updateExpanded();
                    break;
            }
        }

        @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
        @Override
        public void onCheckedChanged(final CompoundButton cButton, final boolean isChecked) {

        }

        void updateExpanded() {
            Group group = filtered.get(getAdapterPosition());
            String name = group.name;
            boolean isExpanded = expanded.containsKey(name) && Boolean.TRUE.equals(expanded.get(name));
            ViewUtil.setViewsVisibility(ivExpander, isExpanded, rvGroups);
        }
    }


    AdapterGroupHooks() { setHasStableIds(true); }
    AdapterGroupHooks(FragmentManager manager, AppGeneric application) {

    }

    @Override
    public long getItemId(int position) { return filtered.get(position).hashCode(); }

    @Override
    public int getItemCount() { return filtered.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hookgroup, parent, false));
    }

    void setGroup(String name) {
        //Set the group from the Spinner / Drop Down Menu at top of UI menu
        //That being said ALL is all but specific kinds
        //Get passed down to here to update view as well as show Restrict button
        //if (group == null ? name != null : !group.equals(name)) {
        //    group = name;
        //}
    }

    public void set(XLuaApp app, List<XLuaHook> hooks, Context context) {
        this.app = app;
        //List<XLuaHook> selectedHooks = new ArrayList<>();
        //for (XLuaHook hook : hooks)
        //    if (hook.isAvailable(app.getPackageName(), collection) &&
        //            (group == null || group.equals(hook.getGroup())))
        //        selectedHooks.add(hook);

        Map<String, Group> map = new HashMap<>();
        for (XLuaHook hook : hooks) {
            Group group;
            if (map.containsKey(hook.getGroup()))
                group = map.get(hook.getGroup());
            else {
                group = new Group();

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
                    Group group = map.get(groupId);
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
        Log.i(TAG, "groups size=" + this.groups.size());

        final Collator collator = Collator.getInstance(Locale.getDefault());
        collator.setStrength(Collator.SECONDARY); // Case insensitive, process accents etc
        Collections.sort(this.groups, new Comparator<Group>() {
            @Override
            public int compare(Group group1, Group group2) {
                return collator.compare(group1.title, group2.title);
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unWire();
        Group group = filtered.get(position);
        holder.tvGroupName.setText(SettingUtil.cleanSettingName(group.title));
        holder.adapterHook.set(group.hooks);
        holder.updateExpanded();
        holder.wire();
    }

    private class Group {
        int id;
        String name;
        String title;
        boolean exception = false;
        int installed = 0;
        int optional = 0;
        long used = -1;
        int assigned = 0;
        List<XLuaHook> hooks = new ArrayList<>();

        Group() {
        }

        boolean hasException() {
            return (assigned > 0 && exception);
        }

        boolean hasInstalled() {
            return (assigned > 0 && installed > 0);
        }

        boolean allInstalled() {
            return (assigned > 0 && installed + optional == assigned);
        }

        long lastUsed() {
            return used;
        }

        boolean hasAssigned() {
            return (assigned > 0);
        }

        boolean allAssigned() {
            return (assigned == hooks.size());
        }
    }
}
