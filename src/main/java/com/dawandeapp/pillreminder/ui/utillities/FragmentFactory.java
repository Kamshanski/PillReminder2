package com.dawandeapp.pillreminder.ui.utillities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.dawandeapp.pillreminder.ui.pages.EditBoxFragment;
import com.dawandeapp.pillreminder.ui.pages.HomeFragment;

public class FragmentFactory {
    Bundle bundle;

    public FragmentFactory() {
    }

    public FragmentFactory setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public Fragment get(String fragmentClass) {
        switch (fragmentClass) {
            case HomeFragment.CLASS:
                return new HomeFragment();
            case EditBoxFragment.CLASS:
                return EditBoxFragment.getInstance(bundle);
            default:
                return new HomeFragment();
        }
    }
}
