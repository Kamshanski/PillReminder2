package com.dawandeapp.pillreminder.ui.myviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.ui.abstracts.BaseCustomViewHolder;
import com.dawandeapp.pillreminder.utilities.M;

public class PanelDay extends BaseCustomViewHolder {
    EditText edtDays;

    public PanelDay(View view) {
        super(view);
    }

    @Override
    protected void findAllViews(View view) {
        edtDays = view.findViewById(R.id.edtDays);
    }

    public int getDays() {
        return Integer.valueOf(edtDays.getText().toString());
    }

    public void setDays(int days) {
        edtDays.setText(String.valueOf(days));
    }

}
