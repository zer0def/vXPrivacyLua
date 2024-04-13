package eu.faircode.xlua;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.ui.HookGroup;
import eu.faircode.xlua.ui.interfaces.ILoader;
import eu.faircode.xlua.ui.ViewFloatingAction;
import eu.faircode.xlua.utilities.UiUtil;

public class FragmentAppControl extends ViewFloatingAction implements ILoader {
    private AdapterGroupHooks rvAdapter;
    private TextView tvGroupsCount;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View main = inflater.inflate(R.layout.hookrecyclerview, container, false);
        this.TAG_ViewFloatingAction = "XLua.FragmentAppControl";
        this.application = AppGeneric.from(getArguments(), getContext());
        super.bindTextViewsToAppId(main, R.id.ivAppControlAppIcon, R.id.tvAppControlPackageName, R.id.tvAppControlPackageFull, R.id.tvAppControlPackageUid);

        //init Refresh
        progressBar = main.findViewById(R.id.pbAppControl);
        int colorAccent = XUtil.resolveColor(requireContext(), R.attr.colorAccent);
        swipeRefresh = main.findViewById(R.id.swipeRefreshAppControl);
        swipeRefresh.setColorSchemeColors(colorAccent, colorAccent, colorAccent);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() { loadData(); }
        });
        tvGroupsCount = main.findViewById(R.id.tvGroupCountAppControl);

        //init RecyclerView
        super.initRecyclerView(main, R.id.rvAppControl, true);
        rvList.setVisibility(View.VISIBLE);
        rvList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) { return true; }
        };

        llm.setAutoMeasureEnabled(true);
        rvList.setLayoutManager(llm);
        rvAdapter = new AdapterGroupHooks(this);
        rvList.setAdapter(rvAdapter);
        loadData();
        return main;
    }


    public void filter(String query) { if (rvAdapter != null) rvAdapter.getFilter().filter(query); }

    @Override
    public FragmentManager getManager() {  return getFragmentManager(); }

    @Override
    public Fragment getFragment() { return this; }

    @Override
    public AppGeneric getApplication() { return this.application; }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onPause() { super.onPause(); }

    @Override
    public void loadData() {
        XLog.i("Data loader started");
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks<DataHolder> dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<DataHolder>() {
        @NonNull @Override
        public Loader<DataHolder> onCreateLoader(int id, Bundle args) { return new DataLoader(getContext()).setApp(application); }

        @Override
        public void onLoadFinished(@NonNull Loader<DataHolder> loader, DataHolder data) {
            try {
                if (data.exception == null) {
                    UiUtil.initTheme(getActivity(), data.theme);
                    rvAdapter.set(data.groups);
                    tvGroupsCount.setText(new StringBuilder().append(" - ").append(data.groups.size()));
                    swipeRefresh.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                } else {
                    XLog.e("Data loader Exception", data.exception, true);
                    Snackbar.make(Objects.requireNonNull(getView()), data.exception.toString(), Snackbar.LENGTH_LONG).show();
                }
            }catch (Exception e) { XLog.e("Failed to load Groups for App Control. ", e); }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<DataHolder> loader) { }
    };

    private static class DataLoader extends AsyncTaskLoader<DataHolder> {
        private AppGeneric application;
        public DataLoader setApp(AppGeneric application) { this.application = application; return this; }
        DataLoader(Context context) { super(context); setUpdateThrottle(1000); }

        @Nullable
        @Override
        public DataHolder loadInBackground() {
            XLog.i("Data loader started");
            DataHolder data = new DataHolder();
            try {
                data.theme = XLuaCall.getTheme(getContext());
                data.show = UiUtil.getShow(getContext());
                XLog.i("show=" + data.show.name());
                data.groups = HookGroup.getGroups(getContext(), application);
                XLog.i("Groups returned =" + data.groups.size());
            } catch (Throwable ex) {
                data.exception = ex;
            }

            XLog.i("Data loader Finished. groups=" + data.groups.size());
            return data;
        }
    }
    
    private static class DataHolder {
        AdapterApp.enumShow show;
        String theme;
        List<HookGroup> groups;
        Throwable exception = null;
    }
}
