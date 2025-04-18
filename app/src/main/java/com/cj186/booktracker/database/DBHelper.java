package com.cj186.booktracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // Database information.
    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 1;

    // Database columns.
    private static final String TABLE_BOOKS = "books";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IMAGE_BLOB = "image";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_ISBN = "isbn";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_YEAR_PUBLISHED = "year_published";
    private static final String COLUMN_FAVORITE_STATUS = "favorite_status";

    // Database creation string.
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_BOOKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_IMAGE_BLOB + " BLOB, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_AUTHOR + " TEXT, " +
                    COLUMN_ISBN + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_YEAR_PUBLISHED + " TEXT, " +
                    COLUMN_FAVORITE_STATUS + " INTEGER, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    // Constructor for our DBHelper.
    public DBHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate we instantiate the database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Column getters.
    public static String getTableBooks() {
        return TABLE_BOOKS;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnImageBlob() {
        return COLUMN_IMAGE_BLOB;
    }

    public static String getColumnTitle() {
        return COLUMN_TITLE;
    }

    public static String getColumnAuthor() {
        return COLUMN_AUTHOR;
    }

    public static String getColumnIsbn() {
        return COLUMN_ISBN;
    }

    public static String getColumnDescription() {
        return COLUMN_DESCRIPTION;
    }

    public static String getColumnStatus() {
        return COLUMN_STATUS;
    }

    public static String getColumnYearPublished() {
        return COLUMN_YEAR_PUBLISHED;
    }

    public static String getColumnFavoriteStatus() {
        return COLUMN_FAVORITE_STATUS;
    }
}
