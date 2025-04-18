package com.cj186.booktracker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cj186.booktracker.BookTrackerApp;
import com.cj186.booktracker.model.Status;

public class SQLHandler {
    private static SQLiteDatabase db;

    public static void init(BookTrackerApp bookTrackerApp) {
        // Create our DBHelper and set db equal to the writeable database.
        DBHelper helper = new DBHelper(bookTrackerApp);
        db = helper.getWritableDatabase();
    }

    public static void insertBook(byte[] imageBytes, String title, String author, String description,
                                  Status status, String yearPublished, String ISBN, boolean favoriteStatus){

        // Put all book values into Content values.
        ContentValues values = new ContentValues();
        values.put(DBHelper.getColumnImageBlob(), imageBytes);
        values.put(DBHelper.getColumnTitle(), title);
        values.put(DBHelper.getColumnAuthor(), author);
        values.put(DBHelper.getColumnDescription(), description);
        values.put(DBHelper.getColumnStatus(), status.getLabel());
        values.put(DBHelper.getColumnYearPublished(), yearPublished);
        values.put(DBHelper.getColumnIsbn(), ISBN);
        values.put(DBHelper.getColumnFavoriteStatus(), favoriteStatus ? 1 : 0);

        // Insert values into the Books table.
        db.insert(DBHelper.getTableBooks(), null, values);
    }

    public static Cursor getAllBooks(){
        // Sort all books by the created date.
        return db.query(DBHelper.getTableBooks(),
                null,
                null,
                null,
                null,
                null,
                "created_at DESC");
    }

    public static void close(){
        // Close the database if it is open and not null.
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
