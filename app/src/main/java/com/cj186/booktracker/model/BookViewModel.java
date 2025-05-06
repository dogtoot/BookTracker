package com.cj186.booktracker.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
