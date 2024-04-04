package eu.faircode.xlua;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActivityAppControl extends ActivityBase {
    private static final String TAG = "XLua.ActivityAppControl";
    private FragmentAppControl fragmentAppControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hookcontent);

        //Show Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentAppControl = new FragmentAppControl();

        // Create a bundle to pass data
        String packageName = getIntent().getStringExtra("packageName");
        Bundle args = new Bundle();
        args.putString("packageName", packageName);  // Add the packageName to the bundle
        fragmentAppControl.setArguments(args);  // Set the arguments to the fragment

        fragmentTransaction.replace(R.id.content_frame_app_control, fragmentAppControl);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "Create options");
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.settingsmenu, menu);
        //this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "Prepare options");

        // Search
        /*MenuItem menuSearch = menu.findItem(R.id.menu_search_settings);
        final SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "Search submit=" + query);
                if (fragmentSettings != null) {
                    fragmentSettings.filter(query);
                    searchView.clearFocus(); // close keyboard
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "Search change=" + newText);
                if (fragmentSettings != null)
                    fragmentSettings.filter(newText);
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
                return true;
            }
        });*/

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
