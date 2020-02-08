package com.dawandeapp.pillreminder.ui.myviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.data.model.Box;
import com.dawandeapp.pillreminder.data.model.Pill;
import com.dawandeapp.pillreminder.utilities.M;
import com.dawandeapp.pillreminder.ui.abstracts.OnSwipeTouchListener;

public class BoxRow extends LinearLayoutCompat {
    final private int index;
    private boolean isEmpty = true;

    TextView tx_name, tx_schedule, tx_number;
    ImageButton button;
    ConstraintLayout background;

    BoxRowListener listener;

    public BoxRow(Context context) {
        this(context, null);
    }

    public BoxRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoxRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.BoxRow);
        index = attributes.getInteger(R.styleable.BoxRow_index, 9);

        if (index < 0 || index > 3) {
            M.d("ERROR: BoxRow: Id is out of bounds!!!");
        }

        attributes.recycle();   // Освободить ресурсы
        init();
    }

    //TODO: заменить attachToRoot на true и заменить в layout LinearLayout на merge
    private void init() {
        removeAllViews();
        //TODO заменит false на тру и сделать merge в лэйауте
        View view = LayoutInflater.from(getContext()).inflate(R.layout.box_row, this, false);
        findViews(view);
        initUi();
        refreshAppearance();
        addView(view);
    }

    private void findViews(View view) {
        tx_name = view.findViewById(R.id.tx_name);
        tx_schedule = view.findViewById(R.id.tx_schedule);
        tx_number = view.findViewById(R.id.tx_number);
        button = view.findViewById(R.id.imb_add_edit);
        background = view.findViewById(R.id.background);

    }

    public void initUi() {
        if (tx_number != null) {
            tx_number.setText(String.valueOf(index +1));
        } else { M.d("tx_number is null!"); }

        button.setOnClickListener(v -> {
            if (listener != null) {
                if (isEmpty) {
                    listener.onAddButtonClick(index);
                } else {
                    listener.onEditButtonClick(index);
                }
            }
        });
    }

    private void refreshAppearance() {
        if (button != null) {

            if (isEmpty) {
                button.setImageDrawable(button.getResources().getDrawable(R.drawable.ic_add, button.getContext().getTheme()));
            }
            else {
                button.setImageDrawable(button.getResources().getDrawable(R.drawable.ic_edit, button.getContext().getTheme()));
            }

            setSwipeListener();
        }
    }

    public void setName(String name) {
        if (tx_name != null) {
            if (name != null && !name.isEmpty()) {
                tx_name.setText(name);
            } else {
                tx_name.setText("Empty BOX_" + index);
                M.d("WARNING: BOXROW: pillName is null or empty");
            }
        } else M.d("tx_name is null!");
    }

    public void setIsEmpty(boolean isEmpty) {
        if (isEmpty != this.isEmpty) {
            this.isEmpty = isEmpty;
            refreshAppearance();
        }
    }

    public void setSchedule(String schedule) {
        if (tx_schedule != null ) {
            tx_schedule.setText(schedule);
        } else M.d("tx_schedule is null!");
    }

    public void setImportance(int importance) {
        //TODO
    }

    public void setListener(BoxRowListener listener) {
        this.listener = listener;
    }

    private void setSwipeListener() {
        setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeRight() {
                M.d("Move pill RIGHT: " + index);
                listener.onSwipeRight(index);
            }

            @Override
            public void onSwipeLeft() {
                M.d("Move pill LEFT: " + index);
                listener.onSwipeLeft(index);
            }

            @Override
            public void onSwipeTop() {
                M.d("Move pill TOP: " + index);
                listener.onSwipeTop(index);
            }

            @Override
            public void onSwipeBottom() {
                M.d("Move pill BOTTOM: " + index);
                listener.onSwipeBottom(index);
            }
        });
    }

//    private void runAddPillDialog() {
//        final PillDialog addDialog = new PillDialog(getContext(), adapter);
//        addDialog.setID(adapter.getPill().getId());
//        addDialog.create(); //Создание Диалога
//        addDialog.show(); //Показ Диалога
//    }
//
//    public void notifyPillChanged() {
//        setPill(adapter.getPill());
//    }

    public int getIndex() {
        return index;
    }

    public interface BoxRowListener {
        default void onClick(int id) {}

        default void onAddButtonClick(int id) {}
        default void onEditButtonClick(int id) {}

        default void onSwipeRight(int id) {}
        default void onSwipeLeft(int id) {}
        default void onSwipeTop(int id) {}
        default void onSwipeBottom(int id) {}

    }
}
