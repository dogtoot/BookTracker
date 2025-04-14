package com.cj186.booktracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView libraryList;
    TextView emptyTextView;
    LibraryAdapter adapter;
    ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryList = findViewById(R.id.book_library);
        emptyTextView = findViewById(R.id.emptyView);

        libraryList.setLayoutManager(new GridLayoutManager(this, 3));

        populateLibrary(false);
    }

    private void populateLibrary(boolean useFavorites){
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.cover);

        bookList = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            bookList.add(new Book(icon, Status.CURRENTLY_READING));
        }

        adapter = new LibraryAdapter(bookList, useFavorites);
        libraryList.setAdapter(adapter);

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