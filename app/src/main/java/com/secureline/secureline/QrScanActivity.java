package com.secureline.secureline;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QrScanActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private TextView qrStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);

        barcodeView = findViewById(R.id.qr_camera_preview);
        qrStatus = findViewById(R.id.qr_status);

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result.getText() != null) {
                    handleQrResult(result.getText());
                }
            }

            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
                // Not needed
            }
        });
    }

    private void handleQrResult(String qrData) {
        qrStatus.setText("تم العثور على جهة اتصال!");
        Toast.makeText(this, "تم مسح الرمز بنجاح", Toast.LENGTH_SHORT).show();

        android.content.Intent resultIntent = new android.content.Intent();
        resultIntent.putExtra("contact_data", qrData);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}
