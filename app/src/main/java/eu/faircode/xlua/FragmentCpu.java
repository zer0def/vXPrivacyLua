package eu.faircode.xlua;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.faircode.xlua.api.XLuaCallApi;
import eu.faircode.xlua.api.XMockCallApi;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;


public class FragmentCpu extends Fragment {
    private final static String TAG = "XLua.FragmentCpu";

    private ProgressBar pbCpu;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterCpu rvCpuAdapter;

    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //spConfigEdit



        //Log.i(TAG, "Init of DB for Cpu Maps");
        //List<XMockCpuIO> maps = XMockProxyApi.queryGetMockCpuMaps(getContext());
        //Log.i(TAG, "Init of CPU Maps has Finished: " + maps.size());

        Log.i(TAG, "FragmentCpu.onCreateView Enter");
        final View main = inflater.inflate(R.layout.cpurecyclerview, container, false);

        pbCpu =  main.findViewById(R.id.pbCpu);

        int colorAccent = XUtil.resolveColor(getContext(), R.attr.colorAccent);
        swipeRefresh = main.findViewById(R.id.swipeRefreshCpu);
        swipeRefresh.setColorSchemeColors(colorAccent, colorAccent, colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        RecyclerView rvCpu = main.findViewById(R.id.rvCpu);
        rvCpu.setVisibility(View.VISIBLE);
        rvCpu.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
                return true;
            }
        };

        llm.setAutoMeasureEnabled(true);
        rvCpu.setLayoutManager(llm);
        rvCpuAdapter = new AdapterCpu();
        rvCpu.setAdapter(rvCpuAdapter);

        Log.i(TAG, "FragmentCpu.onCreateView Leave");
        return main;
    }

    public void saveModifiedProperties(Context context) {
        Log.i(TAG, "Save / Updating properties");
        //rvCpuAdapter.updateFromModified(context);
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
        Log.i(TAG, "Starting data loader");
        LoaderManager manager = getActivity().getSupportLoaderManager();
        //ActivityMain loader data ?
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<CpuDataHolder>() {
        @Override
        public Loader<CpuDataHolder> onCreateLoader(int id, Bundle args) {
            return new CpuDataLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<CpuDataHolder> loader, CpuDataHolder data) {
            Log.i(TAG, "onLoadFinished");
            if(data.exception == null) {
                ActivityBase activity = (ActivityBase) getActivity();
                if (!data.theme.equals(activity.getThemeName()))
                    activity.recreate();

                Collections.sort(data.maps, new Comparator<MockCpu>() {
                    @Override
                    public int compare(MockCpu o1, MockCpu o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });

                rvCpuAdapter.set(data.maps);
                swipeRefresh.setRefreshing(false);
                pbCpu.setVisibility(View.GONE);
            }else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(getView(), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<CpuDataHolder> loader) {
            // Do nothing
        }
    };

    private static class CpuDataLoader extends AsyncTaskLoader<CpuDataHolder> {
        CpuDataLoader(Context context) {
            super(context);
            setUpdateThrottle(1000);
        }

        @Nullable
        @Override
        public CpuDataHolder loadInBackground() {
            Log.i(TAG, "Data loader started");
            CpuDataHolder data = new CpuDataHolder();
            try {

                data.theme = XLuaCallApi.getTheme(getContext());
                //data.theme = XSettingsDatabase.getSettingValue()
                //data.theme = XProvider.getSetting(getContext(), "global", "theme");
                //if (data.theme == null)
                //    data.theme = "light";

                data.maps.clear();
                Log.i(TAG, "Getting Cpu Maps...");
                //List<MockCpu> props = XMockProxyApi.queryGetMockCpuMaps(getContext());
                Collection<MockCpu> maps = XMockCallApi.getCpuMaps(getContext());
                Log.i(TAG, "Props=" + maps.size());
                data.maps.addAll(maps);
                //make sure it syncs with cache if needed
            }catch (Throwable ex) {
                data.maps.clear();
                data.exception = ex;
                Log.e(TAG, ex.getMessage());
            }

            Log.i(TAG, "DataLoader Props Finished=" + data.maps.size());
            return data;
        }
    }

    private static class CpuDataHolder {
        String theme;
        List<MockCpu> maps = new ArrayList<>();
        Throwable exception = null;
    }
}
