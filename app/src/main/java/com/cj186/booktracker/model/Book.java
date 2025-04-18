package com.cj186.booktracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Book implements Parcelable {
    // Fields for a book.
    private byte[] imageBytes;
    private String title;
    private String author;
    private String description;
    private Status status;
    private String yearPublished;
    private String ISBN;
    private boolean favoriteStatus = false;

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

    protected Book(Parcel in) {
        // Create a book from a parcel.
        imageBytes = new byte[in.readInt()];;
        in.readByteArray(imageBytes);
        title = in.readString();
        author = in.readString();
        description = in.readString();
        yearPublished = in.readString();
        ISBN = in.readString();
        favoriteStatus = in.readByte() != 0;
    }

    // Default Creator for a parcelable.
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
    public void setStatus(Status status){ this.status = status; }

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

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        // Write the length of imageBytes to the parcel, for later reading.
        dest.writeInt(imageBytes.length);
        // Write all fields.
        dest.writeByteArray(imageBytes);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(yearPublished);
        dest.writeString(ISBN);

        // Write our boolean favorite status as a byte, either 1 or 0.
        dest.writeByte((byte) (favoriteStatus ? 1 : 0));
    }
}
