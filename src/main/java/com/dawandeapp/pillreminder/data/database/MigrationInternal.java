package com.dawandeapp.pillreminder.data.database;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MigrationInternal implements RealmMigration {
    private static volatile MigrationInternal instance;

    public static MigrationInternal getInstance() {
        if (instance == null) {
            synchronized (MigrationInternal.class) {
                if (instance == null) {
                    instance = new MigrationInternal();
                }
            }
        }
        return instance;
    }

    private MigrationInternal() {}

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
//            schema.create("Person")
//                    .addField("name", String.class)
//                    .addField("age", int.class);
            oldVersion++;
        }

        if (oldVersion == 1) {
//            schema.get("Person")
//                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
//                    .addRealmObjectField("favoriteDog", schema.get("Dog"))
//                    .addRealmListField("dogs", schema.get("Dog"));
            oldVersion++;
        }
    }
}
