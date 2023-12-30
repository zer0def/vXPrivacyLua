package eu.faircode.xlua;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdapterProp extends RecyclerView.Adapter<AdapterProp.ViewHolder> {
    private static final String TAG = "XLua.ADProp";

    private List<XMockPropIO> props = new ArrayList<>();
    private List<XMockPropIO> props_modified = new ArrayList<>();
    private Object lock = new Object();

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public class ViewHolder extends RecyclerView.ViewHolder
        implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, TextWatcher {

        final View itemView;

        final ImageView ivExpanderProps;
        final TextView tvPropNameLabel;
        final TextView tvDefaultValue;
        final ImageView ivValueResetDefault;
        final CheckBox cbEnableMock;
        final TextInputEditText tiPropMockValue;

        private HashMap<String, Boolean> expanded = new HashMap<>();

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            ivExpanderProps = itemView.findViewById(R.id.ivExpanderProps);
            tvPropNameLabel = itemView.findViewById(R.id.tvPropnameLabel);
            tvDefaultValue = itemView.findViewById(R.id.tvDefaultPropValue);
            ivValueResetDefault = itemView.findViewById(R.id.ivSetDefaultValue);
            cbEnableMock = itemView.findViewById(R.id.cbMockTheProp);
            tiPropMockValue = itemView.findViewById(R.id.tiPropMockValue);
        }

        private void unwire() {
            itemView.setOnClickListener(null);
            cbEnableMock.setOnCheckedChangeListener(null);
            ivValueResetDefault.setOnClickListener(null);
            tiPropMockValue.removeTextChangedListener(this);
        }

        private void wire() {
            itemView.setOnClickListener(this);
            cbEnableMock.setOnCheckedChangeListener(this);
            ivValueResetDefault.setOnClickListener(this);
            tiPropMockValue.addTextChangedListener(this);
        }

        @Override
        public void onClick(final View view) {
            Log.i(TAG, "onClick");
            final XMockPropIO prop = props.get(getAdapterPosition());
            int id = view.getId();
            String name = prop.getName();

            Log.i(TAG, "onClick=" + id + "==" + name);

            switch (view.getId()) {
                case R.id.itemViewProps:
                    if(!expanded.containsKey(name))
                        expanded.put(name, false);

                    expanded.put(name, !expanded.get(name));
                    updateExpanded();
                    break;
                case R.id.ivSetDefaultValue:
                    if(prop.mockValue != prop.defaultValue) {
                        unwire();
                        tiPropMockValue.setText(prop.defaultValue);
                        Log.i(TAG, "Set Text of:" + prop.name + " => " + prop.defaultValue);
                        prop.mockValue = prop.defaultValue;
                        wire();
                        /*executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                XMockProxyApi.callPutMockProp(view.getContext(), prop);
                                XMockProvider.updateCache(props);////Might be no point given the different context
                            }
                        });*/
                    }
                    break;
            }
        }

        @Override
        public void onCheckedChanged(final CompoundButton cButton, boolean isChecked) {
            Log.i(TAG, "onCheckedChanged");
            final XMockPropIO prop = props.get(getAdapterPosition());
            final int id = cButton.getId();
            Log.i(TAG, "Item Checked=" + id + "==" + prop.getName());

            //notifyDataSetChanged();

            switch (id) {
                case R.id.cbMockTheProp:
                    prop.enabled = isChecked;
                    //notifyDataSetChanged();
                    //notifyDataSetChanged(); put ?
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            XMockProxyApi.callPutMockProp(cButton.getContext(), prop);
                            //XMockProvider.updateCache(props);////Might be no point given the different context
                            //No need to update our cache since when we modify the elements on this context
                            //it is ref based it will effect the element in the list
                            //Tho this will be OUR Cache in the MAIN APP not System Context or Hooked App Context

                            //So TBH hmm we should not store cache in the app ? instead it constantly grabs when NEEDED

                            //So when the put is called, we have in our context a updated view so yes, our base does not need to carry cache as its cached here
                            //Our System app where it comes from will not BUT if not grabbed from Cache no issue then
                        }
                    });
                    break;
            }

            //case R.id.cbEnableMock
        }

        @Override
        public void afterTextChanged(Editable editable) {
            XMockPropIO prop = props.get(getAdapterPosition());
            prop.mockValue = editable.toString();
            if(!props_modified.contains(prop))
                props_modified.add(prop);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // No implementation needed for this example
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // No implementation needed for this example
        }

        void updateExpanded() {
            XMockPropIO prop = props.get(getAdapterPosition());
            String name = prop.getName();

            boolean isExpanded = expanded.containsKey(name) && expanded.get(name);
            ivExpanderProps.setImageLevel(isExpanded ? 1 : 0);

            tiPropMockValue.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            ivValueResetDefault.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }

    AdapterProp() { setHasStableIds(true); }
    AdapterProp(Context context) {
        setHasStableIds(true);
    }

    void set(List<XMockPropIO> props_vals) {
        props.clear();
        Log.i(TAG, "Set has Init=" + props.size());
        props.addAll(props_vals);
        Log.i(TAG, "Internal Count=" + props.size());
        notifyDataSetChanged();
    }

    public void updateFromModified(final Context context) {
        if(props_modified != null && !props_modified.isEmpty()) {
            Log.i(TAG, "props_modified=" + props_modified.size());
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        //Snackbar.make(, view.getContext().getString("Hiiiiiiiiii"), Snackbar.LENGTH_LONG).show();
                        final Bundle ret = XMockProxyApi.callPutMockProps(context, props_modified);
                        //XMockProvider.updateCache(props);//Might be no point given the different context
                        //No need to update our cache since when we modify the elements on this context
                        //it is ref based it will effect the element in the list

                        props_modified.clear();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if(ret == null) {
                                    Toast.makeText(context, "Should be saved ! ? eh it returned Nulled", Toast.LENGTH_SHORT).show();
                                    //Snackbar.make(this, context.getString("Hiiiiiiiiii"), Snackbar.LENGTH_LONG).show();
                                    //Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.msg_no_service), Snackbar.LENGTH_INDEFINITE);

                                    //Haha this Invokes :P least its not denying it working keke
                                    return;
                                }

                                final int xv = ret.getInt("result");
                                Log.i(TAG, "Return from Modified=" + xv);
                                switch (xv) {
                                    case -1:
                                        Toast.makeText(context, "Fail to Save :(", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 0:
                                        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                                notifyDataSetChanged();
                            }
                        });
                    }

                    //props_modified.clear();
                }
            });
            //notifyDataSetChanged();
        }else {
            Toast.makeText(context, "Nothing Needs to be Saved !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public long getItemId(int position) { return props.get(position).hashCode(); }

    @Override
    public int getItemCount() { return props.size(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.prop, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.unwire();
        XMockPropIO prop = props.get(position);

        holder.tvPropNameLabel.setText(prop.getName());
        holder.tvDefaultValue.setText(prop.getDefaultValue());
        holder.tiPropMockValue.setText(prop.getMockValue());
        holder.cbEnableMock.setChecked(prop.getIsEnabled());

        holder.updateExpanded();
        holder.wire();
    }
}

