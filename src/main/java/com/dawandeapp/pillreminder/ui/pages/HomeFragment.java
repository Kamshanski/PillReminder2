package com.dawandeapp.pillreminder.ui.pages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.data.model.Box;
import com.dawandeapp.pillreminder.ui.abstracts.BaseFragment;
import com.dawandeapp.pillreminder.ui.myviews.BoxRow;
import com.dawandeapp.pillreminder.ui.viewmodels.EditBoxViewModel;
import com.dawandeapp.pillreminder.ui.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends BaseFragment {
    public static final String CLASS = "HomeFragment";
    BaseFragment.OnFragmentInteractionListener fragmentListener;
    List<BoxRow> boxViews = new ArrayList<>();
    HomeViewModel mViewModel;
    HashMap<String, EditBoxViewModel> editBoxViewModelMap = new HashMap<>(4);


    @Override
    protected void findViews(View fragmentV) {
        boxViews.add(fragmentV.findViewById(R.id.boxRow1));
        boxViews.add(fragmentV.findViewById(R.id.boxRow2));
        boxViews.add(fragmentV.findViewById(R.id.boxRow3));
        boxViews.add(fragmentV.findViewById(R.id.boxRow4));
    }

    @Override
    protected void initUI(View fv) {
        LiveData<ArrayList<Box>> liveBoxes = mViewModel.getBoxes();

        for (Box box : liveBoxes.getValue()) {
            final BoxRow br = boxViews.get(box.getId());
            br.setName(box.getPill().getName());
            br.setImportance(box.getImportance());
            br.setSchedule(box.getSchedule().toString());
            br.setIsEmpty(box.isEmpty());
            br.setListener(new BoxRow.BoxRowListener() {
                @Override
                public void onAddButtonClick(int id) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("boxId", id);;
                    fragmentListener.moveTo(EditBoxFragment.CLASS, bundle);

//                    Pill newPill = new Pill("Pill " + id, "Description_" + id);
//                    RealmList<TimePoint> points = new RealmList<>();
//                    points.add(new TimePoint(id, 5));
//                    points.add(new TimePoint(id+1, 55));
//                    points.add(new TimePoint(id+2, 25));
//                    Schedule newSchedule = new Schedule(0b11111111, points);
//                    Box newBox = new Box(id, false, 0, 0, Box.DIETARY_SUPPLIMENTS, newPill, newSchedule);
//                    mViewModel.putBox(newBox);
                }

                @Override
                public void onEditButtonClick(int id) {
                    mViewModel.deleteBox(id);
                }
            });
        }
    }

    @Override
    protected void setNotifiers(View fv) {
        mViewModel.getBoxes().observe(this, boxes -> {
            for (Box box : boxes) {
                final BoxRow br = boxViews.get(box.getId());
                br.setIsEmpty(box.isEmpty());
                br.setName(box.getPill().getName());
                br.setImportance(box.getImportance());
                br.setSchedule(box.getSchedule().toString());
            }
        });
    }

    @Override
    protected void setViewModel() {
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void clearViewModelListeners() {
        mViewModel.getBoxes().removeObservers(this);
        mViewModel = null;
    }

    @Override
    protected void applyFragmentInteraction(Context activity) {
        if (activity instanceof OnFragmentInteractionListener) {
            fragmentListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString() + " должен имплементить OnFragmentInteractionListener");
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }


}
