package eu.faircode.xlua.ui.interfaces;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import eu.faircode.xlua.AppGeneric;

public interface ILoader {
    void loadData();
    void filter(String query);
    FragmentManager getManager();
    Fragment getFragment();
    AppGeneric getApplication();
}
