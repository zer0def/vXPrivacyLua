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

import eu.faircode.xlua.cpu.XMockCpuIO;

public class FragmentPropsEx extends Fragment {
    private final static String TAG = "XLua.FragmentProps";

    private ProgressBar pbProps;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterProp rvPropsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "FragmentPropsEx.onCreateView Enter");
        final View main = inflater.inflate(R.layout.propsview, container, false);

        pbProps =  main.findViewById(R.id.pbProps);

        int colorAccent = XUtil.resolveColor(getContext(), R.attr.colorAccent);
        swipeRefresh = main.findViewById(R.id.swipeRefreshProps);
        swipeRefresh.setColorSchemeColors(colorAccent, colorAccent, colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        RecyclerView rvProps = main.findViewById(R.id.rvProps);
        rvProps.setVisibility(View.VISIBLE);
        rvProps.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
                return true;
            }
        };

        llm.setAutoMeasureEnabled(true);
        rvProps.setLayoutManager(llm);
        rvPropsAdapter = new AdapterProp();
        rvProps.setAdapter(rvPropsAdapter);

        Log.i(TAG, "FragmentPropsEx.onCreateView Leave");
        return main;
    }

    public void saveModifiedProperties(Context context) {
        Log.i(TAG, "Save / Updating properties");
        rvPropsAdapter.updateFromModified(context);
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

    LoaderManager.LoaderCallbacks dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<PropsDataHolder>() {
        @Override
        public Loader<PropsDataHolder> onCreateLoader(int id, Bundle args) {
            return new PropsDataLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<PropsDataHolder> loader, PropsDataHolder data) {
            Log.i(TAG, "onLoadFinished");
            if(data.exception == null) {
                ActivityBase activity = (ActivityBase) getActivity();
                if (!data.theme.equals(activity.getThemeName()))
                    activity.recreate();

                Collections.sort(data.props, new Comparator<XMockPropIO>() {
                    @Override
                    public int compare(XMockPropIO o1, XMockPropIO o2) {
                        return o1.name.compareToIgnoreCase(o2.name);
                    }
                });

                rvPropsAdapter.set(data.props);
                swipeRefresh.setRefreshing(false);
                pbProps.setVisibility(View.GONE);
            }else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(getView(), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<PropsDataHolder> loader) {
            // Do nothing
        }
    };

    private static class PropsDataLoader extends AsyncTaskLoader<PropsDataHolder> {
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
                data.theme = XProvider.getSetting(getContext(), "global", "theme");
                if (data.theme == null)
                    data.theme = "light";

                data.props.clear();
                Log.i(TAG, "Getting Props...");
                //List<XMockPropIO> props = XMockProxyApi.queryGetMockPropsEx(getContext());
                List<XMockPropIO> props = XMockProxyApi.queryGetMockProps(getContext());
                Log.i(TAG, "Props=" + props.size());
                data.props.addAll(props);
                //make sure it syncs with cache if needed
            }catch (Throwable ex) {
                data.props.clear();
                data.exception = ex;
                Log.e(TAG, ex.getMessage());
            }

            Log.i(TAG, "DataLoader Props Finished=" + data.props.size());
            return data;
        }
    }

    private static class PropsDataHolder {
        String theme;
        List<XMockPropIO> props = new ArrayList<>();
        Throwable exception = null;
    }
}
