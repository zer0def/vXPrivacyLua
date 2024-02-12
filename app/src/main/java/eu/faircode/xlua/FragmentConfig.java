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


                if (configName == null ? spConfigSelection.getTag() != null : !configName.equals(spConfigSelection.getTag())) {
                    spConfigSelection.setTag(configName);
                }

                if(selected != null) {
                    List<ConfigSetting> settings = new ArrayList<>(MockConfigConversions.hashMapToListSettings(selected.getSettings()));
                    if(DebugUtil.isDebug())
                        Log.i(TAG, "SELECTED SETTINGS COUNT=" + settings.size());

                    rvConfigAdapter.set(settings);
                }

                Log.i(TAG, "END CONFIG SELECTED=" + configName);
            }
        });


        if(DebugUtil.isDebug())
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

        if(DebugUtil.isDebug())
            Log.i(TAG, "Created Layout Settings for Config Settings, Fragment Config");

        llm.setAutoMeasureEnabled(true);
        rvSettings.setLayoutManager(llm);
        rvConfigAdapter = new AdapterConfig();
        rvSettings.setAdapter(rvConfigAdapter);

        if(DebugUtil.isDebug())
            Log.i(TAG, "Created the Layout for Config Settings, Fragment Config, leaving now...");

        return main;
    }

    @Override
    public void onResume() {
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

                //MockPhoneConfig selected = (MockPhoneConfig) spConfigSelection.getSelectedItem();
                /*MockPhoneConfig selected = (MockPhoneConfig) spConfigSelection.getSelectedItem();
                if(selected != null) {
                    List<ConfigSetting> settings = new ArrayList<>(MockConfigConversions.hashMapToListSettings(selected.getSettings()));
                    Collections.sort(settings, new Comparator<ConfigSetting>() {
                        @Override
                        public int compare(ConfigSetting o1, ConfigSetting o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });

                    rvConfigAdapter.set(settings);
                    //this can be an issue
                    //we can remove the background loader
                    //to being we should not be cross threading modifying these elements
                    //Should happen only on this one single thread so no need for background updater
                    //Remember background updater is for the OTHER component (Pro cam)
                    //We still need to load in at least the configs :P
                    //Tho we can import export so make sure those invoke a update

                }*/


                Log.i(TAG, "onLoad Data mapping...");

                if(data.configs != null) {
                    if(spConfigs.getCount() < 1) {
                        spConfigs.clear();
                        spConfigs.addAll(data.configs);
                        return;
                    }

                    for(int i = 0; i < spConfigs.getCount(); i++) {
                        MockPhoneConfig confA = spConfigs.getItem(i);
                        for(MockPhoneConfig confB : data.configs) {
                            if(confB.equals(confA) && confB.getSettings().size() != confA.getSettings().size()) {
                                spConfigs.clear();
                                spConfigs.addAll(data.configs);
                                return;
                            }
                        }
                    }
                }
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
                data.configs.clear();

                Log.i(TAG, "Getting Phone Configs...");
                List<MockPhoneConfig> configs = new ArrayList<>(XMockQueryApi.getConfigs(getContext(), true));
                Log.i(TAG, "Config size=" + configs.size());
                data.configs.addAll(configs);

            }catch (Throwable ex) {
                data.exception = ex;
                Log.e(TAG, ex.getMessage());
            }

            Log.i(TAG, "DataLoader Configs size=" + data.configs.size());
            return data;
        }
    }

    private static class SettingDataHolder {
        String theme;
        List<MockPhoneConfig> configs = new ArrayList<>();
        Throwable exception = null;
    }
}
