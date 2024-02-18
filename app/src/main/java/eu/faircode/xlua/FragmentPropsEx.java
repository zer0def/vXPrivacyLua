package eu.faircode.xlua;

/*public class FragmentPropsEx extends Fragment {
    private final static String TAG = "XLua.FragmentProps";

    private ProgressBar pbProps;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterProp rvPropsAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(DebugUtil.isDebug())
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

        if(DebugUtil.isDebug())
            Log.i(TAG, "FragmentPropsEx.onCreateView Leave");

        return main;
    }

    public void saveModifiedProperties(Context context) {
        if(DebugUtil.isDebug())
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

                Collections.sort(data.props, new Comparator<MockProp>() {
                    @Override
                    public int compare(MockProp o1, MockProp o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
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
                data.theme = XLuaCallApi.getTheme(getContext());
                //data.theme = XProvider.getSetting(getContext(), "global", "theme");
                //if (data.theme == null)
                //    data.theme = "light";

                data.props.clear();
                Log.i(TAG, "Getting Props...");
                //List<XMockPropIO> props = XMockProxyApi.queryGetMockPropsEx(getContext());
                //List<XMockPropIO> props = XMockProxyApi.queryGetMockProps(getContext());
                Collection<MockProp> props = XMockCallApi.getMockProps(getContext());

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
        List<MockProp> props = new ArrayList<>();
        Throwable exception = null;
    }
}*/
