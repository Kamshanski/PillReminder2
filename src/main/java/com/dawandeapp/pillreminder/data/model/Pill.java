package com.dawandeapp.pillreminder.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Pill extends RealmObject {
    public static final String EMPTY_NAME = "null";
    public static final String EMPTY_DESCRIPTION = "null";

    @PrimaryKey private String name;
    private String description;

    public Pill() {}

    public Pill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public boolean isEmpty() {
        return name == null
                || name.isEmpty()
                || name.equals(Pill.EMPTY_NAME);
    }


    public static Pill getEmpty() {
        return new Pill(EMPTY_NAME, EMPTY_DESCRIPTION);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
