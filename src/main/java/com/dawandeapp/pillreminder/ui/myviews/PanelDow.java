package com.dawandeapp.pillreminder.ui.myviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.ui.abstracts.BaseCustomViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PanelDow extends BaseCustomViewHolder {
    private CheckBox[] checkBoxes;
    private boolean[] dows;

    public PanelDow(View view) {
        super(view);
    }

    // :(.
    @Override
    protected void findAllViews(View view) {
        checkBoxes = new CheckBox[7];
        dows = new boolean[7];
        Arrays.fill(checkBoxes, null);
        checkBoxes[0] = (view.findViewById(R.id.chbMonday));
        checkBoxes[1] = (view.findViewById(R.id.chbTuesday));
        checkBoxes[2] = (view.findViewById(R.id.chbWednesday));
        checkBoxes[3] = (view.findViewById(R.id.chbThursday));
        checkBoxes[4] = (view.findViewById(R.id.chbFriday));
        checkBoxes[5] = (view.findViewById(R.id.chbSaturday));
        checkBoxes[6] = (view.findViewById(R.id.chbSunday));
    }

    @Override
    protected void setDefaults(View view) {
        for (int i = 0; i < 7; i++) {
            dows[i] = checkBoxes[i].isChecked();
        }
    }

    public boolean[] getDows() {
        for (int i = 0; i < 7; i++) {
            dows[i] = this.checkBoxes[i].isChecked();
        }
        return dows;
    }

    public void setDows(boolean[] dows) {
        this.dows = dows;
        for (int i = 0; i < 7; i++) {
            checkBoxes[i].setChecked(dows[i]);
        }
    }
}
