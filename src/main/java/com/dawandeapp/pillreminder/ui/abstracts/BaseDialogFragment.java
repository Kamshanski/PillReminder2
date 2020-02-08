package com.dawandeapp.pillreminder.ui.abstracts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public abstract class BaseDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View dialogView = buildDialogView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Редактировать кнопку")
                .setView(dialogView)
                .setPositiveButton("Add", getBtnListener())
                .setNegativeButton("Cancel", getBtnListener());

        AlertDialog dialog = builder.create();

        findViews(dialogView);
        initUI(dialogView);
        setNotifiers(dialogView);
        dialog.create();

        setDialogButtonsListener(dialog.getButton(AlertDialog.BUTTON_POSITIVE),
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL),
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE));

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        obtainViewModel();
        applyFragmentInteraction(context);
    }


    protected void setDialogButtonsListener(Button posBtn, Button neuBtn, Button negBtn) {

    }
    /** */
    protected View buildDialogView() {return LayoutInflater.from(getContext()).inflate(getLayoutID(), null, false);}
    /** Nice place to set ViewModel. Once per instance */
    protected void obtainViewModel() { }
    /** To do all findViewById()... */
    protected void findViews(View dialogView) {}
    /** To get set all onClickListener, appearance... */
    protected void initUI(View dialogView) {}
    /** To get set all callbacks... */
    protected void setNotifiers(View dialogView) {}
    /** to get Layout ID of this Fragment... */
    protected abstract int getLayoutID();
    /** */
    protected DialogInterface.OnClickListener getBtnListener() {return null;}
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


}
