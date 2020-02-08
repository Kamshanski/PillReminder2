package com.dawandeapp.pillreminder.ui.abstracts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //TODO: заменить attachToRoot на true и поменть FrameL на ConstraintL во всех fragmentLayout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutID(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initInstance();
        findViews(getView());
        initUI(getView());
        setNotifiers(getView());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setViewModel();
        applyFragmentInteraction(context);
    }

    @Override
    public void onDetach() {
        clearViewModelListeners();
        super.onDetach();
    }

    /** Nice place to set ViewModel. Once per instance */
    protected void setViewModel() { }
    /** To get ViewModel... */
    protected void initInstance() {}
    /** To do all findViewById()... */
    protected void findViews(View fv) {}
    /** To get set all onClickListener, appearance... */
    protected void initUI(View fv) {}
    /** To get set all callbacks... */
    protected void setNotifiers(View fv) {}
    /** to get Layout ID of this Fragment... */
    protected abstract int getLayoutID();
    /** */
    protected void clearViewModelListeners() {}
    /** Connection to Activity via interface */
    protected void applyFragmentInteraction(Context activity) {
//        if (activity instanceof OnFragmentInteractionListener) {
//            fragmentListener = ((OnFragmentInteractionListener) activity);
//        } else {
//            throw new RuntimeException(activity.toString() + " должен имплементить OnFragmentInteractionListener");
//        }
    }

    public interface OnFragmentInteractionListener {
        void moveTo(String simpleClassName, Bundle bundle);
    }
}
