package com.dawandeapp.pillreminder.data.model;

import android.annotation.SuppressLint;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;

import java.util.Objects;
import java.util.TreeSet;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TimePoint extends RealmObject {
    private int hour = -1;
    private int min = -1;

    public TimePoint() {}

    public TimePoint(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    public static TimePoint createNew(int hour, int min) {
        if (hour >= 0 && hour < 24 && min >= 0 && min < 60) {
            return new TimePoint(hour, min);
        } else {
            return null;
        }
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    @NonNull
    @Override
    public String toString() {
        return "("+hour+":"+min+")";
    }

    public int validationHashCode() {
        return Objects.hash(hour, min);
    }

    @BindingAdapter("android:text")
    public static void setText(EditText view, int value) {
        if (value < 0) {
            view.setText("");
        } else {
            view.setText(String.valueOf(value));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText(EditText view) {
        String num = view.getText().toString();
        if (num == null || num.isEmpty()) {
            return -1;
        } else {
            return Integer.parseInt(view.getText().toString());
        }
    }
}
