package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.objects.xmock.ConfigSetting;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;

public class AdapterConfig extends RecyclerView.Adapter<AdapterConfig.ViewHolder> {
    private static final String TAG = "XLua.AdapterConfig";

    private List<ConfigSetting> settings = new ArrayList<>();
    private Object lock = new Object();

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        final View itemView;

        final TextView tvSettingName;
        final CheckBox cbSettingEnabled;
        final TextInputEditText tiSettingsValue;
        final ImageView ivExpanderSettings;

        private HashMap<String, Boolean> expanded = new HashMap<>();

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            ivExpanderSettings = itemView.findViewById(R.id.ivSettingConfigExpander);
            tvSettingName = itemView.findViewById(R.id.tvSettingConfigName);
            cbSettingEnabled = itemView.findViewById(R.id.cbEnableConfigSetting);
            tiSettingsValue = itemView.findViewById(R.id.tiConfigSettingsValue);

            Log.i(TAG, "Created the Adapter Item");
        }

        private void unWire() {
            itemView.setOnClickListener(null);
        }

        private void wire() {
            itemView.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            int id = view.getId();
            if(DebugUtil.isDebug())
                Log.i(TAG, "onClick id=" + id);

            final ConfigSetting setting = settings.get(getAdapterPosition());
            String name = setting.getName();

            switch (id) {
                case R.id.itemViewConfig:
                    if(!expanded.containsKey(name))
                        expanded.put(name, false);

                    expanded.put(name, Boolean.FALSE.equals(expanded.get(name)));
                    updateExpanded();
                    break;
            }
        }

        @Override
        public void onCheckedChanged(final CompoundButton cButton, boolean isChecked) {
            if(DebugUtil.isDebug())
                Log.i(TAG, "onCheckedChanged");

        }

        void updateExpanded() {
            if(DebugUtil.isDebug())
                Log.i(TAG, "Expanding Object");

            ConfigSetting setting = settings.get(getAdapterPosition());
            String name = setting.getName();
            boolean isExpanded = expanded.containsKey(name) && Boolean.TRUE.equals(expanded.get(name));

            ivExpanderSettings.setImageLevel(isExpanded ? 1 : 0);
            tiSettingsValue.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }

    AdapterConfig() { setHasStableIds(true); }

    @SuppressLint("NotifyDataSetChanged")
    void set(List<ConfigSetting> settings) {
        this.settings.clear();
        this.settings.addAll(settings);

        if(DebugUtil.isDebug())
            Log.i(TAG, "Config Settings internal size=" + this.settings.size());

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) { return settings.get(position).hashCode(); }

    @Override
    public int getItemCount() { return settings.size(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.configsetting, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.i(TAG, "Adapter Item Creating Internal");
        holder.unWire();
        ConfigSetting cSetting = settings.get(position);
        holder.tvSettingName.setText(cSetting.getName());
        holder.tiSettingsValue.setText(cSetting.getValue());
        holder.cbSettingEnabled.setChecked(true);
        holder.updateExpanded();
        holder.wire();
        Log.i(TAG, "Adapter Item Created Internal");
    }
}
