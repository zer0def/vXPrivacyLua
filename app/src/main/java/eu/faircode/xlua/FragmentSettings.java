package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xmock.XMockQuery;
import eu.faircode.xlua.api.xmock.call.KillAppCommand;
import eu.faircode.xlua.ui.dialogs.SettingAddDialog;
import eu.faircode.xlua.ui.ViewFloatingAction;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.SettingUtil;
import eu.faircode.xlua.utilities.UiUtil;
import eu.faircode.xlua.utilities.ViewUtil;

public class FragmentSettings  extends ViewFloatingAction implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "XLua.FragmentSettings";

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterSetting rvAdapter;

    private ImageView ivExpander;
    private CardView cvAppView;

    private Button btProperties, btConfigs, btKill;
    private boolean isViewOpen = true;
    private int lastHeight = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View main = inflater.inflate(R.layout.settingrecyclerview, container, false);
        this.TAG_ViewFloatingAction = TAG;
        this.application = AppGeneric.from(getArguments(), getContext());
        ivExpander = main.findViewById(R.id.ivExpanderSettingsApp);
        cvAppView = main.findViewById(R.id.cvAppInfoSettings);

        btProperties = main.findViewById(R.id.btSettingsToProperties);
        btConfigs = main.findViewById(R.id.btSettingsToConfigs);
        btKill = main.findViewById(R.id.btSettingsKillApp);

        super.initActions();
        super.bindTextViewsToAppId(main, R.id.ivSettingsAppIcon, R.id.tvSettingsPackageName, R.id.tvSettingsPackageFull, R.id.tvSettingsPackageUid);
        super.setFloatingActionBars(this, this, main,  R.id.flSettingsButtonOne, R.id.flSettingsButtonTwo, R.id.flSettingsButtonThree, R.id.flSettingsButtonFour, R.id.flSettingsButtonFive);

        //init Refresh
        progressBar = main.findViewById(R.id.pbSettings);
        int colorAccent = XUtil.resolveColor(requireContext(), R.attr.colorAccent);
        swipeRefresh = main.findViewById(R.id.swipeRefreshSettings);
        swipeRefresh.setColorSchemeColors(colorAccent, colorAccent, colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { loadData(); }
        });

        //init RecyclerView
        super.initRecyclerView(main, R.id.rvSettings, true);
        rvList.setVisibility(View.VISIBLE);
        rvList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) { return true; }
        };

        llm.setAutoMeasureEnabled(true);
        rvList.setLayoutManager(llm);
        rvAdapter = new AdapterSetting(getFragmentManager());
        rvList.setAdapter(rvAdapter);
        rvList.addItemDecoration(SettingUtil.createSettingsDivider(getContext()));


        //Ensure Padding Between Top behind app card view and First Element
        cvAppView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = cvAppView.getHeight();
                if(height != lastHeight) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cvAppView.getLayoutParams();
                    int totalHeight = height + layoutParams.topMargin + layoutParams.bottomMargin + 15;
                    Log.i(TAG, "Height changed before=" + lastHeight + " now=" + height);
                    rvList.setPadding(0, totalHeight, 0, 0);

                    int lastHeightCopy = lastHeight;
                    lastHeight = height;

                    UiUtil.setSwipeRefreshLayoutEndOffset(getContext(), swipeRefresh, totalHeight);

                    LinearLayoutManager layoutManager = (LinearLayoutManager) rvList.getLayoutManager();
                    assert layoutManager != null;
                    int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();

                    if (firstVisiblePosition == 0) {
                        if(height > lastHeightCopy)
                            rvList.scrollBy(0, -totalHeight);
                    }
                }
            }
        });

        updateExpanded();
        wire();
        loadData();
        return main;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onLongClick(View v) {
        int code = v.getId();
        Log.i(TAG, "onLongClick=" + code);
        switch (code) {
            case R.id.flSettingsButtonTwo:
                Toast.makeText(getContext(), "Randomize All Selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flSettingsButtonThree:
                Toast.makeText(getContext(), "Add Setting", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flSettingsButtonFour:
                Toast.makeText(getContext(), "Save Un-Saved Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flSettingsButtonFive:
                Toast.makeText(getContext(), "Delete ONLY Selected settings", Toast.LENGTH_SHORT).show();
                break;
                //case R.id.flActionConfigImport:
            //    Toast.makeText(getContext(), "Import Config", Toast.LENGTH_SHORT).show();
            //    break;
            case R.id.flSettingsButtonOne:
                Toast.makeText(getContext(), "Options", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @SuppressLint("NonConstantResourceId") @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.i(TAG, " onClick id=" + id);

        switch (id) {
            case R.id.btSettingsKillApp:
                final XResult res = KillAppCommand.invokeEx(v.getContext(), application.getPackageName(), application.getUid());
                Toast.makeText(getContext(), res.getResultMessage(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivExpanderSettingsApp:
                updateExpanded();
                break;
            case R.id.btSettingsToProperties:
                //have ability to go back and resume where we were last
                //Save this instance jump there go back here if needed
                Intent propsIntent = new Intent(v.getContext(), ActivityProperties.class);
                Log.i(TAG, "opening props with package=" + application);
                propsIntent.putExtra("packageName", application.getPackageName());
                v.getContext().startActivity(propsIntent);
                break;
            case R.id.btSettingsToConfigs:
                Intent configIntent = new Intent(v.getContext(), ActivityConfig.class);
                configIntent.putExtra("packageName", application.getPackageName());
                v.getContext().startActivity(configIntent);
                break;
            case R.id.flSettingsButtonOne:
                //invokeFloatingAction();
                invokeFloatingActions();
                break;
            case R.id.flSettingsButtonTwo:
                rvAdapter.randomizeAll(v.getContext());
                break;
            case R.id.flSettingsButtonThree:
                SettingAddDialog setDialog = new SettingAddDialog();
                setDialog.setApplication(application);
                setDialog.show(Objects.requireNonNull(getFragmentManager()), "Add Setting");
                break;
            case R.id.flSettingsButtonFour:
                rvAdapter.saveAll(v.getContext());
                break;
            case R.id.flSettingsButtonFive:
                rvAdapter.deleteSelected(v.getContext());
                break;
        }

    }

    public void filter(String query) { if (rvAdapter != null) rvAdapter.getFilter().filter(query); }

    private void wire() {
        ivExpander.setOnClickListener(this);
        btProperties.setOnClickListener(this);
        btKill.setOnClickListener(this);
        btConfigs.setOnClickListener(this);
        cvAppView.setOnClickListener(this);
    }

    void updateExpanded() {
        isViewOpen = !isViewOpen;
        ViewUtil.setViewsVisibility(ivExpander, isViewOpen, btProperties, btConfigs, btKill);
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onPause() { super.onPause(); }

    public void loadData() {
        Log.i(TAG, "Starting data loader");
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks<SettingsDataHolder> dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<SettingsDataHolder>() {
        @NonNull @Override
        public Loader<SettingsDataHolder> onCreateLoader(int id, Bundle args) { return new SettingsDataLoader(getContext()).setApp(application); }

        @Override
        public void onLoadFinished(Loader<SettingsDataHolder> loader, SettingsDataHolder data) {
            Log.i(TAG, "onLoadFinished");
            if(data.exception == null) {
                ActivityBase activity = (ActivityBase) getActivity();
                if(activity != null) if (!data.theme.equals(activity.getThemeName())) activity.recreate();

                if(CollectionUtil.isValid(data.settings)) {
                    SettingUtil.sortSettings(data.settings);
                    rvAdapter.set(data.settings, application);
                }

                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(Objects.requireNonNull(getView()), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<SettingsDataHolder> loader) { }
    };

    private static class SettingsDataLoader extends AsyncTaskLoader<SettingsDataHolder> {
        private AppGeneric application;
        public SettingsDataLoader setApp(AppGeneric application) {
            this.application = application;
            return this;
        }

        SettingsDataLoader(Context context) {
            super(context);
            setUpdateThrottle(1000);
        }

        @Nullable
        @Override
        public SettingsDataHolder loadInBackground() {
            Log.i(TAG, "Data loader started");
            SettingsDataHolder data = new SettingsDataHolder();
            try {
                data.theme = XLuaCall.getTheme(getContext());
                Log.i(TAG, "Getting settings for=" + application);
                data.settings = new ArrayList<>(XMockQuery.getAllSettings(getContext(), application));
                Log.i(TAG, "settings from cursor=" + data.settings.size());
            }catch (Throwable ex) {
                data.settings.clear();
                data.exception = ex;
                Log.e(TAG, Objects.requireNonNull(ex.getMessage()));
            }

            Log.i(TAG, "DataLoader Settings Finished=" + data.settings.size());
            return data;
        }
    }

    private static class SettingsDataHolder {
        String theme;
        List<LuaSettingExtended> settings = new ArrayList<>();
        Throwable exception = null;
    }
}
