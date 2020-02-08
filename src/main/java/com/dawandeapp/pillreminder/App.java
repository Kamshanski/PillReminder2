package com.dawandeapp.pillreminder;

import android.app.Application;

import com.dawandeapp.pillreminder.data.database.Database;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Database.init(this);


    }
}
