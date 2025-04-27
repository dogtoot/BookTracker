package com.cj186.booktracker.bookdescription;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cj186.booktracker.R;

public class ConfirmDeleteFragment extends DialogFragment {
    public interface ConfirmationListener{
        void onConfirmed(boolean result);
    }

    private ConfirmationListener listener;
    public void setOnConfirmationListener(ConfirmationListener listener){
        this.listener = listener;
    }

    private final String bookName;
    public ConfirmDeleteFragment(String bookName){
        this.bookName = bookName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext(), R.style.CustomDefaultConfirmationDialog)
                .setTitle("Confirm Book Deletion")
                .setMessage("Are you sure you want to remove " + bookName)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirmed(true);
                    }
                })
                .setNegativeButton("No", (dialog, which) -> {
                    if (listener != null) {
                        listener.onConfirmed(false);
                    }
                })
                .create();
    }
}
