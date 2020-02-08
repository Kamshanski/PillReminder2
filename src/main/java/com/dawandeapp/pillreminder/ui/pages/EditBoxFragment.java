package com.dawandeapp.pillreminder.ui.pages;

import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.data.model.TimePoint;
import com.dawandeapp.pillreminder.databinding.DialogBoxEditBinding;
import com.dawandeapp.pillreminder.databinding.PanelDayBinding;
import com.dawandeapp.pillreminder.databinding.PanelDowBinding;
import com.dawandeapp.pillreminder.databinding.TimepointRowBinding;
import com.dawandeapp.pillreminder.ui.abstracts.BaseDialogFragment;
import com.dawandeapp.pillreminder.ui.abstracts.BaseFragment;
import com.dawandeapp.pillreminder.ui.myviews.PanelDay;
import com.dawandeapp.pillreminder.ui.myviews.PanelDow;
import com.dawandeapp.pillreminder.ui.myviews.TimePointRow;
import com.dawandeapp.pillreminder.ui.viewmodels.EditBoxViewModel;
import com.dawandeapp.pillreminder.utilities.M;

import java.util.HashSet;

public class EditBoxFragment extends BaseDialogFragment{
    public static final String CLASS = "EditBoxFragment";
    EditBoxViewModel viewModel;
    private BaseFragment.OnFragmentInteractionListener fragmentListener;
    int boxId = 0;

    TextView btnAddTimePoint;
    EditText edtPillName, edtPillDescription;
    ToggleButton tgbDay, tgbDow;
    FrameLayout flDaysPanel;
    LinearLayout llTimePoints;
    PanelDay panelDay;
    PanelDow panelDow;
    HashSet<TimePointRow> timePointRows;

    public static EditBoxFragment getInstance(Bundle bundle) {
        EditBoxFragment fragment = new EditBoxFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public EditBoxFragment() {
        timePointRows = new HashSet<>();
        if (getArguments() != null) {
            this.boxId = getArguments().getInt("boxId", -1);
        }
    }

    private void switchPanel() {
        flDaysPanel.removeAllViews();
        M.d("It's viewModel.getIsDay().getValue().booleanValue(): " + viewModel.getIsDay().getValue());
        flDaysPanel.addView(viewModel.getIsDay().getValue().booleanValue() ? panelDay.getView() : panelDow.getView());
    }

    private void addTimePointRow(TimePoint tp) {
        if (tp != null) {
            TimepointRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.timepoint_row, llTimePoints, false);
            binding.setLifecycleOwner(this);
            binding.setTimePoint(tp);

            TimePointRow tpr = new TimePointRow(binding.getRoot());
            tpr.setBinding(binding);
            tpr.setOnDeleteBtnListener(v1 -> {
                viewModel.removeTimePoint(tpr.getBinding().getTimePoint());
                tpr.getBinding().unbind();
                llTimePoints.removeView(tpr.getView());
                timePointRows.remove(tpr);
            });
            timePointRows.add(tpr);
            llTimePoints.addView(tpr.getView());
        }
    }

    @Override
    protected void initUI(View dialogView) {
        //switchPanel();

        CompoundButton.OnCheckedChangeListener tgbListener = (buttonView, isChecked) -> {
            M.d(buttonView == tgbDay);
            viewModel.setIsDay((buttonView == tgbDay) == isChecked);
        };
        tgbDay.setOnCheckedChangeListener(tgbListener);
        tgbDow.setOnCheckedChangeListener(tgbListener);

        for (TimePoint tp : viewModel.getTimePoints()) {
            addTimePointRow(tp);
        }
    }

    @Override
    protected void setNotifiers(View dialogView) {
        btnAddTimePoint.setOnClickListener(v -> {
            TimePoint tp = viewModel.addTimePoint();
            addTimePointRow(tp);
            printViewModelReport();
        });
        viewModel.getIsDay().observe(this, isDay -> {
            tgbDay.setChecked(isDay);
            tgbDow.setChecked(!isDay);
            switchPanel();
        });
    }

    @Override
    protected void setDialogButtonsListener(Button posBtn, Button neuBtn, Button negBtn) {
        posBtn.setOnClickListener(v -> {
            Bundle bundle = viewModel.getMistakes();
            int errorCounter = 0;
            String msg;

            String[] simpleMsgs = new String[] {
                    "pillName",
                    "pillDescription",
                    "day",
                    "dow",
                    "timePoints.general"
            };
            for (String key : simpleMsgs) {
                msg = bundle.getString(key);
                if (msg != null) {
                    M.d(msg);
                    errorCounter++;
                }
            }

            msg = bundle.getString("timePoints.empty");
            if (msg != null) {
                M.d(msg + ". Size: " + bundle.getString("timePoints.empty.length"));
                errorCounter++;
            }

            for (String key : new String[] {"wrong", "repetitive"}) {
                msg = bundle.getString("timePoints." + key);
                if (msg != null) {
                    M.d(msg + ". Size: " + bundle.getIntArray("timePoints." + key + ".hours[]"));
                    errorCounter++;
                }
            }

            // TODO странный способ для создания. Лучше переделать
            if (errorCounter == 0) {
                viewModel.createBox();
                dismiss();
            }
        });
    }

    @Override
    protected void findViews(View dialogView) {
        btnAddTimePoint = dialogView.findViewById(R.id.btnAddTimePoint);
        edtPillName = dialogView.findViewById(R.id.edtPillName);
        edtPillDescription = dialogView.findViewById(R.id.edtPillDescription);
        tgbDay = dialogView.findViewById(R.id.tgbDay);
        tgbDow = dialogView.findViewById(R.id.tgbDow);
        flDaysPanel = dialogView.findViewById(R.id.flDaysPanel);
        llTimePoints = dialogView.findViewById(R.id.llTimePoints);
    }

    @Override
    protected View buildDialogView() {
        DialogBoxEditBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutID(), null, false);
        binding.setLifecycleOwner(this);
        binding.setVm(viewModel);

        PanelDayBinding binding1 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.panel_day, flDaysPanel, false);
        binding1.setLifecycleOwner(this);
        binding1.setViewmodel(viewModel);
        panelDay = new PanelDay(binding1.getRoot());

        PanelDowBinding binding2 = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.panel_dow, flDaysPanel, false);
        binding2.setLifecycleOwner(this);
        binding2.setViewmodel(viewModel);
        panelDow = new PanelDow(binding2.getRoot());

        return binding.getRoot();
    }

    @Override
    protected void applyFragmentInteraction(Context activity) {
        if (activity instanceof BaseFragment.OnFragmentInteractionListener) {
            fragmentListener = ((BaseFragment.OnFragmentInteractionListener) activity);
        } else {
            throw new RuntimeException(activity.toString() + " должен имплементить OnFragmentInteractionListener");
        }
    }

    @Override
    protected void obtainViewModel() {
        viewModel = new ViewModelProvider(this).get("BOX_" + boxId, EditBoxViewModel.class);
        viewModel.setBoxId(boxId);
        //editBoxViewModelMap.put("BOX_" + boxId, viewModel);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_box_edit;
    }

    private  void printViewModelReport() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("ViewModelId {")
                .append("\n PillName: ")
                .append(viewModel.getPillName())
                .append("\n PillDescription: ")
                .append(viewModel.getPillDescription())
                .append("\n IsDay: ")
                .append(viewModel.getIsDay().getValue())
                .append("\n Day: ")
                .append(viewModel.getDay())
                .append("\n Dow: [ ");
        if (viewModel.getDow() != null) {
            for (boolean dw: viewModel.getDow()) {
                stringBuilder.append(dw).append(", ");
            }
        } else {
            stringBuilder.append("null");
        }
        stringBuilder
                .append(" ]")
                .append("\n TimePoints: [ ");
        if (viewModel.getTimePoints() != null) {
            for (TimePoint tp: viewModel.getTimePoints()) {
                stringBuilder.append(tp.toString()).append(", ");
            }
        } else {
            stringBuilder.append("null");
        }
        stringBuilder
                .append(" ]")
                .append("\n}");

        M.d(stringBuilder.toString());
    }
}
