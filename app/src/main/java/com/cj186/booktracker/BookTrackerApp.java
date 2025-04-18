package com.cj186.booktracker;

import android.app.Application;

import com.cj186.booktracker.database.SQLHandler;

public class BookTrackerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize SQLHandler once here
        SQLHandler.init(this);
    }
}
