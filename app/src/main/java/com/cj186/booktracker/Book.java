package com.cj186.booktracker;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {
    private byte[] imageBytes;
    private String title;
    private String author;
    private String description;
    private Status status;
    private String yearPublished;
    private String ISBN;
    private boolean favoriteStatus = false;

    public Book(byte[] imageBytes, String title, String author, String description, Status status, String yearPublished, String ISBN, boolean favoriteStatus) {
        this.imageBytes = imageBytes;
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
        this.yearPublished = yearPublished;
        this.ISBN = ISBN;
        this.favoriteStatus = favoriteStatus;
    }

    protected Book(Parcel in) {
        imageBytes = new byte[in.readInt()];;
        in.readByteArray(imageBytes);
        title = in.readString();
        author = in.readString();
        description = in.readString();
        yearPublished = in.readString();
        ISBN = in.readString();
        favoriteStatus = in.readByte() != 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public boolean isFavorite() {
        return favoriteStatus;
    }

    public void setFavoriteStatus(boolean favoriteStatus) {
        this.favoriteStatus = favoriteStatus;
    }

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
    public Book(byte[] image, Status status) {
        this.imageBytes = image;
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(imageBytes.length);
        dest.writeByteArray(imageBytes);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(yearPublished);
        dest.writeString(ISBN);
        dest.writeByte((byte) (favoriteStatus ? 1 : 0));
    }
}
