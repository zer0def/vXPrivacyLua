package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.configs.MockConfig;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.GlobalRandoms;
import eu.faircode.xlua.utilities.SettingUtil;
import eu.faircode.xlua.utilities.ViewUtil;

public class AdapterConfig extends RecyclerView.Adapter<AdapterConfig.ViewHolder> {
    private static final String TAG = "XLua.AdapterConfig";

    private MockConfig config = null;

    private final List<LuaSettingExtended> settings = new ArrayList<>();
    private final HashMap<String, Boolean> expanded = new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private AppGeneric application;

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        final View itemView;

        final TextView tvSettingName;
        final TextInputEditText tiSettingsValue;
        final ImageView ivExpanderSettings;

        final TextView tvDescription;

        final ImageView ivBtRandom;
        final Spinner spRandomSelector;
        final ArrayAdapter<IRandomizer> spRandomizer;

        final CheckBox cbEnable;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            ivExpanderSettings = itemView.findViewById(R.id.ivSettingConfigExpander);
            tvSettingName = itemView.findViewById(R.id.tvSettingConfigName);
            tiSettingsValue = itemView.findViewById(R.id.tiConfigSettingsValue);
            cbEnable = itemView.findViewById(R.id.cbEnableConfigSetting);

            spRandomSelector = itemView.findViewById(R.id.spConfigRandomSelection);
            ivBtRandom = itemView.findViewById(R.id.ivBtRandomConfigSettingValue);
            tvDescription = itemView.findViewById(R.id.tvConfigSettingDescription);


            spRandomizer = new ArrayAdapter<>(itemView.getContext(), android.R.layout.simple_spinner_item);
            initDropDown();
            if(DebugUtil.isDebug())
                Log.i(TAG, "Created the Adapter Item");
        }

        public void initDropDown() {
            //Start of Drop Down
            spRandomizer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                        Log.i(TAG, "RANDOMIZER SELECTED=" + name);

                    if (name == null ? spRandomSelector.getTag() != null : !name.equals(spRandomSelector.getTag()))
                        spRandomSelector.setTag(name);
                }
            });

            spRandomizer.clear();
            spRandomizer.addAll(GlobalRandoms.getRandomizers());
        }

        private void unWire() {
            itemView.setOnClickListener(null);
            cbEnable.setOnCheckedChangeListener(null);
            ivBtRandom.setOnClickListener(null);
        }

        private void wire() {
            itemView.setOnClickListener(this);
            cbEnable.setOnCheckedChangeListener(this);
            ivBtRandom.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            int id = view.getId();
            if(DebugUtil.isDebug())
                Log.i(TAG, "onClick id=" + id);

            final LuaSettingExtended setting = settings.get(getAdapterPosition());
            String name = setting.getName();

            switch (id) {
                case R.id.itemViewConfig:
                    ViewUtil.internalUpdateExpanded(expanded, name);
                    updateExpanded();
                    break;
                case R.id.ivBtRandomConfigSettingValue:
                    Log.i(TAG, "Randomizer Button Selected");
                    IRandomizer randomizer = (IRandomizer) spRandomSelector.getSelectedItem();
                    String randomValue = randomizer.generateString();
                    Log.i(TAG, "Randomized Value=" + randomValue);
                    tiSettingsValue.setText(randomizer.generateString());
                    setting.setValue(randomValue);
                    break;
            }
        }

        @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId"})
        @Override
        public void onCheckedChanged(final CompoundButton cButton, boolean isChecked) {
            if(DebugUtil.isDebug())
                Log.i(TAG, "onCheckedChanged");

            final LuaSettingExtended setting = settings.get(getAdapterPosition());
            final int id = cButton.getId();
            if(DebugUtil.isDebug())
                Log.i(TAG, "Item checked=" + id + " == " + setting);

            switch (id) {
                case R.id.cbEnableConfigSetting:
                    setting.setIsEnabled(isChecked);
                    notifyDataSetChanged();
                    break;
            }
        }

        void updateExpanded() {
            if(DebugUtil.isDebug())
                Log.i(TAG, "Expanding Object");

            LuaSettingExtended setting = settings.get(getAdapterPosition());
            String name = setting.getName();
            boolean isExpanded = expanded.containsKey(name) && Boolean.TRUE.equals(expanded.get(name));

            ViewUtil.setViewsVisibility(ivExpanderSettings, isExpanded, tiSettingsValue, spRandomSelector, ivBtRandom, tvDescription);
        }
    }

    AdapterConfig() { setHasStableIds(true); }
    AdapterConfig(AppGeneric application) {
        setHasStableIds(true);
        this.application = application;
    }

    void applyConfig(Context context) {
        Log.i(TAG, "Applying config setting size=" + settings.size());
        int failed = 0;
        int succeeded = 0;

        for(LuaSettingExtended setting : settings) {
            if(setting.isEnabled()) {
                LuaSettingPacket packet = LuaSettingPacket.create(setting, LuaSettingPacket.CODE_INSERT_UPDATE_SETTING, false)
                        .copyIdentification(application);

                XResult res = XLuaCall.sendMockSetting(context, packet);
                if(res.succeeded())
                    succeeded++;
                else {
                    Log.e(TAG, "Failed to send setting over bridge: " + packet + " msg=" + res.getFullMessage());
                    failed++;
                }
            }
        }

        Toast.makeText(context, "settings applied=" + succeeded + " failed=" + failed, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("NotifyDataSetChanged")
    void set(MockConfig config) {
        this.config = config;
        this.settings.clear();
        this.settings.addAll(config.getSettings());
        if(DebugUtil.isDebug())
            Log.i(TAG, "SELECTED SETTINGS COUNT=" + settings.size());

        notifyDataSetChanged();
    }

    public String getConfigName() { return config.getName(); }
    public List<LuaSettingExtended> getEnabledSettings() {
        List<LuaSettingExtended> settingsEnabled = new ArrayList<>();
        for(LuaSettingExtended setting : settings)
            if(setting.isEnabled())
                settingsEnabled.add(setting);

        return settingsEnabled;
    }

    @Override
    public long getItemId(int position) { return settings.get(position).hashCode(); }

    @Override
    public int getItemCount() { return settings.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.configsetting, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(DebugUtil.isDebug())
            Log.i(TAG, "Adapter Item Creating Internal");

        holder.unWire();
        LuaSettingExtended cSetting = settings.get(position);
        //cSetting.setIsEnabled(true);
        String settingName = cSetting.getName();

        holder.tvSettingName.setText(SettingUtil.cleanSettingName(settingName));
        holder.tiSettingsValue.setText(cSetting.getValue());
        holder.tvDescription.setText(cSetting.getDescription());
        holder.cbEnable.setChecked(cSetting.isEnabled());

        for(int i = 0; i < holder.spRandomizer.getCount(); i++) {
            IRandomizer randomizer = holder.spRandomizer.getItem(i);
            if(randomizer != null && randomizer.isSetting(settingName)) {
                holder.spRandomSelector.setSelection(i);
                break;
            }
        }

        holder.updateExpanded();
        holder.wire();

        if(DebugUtil.isDebug())
            Log.i(TAG, "Adapter Item Created Internal");
    }
}
