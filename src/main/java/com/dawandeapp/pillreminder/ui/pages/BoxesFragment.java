package com.dawandeapp.pillreminder.ui.pages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dawandeapp.pillreminder.R;
import com.dawandeapp.pillreminder.data.model.Box;
import com.dawandeapp.pillreminder.ui.abstracts.BaseFragment;
import com.dawandeapp.pillreminder.ui.viewmodels.BoxesViewModel;
import com.dawandeapp.pillreminder.utilities.M;

import java.util.ArrayList;

public class BoxesFragment extends BaseFragment {
    public static final String CLASS = BoxesFragment.class.getSimpleName();
    private BoxesViewModel mViewModel;

    private ViewPager2 viewPager;

    public static BoxesFragment newInstance() {
        return new BoxesFragment();
    }

    @Override
    protected void initInstance() {
        mViewModel = new ViewModelProvider(this).get(BoxesViewModel.class);
    }

    @Override
    protected void findViews(View fv) {
        viewPager = fv.findViewById(R.id.viewPager);
    }

    @Override
    protected void initUI(View fv) {
        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2, false);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_boxes;
    }

    class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PageHolder> {

        public ViewPagerAdapter() {}

        class PageHolder extends RecyclerView.ViewHolder {
            public PageHolder(@NonNull View itemView) {
                super(itemView);
            }

            int getActualPos() {
                int x = -((Integer.MAX_VALUE / 2) - getLayoutPosition());
                return x >= 0
                        ? x % 4
                        : 3 - (Math.abs(x + 1) % 4);
            }

            void btnSchedule(String s) {
                (itemView.findViewById(R.id.btn_schedule)).setOnClickListener(v -> {
                    M.wSh(itemView.getContext(), "Вы попытались добавить расписание (пока не реализовано)");
                });
            }

            void setSchedule(String s) {
                putTextIn(s, R.id.pillSchedule);
            }

            void setName(String s) {
                putTextIn(s, R.id.tx_pillName);
            }

            void setID(String s) {
                putTextIn(s, R.id.tx_pillID);
            }

            void putTextIn(String s, int id) {
                ((TextView) itemView.findViewById(id)).setText(s);
            }
        }

        @NonNull
        @Override
        public PageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_page, parent, false);
            //M.d("viewType = " + viewType);
            return new PageHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PageHolder holder, int position) {

            LiveData<ArrayList<Box>> liveBoxes = mViewModel.getBoxes();
            Box box = liveBoxes.getValue().get(holder.getActualPos());

            holder.setName(box.getPill().getName());
            holder.setID(String.valueOf(box.getId()));
            holder.setSchedule(box.getSchedule().toString());

            liveBoxes.observe(BoxesFragment.this, boxes -> {
                Box box1 = boxes.get(holder.getActualPos());
                holder.setName(box1.getPill().getName());
                holder.setID(String.valueOf(box1.getId()));
                holder.setSchedule(box1.getSchedule().toString());
            });

            holder.itemView.findViewById(R.id.btn_schedule)
                    .setOnClickListener(v -> M.wSh(holder.itemView.getContext(),
                            "Вы попытались добавить расписание (пока не реализовано)"));
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }
    }
}
