package com.cj186.booktracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView libraryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryList = findViewById(R.id.book_library);
        libraryList.setLayoutManager(new GridLayoutManager(this, 3));

        LibraryAdapter adapter;
        ArrayList<Book> bookList;

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.cover);

        bookList = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            bookList.add(new Book(icon, Status.CURRENTLY_READING));
        }

        adapter = new LibraryAdapter(bookList, libraryList);
        libraryList.setAdapter(adapter);
    }
}