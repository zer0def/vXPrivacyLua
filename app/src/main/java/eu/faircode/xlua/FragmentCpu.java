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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.api.cpu.MockCpu;


public class FragmentCpu extends Fragment {
    private final static String TAG = "XLua.FragmentCpu";

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterCpu rvCpuAdapter;

    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "FragmentCpu.onCreateView Enter");
        final View main = inflater.inflate(R.layout.cpurecyclerview, container, false);
        initRefresh(main);
        initRecyclerView(main);
        return main;
    }

    private void initRefresh(final View view) {
        progressBar = view.findViewById(R.id.pbCpu);
        int colorAccent = XUtil.resolveColor(Objects.requireNonNull(getContext()), R.attr.colorAccent);
        swipeRefresh = view.findViewById(R.id.swipeRefreshCpu);
        swipeRefresh.setColorSchemeColors(colorAccent, colorAccent, colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { loadData(); }
        });
    }

    private void initRecyclerView(final View view) {
        RecyclerView rvCpus = view.findViewById(R.id.rvCpu);
        rvCpus.setVisibility(View.VISIBLE);
        rvCpus.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
                return true;
            }
        };

        llm.setAutoMeasureEnabled(true);
        rvCpus.setLayoutManager(llm);
        rvCpuAdapter = new AdapterCpu();
        rvCpus.setAdapter(rvCpuAdapter);
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
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<CpuDataHolder>() {
        @Override
        public Loader<CpuDataHolder> onCreateLoader(int id, Bundle args) { return new CpuDataLoader(getContext()); }

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
                progressBar.setVisibility(View.GONE);
            }else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(getView(), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<CpuDataHolder> loader) { }
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
                data.theme = XLuaCall.getTheme(getContext());
                Log.i(TAG, "Getting Cpu Maps...");
                data.maps.addAll(XMockCall.getCpuMaps(getContext()));
                Log.i(TAG, "cpu maps size=" + data.maps.size());
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
