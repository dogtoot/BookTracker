package com.cj186.booktracker.addbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.cj186.booktracker.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.Collections;

import android.Manifest;
import android.widget.Toast;

public class ScanISBNFragment extends DialogFragment {
    private CompoundBarcodeView barcodeView;

    // ActivityResultLauncher for getting camera permissions from the user.
    private final ActivityResultLauncher<Intent> LAUNCH_SETTINGS = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), isGranted -> {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        }
    });

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Get our inflater.
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Create our settings view and set it as our builder's view.
        View view = inflater.inflate(R.layout.scan_fragment, null);
        builder.setView(view);

        barcodeView = view.findViewById(R.id.isbn_scanner);

        // Check if permission is granted
        checkCameraPermissionsAndLaunchScan();

        // Super Complicated Cancel Button (not sarcasm).
        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    private void checkCameraPermissionsAndLaunchScan(){
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // If we have camera permissions, we start scanning.
            startScanning();
        }
        else{
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                // If we do not have camera permissions, we ask the user for them.
                if (isGranted) {
                    // If they give us permissions, we start scanning.
                    startScanning();
                }
                else if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)){
                    // If they do not give us permission,
                    // we show them an error then close the dialog.
                    Toast.makeText(requireActivity(), "Permissions to use camera have been denied.", Toast.LENGTH_LONG).show();
                    dismiss();
                }
                else{
                    // If we are permanently denied for camera permissions, we direct user
                    // to settings so they can give us permissions. (We are very aggressive here)
                    showGoToSettingsDialog();
                }
            }).launch(Manifest.permission.CAMERA);
        }
    }

    private void showGoToSettingsDialog() {
        // Create a dialog box that asks the user if they'd like to go to settings.
        new MaterialAlertDialogBuilder(requireActivity(), R.style.CustomDefaultConfirmationDialog)
                .setTitle("Camera Permission Needed")
                .setMessage("Camera permission has been permanently denied. Please go to Settings to allow access.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    // If they do, we call openAppSettings.
                    openAppSettings();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // If they cancel (Rude) we close all dialogs.
                    dialog.dismiss();
                    dismiss();
                })
                .create()
                .show();
    }

    private void openAppSettings() {
        // Open app settings using intents, first we get this activity.
        AddBookActivity addBookActivity = (AddBookActivity) requireActivity();
        // Second we create our intent, using settings.
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        // Third we get the uri for this package.
        Uri uri = Uri.fromParts("package", addBookActivity.getPackageName(), null);
        // Lastly we launch the intent.
        intent.setData(uri);
        LAUNCH_SETTINGS.launch(intent);
    }

    private void startScanning(){
        // Get the activity.
        AddBookActivity addBookActivity = (AddBookActivity) requireActivity();
        // Create a CameraSettings object.
        CameraSettings settings = new CameraSettings();
        // Set our focus settings.
        settings.setAutoFocusEnabled(true);
        settings.setFocusMode(CameraSettings.FocusMode.CONTINUOUS);
        // Set our camera settings.
        barcodeView.setCameraSettings(settings);
        // Allow the user to tap to focus.
        barcodeView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                barcodeView.getBarcodeView().requestFocus();
            }
            return true;
        });

        // Set the barcode decoder to EAN_13 which is most commonly used for ISBN codes.
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(Collections.singleton(BarcodeFormat.EAN_13)));
        // Set the instruction text.
        barcodeView.setStatusText("Scan an ISBN barcode.");
        // Check the permissions.
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        // Start scanning.
        barcodeView.decodeContinuous(result -> {
            // If we find a barcode, we pause the camera.
            barcodeView.pause();
            if((result.getText().length() == 13 &&
                    (result.getText().startsWith("978") ||
                    result.getText().startsWith("979"))) ||
                    result.getText().length() == 10){
                // If its likely to be a valid ISBN, we pass it to populateBook.
                addBookActivity.populateBook(result.getText());
            }
            else{
                // If it is most likely not an ISBN, we show an error.
                Toast.makeText(addBookActivity, "Unable to retrieve ISBN.", Toast.LENGTH_LONG).show();
            }
            dismiss();
        });
    }

    @Override
    public void onResume() {
        barcodeView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        barcodeView.pause();
        super.onPause();
    }
}
