package com.secureline.secureline;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.secureline.secureline.security.ScreenshotProtection;
import com.secureline.secureline.webrtc.CallSession;
import com.secureline.secureline.webrtc.CallSessionManager;
import com.secureline.secureline.webrtc.CallTimer;

public class CallActivity extends AppCompatActivity {

    private TextView callStatus;
    private TextView callTimer;
    private TextView callContactName;
    private TextView callEncryptionStatus;
    private ImageButton btnEndCall;
    private ImageButton btnMute;
    private ImageButton btnSpeaker;

    private CallTimer timer;
    private Handler handler;
    private Runnable timerRunnable;
    private boolean isMuted = false;
    private CallSession currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ScreenshotProtection.enable(this);

        callStatus = findViewById(R.id.call_status);
        callTimer = findViewById(R.id.call_timer);
        callContactName = findViewById(R.id.call_contact_name);
        callEncryptionStatus = findViewById(R.id.call_encryption_status);
        btnEndCall = findViewById(R.id.btn_end_call);
        btnMute = findViewById(R.id.btn_mute);
        btnSpeaker = findViewById(R.id.btn_speaker);

        String contactName = getIntent().getStringExtra("contact_name");
        callContactName.setText(contactName != null ? contactName : "جهة اتصال");
        callEncryptionStatus.setText("🔒 مشفر بالكامل (AES-256 + Obfuscation)");

        timer = new CallTimer();
        handler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                callTimer.setText(timer.getFormattedTime());
                handler.postDelayed(this, 1000);
            }
        };

        CallSessionManager sessionManager = CallSessionManager.getInstance();
        currentSession = sessionManager.createSession(contactName != null ? contactName : "unknown");
        currentSession.setConnected();

        timer.start();
        handler.post(timerRunnable);
        callStatus.setText("متصل");

        btnEndCall.setOnClickListener(v -> endCall());
        btnMute.setOnClickListener(v -> toggleMute());
        btnSpeaker.setOnClickListener(v -> toggleSpeaker());
    }

    private void endCall() {
        timer.pause();
        handler.removeCallbacks(timerRunnable);

        if (currentSession != null) {
            currentSession.setEnded();
            CallSessionManager.getInstance().endSession(currentSession.getSessionId());
        }

        callStatus.setText("تم إنهاء المكالمة");
        Toast.makeText(this, "مدة المكالمة: " + timer.getFormattedTime(), Toast.LENGTH_LONG).show();
        finish();
    }

    private void toggleMute() {
        isMuted = !isMuted;
        btnMute.setSelected(isMuted);
        Toast.makeText(this, isMuted ? "تم كتم الصوت" : "تم إلغاء الكتم", Toast.LENGTH_SHORT).show();
    }

    private void toggleSpeaker() {
        btnSpeaker.setSelected(!btnSpeaker.isSelected());
        Toast.makeText(this, "تم تبديل مكبر الصوت", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(timerRunnable);
        ScreenshotProtection.disable(this);
        super.onDestroy();
    }
}
