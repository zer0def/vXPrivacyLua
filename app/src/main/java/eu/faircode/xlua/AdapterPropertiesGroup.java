package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.properties.MockPropGroupHolder;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.randomizers.GlobalRandoms;
import eu.faircode.xlua.randomizers.IRandomizer;
import eu.faircode.xlua.utilities.SettingUtil;
import eu.faircode.xlua.utilities.ViewUtil;

public class AdapterPropertiesGroup extends RecyclerView.Adapter<AdapterPropertiesGroup.ViewHolder> implements Filterable {
    private static final String TAG = "XLua.AdapterPropertiesGroup";

    private final List<MockPropGroupHolder> groups = new ArrayList<>();
    private List<MockPropGroupHolder> filtered = new ArrayList<>();
    private final HashMap<String, Boolean> expanded = new HashMap<>();

    private final HashMap<LuaSettingExtended, String> modified = new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    private boolean dataChanged = false;
    private CharSequence query = null;

    private FragmentManager fragmentManager;
    private AppGeneric application;

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, TextWatcher, View.OnTouchListener {

        final View itemView;

        final ImageView ivDropDown;
        final TextView tvSettingName;
        final TextView tvSettingNameFull;

        final AdapterProperty adapterProps;
        final RecyclerView rvGroupProps;

        final TextView tvSettingDescription;
        final TextInputEditText tiSettingValue;

        final ImageView ivBtSave;
        final ImageView ivBtDelete;
        final ImageView ivBtRandomize;

        final Spinner spRandomSelector;
        final ArrayAdapter<IRandomizer> spRandomizer;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            ivDropDown = itemView.findViewById(R.id.ivSettingDropDown);
            tvSettingName = itemView.findViewById(R.id.tvSettingName);
            tvSettingNameFull = itemView.findViewById(R.id.tvGroupPropertiesSettingFullName);

            tvSettingDescription = itemView.findViewById(R.id.tvSettingDescriptionFromProperties);
            tiSettingValue = itemView.findViewById(R.id.tiSettingValueFromProperties);

            ivBtSave = itemView.findViewById(R.id.ivBtSaveSettingFromProperties);
            ivBtDelete = itemView.findViewById(R.id.ivBtDeleteSettingFromProperties);
            ivBtRandomize = itemView.findViewById(R.id.ivBtRandomSettingValueFromProperties);

            //Start of Drop Down
            spRandomizer = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item);
            spRandomizer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRandomSelector = itemView.findViewById(R.id.spSettingRandomizerSpinnerProperties);
            if(DebugUtil.isDebug())
                Log.i(TAG, "Created the Empty Array for Configs Fragment Config");

            spRandomSelector.setTag(null);
            spRandomSelector.setAdapter(spRandomizer);
            spRandomSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updateSelection(); }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    updateSelection();
                }

                private void updateSelection() {
                    IRandomizer selected = (IRandomizer) spRandomSelector.getSelectedItem();
                    String name = selected.getName();
                    if(DebugUtil.isDebug())
                        Log.i(TAG, "Selected Randomizer=" + name);

                    if (name == null ? spRandomSelector.getTag() != null : !name.equals(spRandomSelector.getTag()))
                        spRandomSelector.setTag(name);
                }
            });

            spRandomizer.clear();
            spRandomizer.addAll(GlobalRandoms.getRandomizers());

            //Init settings Adapter
            rvGroupProps = itemView.findViewById(R.id.rvGroupProperties);
            rvGroupProps.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext());
            llm.setAutoMeasureEnabled(true);
            rvGroupProps.setLayoutManager(llm);
            Log.i(TAG, "Creating the Adapter for property: application" + application);
            adapterProps = new AdapterProperty(fragmentManager, application);
            rvGroupProps.setAdapter(adapterProps);
        }


        @SuppressLint("ClickableViewAccessibility")
        private void unWire() {
            itemView.setOnClickListener(null);
            tiSettingValue.removeTextChangedListener(this);
            ivDropDown.setOnClickListener(null);

            ivBtDelete.setOnClickListener(null);
            ivBtRandomize.setOnClickListener(null);
            ivBtSave.setOnClickListener(null);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void wire() {
            itemView.setOnClickListener(this);
            tiSettingValue.addTextChangedListener(this);
            ivDropDown.setOnClickListener(this);

            ivBtDelete.setOnClickListener(this);
            ivBtRandomize.setOnClickListener(this);
            ivBtSave.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            int id = view.getId();
            if(DebugUtil.isDebug())
                Log.i(TAG, "onClick id=" + id);

            final MockPropGroupHolder group = filtered.get(getAdapterPosition());
            String name = group.getSettingName();

            Log.i(TAG, "selected=" + group);

            switch (id) {
                case R.id.ivSettingDropDown:
                case R.id.itemViewPropGroup:
                    ViewUtil.internalUpdateExpanded(expanded, name);
                    updateExpanded();
                    break;
                case R.id.ivBtDeleteSettingFromProperties:
                    sendSetting(itemView.getContext(), group.getSetting(), true, false);
                    break;
                case R.id.ivBtRandomSettingValueFromProperties:
                    IRandomizer randomizer = (IRandomizer) spRandomSelector.getSelectedItem();
                    String randomValue = randomizer.generateString();
                    SettingUtil.updateSetting(group.getSetting(), randomValue, modified);
                    tiSettingValue.setText(randomValue);
                    break;
                case R.id.ivBtSaveSettingFromProperties:
                    if(modified.containsKey(group.getSetting()))
                        sendSetting(view.getContext(), group.getSetting(), false, false);
                    break;
            }
        }

        public void sendSetting(final Context context, final LuaSettingExtended setting, boolean deleteSetting, boolean forceKill) {
            final LuaSettingPacket packet = LuaSettingPacket.create(setting, LuaSettingPacket.getCodeInsertOrDelete(deleteSetting), forceKill)
                            .copyIdentification(application);

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        final XResult ret = XLuaCall.sendSetting(context, packet);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {
                                if(ret.succeeded())
                                    modified.remove(setting);

                                Toast.makeText(context, ret.getResultMessage(), Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            });
        }

        @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
        @Override
        public void onCheckedChanged(final CompoundButton cButton, final boolean isChecked) {
            Log.i(TAG, "onCheckedChanged");
            final MockPropGroupHolder group = filtered.get(getAdapterPosition());
            final String name = group.getSettingName();
            final int id = cButton.getId();

            Log.i(TAG, "Item Checked=" + id + "==" + name);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        void updateExpanded() {
            MockPropGroupHolder group = filtered.get(getAdapterPosition());
            String name = group.getSettingName();
            boolean isExpanded = expanded.containsKey(name) && Boolean.TRUE.equals(expanded.get(name));
            ViewUtil.setViewsVisibility(ivDropDown, isExpanded, tiSettingValue, rvGroupProps, ivBtDelete, ivBtRandomize, ivBtSave, tvSettingDescription, spRandomSelector);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            int action = event.getActionMasked();

            switch (action) {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            return false;
        }
    }

    AdapterPropertiesGroup() { setHasStableIds(true); }
    AdapterPropertiesGroup(FragmentManager manager, AppGeneric application) {
        Log.i(TAG, "Adapter Created application=" + application);
        setHasStableIds(true);
        this.fragmentManager = manager;
        this.application = application;
    }

    void set(List<MockPropGroupHolder> groups) {
        this.dataChanged = true;
        this.groups.clear();
        this.groups.addAll(groups);

        Log.i(TAG, "Internal Count=" + this.groups.size());
        getFilter().filter(query);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            private boolean expanded1 = false;

            @Override
            protected FilterResults performFiltering(CharSequence query) {
                AdapterPropertiesGroup.this.query = query;
                List<MockPropGroupHolder> visible = new ArrayList<>(groups);
                List<MockPropGroupHolder> results = new ArrayList<>();

                if (TextUtils.isEmpty(query))
                    results.addAll(visible);
                else {
                    String q = query.toString().toLowerCase().trim();
                    for(MockPropGroupHolder p : visible) {
                        if(p.getSettingName().toLowerCase().contains(q))
                            results.add(p);
                        else if(p.getValue() != null && p.getValue().toLowerCase().contains(q))
                            results.add(p);
                        else {
                            /*for(XMockPropMapped setting : p.getProperties()) {
                                if(setting.getPropertyName().toLowerCase().contains(q))
                                    results.add(p);
                            }*/
                        }
                    }
                }

                if (results.size() == 1) {
                    String settingName = results.get(0).getSettingName();
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
                final List<MockPropGroupHolder> groups = (result.values == null ? new ArrayList<MockPropGroupHolder>() : (List<MockPropGroupHolder>) result.values);
                Log.i(TAG, "Filtered groups size=" + groups.size());

                if(dataChanged) {
                    dataChanged = false;
                    filtered = groups;
                    notifyDataSetChanged();
                }else {
                    DiffUtil.DiffResult diff =
                            DiffUtil.calculateDiff(new AppDiffCallback(expanded1, filtered, groups));
                    filtered = groups;
                    diff.dispatchUpdatesTo(AdapterPropertiesGroup.this);
                }
            }
        };
    }

    private static class AppDiffCallback extends DiffUtil.Callback {
        private final boolean refresh;
        private final List<MockPropGroupHolder> prev;
        private final List<MockPropGroupHolder> next;

        AppDiffCallback(boolean refresh, List<MockPropGroupHolder> prev, List<MockPropGroupHolder> next) {
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
                return false;

            /*for(XMockPropMapped setting : g1.getProperties()) {
                if(!g2.containsProperty(setting))
                    return false;
            }*/

            return true;
        }
    }

    @Override
    public long getItemId(int position) { return filtered.get(position).hashCode(); }

    @Override
    public int getItemCount() { return filtered.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.propgroup, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unWire();
        MockPropGroupHolder group = filtered.get(position);
        String sName = group.getSettingName();

        holder.tvSettingName.setText(SettingUtil.cleanSettingName(sName));
        holder.tvSettingNameFull.setText(sName);
        holder.tiSettingValue.setText(group.getValue());
        holder.tvSettingDescription.setText(group.getDescription());

        Log.i(TAG, "props in settings group=" + group.getProperties().size());

        holder.adapterProps.set(group.getProperties());

        for(int i = 0; i < holder.spRandomizer.getCount(); i++) {
            IRandomizer randomizer = holder.spRandomizer.getItem(i);
            if(randomizer != null && randomizer.isSetting(sName)) {
                holder.spRandomSelector.setSelection(i);
                break;
            }
        }

        holder.updateExpanded();
        holder.wire();
    }
}
