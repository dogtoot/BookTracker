package com.cj186.booktracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Spinner statusDropDown;
    private ImageView cover;
    private EditText bookTitle;
    private EditText ISBN;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText yearPublished;

    private Button cancelBtn;
    private Button addBookBtn;

    private Button scanISBNBtn;
    private Button inputISBNBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        populateViews();

        addBookBtn.setOnClickListener(this::addBook);

        String[] items = {Status.COMPLETED.getLabel(), Status.REREADING.getLabel(), Status.PLANNING_TO_READ.getLabel(), Status.CURRENTLY_READING.getLabel()};
        registerImagePicker();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

        statusDropDown.setAdapter(adapter);
    }

    private void addBook(View view){

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
    }

    private void registerImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        cover.setImageURI(imageUri);
                    }
                }
        );

        cover.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
    }
}