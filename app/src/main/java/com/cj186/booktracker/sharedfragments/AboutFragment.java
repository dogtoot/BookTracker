package com.cj186.booktracker.sharedfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cj186.booktracker.R;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This fragment shows an about screen.
 */

public class AboutFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Get our inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Create our about view and set it as our builder's view.
        View view = inflater.inflate(R.layout.about_fragment, null);
        builder.setView(view);

        // Set functionality for the close button.
        Button closeBtn = view.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v -> dismiss());
        return builder.create();
    }
}
