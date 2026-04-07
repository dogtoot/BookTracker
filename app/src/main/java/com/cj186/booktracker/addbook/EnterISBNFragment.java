package com.cj186.booktracker.addbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cj186.booktracker.R;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This fragment allows the user to manually enter an ISBN
 */

public class EnterISBNFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Get our inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Create our isbn fragment view and set it as our builder's view.
        View view = inflater.inflate(R.layout.manual_enter_fragment, null);
        builder.setView(view);

        // Populate our add and cancel buttons.
        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        // Populate our ISBN field.
        EditText isbnField = view.findViewById(R.id.isbn);
        // Set our addBookActivity.
        AddBookActivity addBookActivity = (AddBookActivity) requireActivity();

        // On click listener for cancel just dismisses the dialog.
        cancel.setOnClickListener(i -> dismiss());

        add.setOnClickListener(i ->{
            if(isbnField.getText().length() == 13 || isbnField.getText().length() == 10){
                // If the ISBN is 10 or 13 characters long,
                // we call populateBook with the ISBN field's text.
                // Then we dismiss the dialog.
                addBookActivity.populateBook(String.valueOf(isbnField.getText()));
                dismiss();
            }
            else{
                // If the ISBN is not the correct length, we show the user an error.
                Toast.makeText(addBookActivity, "Please enter a valid ISBN-10 or ISBN-13 code.", Toast.LENGTH_LONG).show();
            }
        });

        return builder.create();
    }
}
