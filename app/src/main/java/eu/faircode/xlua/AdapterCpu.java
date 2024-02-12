package eu.faircode.xlua;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.XMockCallApi;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;

public class AdapterCpu extends RecyclerView.Adapter<AdapterCpu.ViewHolder> {
    private static final String TAG = "XLua.ADCpu";

    private List<MockCpu> maps = new ArrayList<>();
    private Object lock = new Object();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        final View itemView;

        final TextView tvCpuName;
        final TextView tvCpuManName;
        final TextView tvCpuModelName;
        final TextView tvCpuMapContents;
        final ImageView ivExpanderCpu;
        final ImageView ivCpuIcon;
        final CheckBox cbCpuSelected;

        private HashMap<String, Boolean> expanded = new HashMap<>();

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            ivExpanderCpu = itemView.findViewById(R.id.ivCpuExpander);
            tvCpuName = itemView.findViewById(R.id.tvCpuName);
            tvCpuManName = itemView.findViewById(R.id.tvCpuManName);
            tvCpuModelName = itemView.findViewById(R.id.tvCpuModelName);
            tvCpuMapContents = itemView.findViewById(R.id.tvCpuMapContents);
            ivCpuIcon = itemView.findViewById(R.id.ivCpuIcon);
            cbCpuSelected = itemView.findViewById(R.id.cbCpuSelected);
        }

        private void unWire() {
            itemView.setOnClickListener(null);
            cbCpuSelected.setOnCheckedChangeListener(null);
        }

        private void wire() {
            itemView.setOnClickListener(this);
            cbCpuSelected.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(final View view) {
            Log.i(TAG, "onClick");
            final MockCpu map = maps.get(getAdapterPosition());
            String name = map.getName();

            switch (view.getId()) {
                case R.id.itemViewCpu:
                    if(!expanded.containsKey(name))
                        expanded.put(name, false);

                    expanded.put(name, !expanded.get(name));
                    updateExpanded();
                    break;
            }
        }

        @Override
        public void onCheckedChanged(final CompoundButton cButton, boolean isChecked) {
            Log.i(TAG, "onCheckedChanged");
            final MockCpu cpu = maps.get(getAdapterPosition());
            final int id = cButton.getId();
            Log.i(TAG, "Item Checked=" + id + "==" + cpu.getName());

            switch (id) {
                case R.id.cbCpuSelected:
                    cpu.setSelected(isChecked);
                    notifyDataSetChanged();
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "put cpu result=" + XMockCallApi.putMockCpu(cButton.getContext(), cpu));
                        }
                    });

                    break;
            }
        }

        void updateExpanded() {
            MockCpu map = maps.get(getAdapterPosition());
            String name = map.getName();
            boolean isExpanded = expanded.containsKey(name) && expanded.get(name);
            ivExpanderCpu.setImageLevel(isExpanded ? 1 : 0);
            tvCpuMapContents.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }

    }

    AdapterCpu() { setHasStableIds(true); }

    void set(List<MockCpu> maps_c) {
        maps.clear();
        maps.addAll(maps_c);
        if(DebugUtil.isDebug())
            Log.i(TAG, "Internal Count=" + maps_c.size());

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) { return maps.get(position).hashCode(); }

    @Override
    public int getItemCount() { return maps.size(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdapterCpu.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cpu, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unWire();
        MockCpu cpu = maps.get(position);
        holder.tvCpuName.setText(cpu.getName());
        holder.tvCpuModelName.setText(cpu.getModel());
        holder.tvCpuManName.setText(cpu.getManufacturer());
        holder.tvCpuMapContents.setText(cpu.getContents());
        holder.cbCpuSelected.setChecked(cpu.getSelected());
        holder.updateExpanded();
        holder.wire();
    }
}
