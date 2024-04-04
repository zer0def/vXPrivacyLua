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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.utilities.SettingUtil;
import eu.faircode.xlua.utilities.ViewUtil;

public class AdapterHook extends RecyclerView.Adapter<AdapterHook.ViewHolder> {
    private static final String TAG = "XLua.AdapterGroupHooks";

    private final List<XLuaHook> hooks = new ArrayList<>();
    private final List<XLuaHook> filtered = new ArrayList<>();
    private final HashMap<String, Boolean> expanded = new HashMap<>();

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        final View view;
        final TextView tvHookName;
        final CheckBox cbEnableHook;

        final RecyclerView rvHooks;
        final AdapterHookSettings adapterSettings;

        ViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            this.tvHookName = view.findViewById(R.id.tvHookName);
            this.rvHooks = view.findViewById(R.id.rvHookSettings);
            this.cbEnableHook = view.findViewById(R.id.cbHook);

            //init RV
            rvHooks.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(itemView.getContext());
            llm.setAutoMeasureEnabled(true);
            rvHooks.setLayoutManager(llm);
            adapterSettings = new AdapterHookSettings();
            rvHooks.setAdapter(adapterSettings);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void unWire() {
            this.view.setOnClickListener(null);
            this.tvHookName.setOnClickListener(null);
            this.cbEnableHook.setOnCheckedChangeListener(null);
        }

        @SuppressLint("ClickableViewAccessibility")
        private void wire() {
            this.view.setOnClickListener(this);
            this.tvHookName.setOnClickListener(this);
            this.cbEnableHook.setOnCheckedChangeListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            int code = view.getId();
            Log.i(TAG, "onClick=" + code);

            final XLuaHook hook = filtered.get(getAdapterPosition());

            switch (code) {
                case R.id.itemViewHooks:
                case R.id.tvHookName:
                    ViewUtil.internalUpdateExpanded(expanded, hook.getName());
                    updateExpanded();
                    break;
            }

        }

        @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
        @Override
        public void onCheckedChanged(final CompoundButton cButton, final boolean isChecked) {

        }

        void updateExpanded() {
            XLuaHook hook = filtered.get(getAdapterPosition());
            String name = hook.getName();
            boolean isExpanded = expanded.containsKey(name) && Boolean.TRUE.equals(expanded.get(name));
            ViewUtil.setViewsVisibility(null, isExpanded, rvHooks);
        }
    }

    AdapterHook() { setHasStableIds(true); }
    AdapterHook(FragmentManager manager, AppGeneric application) {  }

    public void set(List<XLuaHook> hooks) {
        this.hooks.clear();
        this.hooks.addAll(hooks);
        this.filtered.clear();
        this.filtered.addAll(hooks);
    }

    @Override
    public long getItemId(int position) { return filtered.get(position).hashCode(); }

    @Override
    public int getItemCount() { return filtered.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hookgrouphook, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unWire();
        final XLuaHook hook = filtered.get(position);
        holder.tvHookName.setText(hook.getName());
        //holder.adapterSettings.set();
        holder.updateExpanded();
        holder.wire();
    }
}
