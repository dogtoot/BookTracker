package com.cj186.booktracker;

import android.app.Application;

public class BookTrackerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize SQLHandler once here
        SQLHandler.init(this);
    }
}
