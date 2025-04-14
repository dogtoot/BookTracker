package com.cj186.booktracker;

import android.graphics.Bitmap;

public class Book {
    private Bitmap image;
    private String title;
    private String author;
    private String description;
    private Status status;
    private String yearPublished;
    private String ISBN;
    private boolean favoriteStatus = false;

    public Book(Bitmap image, String title, String author, String description, Status status, String yearPublished, String ISBN, boolean favoriteStatus) {
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
        this.yearPublished = yearPublished;
        this.ISBN = ISBN;
        this.favoriteStatus = favoriteStatus;
    }

    public boolean isFavorite() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

    public Bitmap getImage() {
        return image;
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

    public Status getStatus() {
        return status;
    }

    public String getYearPublished() {
        return yearPublished;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
//Testing
    public Book(Bitmap image, Status status) {
        this.image = image;
        this.status = status;
    }
}
