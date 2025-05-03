package com.cj186.booktracker.library;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cj186.booktracker.BaseActivity;
import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.R;
import com.cj186.booktracker.database.SQLHandler;
import com.cj186.booktracker.addbook.AddBookActivity;

import java.util.ArrayList;

/**
 * Welcome to BookTracker, this is the first activity shown when the app is run.
 * It is the library where users can see all of their books.
 * <pre>
 *        .--.                   .---.
 *    .---|__|           .-.     |~~~|
 * .--|===|--|_          |_|     |~~~|--.
 * |  |===|  |'\     .---!~|  .--|   |--|
 * |%%|   |  |.'\    |===| |--|%%|   |  |
 * |%%|   |  |\.'\   |   | |__|  |   |  |
 * |  |   |  | \  \  |===| |==|  |   |  |
 * |  |   |__|  \.'\ |   |_|__|  |~~~|__|
 * |  |===|--|   \.'\|===|~|--|%%|~~~|--|
 * ^--^---'--^    `-'`---^-^--^--^---'--' hjw
 * <a href="https://www.asciiart.eu/books/books">ASCII Art Source</a>
 * </pre>
 */

public class MainActivity extends BaseActivity {

    private RecyclerView libraryList;
    private TextView emptyTextView;
    private LibraryAdapter adapter;
    private ArrayList<Book> bookList;

    private Button favoriteBtn;
    private Button addBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLHandler.init(this);
        populateViews();
        loadBooks();

        adapter = new LibraryAdapter(this, bookList);
        libraryList.setLayoutManager(
                new GridLayoutManager(
                        this,
                        getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 6 : 3
                        ));
        libraryList.setAdapter(adapter);

        populateLibrary();

        favoriteBtn.setOnClickListener(view -> swapLibrary());
        addBook.setOnClickListener(view -> startAddBookActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
        adapter.updateBooks(bookList);
        populateLibrary();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLHandler.close();
    }

    private void startAddBookActivity(){
        // Start the add book activity using an intent.
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    private void populateViews(){
        libraryList = findViewById(R.id.book_library);
        emptyTextView = findViewById(R.id.emptyView);
        favoriteBtn = findViewById(R.id.favorites_btn);
        addBook = findViewById(R.id.add_book_btn);
    }

    private void loadBooks(){
        Cursor cursor = SQLHandler.getAllBooks();
        bookList = new ArrayList<>();
        // Loop through all rows, and add them as a book to bookList.
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(cursor);
                bookList.add(book);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
    }

    private void populateLibrary(){
        boolean useFavorites = favoriteBtn.getText().toString().equals(getString(R.string.favorites_btn_str));
        adapter.setFilter(!useFavorites);
        // If the adapter is empty, show a textview saying the library has no books.
        if(adapter.getItemCount() == 0){
            emptyTextView.setVisibility(View.VISIBLE);
            libraryList.setVisibility(View.GONE);
        }
        else{
            emptyTextView.setVisibility(View.GONE);
            libraryList.setVisibility(View.VISIBLE);
        }
    }

    private void swapLibrary(){
        boolean useFavorites = favoriteBtn.getText().toString().equals(getString(R.string.favorites_btn_str));
        favoriteBtn.setText(useFavorites ? R.string.all_books_btn_str : R.string.favorites_btn_str);
        adapter.setFilter(useFavorites);

        populateLibrary();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("USE_FAVORITES", favoriteBtn.getText().toString().equals(getString(R.string.favorites_btn_str)));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inState){
        super.onRestoreInstanceState(inState);

    }
}