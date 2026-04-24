package com.cj186.booktracker.sharedfragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.cj186.booktracker.R;

/**
 * Collin J. Johnson
 * 4/21/2026
 * No longer in class.
 * This fragment shows a settings screen.
 */

public class SettingsFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Get our inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Create our about view and set it as our builder's view.
        View view = inflater.inflate(R.layout.settings_fragment, null);
        builder.setView(view);

        CheckBox useDarkModeCheckbox = view.findViewById(R.id.use_darkmode);
        SharedPreferences pref = requireActivity().getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE);
        boolean isDarkMode = pref.getBoolean("USE_DARKMODE", (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);

        useDarkModeCheckbox.setChecked(isDarkMode);
        Button confirmBtn = view.findViewById(R.id.save_btn);

        // Apply changes.
        confirmBtn.setOnClickListener(v -> {
            // Write changes to preferences.
            pref.edit().putBoolean("USE_DARKMODE", useDarkModeCheckbox.isChecked()).apply();

            // Dismiss the fragment.
            dismiss();

            // Apply changes literally.
            AppCompatDelegate.setDefaultNightMode(
                    useDarkModeCheckbox.isChecked() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        // Set functionality for the close button.
        Button closeBtn = view.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(v -> dismiss());
        return builder.create();
    }
}
