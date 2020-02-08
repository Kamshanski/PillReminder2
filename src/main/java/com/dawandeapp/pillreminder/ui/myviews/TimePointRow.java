package com.dawandeapp.pillreminder.ui.myviews;

import android.view.View;
import android.widget.ImageButton;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.databinding.TimepointRowBinding;
import com.dawandeapp.pillreminder.ui.abstracts.BaseCustomViewHolder;

public class TimePointRow extends BaseCustomViewHolder {
    TimepointRowBinding binding;
    ImageButton imbDeleteTimePoint;
    public TimePointRow(View view) {
        super(view);
        init();
    }

    public void setBinding(TimepointRowBinding binding) {
        this.binding = binding;
    }

    public TimepointRowBinding getBinding() {
        return binding;
    }

    public void setOnDeleteBtnListener(View.OnClickListener listener) {
        imbDeleteTimePoint.setOnClickListener(listener);
    }

    @Override
    protected void findAllViews(View view) {
        imbDeleteTimePoint = view.findViewById(R.id.imbDeleteTimePoint);
    }
}
