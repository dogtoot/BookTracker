package com.cj186.booktracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    RecyclerView libraryList;
    TextView emptyTextView;
    LibraryAdapter adapter;
    ArrayList<Book> bookList;

    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        libraryList = findViewById(R.id.book_library);
        emptyTextView = findViewById(R.id.emptyView);

        libraryList.setLayoutManager(new GridLayoutManager(this, 3));

        populateLibrary(false);

        Button addBook = findViewById(R.id.add_book_btn);
        addBook.setOnClickListener(this::startAddBookActivity);
    }

    private void startAddBookActivity(View view){
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    private void populateLibrary(boolean useFavorites){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cover);


        // Convert Bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byte[] icon = byteArrayOutputStream.toByteArray();
        try{
            //icon = apiHandler.getImage("covers.openlibrary.org/b/id/8745635-L.jpg");
            bookList = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                bookList.add(new Book(icon, Status.CURRENTLY_READING));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        adapter = new LibraryAdapter(this, bookList, useFavorites);
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