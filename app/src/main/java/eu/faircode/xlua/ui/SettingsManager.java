package eu.faircode.xlua.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.DiffUtil;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.AdapterPropertiesGroup;
import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.api.properties.MockPropGroupHolder;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.StringUtil;

//public class SettingsManager implements Filterable {
    /*public interface  IFilterSettings {
        void onComplete(boolean dataHasChanged);
    }

    private AppGeneric application;
    private CharSequence query_value;
    private boolean dataChanged = false;
    private final Object lock = new Object();
    //INotifySearchFinished
    private Map<String, Boolean> expanded = new HashMap<>();
    private Map<Integer, LuaSettingExtended> settings = new HashMap<>();
    private Map<Integer, LuaSettingExtended> filtered = new HashMap<>();
    private Map<String, LuaSettingExtended> busy = new HashMap<>();


    //tbh this will not work good only for Settings UI but settings UI is already perfect so this is not needed..
    //nice concept to disect but thats all nothing more
    public void sendSetting() {
    }

    private void internalReplenishCache() {
        try {

        }catch (Exception e) {
            XLog.e("Failed to Replenish Settings cache: app=" + application, e, true);
        }
    }

    //but we dont treat prop view settings the same :P
    //so this whole filter thing can be an issue
    //mabe for each group we can link the setting back to here or something ?

    //another issues is when adapter item also inherits the settings
    //others that use settings need updates
    //this can help but binding Adapter index / positions to adapter

    //maybe sub hooks can like link to sub managers ?
    //

    public boolean hasSetting(int id) { return filtered.containsKey(id); }
    public LuaSettingExtended getSetting(int id) { return filtered.get(id); }

    @Override
    public Filter getFilter() {
        return new Filter() {
            private boolean expanded1 = false;
            @Override
            protected FilterResults performFiltering(CharSequence query) {
                query_value = query;
                List<LuaSettingExtended> visible = new ArrayList<>(settings.values());
                List<LuaSettingExtended> results = new ArrayList<>();
                if (!StringUtil.isValidAndNotWhitespaces(query)) results.addAll(visible);
                else {
                    String q = query.toString().toLowerCase().trim();
                    for(LuaSettingExtended setting : visible) {
                        if(setting.getName().toLowerCase().contains(q)) results.add(setting);
                        else if(setting.getModifiedValue() != null && setting.getValue().toLowerCase().contains(q)) results.add(setting);
                        else if(setting.getModifiedValue() !=  null && setting.getModifiedValue().toLowerCase().contains(q)) results.add(setting);
                    }
                }

                if (results.size() == 1) {
                    String settingName = results.get(0).getName();
                    if (!expanded.containsKey(settingName)) {
                        expanded1 = true;
                        expanded.put(settingName, true);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = results;
                filterResults.count = results.size();
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence query, FilterResults result) {
                final List<LuaSettingExtended> settings_results = (result.values == null ? new ArrayList<LuaSettingExtended>() : (List<LuaSettingExtended>) result.values);
                if(dataChanged) {
                    dataChanged = false;
                    Map<Integer, LuaSettingExtended> settings_map = new HashMap<>(settings_results.size());
                    for(LuaSettingExtended setting : settings_results)
                        settings_map.put(setting.getName().hashCode(), setting);

                    filtered.clear();
                    filtered = settings_map;

                    //notifyDataSetChanged();
                }else {
                    DiffUtil.DiffResult diff =
                            DiffUtil.calculateDiff(new AppDiffCallback(expanded1, new ArrayList<>(filtered.values()), settings_results));
                    filtered = groups;
                    diff.dispatchUpdatesTo(AdapterPropertiesGroup.this);
                }
            }
        };
    }

    //just check if data is different
    private static class AppDiffCallback extends DiffUtil.Callback {
        private final boolean refresh;
        private final List<LuaSettingExtended> prev;
        private final List<LuaSettingExtended> next;

        AppDiffCallback(boolean refresh, List<LuaSettingExtended> prev, List<LuaSettingExtended> next) {
            this.refresh = refresh;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public int getOldListSize() {
            return prev.size();
        }

        @Override
        public int getNewListSize() {
            return next.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            MockPropGroupHolder g1 = prev.get(oldItemPosition);
            MockPropGroupHolder g2 = next.get(newItemPosition);
            return (!refresh && g1.getSettingName().equalsIgnoreCase(g2.getSettingName()));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MockPropGroupHolder g1 = prev.get(oldItemPosition);
            MockPropGroupHolder g2 = next.get(newItemPosition);

            if(!g1.getSettingName().equalsIgnoreCase(g2.getSettingName()))
                return false;*/

            /*for(XMockPropMapped setting : g1.getProperties()) {
                if(!g2.containsProperty(setting))
                    return false;
            }*/

            //return true;
        //}
    //}
//}
