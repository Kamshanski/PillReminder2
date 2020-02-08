package com.dawandeapp.pillreminder.data.database;

import android.app.Application;

import com.dawandeapp.pillreminder.App;
import com.dawandeapp.pillreminder.data.model.Box;

import java.util.ArrayList;
import java.util.List;

import io.realm.ImportFlag;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

public class Database {
    public static volatile Database instance = new Database();
    public static final int VERSION = 0;
    public static final String NAME = "internal.realm";

    public Box get(int id) {
        return get(id, false);
    }

    public Box get(int id, boolean external) {
        if (id < 0 || id > 3)
            return null;

        int complexId = Box.idToComplex(id, external);
        Realm r = Realm.getInstance(getConfig());
        Box b = r.where(Box.class).equalTo("complexId", complexId).findFirst();
        r.close();
        return b;
    }

    public List<Box> getAll() {
        return getAll(false);
    }

    public ArrayList<Box> getAll(boolean external) {
        Realm r = Realm.getInstance(getConfig());
        int complexIdFrom = external ? Box.idToComplex(3, external) : 0;
        int complexIdTo = external ? Box.idToComplex(0, external) : 3;
        RealmResults<Box> b = r.where(Box.class).between("complexId", complexIdFrom, complexIdTo).findAll();
        r.close();
        return new ArrayList<>(b);
    }

    public Box put(Box box) {
        Realm r = Realm.getInstance(getConfig());
        r.beginTransaction();
        Box recordedBox = r.copyToRealmOrUpdate(box, ImportFlag.CHECK_SAME_VALUES_BEFORE_SET);
        r.commitTransaction();
        return recordedBox;
    }

    public Box delete(Box box) {
        Realm r = Realm.getInstance(getConfig());
        r.beginTransaction();
        box.deleteFromRealm();
        Box recordedBox = r.copyToRealmOrUpdate(box, ImportFlag.CHECK_SAME_VALUES_BEFORE_SET);
        r.commitTransaction();
        return recordedBox;
    }

    public static Database getInstance() {
        return instance;
    }

    public static RealmConfiguration getConfig() {
        return new RealmConfiguration.Builder()
                .schemaVersion(VERSION)
                .migration(MigrationInternal.getInstance())
                .name(NAME)
                .build();
    }

    public static void init(Application application) {
        Realm.init(application);
        Realm r = Realm.getInstance(getConfig());

        RealmResults<Box> res = r.where(Box.class).findAll();
        if (res.size() != 8) {
            // Internal
            for (int i = 0; i < 8; i++) {
                r.beginTransaction();
                Box b = Box.getEmpty(
                        Box.idToComplex(
                                i < 4 ? i : i-4,
                                i >= 4));
                r.insertOrUpdate(b);
                r.commitTransaction();
            }
            // External
            for (int i = 0; i < 4; i++) {
                r.beginTransaction();
                Box b = Box.getEmpty(i);
                r.insertOrUpdate(b);
                r.commitTransaction();
            }
        }

    }
}
