package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.configs.MockConfig;
import eu.faircode.xlua.api.configs.MockConfigPacket;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.api.xmock.XMockQuery;
import eu.faircode.xlua.ui.ViewFloatingAction;
import eu.faircode.xlua.utilities.FileDialogUtil;

public class  FragmentConfig extends ViewFloatingAction implements View.OnClickListener, View.OnLongClickListener {
    private final static String TAG = "XLua.FragmentConfig";
    private static final int PICK_FILE_REQUEST_CODE = 1; // This is a request code you define to identify your request
    private static final int PICK_FOLDER_RESULT_CODE = 2;

    private AdapterConfig rvConfigAdapter;
    private Spinner spConfigSelection;
    private ArrayAdapter<MockConfig> spConfigs;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public View onCreateView(final @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "FragmentConfig.onCreateView Enter");

        final View main = inflater.inflate(R.layout.configeditor, container, false);

        this.application = AppGeneric.from(getArguments(), getContext());
        this.TAG_ViewFloatingAction = TAG;
        super.initActions();
        super.bindTextViewsToAppId(main, R.id.ivConfigsAppIcon, R.id.tvConfigsPackageName, R.id.tvConfigsPackageFull, R.id.tvConfigsPackageUid);
        super.setFloatingActionBars(this, this, main, R.id.flActionConfigOptions, R.id.flActionConfigSave, R.id.flActionConfigImport, R.id.flActionConfigApply, R.id.flActionConfigExport);

        progressBar = main.findViewById(R.id.pbConfigs);
        swipeRefresh = main.findViewById(R.id.swipeRefreshConfigs);

        super.initRecyclerView(main, R.id.rvConfigSettings, true);
        rvList.setVisibility(View.VISIBLE);
        rvList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(@NonNull RecyclerView parent, @NonNull RecyclerView.State state, @NonNull View child, View focused) {
                return true;
            }
        };

        if(DebugUtil.isDebug()) Log.i(TAG, "Created Layout Settings for Config Settings, Fragment Config");
        llm.setAutoMeasureEnabled(true);
        rvList.setLayoutManager(llm);
        rvConfigAdapter = new AdapterConfig(application);
        rvList.setAdapter(rvConfigAdapter);
        if(DebugUtil.isDebug()) Log.i(TAG, "Created the Layout for Config Settings, Fragment Config, leaving now...");

        initDropDown(main);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { loadData(); }
        });

        return main;
    }

    public void pushConfig(MockConfig config) {
        Log.i(TAG, "[pushConfig] config=" + config);
        spConfigs.add(config);
        spConfigs.notifyDataSetChanged();
    }

    public void pushConfigs(List<MockConfig> configs) {
        Log.i(TAG, "[pushConfigs] configs size=" + configs.size());
        spConfigs.clear();
        for(MockConfig c : configs) {
            for (LuaSettingExtended s : c.getSettings())
                s.setIsEnabled(true);
        }

        spConfigs.addAll(configs);
        spConfigs.notifyDataSetChanged();
    }

    @Override
    public void onResume() { super.onResume();  loadData(); }

    @Override
    public void onPause() { super.onPause(); }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int code = v.getId();
        Log.i(TAG, "onClick=" + code);

        switch (code) {
            case R.id.flActionConfigApply:
                Log.i(TAG, "Applying Settings from config=" + rvConfigAdapter.getConfigName());

                swipeRefresh.setRefreshing(true);
                progressBar.setVisibility(View.VISIBLE);
                rvConfigAdapter.applyConfig(getContext());
                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Finished Applying Settings!", Toast.LENGTH_LONG).show();
                break;
            case R.id.flActionConfigSave:
                final MockConfigPacket packet = MockConfigPacket.create(rvConfigAdapter.getConfigName(), rvConfigAdapter.getEnabledSettings());
                packet.setCode(MockConfigPacket.CODE_INSERT_UPDATE_CONFIG);
                //config.setSettings(XMockConfigConversions.listToHashMapSettings(settings, false));
                //config.orderSettings(true);
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        final XResult ret = XMockCall.putMockConfig(getContext(), packet);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), ret.getResultMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            case R.id.flActionConfigExport:
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                try {
                    startActivityForResult(intent, PICK_FOLDER_RESULT_CODE);
                } catch (Exception e) {
                    Log.e(TAG, "Open Directory Error: " + e);
                    Toast.makeText(getContext(), "An error occurred while opening the directory picker.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.flActionConfigImport:
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("*/*"); // Use "image/*" for images, "application/pdf" for PDF, etc.
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent2, "Select a file"), PICK_FILE_REQUEST_CODE);
                } catch (Exception e) {
                    Log.e(TAG, "Open File Error: " + e);
                    Toast.makeText(getContext(), "An error occurred while opening target Config File.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.flActionConfigOptions:
                //invokeFloatingAction();
                invokeFloatingActions();
                break;

        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onLongClick(View v) {
        int code = v.getId();
        Log.i(TAG, "onLongClick=" + code);
        switch (code) {
            case R.id.flActionConfigApply:
                Toast.makeText(getContext(), "Apply Config", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flActionConfigSave:
                Toast.makeText(getContext(), "Save Config", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flActionConfigExport:
                Toast.makeText(getContext(), "Export Config", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flActionConfigImport:
                Toast.makeText(getContext(), "Import Config", Toast.LENGTH_SHORT).show();
                break;
            case R.id.flActionConfigOptions:
                Toast.makeText(getContext(), "Options", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null)
            return;

        Uri selectedFileUri = data.getData();
        if(selectedFileUri == null || resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FILE_REQUEST_CODE:
                String mimeType = Objects.requireNonNull(getContext()).getContentResolver().getType(selectedFileUri);
                if ("application/json".equals(mimeType) || "text/plain".equals(mimeType)) {
                    final MockConfig config = FileDialogUtil.readPhoneConfig(getContext(), selectedFileUri);
                    if(config == null)
                        Toast.makeText(getContext(), "Failed Read Config File: " + selectedFileUri.getPath(), Toast.LENGTH_SHORT).show();
                    else {
                        String configName = config.getName();
                        for(int i = 0; i < spConfigs.getCount(); i++) {
                            MockConfig conf = spConfigs.getItem(i);
                            assert conf != null;
                            if(configName.equals(conf.getName())) {
                                configName += "-" + ThreadLocalRandom.current().nextInt(10000,999999999);
                                config.setName(configName);
                                break;
                            }
                        }

                        pushConfig(config);
                        Toast.makeText(getContext(), "Read Config: " + configName, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "File type for Parsing is not Supported: " + mimeType, Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_FOLDER_RESULT_CODE:
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                Objects.requireNonNull(getContext()).getContentResolver().takePersistableUriPermission(selectedFileUri, takeFlags);

                if(!FileDialogUtil.saveConfigSettings(getContext(), selectedFileUri, rvConfigAdapter))
                    Toast.makeText(getContext(), "Failed to Save File: " + rvConfigAdapter.getConfigName(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Saved File: " + rvConfigAdapter.getConfigName(), Toast.LENGTH_SHORT).show();

                break;
        }
    }

    public void initDropDown(View view) {
        if(DebugUtil.isDebug())
            Log.i(TAG, "Creating the Drop Down for Configs Fragment Config");

        //Start of Drop Down
        spConfigs = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item);
        spConfigs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(DebugUtil.isDebug())
            Log.i(TAG, "Created the Empty Array for Configs Fragment Config");

        spConfigSelection = view.findViewById(R.id.spConfigEdit);
        spConfigSelection.setTag(null);
        spConfigSelection.setAdapter(spConfigs);
        spConfigSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updateSelection(); }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateSelection();
            }

            private void updateSelection() {
                MockConfig selected = (MockConfig) spConfigSelection.getSelectedItem();
                String configName = (selected == null ? null : selected.getName());
                if(DebugUtil.isDebug())
                    Log.i(TAG, "CONFIG SELECTED=" + configName);

                if (configName == null ? spConfigSelection.getTag() != null : !configName.equals(spConfigSelection.getTag()))
                    spConfigSelection.setTag(configName);

                if(selected != null)
                    rvConfigAdapter.set(selected);
            }
        });
    }

    private void loadData() {
        Log.i(TAG, "Starting data loader");
        LoaderManager manager = Objects.requireNonNull(getActivity()).getSupportLoaderManager();
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks<PropsDataHolder> dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<PropsDataHolder>() {
        @NonNull
        @Override
        public Loader<PropsDataHolder> onCreateLoader(int id, Bundle args) { return new ConfigsDataLoader(getContext()); }

        @Override
        public void onLoadFinished(@NonNull Loader<PropsDataHolder> loader, PropsDataHolder data) {
            Log.i(TAG, "onLoadFinished");
            if(data.exception == null) {
                ActivityBase activity = (ActivityBase) getActivity();
                assert activity != null;
                if (!data.theme.equals(activity.getThemeName()))
                    activity.recreate();

                pushConfigs(data.configs);
                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(Objects.requireNonNull(getView()), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<PropsDataHolder> loader) { }
    };

    private static class ConfigsDataLoader extends AsyncTaskLoader<PropsDataHolder> {
        ConfigsDataLoader(Context context) {
            super(context);
            setUpdateThrottle(1000);
        }

        @Nullable
        @Override
        public PropsDataHolder loadInBackground() {
            Log.i(TAG, "Data loader started");
            PropsDataHolder data = new PropsDataHolder();
            try {
                Log.i(TAG, "Getting cursor");
                data.theme = XLuaCall.getTheme(getContext());
                data.configs = new ArrayList<>(XMockQuery.getConfigs(getContext()));
                Log.i(TAG, "configs from cursor=" + data.configs.size());
            }catch (Throwable ex) {
                data.configs.clear();
                data.exception = ex;
                Log.e(TAG, Objects.requireNonNull(ex.getMessage()));
            }

            Log.i(TAG, "DataLoader Props Finished=" + data.configs.size());
            return data;
        }
    }

    private static class PropsDataHolder {
        String theme;
        List<MockConfig> configs = new ArrayList<>();
        Throwable exception = null;
    }
}
