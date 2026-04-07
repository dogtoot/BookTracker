package com.cj186.booktracker.model;

import android.database.Cursor;

import com.cj186.booktracker.database.DBHelper;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This class holds book data.
 */

public class Book {
    // Fields for a book.
    private int id;
    private final byte[] imageBytes;
    private final String title;
    private final String author;
    private final String description;
    private final Status status;
    private final String yearPublished;
    private final String ISBN;
    private boolean favoriteStatus;

    public Book(byte[] imageBytes, String title, String author, String description, Status status, String yearPublished, String ISBN, boolean favoriteStatus) {
        // Constructor
        this.imageBytes = imageBytes;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
        this.yearPublished = yearPublished;
        this.ISBN = ISBN;
        this.favoriteStatus = favoriteStatus;
    }

    public Book(int id, byte[] imageBytes, String title, String author, String description, Status status, String yearPublished, String ISBN, boolean favoriteStatus) {
        // Constructor
        this.id = id;
        this.imageBytes = imageBytes;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
        this.yearPublished = yearPublished;
        this.ISBN = ISBN;
        this.favoriteStatus = favoriteStatus;
    }

    public Book(Cursor dbResult){
        // Create a book using a cursor.
        this.id = dbResult.getInt(dbResult.getColumnIndexOrThrow(DBHelper.getColumnId()));
        this.imageBytes = dbResult.getBlob(dbResult.getColumnIndexOrThrow(DBHelper.getColumnImageBlob()));
        this.title = dbResult.getString(dbResult.getColumnIndexOrThrow(DBHelper.getColumnTitle()));
        this.author = dbResult.getString(dbResult.getColumnIndexOrThrow(DBHelper.getColumnAuthor()));
        this.ISBN = dbResult.getString(dbResult.getColumnIndexOrThrow(DBHelper.getColumnIsbn()));
        this.description = dbResult.getString(dbResult.getColumnIndexOrThrow(DBHelper.getColumnDescription()));
        this.status = Status.fromLabel(dbResult.getString(dbResult.getColumnIndexOrThrow(DBHelper.getColumnStatus())));
        this.yearPublished = dbResult.getString(dbResult.getColumnIndexOrThrow(DBHelper.getColumnYearPublished()));
        this.favoriteStatus = dbResult.getInt(dbResult.getColumnIndexOrThrow(DBHelper.getColumnFavoriteStatus())) != 0;
    }

    // Getter and setter for favorite.
    public boolean isFavorite() {
        return favoriteStatus;
    }
    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    // Getter and setter for status.
    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    // Getters for all other values.
    public byte[] getImageBytes() {
        return imageBytes;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getYearPublished() {
        return yearPublished;
    }

    public String getISBN() {
        return ISBN;
    }
}
