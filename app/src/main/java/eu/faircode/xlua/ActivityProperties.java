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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.ui.SettingsDialogCallbackActivity;
import eu.faircode.xlua.ui.dialogs.IPropertyDialogListener;

public class ActivityProperties extends SettingsDialogCallbackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propcontent);
        initFragmentTransaction(FragmentProperties.class, R.id.content_frame_properties, true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) { super.onPostCreate(savedInstanceState); }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) { super.onConfigurationChanged(newConfig); }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.propmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        prepareMenuSearch(menu.findItem(R.id.menu_search_props));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { return super.onOptionsItemSelected(item); }
}
