package com.cj186.booktracker.bookdescription;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cj186.booktracker.R;
import com.cj186.booktracker.database.SQLHandler;
import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.model.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookDescriptionActivity extends AppCompatActivity {

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        Intent intent = getIntent();
        book = intent.getParcelableExtra("book_obj");

        ImageView cover = findViewById(R.id.book_cover);
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        TextView isbn = findViewById(R.id.isbn);
        Spinner status = findViewById(R.id.status_dropdown);
        TextView publishYear = findViewById(R.id.publish_year);
        TextView description = findViewById(R.id.description);

        // Add our statuses to our dropdown menu.
        String[] items = {Status.COMPLETED.getLabel(), Status.REREADING.getLabel(), Status.PLANNING_TO_READ.getLabel(), Status.CURRENTLY_READING.getLabel()};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        status.setAdapter(adapter);
        status.setSelection(adapter.getPosition(book.getStatus().getLabel()));

        cover.setImageBitmap(BitmapFactory.decodeByteArray(book.getImageBytes(), 0,book.getImageBytes().length));
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        isbn.setText((!book.getISBN().isEmpty()) ? book.getISBN() : "No ISBN Provided.");
        publishYear.setText(getString(R.string.publish_year_s, (!book.getYearPublished().isEmpty()) ? book.getYearPublished() : "N/A"));
        description.setText((!book.getDescription().isEmpty()) ? book.getDescription() : "");

        Button closeBtn = findViewById(R.id.close_btn);
        ImageButton deleteBtn = findViewById(R.id.remove_book_btn);
        deleteBtn.setOnClickListener(v -> {
            // Check if user is certain
            SQLHandler.removeBook(book.getId());
            finish();
        });

        closeBtn.setOnClickListener(i -> {
            if(!status.getSelectedItem().equals(book.getStatus().getLabel()))
                SQLHandler.updateBook(book.getId(), Status.fromLabel(status.getSelectedItem().toString()));
            //if(!status.getSelectedItem().equals(book.getStatus().getLabel()))
            //    SQLHandler.updateBook(book.getId(), book.isFavorite());
            finish();
        });
    }
}