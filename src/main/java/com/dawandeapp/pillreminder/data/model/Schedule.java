package com.dawandeapp.pillreminder.data.model;

import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Schedule extends RealmObject {
    public static final int EMPTY_DAY = 0b100000000;
    public static final int EMPTY_DOW = 0b000000000;
    public static final int MONDAY = 1 << 6;
    public static final int TUESDAY = 1 << 5;
    public static final int WEDNESDAY = 1 << 4;
    public static final int THURSDAY = 1 << 3;
    public static final int FRIDAY = 1 << 2;
    public static final int SATURDAY = 1 << 1;
    public static final int SUNDAY = 1;


    @PrimaryKey int dayOrDow = EMPTY_DAY;
    RealmList<TimePoint> times = new RealmList<>();

    //dayOrDow = 0b isDay xxxxxxx
    // if isDay xxxxxxx = int days gap between taking pills
    // if dow xxxxxxx = Mon, Tue ... Sun
    // Ex.: 0b10000010 = each 2 days take pills
    // Ex.: 0b01100110 = each Mon, Tue, Fri, Sat take pills

    public Schedule() {}

    public Schedule(int dayOrDow, RealmList<TimePoint> times) {
        this.dayOrDow = dayOrDow;
        this.times = times;
    }

    public boolean isEmpty() {
        return dayOrDow == EMPTY_DAY
                || times == null
                || times.isEmpty()
                || times.size() < 1;
    }

    public static Schedule getEmpty() {
        final Schedule schedule = new Schedule();
        schedule.dayOrDow = EMPTY_DAY;
        return schedule;
    }

    public void put(TimePoint tp) {
        times.add(tp);
    }

    public void remove(TimePoint tp) {
        times.remove(tp);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "dayOrDow=" + dayOrDow +
                ", times=" + (times == null ? "null" : times) +
                '}';
    }

    public int getDayOrDow() {
        return dayOrDow;
    }

    public void setDayOrDow(int dayOrDow) {
        this.dayOrDow = dayOrDow;
    }

    public RealmList<TimePoint> getTimes() {
        return times;
    }

    public void setTimes(RealmList<TimePoint> times) {
        this.times = times;
    }
}
