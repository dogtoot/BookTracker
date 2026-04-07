package com.cj186.booktracker.bookdescription;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cj186.booktracker.BaseActivity;
import com.cj186.booktracker.R;
import com.cj186.booktracker.database.SQLHandler;
import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.model.Status;
import com.cj186.booktracker.utils.MediaHandler;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This activity shows a description for the book selected in the main library.
 */

public class BookDescriptionActivity extends BaseActivity {

    private Book book;
    private ImageButton favoriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view based up orientation.
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_book_description);
        else
            setContentView(R.layout.activity_book_description_landscape);

        // Get the book's id from intent, then pull the book from our database.
        Intent intent = getIntent();
        int id = intent.getIntExtra("book_obj", -1);
        if(id == -1)
            finish();
        Cursor dbResult = SQLHandler.getBookById(id);

        // Create our book.
        if(dbResult.moveToFirst())
            book = new Book(dbResult);
        boolean storedFavoriteValue = book.isFavorite();

        // Populate our views.
        ImageView cover = findViewById(R.id.book_cover);
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        TextView isbn = findViewById(R.id.isbn);
        Spinner status = findViewById(R.id.status_dropdown);
        TextView publishYear = findViewById(R.id.publish_year);
        TextView description = findViewById(R.id.description);

        // Add our statuses to our dropdown menu.
        String[] items = {Status.COMPLETED.getLabel(), Status.REREADING.getLabel(), Status.PLANNING_TO_READ.getLabel(), Status.CURRENTLY_READING.getLabel()};
        // Populate our spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        status.setAdapter(adapter);
        status.setSelection(adapter.getPosition(book.getStatus().getLabel()));

        // Set the views with our book's data.
        cover.setImageBitmap(BitmapFactory.decodeByteArray(book.getImageBytes(), 0,book.getImageBytes().length));
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        isbn.setText((!book.getISBN().isEmpty()) ? getString(R.string.isbn_s, book.getISBN()) : "No ISBN Provided.");
        publishYear.setText(getString(R.string.publish_year_s, (!book.getYearPublished().isEmpty()) ? book.getYearPublished() : "N/A"));
        description.setText((!book.getDescription().isEmpty()) ? book.getDescription() : "");

        // Populate the close button, delete button, and favorite button.
        Button closeBtn = findViewById(R.id.close_btn);
        ImageButton deleteBtn = findViewById(R.id.remove_book_btn);
        favoriteBtn = findViewById(R.id.favorite_btn);
        deleteBtn.setOnClickListener(v -> {
            // On Click listener asks the user if they're sure,
            // then removes the book from the database.
            ConfirmDeleteFragment confirmDeleteFragment = new ConfirmDeleteFragment();
            confirmDeleteFragment.setOnConfirmationListener(result ->{
                if(result){
                    SQLHandler.removeBook(book.getId());
                    finish();
                }
            });
            confirmDeleteFragment.show(getSupportFragmentManager(), "confirmDialog");
        });

        closeBtn.setOnClickListener(i -> {
            // Closes the activity and saves any changed data.
            if(!status.getSelectedItem().equals(book.getStatus().getLabel()))
                SQLHandler.updateBook(book.getId(), Status.fromLabel(status.getSelectedItem().toString()));
            if(!book.isFavorite() == storedFavoriteValue)
                SQLHandler.updateBook(book.getId(), book.isFavorite());
            finish();
        });

        flipCheckbox();
    }

    private void flipCheckbox(){
        // Change the image button for favorites.
        if(book.isFavorite())
            favoriteBtn.setImageResource(R.drawable.filled_star);
        else
            favoriteBtn.setImageResource(R.drawable.unfilled_star);

        favoriteBtn.setOnClickListener(i ->{
            // Play a sound and change the image button's icon.
            book.setFavoriteStatus(!book.isFavorite());
            MediaHandler.playSfx(this, R.raw.pop);
            if(book.isFavorite())
                favoriteBtn.setImageResource(R.drawable.filled_star);
            else
                favoriteBtn.setImageResource(R.drawable.unfilled_star);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Put our book's favorite status.
        outState.putBoolean("FAVORITE_VALUE", book.isFavorite());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inState){
        super.onRestoreInstanceState(inState);
        // Get our boolean and set our checkbox.
        book.setFavoriteStatus(inState.getBoolean("FAVORITE_VALUE"));
        flipCheckbox();
    }
}