package eu.faircode.xlua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.ui.dialogs.IPropertyDialogListener;

public class ActivityProperties extends ActivityBase implements IPropertyDialogListener {
    private static final String TAG = "XLua.ActivityProperties";
    private FragmentProperties fragmentProps;
    private Menu menu = null;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propcontent);

        //Show Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentProps = new FragmentProperties();

        // Create a bundle to pass data
        String packageName = getIntent().getStringExtra("packageName");
        Bundle args = new Bundle();
        args.putString("packageName", packageName);  // Add the packageName to the bundle
        fragmentProps.setArguments(args);  // Set the arguments to the fragment

        fragmentTransaction.replace(R.id.content_frame_properties, fragmentProps);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "Create options");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.propmenu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "Prepare options");

        // Search
        MenuItem menuSearch = menu.findItem(R.id.menu_search_props);
        final SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "Search submit=" + query);
                if (fragmentProps != null) {
                    fragmentProps.filter(query);
                    searchView.clearFocus(); // close keyboard
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "Search change=" + newText);
                if (fragmentProps != null)
                    fragmentProps.filter(newText);
                return true;
            }
        });

        menuSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                Log.i(TAG, "Search expand");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Log.i(TAG, "Search collapse");

                //I think this is where we clear filter or re add items back to the RV

                // Search uid once
                //Intent intent = getIntent();
                //intent.removeExtra("package");
                //setIntent(intent);

                return true;
            }
        });

        //updateMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
        //return false;
    }

    @Override
    public void pushMockPropPacket(final MockPropPacket setting) {
        final Context context = getApplicationContext();
        Log.i(TAG, "Packet being sent to bridge =" + setting);

        executor.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    final XResult ret = XMockCall.putMockProp(context, setting);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {
                            Toast.makeText(context, ret.getResultMessage(), Toast.LENGTH_SHORT).show();
                            fragmentProps.loadData();
                        }
                    });
                }
            }
        });
    }
}
