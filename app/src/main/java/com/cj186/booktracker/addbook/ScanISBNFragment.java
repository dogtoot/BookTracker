package com.cj186.booktracker.addbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.cj186.booktracker.R;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Collections;

import android.Manifest;
import android.widget.Toast;

public class ScanISBNFragment extends DialogFragment {
    private CompoundBarcodeView barcodeView;

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
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startScanning();
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }

        Button cancelBtn = view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(v -> dismiss());
        return builder.create();
    }

    private void startScanning(){
        AddBookActivity bookActivity = (AddBookActivity) requireActivity();
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(Collections.singleton(BarcodeFormat.EAN_13)));
        barcodeView.setStatusText("Scan an ISBN barcode.");
        barcodeView.decodeContinuous(result -> {
            barcodeView.pause();
            if(result.getText().length() == 13)
                bookActivity.getBookFromAPI(result.getText());
            else
                Toast.makeText(bookActivity, "Unable to retrieve ISBN.", Toast.LENGTH_LONG).show();
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
