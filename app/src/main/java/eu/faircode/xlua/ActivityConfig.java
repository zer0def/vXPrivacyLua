package eu.faircode.xlua;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActivityConfig extends ActivityBase {
    private static final String TAG = "XLua.ActivityConfig";
    private FragmentConfig fragmentConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.configeditorview);

        Log.i(TAG, "Creating Fragment");

        //Show Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentConfig = new FragmentConfig();

        // Create a bundle to pass data
        String packageName = getIntent().getStringExtra("packageName");
        Bundle args = new Bundle();
        args.putString("packageName", packageName);  // Add the packageName to the bundle
        fragmentConfig.setArguments(args);  // Set the arguments to the fragment

        Log.i(TAG, "Creating Fragment Config Replacement");

        fragmentTransaction.replace(R.id.content_frame_configs, fragmentConfig);
        fragmentTransaction.commit();

        Log.i(TAG, "Fragment Committed");
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
