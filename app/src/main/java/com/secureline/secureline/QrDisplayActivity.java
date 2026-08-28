package com.secureline.secureline;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.secureline.secureline.security.IdentityManager;

public class QrDisplayActivity extends AppCompatActivity {

    private ImageView qrCodeImage;
    private TextView qrFingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_display);

        qrCodeImage = findViewById(R.id.qr_code_image);
        qrFingerprint = findViewById(R.id.qr_fingerprint);

        IdentityManager identityManager = new IdentityManager();
        String identityId = identityManager.getIdentityId();
        String fingerprint = identityManager.getFingerprint();

        qrFingerprint.setText("البصمة: " + fingerprint);

        if (identityId != null) {
            Bitmap qrBitmap = generateQrCode(identityId);
            if (qrBitmap != null) {
                qrCodeImage.setImageBitmap(qrBitmap);
            }
        }
    }

    private Bitmap generateQrCode(String data) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512);
            Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565);
            for (int x = 0; x < 512; x++) {
                for (int y = 0; y < 512; y++) {
                    bitmap.setPixel(x, y, matrix.get(x, y) ? 
                        android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            return null;
        }
    }
}
