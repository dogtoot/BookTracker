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

public class EnterISBNFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Get our inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Create our settings view and set it as our builder's view.
        View view = inflater.inflate(R.layout.manual_enter_fragment, null);
        builder.setView(view);

        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);

        EditText isbnField = view.findViewById(R.id.isbn);

        AddBookActivity addBookActivity = (AddBookActivity) requireActivity();

        cancel.setOnClickListener(i -> dismiss());
        add.setOnClickListener(i ->{
            if(isbnField.getText().length() == 13 || isbnField.getText().length() == 10){
                addBookActivity.populateBook(String.valueOf(isbnField.getText()));
                dismiss();
            }
            else{
                Toast.makeText(addBookActivity, "Please enter a valid IBSN-10 or ISBN-13 code.", Toast.LENGTH_LONG).show();
            }
        });

        return builder.create();
    }
}
