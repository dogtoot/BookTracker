package com.cj186.booktracker.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BookViewModel extends ViewModel {
    private final MutableLiveData<Book> intermediateBook = new MutableLiveData<>();

    public LiveData<Book> getIntermediateBook() {
        return intermediateBook;
    }

    public void setIntermediateBook(Book book) {
        intermediateBook.setValue(book);
    }
}
