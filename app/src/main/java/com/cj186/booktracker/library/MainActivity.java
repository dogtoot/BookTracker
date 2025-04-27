package com.cj186.booktracker.library;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cj186.booktracker.BaseActivity;
import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.database.DBHelper;
import com.cj186.booktracker.R;
import com.cj186.booktracker.database.SQLHandler;
import com.cj186.booktracker.model.Status;
import com.cj186.booktracker.addbook.AddBookActivity;

import java.util.ArrayList;

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
        libraryList.setLayoutManager(new GridLayoutManager(this, 3));
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
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.getColumnId()));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.getColumnImageBlob()));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.getColumnTitle()));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.getColumnAuthor()));
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.getColumnIsbn()));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.getColumnDescription()));
                Status status = Status.fromLabel(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.getColumnStatus())));
                String yearPublished = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.getColumnYearPublished()));
                boolean favoriteStatus = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.getColumnFavoriteStatus())) != 0;

                Book book = new Book(id, imageBytes, title, author, description, status, yearPublished, isbn, favoriteStatus);
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
}