package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.api.cpu.MockCpu;
import eu.faircode.xlua.utilities.ViewUtil;

public class AdapterCpu extends RecyclerView.Adapter<AdapterCpu.ViewHolder> {
    private static final String TAG = "XLua.ADCpu";

    private final List<MockCpu> maps = new ArrayList<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final HashMap<String, Boolean> expanded = new HashMap<>();
    private final Object lock = new Object();

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

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(final View view) {
            Log.i(TAG, "onClick");
            final MockCpu map = maps.get(getAdapterPosition());
            String name = map.getName();

            if (view.getId() == R.id.itemViewCpu) {
                ViewUtil.internalUpdateExpanded(expanded, name);
                updateExpanded();
            }
        }

        @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
        @Override
        public void onCheckedChanged(final CompoundButton cButton, boolean isChecked) {
            Log.i(TAG, "onCheckedChanged");
            final MockCpu cpu = maps.get(getAdapterPosition());
            final int id = cButton.getId();
            Log.i(TAG, "Item Checked=" + id + "==" + cpu.getName());

            if (id == R.id.cbCpuSelected) {
                cpu.setSelected(isChecked);
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (lock) {
                            final XResult ret = XMockCall.putMockCpu(itemView.getContext(), cpu);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void run() {
                                    Toast.makeText(itemView.getContext(), ret.getResultMessage(), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
            }
        }

        void updateExpanded() {
            MockCpu map = maps.get(getAdapterPosition());
            String name = map.getName();
            boolean isExpanded = expanded.containsKey(name) && Boolean.TRUE.equals(expanded.get(name));
            ViewUtil.setViewsVisibility(ivExpanderCpu, isExpanded, tvCpuMapContents);
        }
    }

    AdapterCpu() { setHasStableIds(true); }

    @SuppressLint("NotifyDataSetChanged")
    void set(List<MockCpu> maps) {
        this.maps.clear();
        this.maps.addAll(maps);
        if(DebugUtil.isDebug())
            Log.i(TAG, "Internal Count=" + maps.size());

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) { return maps.get(position).hashCode(); }

    @Override
    public int getItemCount() { return maps.size(); }

    @NonNull
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
        holder.cbCpuSelected.setChecked(cpu.isSelected());
        holder.updateExpanded();
        holder.wire();
    }
}
