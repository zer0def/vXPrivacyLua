/*
    This file is part of XPrivacyLua.

    XPrivacyLua is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    XPrivacyLua is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with XPrivacyLua.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2017-2019 Marcel Bokhorst (M66B)
 */

package eu.faircode.xlua;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xlua.XLuaQuery;
import eu.faircode.xlua.api.hook.HookDatabaseEntry;
import eu.faircode.xlua.api.hook.LuaHookPacket;
import eu.faircode.xlua.api.xlua.call.PutHookCommand;
import eu.faircode.xlua.ui.dialogs.PrivacyGroupWarningDialog;
import eu.faircode.xlua.ui.interfaces.ILoader;
import eu.faircode.xlua.utilities.CollectionUtil;

import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.api.app.XLuaApp;
import eu.faircode.xlua.utilities.PrefUtil;
import eu.faircode.xlua.utilities.StringUtil;
import eu.faircode.xlua.utilities.UiUtil;


public class FragmentMain extends Fragment implements ILoader {
    private final static String TAG = "XLua.Fragment";

    private ProgressBar pbApplication;
    private Spinner spGroup;
    private ArrayAdapter<XUiGroup> spAdapter;
    private Button btnRestrict;
    private TextView tvRestrict;
    private Group grpApplication;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterApp rvAdapter;

    //Filter what apps to show not needed for us
    private AdapterApp.enumShow show = AdapterApp.enumShow.none;

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View main = inflater.inflate(R.layout.restrictions, container, false);

        List<String> collections = XLuaCall.getCollections(getContext());
        if(!collections.isEmpty()) {
            if(collections.contains("Privacy")) {
                boolean privacyWarning = PrefUtil.getBoolean(getContext(), "privacyWarn", false, true);
                if(!privacyWarning) {
                    PrefUtil.setBoolean(getContext(), "privacyWarn", true);
                    collections.remove("Privacy");
                    if(collections.isEmpty())
                        collections.add("PrivacyEx");

                    XLuaCall.putSetting(getContext(), "collection", StringUtil.join(collections));
                    new PrivacyGroupWarningDialog()
                            .show(Objects.requireNonNull(getFragmentManager()), getString(R.string.title_collection_privacy));
                }
            }
        }

        pbApplication = main.findViewById(R.id.pbApplication);
        btnRestrict = main.findViewById(R.id.btnRestrict);
        tvRestrict = main.findViewById(R.id.tvRestrict);
        grpApplication = main.findViewById(R.id.grpApplication);

        int colorAccent = XUtil.resolveColor(getContext(), R.attr.colorAccent);

        swipeRefresh = main.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(colorAccent, colorAccent, colorAccent);

        //RIP
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        // Initialize app list
        RecyclerView rvApplication = main.findViewById(R.id.rvApplication);
        rvApplication.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean onRequestChildFocus(RecyclerView parent, RecyclerView.State state, View child, View focused) {
                return true;
            }
        };
        llm.setAutoMeasureEnabled(true);
        rvApplication.setLayoutManager(llm);
        rvAdapter = new AdapterApp(getActivity(), this);
        rvApplication.setAdapter(rvAdapter);

        //This point i dont use cuz its the menu drop down items
        spAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spGroup = main.findViewById(R.id.spGroup);
        spGroup.setTag(null);
        spGroup.setAdapter(spAdapter);
        spGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updateSelection(); }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                updateSelection();
            }

            private void updateSelection() {
                XUiGroup selected = (XUiGroup) spGroup.getSelectedItem();
                String group = (selected == null ? null : selected.name);

                if (group == null ? spGroup.getTag() != null : !group.equals(spGroup.getTag())) {
                    Log.i(TAG, "Select group=" + group);
                    spGroup.setTag(group);
                    rvAdapter.setGroup(group);
                }

                tvRestrict.setVisibility(group == null ? View.VISIBLE : View.GONE);
                btnRestrict.setVisibility(group == null ? View.INVISIBLE : View.VISIBLE);
            }
        });

        btnRestrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XUiGroup selected = (XUiGroup) spGroup.getSelectedItem();
                XUtil.areYouSure(
                        (ActivityBase) getActivity(),
                        getString(R.string.msg_restrict_sure, selected.title),
                        new XUtil.DoubtListener() {
                            @Override
                            public void onSure() {
                                rvAdapter.restrict(getContext());
                            }
                        });
            }
        });

        return main;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter ifPackage = new IntentFilter();
        ifPackage.addAction(Intent.ACTION_PACKAGE_ADDED);
        ifPackage.addAction(Intent.ACTION_PACKAGE_CHANGED);
        ifPackage.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
        ifPackage.addDataScheme("package");
        Objects.requireNonNull(getContext()).registerReceiver(packageChangedReceiver, ifPackage);
        loadData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getContext()).unregisterReceiver(packageChangedReceiver);
    }

    public AdapterApp.enumShow getShow() {
        return this.show;
    }

    public void setShow(AdapterApp.enumShow value) {
        this.show = value;
        if (rvAdapter != null)  rvAdapter.setShow(value);
    }

    public void filter(String query) {
        if (rvAdapter != null) rvAdapter.getFilter().filter(query);
    }

    @Override
    public FragmentManager getManager() { return getFragmentManager(); }

    @Override
    public Fragment getFragment() { return this; }

    @Override
    public AppGeneric getApplication() { return null; }

    @Override
    public void loadData() {
        Log.i(TAG, "Starting data loader");
        LoaderManager manager = getActivity().getSupportLoaderManager();
        manager.restartLoader(ActivityMain.LOADER_DATA, new Bundle(), dataLoaderCallbacks).forceLoad();
    }

    LoaderManager.LoaderCallbacks<DataHolder> dataLoaderCallbacks = new LoaderManager.LoaderCallbacks<DataHolder>() {
        @NonNull
        @Override
        public Loader<DataHolder> onCreateLoader(int id, Bundle args) {
            return new DataLoader(getContext());
        }

        @Override
        public void onLoadFinished(@NonNull Loader<DataHolder> loader, DataHolder data) {
            if (data.exception == null) {
                //ActivityBase activity = (ActivityBase) getActivity();
                //if (!data.theme.equals(activity.getThemeName()))
                //    activity.recreate();

                UiUtil.initTheme(getActivity(), data.theme);
                spAdapter.clear();
                spAdapter.addAll(data.groups);

                show = data.show;
                rvAdapter.setShow(data.show);
                rvAdapter.set(data.collection, data.hooks, data.apps);

                swipeRefresh.setRefreshing(false);
                pbApplication.setVisibility(View.GONE);
                grpApplication.setVisibility(View.VISIBLE);

                //This will determine if its all or a actual name
                //If a actual grouup is selected then show the restrict button
                XUiGroup selected = (XUiGroup) spGroup.getSelectedItem();
                String group = (selected == null ? null : selected.name);
                tvRestrict.setVisibility(group == null ? View.VISIBLE : View.GONE);
                btnRestrict.setVisibility(group == null ? View.INVISIBLE : View.VISIBLE);
            } else {
                Log.e(TAG, Log.getStackTraceString(data.exception));
                Snackbar.make(Objects.requireNonNull(getView()), data.exception.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<DataHolder> loader) { }
    };

    private static class DataLoader extends AsyncTaskLoader<DataHolder> {
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
                if (BuildConfig.DEBUG) {
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
                }

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
                }else {
                    Log.i(TAG, "Groups list is null...");
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

                // Load hooks
                Collection<XLuaHook> hooksCopy = XLuaQuery.getHooks(getContext(), true);
                Log.i(TAG, "Hooks loaded=" + hooksCopy.size());
                data.hooks.addAll(hooksCopy);
                // Load apps
                Collection<XLuaApp> appsCopy = XLuaQuery.getApps(getContext(), true);
                Log.i(TAG, "Apps loaded=" + appsCopy.size());
                data.apps.addAll(appsCopy);
            } catch (Throwable ex) {
                data.collection = null;
                data.groups.clear();
                data.hooks.clear();
                data.apps.clear();
                data.exception = ex;
            }

            Log.i(TAG, "Data loader finished groups=" + data.groups.size() +
                    " hooks=" + data.hooks.size() + " apps=" + data.apps.size());
            return data;
        }
    }

    private BroadcastReceiver packageChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Received " + intent);
            String packageName = intent.getData().getSchemeSpecificPart();
            int uid = intent.getIntExtra(Intent.EXTRA_UID, -1);
            Log.i(TAG, "pkg=" + packageName + ":" + uid);
            loadData();
        }
    };

    private static class DataHolder {
        AdapterApp.enumShow show;
        String theme;
        List<String> collection = new ArrayList<>();
        List<XUiGroup> groups = new ArrayList<>();
        List<XLuaHook> hooks = new ArrayList<>();
        List<XLuaApp> apps = new ArrayList<>();
        Throwable exception = null;
    }
}
