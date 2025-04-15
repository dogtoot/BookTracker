package com.cj186.booktracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Blob;

public class SQLHandler {
    private static SQLiteDatabase db;

    public static void init(BookTrackerApp bookTrackerApp) {
        DBHelper helper = new DBHelper(bookTrackerApp);
        db = helper.getWritableDatabase();
    }

    public static void insertBook(byte[] imageBytes, String title, String author, String description,
                                  Status status, String yearPublished, String ISBN, boolean favoriteStatus){
        ContentValues values = new ContentValues();
        values.put(DBHelper.getColumnImageBlob(), imageBytes);
        values.put(DBHelper.getColumnTitle(), title);
        values.put(DBHelper.getColumnAuthor(), author);
        values.put(DBHelper.getColumnDescription(), description);
        values.put(DBHelper.getColumnStatus(), status.getLabel());
        values.put(DBHelper.getColumnYearPublished(), yearPublished);
        values.put(DBHelper.getColumnIsbn(), ISBN);
        values.put(DBHelper.getColumnFavoriteStatus(), favoriteStatus ? 1 : 0);
    }
}
