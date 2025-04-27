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
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.Collections;

import android.Manifest;
import android.widget.Toast;

public class ScanISBNFragment extends DialogFragment {
    private CompoundBarcodeView barcodeView;

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

        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> dismiss());
        return builder.create();
    }

    private void checkCameraPermissionsAndLaunchScan(){
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        }
        else{
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startScanning();
                }
                else if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)){
                    Toast.makeText(requireActivity(), "Permissions to use camera have been denied.", Toast.LENGTH_LONG).show();
                    dismiss();
                }
                else{
                    showGoToSettingsDialog();
                }
            }).launch(Manifest.permission.CAMERA);
        }
    }

    private void showGoToSettingsDialog() {
        new AlertDialog.Builder(requireActivity(), R.style.CustomDefaultConfirmationDialog)
                .setTitle("Camera Permission Needed")
                .setMessage("Camera permission has been permanently denied. Please go to Settings to allow access.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    openAppSettings();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    dismiss();
                })
                .create()
                .show();
    }

    private void openAppSettings() {
        AddBookActivity addBookActivity = (AddBookActivity) requireActivity();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", addBookActivity.getPackageName(), null);
        intent.setData(uri);
        LAUNCH_SETTINGS.launch(intent);
    }

    private void startScanning(){
        AddBookActivity addBookActivity = (AddBookActivity) requireActivity();
        CameraSettings settings = new CameraSettings();
        settings.setAutoFocusEnabled(true);
        settings.setFocusMode(CameraSettings.FocusMode.AUTO);
        barcodeView.setCameraSettings(settings);
        barcodeView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                barcodeView.getBarcodeView().requestFocus();
            }
            return true;
        });
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(Collections.singleton(BarcodeFormat.EAN_13)));
        barcodeView.setStatusText("Scan an ISBN barcode.");
        ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        barcodeView.decodeContinuous(result -> {
            barcodeView.pause();
            if((result.getText().length() == 13 &&
                    (result.getText().startsWith("978") ||
                    result.getText().startsWith("979"))) ||
                    result.getText().length() == 10){
                addBookActivity.populateBook(result.getText());
            }
            else{
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
