package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import eu.faircode.xlua.api.properties.MockPropConversions;
import eu.faircode.xlua.api.properties.MockPropGroupHolder;
import eu.faircode.xlua.api.properties.MockPropSetting;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xmock.XMockQuery;
import eu.faircode.xlua.ui.ViewFloatingAction;
import eu.faircode.xlua.ui.dialogs.PropertyAddDialog;

public class FragmentProperties extends ViewFloatingAction implements View.OnClickListener, View.OnLongClickListener {
    private final static String TAG = "XLua.FragmentProperties";

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterPropertiesGroup rvPropsAdapter;

    private TextView tvPropCount;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View main = inflater.inflate(R.layout.proprecyclerview, container, false);

        this.TAG_ViewFloatingAction = TAG;
        this.application = AppGeneric.from(getArguments(), getContext());

        super.initActions();
        super.bindTextViewsToAppId(main, R.id.ivPropertiesAppIcon, R.id.tvPropertiesPackageName, R.id.tvPropertiesPackageFull, R.id.tvPropertiesPackageUid);
        super.setFloatingActionBars(this, this, main,  R.id.flPropertiesMainButton, R.id.flPropertiesAddMapButton);

        tvPropCount = main.findViewById(R.id.tvPropCountProperties);

        progressBar = main.findViewById(R.id.pbProperties);
        int colorAccent = XUtil.resolveColor(Objects.requireNonNull(getContext()), R.attr.colorAccent);
        swipeRefresh = main.findViewById(R.id.swipeRefreshProperties);
        swipeRefresh.setColorSchemeColors(colorAccent, colorAccent, colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { loadData(); }
        });

        super.initRecyclerView(main, R.id.rvProperties, true);
        rvList.setVisibility(View.VISIBLE);
        rvList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(@NonNull RecyclerView parent, @NonNull RecyclerView.State state, @NonNull View child, View focused) { return true; }
        };

        llm.setAutoMeasureEnabled(true);
        rvList.setLayoutManager(llm);
        rvPropsAdapter = new AdapterPropertiesGroup(getFragmentManager(), application);
        rvList.setAdapter(rvPropsAdapter);

        loadData();
        return main;
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.i(TAG, " onClick id=" + id);

        switch (id) {
            case R.id.flPropertiesMainButton:
                //invokeFloatingAction();
                invokeFloatingActions();
                break;
            case R.id.flPropertiesAddMapButton:
                PropertyAddDialog setDialog = new PropertyAddDialog();
                setDialog.show(Objects.requireNonNull(getFragmentManager()), "Add Property");
                break;
        }
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onPause() { super.onPause(); }

    public void filter(String query) { if (rvPropsAdapter != null) rvPropsAdapter.getFilter().filter(query); }


    public void loadData() {
        Log.i(TAG, "Starting data loader");
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks<PropsDataHolder> dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<PropsDataHolder>() {
        @NonNull
        @Override
        public Loader<PropsDataHolder> onCreateLoader(int id, Bundle args) { return new PropsDataLoader(getContext()).setApp(application); }

        @SuppressLint("SetTextI18n")
        @Override
        public void onLoadFinished(Loader<PropsDataHolder> loader, PropsDataHolder data) {
            Log.i(TAG, "onLoadFinished");
            if(data.exception == null) {
                ActivityBase activity = (ActivityBase) getActivity();
                if (!data.theme.equals(activity.getThemeName()))
                    activity.recreate();

                Collections.sort(data.propGroups, new Comparator<MockPropGroupHolder>() {
                    @Override
                    public int compare(MockPropGroupHolder o1, MockPropGroupHolder o2) {
                        return o1.getSettingName().compareToIgnoreCase(o2.getSettingName());
                    }
                });

                tvPropCount.setText(Integer.toString(data.totalProps));
                rvPropsAdapter.set(data.propGroups);
                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(getView(), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<PropsDataHolder> loader) { }
    };

    private static class PropsDataLoader extends AsyncTaskLoader<PropsDataHolder> {
        private AppGeneric application;
        public PropsDataLoader setApp(AppGeneric application) {
            this.application = application;
            return this;
        }

        PropsDataLoader(Context context) {
            super(context);
            setUpdateThrottle(1000);
        }

        @Nullable
        @Override
        public PropsDataHolder loadInBackground() {
            Log.i(TAG, "Data loader started");
            PropsDataHolder data = new PropsDataHolder();
            try {
                data.theme = XLuaCall.getTheme(getContext());
                Log.i(TAG, "Loading properties with application=" + application);
                Collection<MockPropSetting> props = XMockQuery.getAllProperties(getContext(), application);
                Collection<LuaSettingExtended> settings = new ArrayList<>(XMockQuery.getAllSettings(getContext(), true, application.getUid(), application.getPackageName()));
                data.totalProps = props.size();
                Log.i(TAG, "props size=" + props.size() + " settings size=" + settings.size());
                data.propGroups = new ArrayList<>(MockPropConversions.createHolders(getContext(), props, settings));
                Log.i(TAG, "prop groups from cursor=" + data.propGroups.size());
            }catch (Throwable ex) {
                data.propGroups.clear();
                data.exception = ex;
                data.totalProps = 0;
                Log.e(TAG, ex.getMessage());
            }

            Log.i(TAG, "DataLoader Props Finished=" + data.propGroups.size());
            return data;
        }
    }

    private static class PropsDataHolder {
        String theme;
        List<MockPropGroupHolder> propGroups = new ArrayList<>();
        int totalProps;
        Throwable exception = null;
    }
}
