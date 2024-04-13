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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.ui.interfaces.ILoader;
import eu.faircode.xlua.ui.dialogs.SettingAddDialog;
import eu.faircode.xlua.ui.ViewFloatingAction;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.SettingUtil;
import eu.faircode.xlua.utilities.UiUtil;
import eu.faircode.xlua.utilities.ViewUtil;

public class FragmentSettings
        extends
        ViewFloatingAction
        implements
        View.OnClickListener,
        View.OnLongClickListener,
        CompoundButton.OnCheckedChangeListener,
        ILoader {

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterSetting rvAdapter;

    private ImageView ivExpander;
    private CardView cvAppView;
    private CheckBox cbUseDefault;

    private Button btProperties, btConfigs, btKill;
    private boolean isViewOpen = true;
    private int lastHeight = 0;

    private static final String USE_DEFAULT = "useDefault";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View main = inflater.inflate(R.layout.settingrecyclerview, container, false);
        this.TAG_ViewFloatingAction = "XLua.FragmentSettings";
        this.application = AppGeneric.from(getArguments(), getContext());
        ivExpander = main.findViewById(R.id.ivExpanderSettingsApp);
        cvAppView = main.findViewById(R.id.cvAppInfoSettings);

        btProperties = main.findViewById(R.id.btSettingsToProperties);
        btConfigs = main.findViewById(R.id.btSettingsToConfigs);
        btKill = main.findViewById(R.id.btSettingsKillApp);
        cbUseDefault = main.findViewById(R.id.cbUseDefaultSettings);
        if(this.application.isGlobal()) {
            cbUseDefault.setEnabled(false);
            btKill.setEnabled(false);
        }else cbUseDefault.setChecked(XLuaCall.getSettingBoolean(getContext(), application.getUid(), application.getPackageName(), USE_DEFAULT));

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
        XLog.i("onLongClick=" + code);
        switch (code) {
            case R.id.flSettingsButtonTwo:
                Snackbar.make(v, R.string.menu_settings_randomize_hint, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.flSettingsButtonThree:
                Snackbar.make(v, R.string.menu_settings_add_hint, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.flSettingsButtonFour:
                Snackbar.make(v, R.string.menu_settings_save_hint, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.flSettingsButtonFive:
                Snackbar.make(v, R.string.menu_settings_delete_hint, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.flSettingsButtonOne:
                Snackbar.make(v, R.string.menu_settings_hint, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.cbUseDefaultSettings:
                Toast.makeText(getContext(), R.string.menu_settings_use_default_hint, Toast.LENGTH_LONG).show();
                break;
        }

        return true;
    }

    @SuppressLint("NonConstantResourceId") @Override
    public void onClick(View v) {
        int id = v.getId();
        XLog.i("onClick id=" + id);
        switch (id) {
            case R.id.btSettingsKillApp:
                final XResult res = KillAppCommand.invokeEx(v.getContext(), application.getPackageName(), application.getUid());
                Snackbar.make(v, res.getResultMessage(), Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.ivExpanderSettingsApp:
                updateExpanded();
                break;
            case R.id.btSettingsToProperties:
                Intent propsIntent = new Intent(v.getContext(), ActivityProperties.class);
                propsIntent.putExtra("packageName", application.getPackageName());
                v.getContext().startActivity(propsIntent);
                break;
            case R.id.btSettingsToConfigs:
                Intent configIntent = new Intent(v.getContext(), ActivityConfig.class);
                configIntent.putExtra("packageName", application.getPackageName());
                v.getContext().startActivity(configIntent);
                break;
            case R.id.flSettingsButtonOne:
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

    @Override
    public FragmentManager getManager() {  return getFragmentManager(); }

    @Override
    public Fragment getFragment() { return this; }

    @Override
    public AppGeneric getApplication() { return this.application; }

    @Override
    public void filter(String query) { if (rvAdapter != null) rvAdapter.getFilter().filter(query); }

    private void wire() {
        ivExpander.setOnClickListener(this);
        btProperties.setOnClickListener(this);
        btKill.setOnClickListener(this);
        btConfigs.setOnClickListener(this);
        cvAppView.setOnClickListener(this);
        cbUseDefault.setOnCheckedChangeListener(this);
        cbUseDefault.setOnLongClickListener(this);
    }

    void updateExpanded() {
        isViewOpen = !isViewOpen;
        ViewUtil.setViewsVisibility(ivExpander, isViewOpen, btProperties, btConfigs, btKill, cbUseDefault);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        XLog.i("onCheckedChanged id=" + id);
        try {
            if(id == R.id.cbUseDefaultSettings) {
                XLuaCall.putSettingBoolean(
                        cbUseDefault.getContext(),
                        application.getUid(),
                        application.getPackageName(),
                        USE_DEFAULT,
                        isChecked,
                        application.getForceStop());
            }
        }catch (Exception e) { XLog.e("onCheckedChanged Failed, id=" + id, e, true); }
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onPause() { super.onPause(); }

    @Override
    public void loadData() {
        XLog.i("Starting data loader");
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks<SettingsDataHolder> dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<SettingsDataHolder>() {
        @NonNull @Override
        public Loader<SettingsDataHolder> onCreateLoader(int id, Bundle args) { return new SettingsDataLoader(getContext()).setApp(application); }

        @Override
        public void onLoadFinished(Loader<SettingsDataHolder> loader, SettingsDataHolder data) {
            XLog.i("onLoadFinished Data Loader Finished");
            if(data.exception == null) {
                UiUtil.initTheme(getActivity(), data.theme);
                if(CollectionUtil.isValid(data.settings)) {
                    SettingUtil.sortSettings(data.settings);
                    rvAdapter.set(data.settings, application);
                }

                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }else Snackbar.make(Objects.requireNonNull(getView()), data.exception.toString(), Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onLoaderReset(@NonNull Loader<SettingsDataHolder> loader) { }
    };

    private static class SettingsDataLoader extends AsyncTaskLoader<SettingsDataHolder> {
        private AppGeneric application;
        public SettingsDataLoader setApp(AppGeneric application) { this.application = application; return this; }

        SettingsDataLoader(Context context) { super(context); setUpdateThrottle(1000); }

        @Nullable
        @Override
        public SettingsDataHolder loadInBackground() {
            XLog.i("Data loader started");
            SettingsDataHolder data = new SettingsDataHolder();
            try {
                data.theme = XLuaCall.getTheme(getContext());
                data.settings = new ArrayList<>(XMockQuery.getAllSettings(getContext(), application));
                XLog.i("Settings from Data Loader=" + data.settings.size());
            }catch (Throwable ex) {
                data.settings.clear();
                data.exception = ex;
                XLog.e("Data Loader Exception", ex, true);
            }

            XLog.i("Data Loader Settings Finished=" + data.settings.size());
            return data;
        }
    }

    private static class SettingsDataHolder {
        String theme;
        List<LuaSettingExtended> settings = new ArrayList<>();
        Throwable exception = null;
    }
}
