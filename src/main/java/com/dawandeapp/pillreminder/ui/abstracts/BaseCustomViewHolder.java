package com.dawandeapp.pillreminder.ui.abstracts;

import android.view.View;

public abstract class BaseCustomViewHolder {
    protected View view;

    public BaseCustomViewHolder(View view) {
        this.view = view;
        init();
    }

    //TODO: заменить attachToRoot на true и заменить в layout LinearLayout на merge
    protected void init() {
        findAllViews(view);
        setDefaults(view);
    }


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    protected void findAllViews(View view) {}
    protected void setDefaults(View view) {}
}
