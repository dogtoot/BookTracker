package com.cj186.booktracker.bookdescription;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cj186.booktracker.R;
import com.cj186.booktracker.model.Book;

public class BookDescriptionActivity extends AppCompatActivity {

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        Intent intent = getIntent();
        book = intent.getParcelableExtra("book_obj");

        Button closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(i -> finish());
    }
}