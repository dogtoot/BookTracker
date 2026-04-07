package com.cj186.booktracker.addbook;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.cj186.booktracker.BaseActivity;
import com.cj186.booktracker.model.BookViewModel;
import com.cj186.booktracker.network.APIHandler;
import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.R;
import com.cj186.booktracker.database.SQLHandler;
import com.cj186.booktracker.model.Status;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This activity allows the user to add books.
 */

public class AddBookActivity extends BaseActivity {
    // Image picker
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    // Fields for the user to fill.
    private Spinner statusDropDown;
    private ImageView cover;
    private EditText bookTitle;
    private EditText ISBN;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText yearPublished;
    private CheckBox favoriteStatus;

    private Button cancelBtn;
    private Button addBookBtn;

    private Button scanISBNBtn;
    private Button inputISBNBtn;

    private final String[] spinnerItems = {
            Status.COMPLETED.getLabel(),
            Status.REREADING.getLabel(),
            Status.PLANNING_TO_READ.getLabel(),
            Status.CURRENTLY_READING.getLabel()
    };

    private Book intermediateBook;
    private BookViewModel bookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view based up orientation.
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            setContentView(R.layout.activity_add_book);
        else
            setContentView(R.layout.activity_add_book_landscape);

        // Populate our views.
        populateViews();

        // Add a listener to our addbook button.
        addBookBtn.setOnClickListener(view -> addBook());
        cancelBtn.setOnClickListener(view -> finish());

        // Get the bookViewModel.
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        getSupportFragmentManager().setFragmentResultListener("correct_book_result", this, (requestKey, result) -> {
            // Populate fields if the book is correct, using bookViewModel.
            boolean isCorrect = result.getBoolean("isCorrect");
            if (isCorrect) {
                fillBookFields(bookViewModel.getIntermediateBook().getValue());
            }
        });

        scanISBNBtn.setOnClickListener(view -> {
            // Open the scan fragment.
            ScanISBNFragment dialog = new ScanISBNFragment();
            dialog.show(getSupportFragmentManager(), "ScanISBN");
        });

        inputISBNBtn.setOnClickListener(view ->{
            // Open the manual input fragment.
            EnterISBNFragment dialog = new EnterISBNFragment();
            dialog.show(getSupportFragmentManager(), "EnterISBN");
        });

        // Register the image picker for when the image view is pressed.
        registerImagePicker();

        // Add our statuses to our dropdown menu.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        statusDropDown.setAdapter(adapter);
    }

    private void addBook(){
        // Make sure required fields are filled.
        if(validateFields())
            return;
        // Insert a book using the values we got from the user.
        SQLHandler.insertBook(
                imageViewToByteArray(cover),
                bookTitle.getText().toString(),
                bookAuthor.getText().toString(),
                bookDescription.getText().toString(),
                Status.fromLabel(statusDropDown.getSelectedItem().toString()),
                yearPublished.getText().toString(),
                ISBN.getText().toString(),
                favoriteStatus.isChecked()
        );
        finish();
    }

    private boolean validateFields(){
        // Make sure all required fields are filled, if not, return the true and show errors.
        boolean errorRaised = false;
        if(bookTitle.getText().length() < 2){
            bookTitle.setError("Book Title is a required field.");
            errorRaised = true;
        }
        if(bookAuthor.getText().length() < 2){
            bookAuthor.setError("Author is a required field.");
            errorRaised = true;
        }
        if(cover.getDrawable() == null){
            Toast.makeText(this, "Unable to add book, no cover provided.", Toast.LENGTH_LONG).show();
            errorRaised = true;
        }
        return errorRaised;
    }

    protected void populateBook(String isbn){
        // Open a dialog to show that something is happening.
        AlertDialog progressDialog = new AlertDialog.Builder(this)
                .setMessage("Loading book, please wait...")
                .setCancelable(false)
                .create();

        progressDialog.show();

        try{
            // Start a new thread because android doesn't
            // allow network operations on the main thread.
            new Thread(() -> {
                // Get our intermediate book from APIHandler.
                intermediateBook = APIHandler.getBookFromISBN(isbn);
                // Create a new handler.
                new Handler(Looper.getMainLooper()).post(() -> {
                    // Close our loading screen.
                    progressDialog.dismiss();
                    if(intermediateBook != null) {
                        // If we have a book, bookViewModel's intermediate book to intermediateBook.
                        bookViewModel.setIntermediateBook(intermediateBook);
                        // Finally we ask the user if intermediateBook is the correct book.
                        askUserIfCorrectBook();
                    }
                    else{
                        // If we can't get the book, we show an error.
                        Toast.makeText(this, "Unable to get book.", Toast.LENGTH_LONG).show();
                    }
                });
            }).start();
        }
        catch (Exception e){
            // If an error is thrown, we show an error.
            Toast.makeText(this, "Unable to get book.", Toast.LENGTH_LONG).show();
        }
    }

    private void askUserIfCorrectBook(){
        // Create a new correctBookFragment and show it.
        CorrectBookFragment correctBookFragment = new CorrectBookFragment();
        correctBookFragment.show(getSupportFragmentManager(), "isCorrect");
    }

    private void fillBookFields(Book openLibraryBook){
        // Populate all fields in add book.
        int indexOfStatus = 0;
        for(int i = 0; i < spinnerItems.length; i++){
            if(spinnerItems[i].equals(openLibraryBook.getStatus().getLabel())){
                indexOfStatus = i;
                break;
            }
        }
        statusDropDown.setSelection(indexOfStatus);
        bookTitle.setText(openLibraryBook.getTitle());
        bookAuthor.setText(openLibraryBook.getAuthor());
        bookDescription.setText(openLibraryBook.getDescription());
        ISBN.setText(openLibraryBook.getISBN());
        yearPublished.setText(openLibraryBook.getYearPublished());
        favoriteStatus.setChecked(openLibraryBook.isFavorite());
        byte[] imageBytes = openLibraryBook.getImageBytes();
        if (imageBytes != null && imageBytes.length > 0) {
            // If we do not create a new thread,
            // things can get very laggy when attempting to decode the bitmap.
            new Thread(() -> {
                // Decode imageBytes and set cover to the bitmap returned.
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                new Handler(Looper.getMainLooper()).post(() -> cover.setImageBitmap(bitmap));
            }).start();
        }
    }

    private static byte[] imageViewToByteArray(ImageView view){
        // Get the image as a drawable.
        Drawable drawable = view.getDrawable();
        if(drawable == null)
            return new byte[0];

        // Turn the drawable into a bitmap.
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // Compress the bitmap into a byte array.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    private void populateViews(){
        // Populate all our variables for our views.
        statusDropDown = findViewById(R.id.status_dropdown);
        cover = findViewById(R.id.book_cover);
        bookTitle = findViewById(R.id.book_title);
        ISBN = findViewById(R.id.book_isbn);
        bookAuthor = findViewById(R.id.book_author);
        bookDescription = findViewById(R.id.book_description);
        yearPublished = findViewById(R.id.book_year_published);
        cancelBtn = findViewById(R.id.cancel_btn);
        addBookBtn = findViewById(R.id.add_btn);
        scanISBNBtn = findViewById(R.id.scan_btn);
        inputISBNBtn = findViewById(R.id.enter_isbn);
        favoriteStatus = findViewById(R.id.favorite_checkbox);
    }

    private void registerImagePicker() {
        // Create our imagePickerLauncher.
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // If we have a result, get the result and set the image view's image to it.
                        Uri imageUri = result.getData().getData();
                        cover.setImageURI(imageUri);
                    }
                }
        );

        cover.setOnClickListener(v -> {
            // Launch the image picker, and allow the user to select images.
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Cache the cover for orientation change.
        if(cover.getDrawable() != null){
            // Create a new file using the cache directory called cover.jpg.
            File coverCacheFile = new File(getCacheDir(), "cover.jpg");
            try(FileOutputStream out = new FileOutputStream(coverCacheFile)){
                // Save the file to the cache, then write the path to the outState.
                Bitmap bitmap = ((BitmapDrawable) cover.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                outState.putString("COVER_CACHE_FILE", coverCacheFile.getAbsolutePath());
            }
            catch (IOException e){
                Toast.makeText(this, "Unable to cache image during orientation.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inState){
        super.onRestoreInstanceState(inState);
        // Load the cover path from cache.
        String path = inState.getString("COVER_CACHE_FILE");
        if (path != null) {
            // Load the file from the path and set the cover.
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            cover.setImageBitmap(bitmap);
            // Delete the file now that we are done with it.
            new File(path).delete();
        }
    }
}