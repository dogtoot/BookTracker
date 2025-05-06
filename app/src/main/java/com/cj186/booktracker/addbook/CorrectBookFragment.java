package com.cj186.booktracker.addbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cj186.booktracker.model.Book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cj186.booktracker.R;
import com.cj186.booktracker.model.BookViewModel;

public class CorrectBookFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AddBookActivity addBookActivity = (AddBookActivity) requireActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(addBookActivity, R.style.AlertDialogCustom);
        // Get our inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Create our settings view and set it as our builder's view.
        View view = inflater.inflate(R.layout.is_correct_book_fragment, null);

        // Get our book.
        BookViewModel bookViewModel = new ViewModelProvider(addBookActivity).get(BookViewModel.class);
        Book book = bookViewModel.getIntermediateBook().getValue();

        // Populate our buttons
        Button no = view.findViewById(R.id.no);
        Button yes = view.findViewById(R.id.yes);

        // Populate our book specific views.
        ImageView cover = view.findViewById(R.id.book_cover);
        TextView title = view.findViewById(R.id.title);
        TextView author = view.findViewById(R.id.author);

        if(book == null){
            // If we don't have a book, dismiss the view.
            Bundle result = new Bundle();
            result.putBoolean("isCorrect", false);
            getParentFragmentManager().setFragmentResult("correct_book_result", result);
            dismiss();
        }
        else{
            // Else we populate all fields with our book's data.
            cover.setImageBitmap(BitmapFactory.decodeByteArray(book.getImageBytes(), 0, book.getImageBytes().length));
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
        }

        no.setOnClickListener(v -> {
            // If "No" is clicked, we set false as our result and dismiss the dialog.
            Bundle result = new Bundle();
            result.putBoolean("isCorrect", false);
            getParentFragmentManager().setFragmentResult("correct_book_result", result);
            dismiss();
        });

        yes.setOnClickListener(v -> {
            // If "Yes" is clicked, we set true as our result and dismiss the dialog.
            Bundle result = new Bundle();
            result.putBoolean("isCorrect", true);
            getParentFragmentManager().setFragmentResult("correct_book_result", result);
            dismiss();
        });

        // Return the builder.
        builder.setView(view);
        return builder.create();
    }
}
