package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.utilities.ViewUtil;

public class AdapterHookSettings extends RecyclerView.Adapter<AdapterHookSettings.ViewHolder> {
    private static final String TAG = "XLua.AdapterGroupHooks";

    private final List<LuaSettingExtended> settings = new ArrayList<>();
    private final List<LuaSettingExtended> filtered = new ArrayList<>();
    private final HashMap<String, Boolean> expanded = new HashMap<>();

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        final View view;
        final TextView tvSettingName;
        final TextInputEditText tiSettingValue;
        final Button btRandomize, btReset, btSave, btDelete;
        final Spinner spRandomizer;

        ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            this.tvSettingName = view.findViewById(R.id.tvHookSettingName);
            this.tiSettingValue = view.findViewById(R.id.tvHookSettingValue);
            this.btSave = view.findViewById(R.id.ivBtHookSettingDelete);
            this.btDelete = view.findViewById(R.id.ivBtHookSettingSave);
            this.btReset = view.findViewById(R.id.ivBtHookSettingReset);
            this.btRandomize = view.findViewById(R.id.ivBtHookSettingRandomize);
            this.spRandomizer = view.findViewById(R.id.spHookSettingRandomizer);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void unWire() {
            this.view.setOnClickListener(null);
            this.tvSettingName.setOnClickListener(null);
            this.btSave.setOnClickListener(null);
            this.btReset.setOnClickListener(null);
            this.btRandomize.setOnClickListener(null);
            this.btDelete.setOnClickListener(null);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void wire() {
            this.view.setOnClickListener(this);
            this.tvSettingName.setOnClickListener(this);
            this.btSave.setOnClickListener(this);
            this.btReset.setOnClickListener(this);
            this.btRandomize.setOnClickListener(this);
            this.btDelete.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            int code = view.getId();
            Log.i(TAG, "onClick=" + code);

            final LuaSettingExtended setting = filtered.get(getAdapterPosition());

            switch (code) {
                case R.id.itemViewHookSettings:
                case R.id.tvHookSettingName:
                    ViewUtil.internalUpdateExpanded(expanded, setting.getName());
                    updateExpanded();
                    break;
            }
        }

        @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
        @Override
        public void onCheckedChanged(final CompoundButton cButton, final boolean isChecked) {

        }

        void updateExpanded() {
            LuaSettingExtended setting = filtered.get(getAdapterPosition());
            String name = setting.getName();
            boolean isExpanded = expanded.containsKey(name) && Boolean.TRUE.equals(expanded.get(name));
            ViewUtil.setViewsVisibility(null, isExpanded, tiSettingValue, btDelete, btRandomize, btReset, btSave, spRandomizer);
        }
    }

    AdapterHookSettings() { setHasStableIds(true); }
    AdapterHookSettings(FragmentManager manager, AppGeneric application) {  }

    public void set(List<LuaSettingExtended> settings) {
        this.settings.clear();
        this.settings.addAll(settings);
        this.filtered.clear();
        this.filtered.addAll(settings);
    }

    @Override
    public long getItemId(int position) { return filtered.get(position).hashCode(); }

    @Override
    public int getItemCount() { return filtered.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hooksetting, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unWire();
        final LuaSettingExtended setting = filtered.get(position);
        holder.tvSettingName.setText(setting.getName());
        holder.updateExpanded();
        holder.wire();
    }
}
