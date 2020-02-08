package com.dawandeapp.pillreminder.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.ui.pages.EditBoxFragment;
import com.dawandeapp.pillreminder.ui.utillities.FragmentFactory;
import com.dawandeapp.pillreminder.ui.abstracts.BaseFragment;
import com.dawandeapp.pillreminder.ui.pages.HomeFragment;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {

    FragmentManager fm;
    int containerId;
    FragmentFactory fragmentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        containerId = R.id.frame_container;
        fm = getSupportFragmentManager();
        fragmentFactory = new FragmentFactory();


        fm.beginTransaction()
                .replace(containerId, fragmentFactory.get(HomeFragment.CLASS))
                .commit();
    }


    @Override
    public void moveTo(String simpleClassName, Bundle bundle) {
        Fragment fragment = fragmentFactory.setBundle(bundle).get(simpleClassName);
        if (simpleClassName.equals(EditBoxFragment.CLASS)) {
            ((DialogFragment) fragment).show(fm, "BOX_" + bundle.getInt("boxId", 0));
        } else {
            fm.beginTransaction()
                    .replace(containerId, fragmentFactory.setBundle(bundle).get(simpleClassName))
                    .commit();
        }
    }
}
