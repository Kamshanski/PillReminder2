package com.dawandeapp.pillreminder.data.model;

import java.util.HashSet;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Box extends RealmObject {
    public static final int IMPORTANT = 5;
    public static final int VITAL = 2;
    public static final int NOT_IMPORTANT = 3;
    public static final int DIETARY_SUPPLIMENTS = 4;

    @PrimaryKey private int complexId;
    private long syncTime = -1L;
    private int times = -1;
    private int importance = -1;
    private Pill pill;
    private Schedule schedule;

    public Box() {}

    public Box(int id, boolean external, long syncTime, int times, int importance, Pill pill, Schedule schedule) {
        this(Box.idToComplex(id, external), syncTime, times, importance, pill, schedule);
    }

    public Box(int complexId, long syncTime, int times, int importance, Pill pill, Schedule schedule) {
        this.complexId = complexId;
        this.syncTime = syncTime;
        this.times = times;
        this.importance = importance;
        this.pill = pill;
        this.schedule = schedule;
    }

    public static int idToComplex(int id, boolean external) {
        return external ? ~id : id;
    }

    public static int complexToId(int complexId) {
        return complexId >= 0 ? complexId : ~complexId;
    }

    public static boolean isInternal(int complexId) {
        return complexId >= 0;
    }

    public static Box getEmpty(int complexId) {
        return new Box(complexId, 0, 0, -1, Pill.getEmpty(), Schedule.getEmpty());
    }

    public boolean isValidBox(boolean external) {
        return getId() >= 0 && getId() < 4
                && (isInternal(complexId) == !external)
                && importance > 0
                && pill != null
                && pill.getName() != null
                && !pill.getName().isEmpty()
                && schedule != null
                && schedule.times != null
                && !schedule.times.isEmpty();
    }

    public boolean isEmpty() {
        return importance < 0 ||
                pill.isEmpty() ||
                schedule.isEmpty();

    }


    // Getters & setters && isXXXX
        //Custom get/set
    public boolean isInternal() {return complexId >=0;}

    public int getId() {
        return Box.complexToId(this.complexId);
    }

    public void setId(int id, boolean external) {
        setComplexId(Box.idToComplex(id, external));
    }

        // Auto Get/Set
    public int getComplexId() {
        return complexId;
    }
    public void setComplexId(int complexId) {
        this.complexId = complexId;
    }
    public long getSyncTime() {
        return syncTime;
    }
    public void setSyncTime(long syncTime) {
        this.syncTime = syncTime;
    }
    public int getTimes() {
        return times;
    }
    public void setTimes(int times) {
        this.times = times;
    }
    public int getImportance() {
        return importance;
    }
    public void setImportance(int importance) {
        this.importance = importance;
    }
    public Pill getPill() {
        return pill;
    }
    public void setPill(Pill pill) {
        this.pill = pill;
    }
    public Schedule getSchedule() {
        return schedule;
    }
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }


    public static class BoxBuilder {
        int complexId;
        private Pill pill = Pill.getEmpty();
        private Schedule schedule = Schedule.getEmpty();

        //Additional vars
        // TODO Заменить на нормальное задание!!!
        private String pillName;
        private String pillDescription;
        private int importance = DIETARY_SUPPLIMENTS;
        private boolean isDay;
        private int day;
        private boolean[] dow;
        private HashSet<TimePoint> timePoints;


        public BoxBuilder(int id, boolean external) {
            this.complexId = Box.idToComplex(id, external);
        }

        public Box build() {
            pill.setName(pillName);
            pill.setDescription(pillDescription);

            int dayOrDow;
            if (isDay) {
                dayOrDow = Schedule.EMPTY_DAY | day;
            } else {
                dayOrDow = Schedule.EMPTY_DOW
                        | (!dow[0] ? 0 : Schedule.MONDAY)
                        | (!dow[1] ? 0 : Schedule.TUESDAY)
                        | (!dow[2] ? 0 : Schedule.WEDNESDAY)
                        | (!dow[3] ? 0 : Schedule.THURSDAY)
                        | (!dow[4] ? 0 : Schedule.FRIDAY)
                        | (!dow[5] ? 0 : Schedule.SATURDAY)
                        | (!dow[6] ? 0 : Schedule.SUNDAY);
            }
            schedule.setDayOrDow(dayOrDow);

            for (TimePoint tp : timePoints) {
                schedule.put(tp);
            }

            final Box box = new Box(complexId, 0, 0, importance, pill, schedule);
            return box;
        }

        public BoxBuilder setPillName(String pillName) {
            this.pillName = pillName;
            return this;
        }

        public BoxBuilder setPillDescription(String pillDescription) {
            this.pillDescription = pillDescription;
            return this;
        }

        public BoxBuilder setImportance(int importance) {
            this.importance = importance;
            return this;
        }

        public BoxBuilder setIsDay(boolean isDay) {
            isDay = isDay;
            return this;
        }

        public BoxBuilder setDay(int day) {
            this.day = day;
            return this;
        }

        public BoxBuilder setDow(boolean[] dow) {
            this.dow = dow;
            return this;
        }

        public BoxBuilder setTimePoints(HashSet<TimePoint> timePoints) {
            this.timePoints = timePoints;
            return this;
        }
    }
}
