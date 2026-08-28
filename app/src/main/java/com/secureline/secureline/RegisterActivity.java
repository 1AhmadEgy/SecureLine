package com.secureline.secureline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.secureline.secureline.security.AuthenticationService;
import com.secureline.secureline.security.IdentityManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputDisplayName;
    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Button btnCreateAccount;
    private TextView btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputDisplayName = findViewById(R.id.input_display_name);
        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);
        btnCreateAccount = findViewById(R.id.btn_create_account);
        btnBackToLogin = findViewById(R.id.btn_back_to_login);

        btnCreateAccount.setOnClickListener(v -> createAccount());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void createAccount() {
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "أدخل اسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "كلمة المرور يجب أن تكون 8 أحرف على الأقل", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "كلمتا المرور غير متطابقتين", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthenticationService authService = new AuthenticationService();
        authService.registerUser(username, password);

        IdentityManager identityManager = new IdentityManager();
        String fingerprint = identityManager.getFingerprint();

        Toast.makeText(this, "تم إنشاء الحساب بنجاح\nبصمتك: " + fingerprint, 
            Toast.LENGTH_LONG).show();

        startActivity(new android.content.Intent(this, LoginActivity.class));
        finish();
    }
}
