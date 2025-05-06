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
 * Welcome to Book Tracker, this is the first activity shown when the app is run.
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
        // Initiate SQLHandler.
        SQLHandler.init(this);
        // Populate our views and load our books.
        populateViews();
        loadBooks();

        // Set up our Library adapter.
        adapter = new LibraryAdapter(this, bookList);
        libraryList.setLayoutManager(
                new GridLayoutManager(
                        this,
                        getResources().getConfiguration().orientation
                                // If we are landscape, our adapter is 6 books wide,
                                // if we are portrait, it is 3 books wide.
                                == Configuration.ORIENTATION_LANDSCAPE ? 6 : 3));
        // Set the adapter of our libraryList.
        libraryList.setAdapter(adapter);

        // Populate our library.
        populateLibrary();

        // Populate our buttons.
        favoriteBtn.setOnClickListener(view -> swapLibrary());
        addBook.setOnClickListener(view -> startAddBookActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load books, update books, and populate library.
        loadBooks();
        adapter.updateBooks(bookList);
        populateLibrary();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close our SQLHandler.
        SQLHandler.close();
    }

    private void startAddBookActivity(){
        // Start the add book activity using an intent.
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    private void populateViews(){
        // Populate our views.
        libraryList = findViewById(R.id.book_library);
        emptyTextView = findViewById(R.id.emptyView);
        favoriteBtn = findViewById(R.id.favorites_btn);
        addBook = findViewById(R.id.add_book_btn);
    }

    private void loadBooks(){
        // Get the cursor from SQLHandler for all books.
        Cursor cursor = SQLHandler.getAllBooks();
        bookList = new ArrayList<>();
        // Loop through all rows, and add them as a book to bookList.
        if (cursor.moveToFirst()) {
            do {
                // Create a book using the cursor, then add the book to bookList.
                Book book = new Book(cursor);
                bookList.add(book);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
    }

    private void populateLibrary(){
        // Set the adapter's filter to the opposite of useFavorites.
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
        // Set the adapter's filter to useFavorites.
        boolean useFavorites = favoriteBtn.getText().toString().equals(getString(R.string.favorites_btn_str));
        favoriteBtn.setText(useFavorites ? R.string.all_books_btn_str : R.string.favorites_btn_str);
        adapter.setFilter(useFavorites);

        // Populate our library.
        populateLibrary();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save useFavorites for orientation change.
        outState.putBoolean("USE_FAVORITES", favoriteBtn.getText().toString().equals(getString(R.string.favorites_btn_str)));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inState){
        super.onRestoreInstanceState(inState);
        // Load USE_FAVORITES
        favoriteBtn.setText(inState.getBoolean("USE_FAVORITES") ? R.string.favorites_btn_str : R.string.all_books_btn_str);
    }
}