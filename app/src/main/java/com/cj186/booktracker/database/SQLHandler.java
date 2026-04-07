package com.cj186.booktracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cj186.booktracker.model.Status;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This class gets and puts data into the database.
 */

public class SQLHandler {
    private static SQLiteDatabase db;

    public static void init(Context bookTrackerApp) {
        // Create our DBHelper and set db equal to the writeable database.
        DBHelper helper = new DBHelper(bookTrackerApp);
        db = helper.getWritableDatabase();
    }

    public static void insertBook(byte[] imageBytes, String title, String author, String description, Status status, String yearPublished, String ISBN, boolean favoriteStatus){
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

    public static void updateBook(int id, Status status){
        // Update a book's status using ID.
        ContentValues values = new ContentValues();
        values.put(DBHelper.getColumnStatus(), status.getLabel());

        db.update(DBHelper.getTableBooks(), values, DBHelper.getColumnId() + " = ?", new String[] { String.valueOf(id)});
    }

    public static void updateBook(int id, boolean favoriteStatus){
        // Update a book's favorite status using ID.
        ContentValues values = new ContentValues();
        values.put(DBHelper.getColumnFavoriteStatus(), favoriteStatus ? 1 : 0);

        db.update(DBHelper.getTableBooks(), values, DBHelper.getColumnId() + " = ?", new String[] { String.valueOf(id)});
    }

    public static void removeBook(int id){
        // Delete book based on ID.
        db.delete(DBHelper.getTableBooks(), DBHelper.getColumnId() + " = ?", new String[] { String.valueOf(id)});
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

    public static Cursor getBookById(int id){
        // Gets a book by id.
        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.query(
                DBHelper.getTableBooks(),
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    public static void close(){
        // Close the database if it is open and not null.
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
