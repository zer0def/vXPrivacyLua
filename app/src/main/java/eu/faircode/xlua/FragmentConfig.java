package eu.faircode.xlua;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.api.XLuaCallApi;
import eu.faircode.xlua.api.XMockQueryApi;
import eu.faircode.xlua.api.objects.xmock.ConfigSetting;
import eu.faircode.xlua.api.objects.xmock.phone.MockConfigConversions;
import eu.faircode.xlua.api.objects.xmock.phone.MockPhoneConfig;

public class FragmentConfig extends Fragment {
    private final static String TAG = "XLua.FragmentConfig";

    private AdapterConfig rvConfigAdapter;

    //private List<MockPhoneConfig> configs = new ArrayList<>();
    private Spinner spConfigSelection;

    private ArrayAdapter<MockPhoneConfig> spConfigs;

    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(DebugUtil.isDebug())
            Log.i(TAG, "FragmentConfig.onCreateView Enter");


        final View main = inflater.inflate(R.layout.configeditor, container, false);
        Log.i(TAG, "MAIN View Created for Fragment Config");


        Log.i(TAG, "Creating the Drop Down for Configs Fragment Config");
        //Start of Drop Down
        spConfigs = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        spConfigs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Log.i(TAG, "Created the Empty Array for Configs Fragment Config");
        spConfigSelection = main.findViewById(R.id.spConfigEdit);
        spConfigSelection.setTag(null);
        spConfigSelection.setAdapter(spConfigs);
        spConfigSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateSelection();
            }

            private void updateSelection() {
                MockPhoneConfig selected = (MockPhoneConfig) spConfigSelection.getSelectedItem();
                String configName = (selected == null ? null : selected.getName());

                Log.i(TAG, "CONFIG SELECTED=" + configName);
            }
        });


        Log.i(TAG, "Created Configs Drop Down, Getting Rotate View For Config Settings, Fragment Config");
        RecyclerView rvSettings = main.findViewById(R.id.rvConfigSettings);
        rvSettings.setVisibility(View.VISIBLE);
        rvSettings.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
                return true;
            }
        };

        Log.i(TAG, "Created Layout Settings for Config Settings, Fragment Config");

        llm.setAutoMeasureEnabled(true);
        rvSettings.setLayoutManager(llm);
        rvConfigAdapter = new AdapterConfig();
        rvSettings.setAdapter(rvConfigAdapter);

        Log.i(TAG, "Created the Layout for Config Settings, Fragment Config");

        if(DebugUtil.isDebug())
            Log.i(TAG, "FragmentConfig.onCreateView Leave");

        return main;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "Starting on RESUME");
        super.onResume();
        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void loadData() {
        if(DebugUtil.isDebug())
            Log.i(TAG, "Starting data loader");

        LoaderManager manager = getActivity().getSupportLoaderManager();
        //ActivityMain loader data ?
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<SettingDataHolder>() {
        @Override
        public Loader<SettingDataHolder> onCreateLoader(int id, Bundle args) {
            return new SettingDataLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<SettingDataHolder> loader, SettingDataHolder data) {
            if(DebugUtil.isDebug())
                Log.i(TAG, "onLoadFinished");

            if(data.exception == null) {
                Log.i(TAG, "onLoad Data first stage");
                ActivityBase activity = (ActivityBase) getActivity();
                if (!data.theme.equals(activity.getThemeName()))
                    activity.recreate();

                Log.i(TAG, "Created some activity");

                Collections.sort(data.settings, new Comparator<ConfigSetting>() {
                    @Override
                    public int compare(ConfigSetting o1, ConfigSetting o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });

                Log.i(TAG, "onLoad Data mapping...");

                spConfigs.clear();
                spConfigs.addAll(data.configs);

                //DataLoader settings is not needed as it will be resolved here
                MockPhoneConfig config = (MockPhoneConfig) spConfigSelection.getSelectedItem();
                Log.i(TAG, "Config selected=" + config.getName());


                //rvCpuAdapter.set(data.maps);
                //swipeRefresh.setRefreshing(false);
                //pbCpu.setVisibility(View.GONE);
            }else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(getView(), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<SettingDataHolder> loader) {
            // Do nothing
        }
    };

    private static class SettingDataLoader extends AsyncTaskLoader<SettingDataHolder> {
        SettingDataLoader(Context context) {
            super(context);
            setUpdateThrottle(1000);
        }

        @Nullable
        @Override
        public SettingDataHolder loadInBackground() {
            Log.i(TAG, "Data loader started");

            SettingDataHolder data = new SettingDataHolder();
            try {
                data.theme = XLuaCallApi.getTheme(getContext());
                data.settings.clear();
                data.configs.clear();

                Log.i(TAG, "Getting Phone Configs...");
                List<MockPhoneConfig> configs = new ArrayList<>(XMockQueryApi.getConfigs(getContext(), true));
                Log.i(TAG, "Config size=" + configs.size());
                data.configs.addAll(configs);

                Map<String, String> settingsMap = configs.get(0).getSettings();
                Log.i(TAG, "Config Settings size=" + settingsMap.size());
                List<ConfigSetting> settings = new ArrayList<>(MockConfigConversions.hashMapToListSettings(settingsMap));
                data.settings.addAll(settings);
            }catch (Throwable ex) {
                data.settings.clear();
                data.exception = ex;
                Log.e(TAG, ex.getMessage());
            }

            Log.i(TAG, "DataLoader Configs size=" + data.configs.size() + " Settings size=" + data.settings.size());
            return data;
        }
    }

    private static class SettingDataHolder {
        String theme;
        List<MockPhoneConfig> configs = new ArrayList<>();
        List<ConfigSetting> settings = new ArrayList<>();
        Throwable exception = null;
    }
}
