package com.cj186.booktracker.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This view model allows a book to be easily passed to and from fragments.
 */

public class BookViewModel extends ViewModel {
    private final MutableLiveData<Book> intermediateBook = new MutableLiveData<>();
    // Get the book.
    public LiveData<Book> getIntermediateBook() {
        return intermediateBook;
    }
    // Set the book.
    public void setIntermediateBook(Book book) {
        intermediateBook.setValue(book);
    }
}
