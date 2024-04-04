package eu.faircode.xlua;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import eu.faircode.xlua.api.app.XLuaApp;
import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xlua.XLuaQuery;
import eu.faircode.xlua.ui.ViewFloatingAction;
import eu.faircode.xlua.utilities.CollectionUtil;

public class FragmentAppControl  extends ViewFloatingAction {
    private static final String TAG = "XLua.FragmentAppControl";

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterGroupHooks rvAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View main = inflater.inflate(R.layout.hookrecyclerview, container, false);
        this.TAG_ViewFloatingAction = TAG;
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
        rvAdapter = new AdapterGroupHooks();
        rvList.setAdapter(rvAdapter);

        loadData();
        return main;
    }


    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onPause() { super.onPause(); }

    private void loadData() {
        Log.i(TAG, "Starting data loader");
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks<DataHolder> dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<DataHolder>() {
        @Override
        public Loader<DataHolder> onCreateLoader(int id, Bundle args) { return new DataLoader(getContext()).setApp(application); }

        @Override
        public void onLoadFinished(Loader<DataHolder> loader, DataHolder data) {
            if (data.exception == null) {
                ActivityBase activity = (ActivityBase) getActivity();
                if (!data.theme.equals(activity.getThemeName()))
                    activity.recreate();


                Log.i(TAG, "Hook size=" + data.hooks.size());
                rvAdapter.set(data.app, data.hooks, getContext());
                //data.hooks.

                /*spAdapter.clear();
                spAdapter.addAll(data.groups);

                show = data.show;
                rvAdapter.setShow(data.show);
                rvAdapter.set(data.collection, data.hooks, data.apps);

                /*XUiConfig con1 = new XUiConfig();
                con1.name = "NONE";

                XUiConfig con2 = new XUiConfig();
                con2.name = "Cool";

                List<XUiConfig> confs = new ArrayList<>();
                confs.add(con1);
                confs.add(con2);

                rvAdapter.setConfigs(confs);*/

                swipeRefresh.setRefreshing(false);
                progressBar.setVisibility(View.GONE);

                /*swipeRefresh.setRefreshing(false);
                pbApplication.setVisibility(View.GONE);
                grpApplication.setVisibility(View.VISIBLE);

                //This will determine if its all or a actual name
                //If a actual grouup is selected then show the restrict button
                XUiGroup selected = (XUiGroup) spGroup.getSelectedItem();
                String group = (selected == null ? null : selected.name);
                tvRestrict.setVisibility(group == null ? View.VISIBLE : View.GONE);
                btnRestrict.setVisibility(group == null ? View.INVISIBLE : View.VISIBLE);*/
            } else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(Objects.requireNonNull(getView()), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<DataHolder> loader) {
            // Do nothing
        }
    };

    private static class DataLoader extends AsyncTaskLoader<DataHolder> {
        private AppGeneric application;
        public DataLoader setApp(AppGeneric application) {
            this.application = application;
            return this;
        }

        DataLoader(Context context) {
            super(context);
            setUpdateThrottle(1000);
        }

        @Nullable
        @Override
        public DataHolder loadInBackground() {
            Log.i(TAG, "Data loader started");

            DataHolder data = new DataHolder();
            try {
                Log.i(TAG, "Getting Theme");
                data.theme = XLuaCall.getTheme(getContext());
                Log.i(TAG, "Theme=" + data.theme);

                // Define hooks
                /*if (BuildConfig.DEBUG) {
                    String apk = getContext().getApplicationInfo().publicSourceDir;
                    List<XLuaHook> hooks = XLuaHook.readHooks(getContext(), apk);
                    //Have one but that just takes in HookPacket / HookDatabase Entry
                    Log.i(TAG, "Loaded hooks=" + hooks.size());
                    for (XLuaHook hook : hooks) {
                        HookDatabaseEntry entry = hook.toHookDatabase();
                        if(entry == null)
                            continue;

                        PutHookCommand.invoke(getContext(), (LuaHookPacket)entry);
                    }
                }*/

                //Get Show
                String show = XLuaCall.getSettingValue(getContext(), "show");
                if (show != null && show.equals("user")) data.show = AdapterApp.enumShow.user;
                else if (show != null && show.equals("all")) data.show = AdapterApp.enumShow.all;
                else data.show = AdapterApp.enumShow.icon;

                // Get collection
                data.collection.addAll(XLuaCall.getCollections(getContext()));
                Log.i(TAG, "Getting groups");
                // Load groups
                Resources res = getContext().getResources();
                List<String> groupsCopy = XLuaCall.getGroups(getContext());
                if(CollectionUtil.isValid(groupsCopy)) {
                    Log.i(TAG, " groups=" + groupsCopy.size());
                    for(String name : groupsCopy) {
                        String g = name.toLowerCase().replaceAll("[^a-z]", "_");
                        int id = res.getIdentifier("group_" + g, "string", getContext().getPackageName());
                        XUiGroup group = new XUiGroup();
                        group.name = name;
                        group.title = (id > 0 ? res.getString(id) : name);
                        data.groups.add(group);
                    }
                }

                final Collator collator = Collator.getInstance(Locale.getDefault());
                collator.setStrength(Collator.SECONDARY); // Case insensitive, process accents etc
                Collections.sort(data.groups, new Comparator<XUiGroup>() {
                    @Override
                    public int compare(XUiGroup group1, XUiGroup group2) {
                        return collator.compare(group1.title, group2.title);
                    }
                });

                XUiGroup all = new XUiGroup();
                all.name = null;
                all.title = getContext().getString(R.string.title_all);
                data.groups.add(0, all);

                Log.i(TAG, "Getting Hooks");
                // Load hooks
                Collection<XLuaHook> hooksCopy = XLuaQuery.getHooks(getContext(), true);
                Log.i(TAG, "Hooks loaded=" + hooksCopy.size());
                data.hooks.addAll(hooksCopy);
                data.app = XLuaCall.getApp(getContext(), application, true, true);
            } catch (Throwable ex) {
                data.collection = null;
                data.groups.clear();
                data.hooks.clear();
                data.app = null;
                data.exception = ex;
            }

            Log.i(TAG, "Data loader finished groups=" + data.groups.size() +
                    " hooks=" + data.hooks.size() + " apps=" + data.app);
            return data;
        }
    }
    
    private static class DataHolder {
        AdapterApp.enumShow show;
        String theme;
        XLuaApp app;
        List<String> collection = new ArrayList<>();
        List<XUiGroup> groups = new ArrayList<>();
        List<XLuaHook> hooks = new ArrayList<>();
        Throwable exception = null;
    }
}
