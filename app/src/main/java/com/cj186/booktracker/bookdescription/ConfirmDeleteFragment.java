package com.cj186.booktracker.bookdescription;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cj186.booktracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This fragment asks the user to confirm the deletion of a book.
 */

public class ConfirmDeleteFragment extends DialogFragment {
    // Listener interface.
    public interface ConfirmationListener{
        void onConfirmed(boolean result);
    }

    // Listener setter.
    private ConfirmationListener listener;
    public void setOnConfirmationListener(ConfirmationListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Return a dialog box with yes and no as options.
        return new MaterialAlertDialogBuilder(requireContext(), R.style.CustomDefaultConfirmationDialog)
                .setTitle("Confirm Book Deletion")
                .setMessage("Are you sure you want to delete this book?")
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
