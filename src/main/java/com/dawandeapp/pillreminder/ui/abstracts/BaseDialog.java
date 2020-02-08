package com.dawandeapp.pillreminder.ui.abstracts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        buildDialog();
        setOnCancelListener(this::destroy);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        buildDialog();

        setOnCancelListener(this::destroy);
    }

    @Override
    public void create() {
        super.create();
        findViews();
        setDefaults();
        setListeners();
    }

    @Override
    public void show() {
        super.show();
        preform();
    }
    /** Build dialog: title, View... ...*/
    abstract protected void buildDialog();
    abstract protected void findViews();
    abstract protected void setDefaults();
    abstract protected void setListeners();
    abstract protected void preform();
    protected void destroy(DialogInterface dialogInterface) {

    }
}