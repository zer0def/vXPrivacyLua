package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropSetting;

import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.ui.dialogs.PropertyDeleteDialog;

public class AdapterProperty  extends RecyclerView.Adapter<AdapterProperty.ViewHolder> implements Filterable {
    private static final String TAG = "XLua.AdapterProperty";

    private List<MockPropSetting> properties = new ArrayList<>();
    private List<MockPropSetting> filtered = new ArrayList<>();

    private boolean dataChanged = false;
    private CharSequence query = null;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    private FragmentManager fragmentManager;
    private AppGeneric application;

    public class ViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

        final View itemView;
        final TextView tvPropName;
        final CheckBox cbHide;
        final CheckBox cbSkip;

        final ConstraintLayout constraintLayout;
        final CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            tvPropName = itemView.findViewById(R.id.tvPropPropertyName);
            cbHide = itemView.findViewById(R.id.cbPropHide);
            cbSkip = itemView.findViewById(R.id.cbPropSkip);

            constraintLayout = itemView.findViewById(R.id.clPropertiesPropPropLayout);
            cardView = itemView.findViewById(R.id.cvPropertyProp);
        }

        private void unWire() {
            cbHide.setOnCheckedChangeListener(null);
            cbSkip.setOnCheckedChangeListener(null);
            constraintLayout.setOnLongClickListener(null);
            cardView.setOnLongClickListener(null);
        }

        private void wire() {
            cbHide.setOnCheckedChangeListener(this);
            cbSkip.setOnCheckedChangeListener(this);
            constraintLayout.setOnLongClickListener(this);
            cardView.setOnLongClickListener(this);
        }

        @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
        @Override
        public void onCheckedChanged(final CompoundButton cButton, final boolean isChecked) {
            Log.i(TAG, "onCheckedChanged: " + cButton.getId() + " isChecked=" + isChecked);
            final MockPropSetting setting = filtered.get(getAdapterPosition());

            int valueNeeded = 0;
            if(isChecked) {
                switch (cButton.getId()) {
                    case R.id.cbPropSkip: valueNeeded = MockPropPacket.PROP_SKIP; break;
                    case R.id.cbPropHide: valueNeeded = MockPropPacket.PROP_HIDE; break;
                    default: valueNeeded = MockPropPacket.PROP_NULL; break;
                }
            }

            final int code = isChecked && valueNeeded != MockPropPacket.PROP_NULL ? MockPropPacket.CODE_INSERT_UPDATE_PROP_SETTING : MockPropPacket.CODE_DELETE_PROP_SETTING;
            Log.i(TAG, "CheckBox Invoked code =" + code + " valueNeeded=" + valueNeeded + " application=" + application + " isChecked=" + isChecked + " id=" + cButton.getId() + " setting=" + setting);
            final MockPropPacket packet = MockPropPacket.create(application.getUid(), application.getPackageName(), setting.getName(), setting.getSettingName(), valueNeeded, code);
            final Context context = cButton.getContext();

            Log.i(TAG, "Packet Created for Property: packet=" + packet);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        final XResult ret = XMockCall.putMockProp(context, packet);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {
                                if(ret.succeeded())
                                    setting.setValue(packet.getValue());

                                Toast.makeText(context, ret.getResultMessage(), Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            });


            notifyDataSetChanged();
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onLongClick(View v) {
            int code = v.getId();
            Log.i(TAG, "onLongClick=" + code);

            MockPropSetting setting = filtered.get(getAdapterPosition());

            switch (code) {
                case R.id.cvPropertyProp:
                case R.id.clPropertiesPropPropLayout:
                    PropertyDeleteDialog setDialog = new PropertyDeleteDialog();
                    assert fragmentManager != null;
                    setDialog.addApplication(application);
                    setDialog.addSetting(setting);
                    setDialog.show(fragmentManager, "Delete Property");
                    break;
            }

            return false;
        }
    }

    AdapterProperty() { setHasStableIds(true); }
    AdapterProperty(FragmentManager manager, AppGeneric application) {
        Log.i(TAG, "Within Adapter for Property , application=" + application);
        setHasStableIds(true);
        this.fragmentManager = manager;
        this.application = application;
    }

    void set(List<MockPropSetting> properties) {
        this.dataChanged = true;
        this.properties.clear();
        this.properties.addAll(properties);

        if(DebugUtil.isDebug())
            Log.i(TAG, "Internal Count=" + this.properties.size());

        getFilter().filter(query);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            private boolean expanded1 = false;

            @Override
            protected FilterResults performFiltering(CharSequence query) {
                AdapterProperty.this.query = query;
                List<MockPropSetting> visible = new ArrayList<>(properties);
                List<MockPropSetting> results = new ArrayList<>();

                if (TextUtils.isEmpty(query))
                    results.addAll(visible);
                else {
                    String q = query.toString().toLowerCase().trim();
                    for(MockPropSetting prop : visible) {
                        if(prop.getName().toLowerCase().contains(q))
                            results.add(prop);
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
                final List<MockPropSetting> props = (result.values == null ? new ArrayList<MockPropSetting>() : (List<MockPropSetting>) result.values);
                Log.i(TAG, "Filtered props size=" + props.size());

                if(dataChanged) {
                    dataChanged = false;
                    filtered = props;
                    notifyDataSetChanged();
                }else {
                    DiffUtil.DiffResult diff =
                            DiffUtil.calculateDiff(new AppDiffCallback(expanded1, filtered, props));
                    filtered = props;
                    diff.dispatchUpdatesTo(AdapterProperty.this);
                }
            }
        };
    }

    private static class AppDiffCallback extends DiffUtil.Callback {
        private final boolean refresh;
        private final List<MockPropSetting> prev;
        private final List<MockPropSetting> next;

        AppDiffCallback(boolean refresh, List<MockPropSetting> prev, List<MockPropSetting> next) {
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
            MockPropSetting s1 = prev.get(oldItemPosition);
            MockPropSetting s2 = next.get(newItemPosition);
            return (!refresh && s1.getName().equalsIgnoreCase(s2.getName()));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            MockPropSetting s1 = prev.get(oldItemPosition);
            MockPropSetting s2 = next.get(newItemPosition);
            return s1.getName().equalsIgnoreCase(s2.getName());
        }
    }

    @Override
    public long getItemId(int position) { return filtered.get(position).hashCode(); }

    @Override
    public int getItemCount() { return filtered.size(); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.propelement, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unWire();
        MockPropSetting property = filtered.get(position);

        holder.tvPropName.setText(property.getName());
        holder.cbSkip.setChecked(property.isSkip());
        holder.cbHide.setChecked(property.isHide());

        holder.wire();
    }
}
