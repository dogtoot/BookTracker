package com.cj186.booktracker.addbook;

import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cj186.booktracker.network.APIHandler;
import com.cj186.booktracker.model.Book;
import com.cj186.booktracker.R;
import com.cj186.booktracker.database.SQLHandler;
import com.cj186.booktracker.model.Status;

import java.io.ByteArrayOutputStream;

public class AddBookActivity extends AppCompatActivity {
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

    private final String[] items = {Status.COMPLETED.getLabel(), Status.REREADING.getLabel(), Status.PLANNING_TO_READ.getLabel(), Status.CURRENTLY_READING.getLabel()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Populate our views.
        populateViews();

        // Add a listener to our addbook button.
        addBookBtn.setOnClickListener(view -> addBook());
        cancelBtn.setOnClickListener(view -> finish());

        scanISBNBtn.setOnClickListener(view -> {
            ScanISBNFragment dialog = new ScanISBNFragment();
            dialog.show(getSupportFragmentManager(), "ScanISBN");
        });

        inputISBNBtn.setOnClickListener(view ->{
            EnterISBNFragment dialog = new EnterISBNFragment();
            dialog.show(getSupportFragmentManager(), "EnterISBN");
        });

        // Register the image picker for when the image view is pressed.
        registerImagePicker();

        // Add our statuses to our dropdown menu.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        statusDropDown.setAdapter(adapter);
    }

    private void addBook(){
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

    protected void populateBook(String isbn){
        new Thread(() -> {
            Book openLibraryBook = APIHandler.getBookFromISBN(isbn);
            new Handler(Looper.getMainLooper()).post(() -> {
                if(openLibraryBook != null) {
                    askUserIfCorrectBook(openLibraryBook);
                }
                else{
                    Toast.makeText(this, "Unable to get book.", Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    private void askUserIfCorrectBook(Book book){
        CorrectBookFragment correctBookFragment = new CorrectBookFragment(book);
        correctBookFragment.show(getSupportFragmentManager(), "isCorrect");
        getSupportFragmentManager().setFragmentResultListener("correct_book_result", this, (requestKey, result) -> {
            boolean isCorrect = result.getBoolean("isCorrect");
            if (isCorrect) {
                fillBookFields(book);
            }
        });

    }

    private void fillBookFields(Book openLibraryBook){
        int indexOfStatus = 0;
        for(int i = 0; i < items.length; i++){
            if(items[i].equals(openLibraryBook.getStatus().getLabel())){
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
            new Thread(() -> {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                new Handler(Looper.getMainLooper()).post(() -> {
                    cover.setImageBitmap(bitmap);
                });
            }).start();
        }
    }

    private static byte[] imageViewToByteArray(ImageView view){
        // Convert the image view to a byte array at
        // 40% quality to ensure it can be passed through intents.
        Drawable drawable = view.getDrawable();

        if(drawable == null)
            return new byte[0];

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
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
}