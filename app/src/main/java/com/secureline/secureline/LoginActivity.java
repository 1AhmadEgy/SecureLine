package com.secureline.secureline;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.secureline.secureline.security.AuthenticationService;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername;
    private EditText inputPassword;
    private Button btnLogin;
    private TextView btnRegister;
    private AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.input_username);
        inputPassword = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        authService = new AuthenticationService();

        btnLogin.setOnClickListener(v -> performLogin());
        btnRegister.setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, RegisterActivity.class));
        });
    }

    private void performLogin() {
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "أدخل اسم المستخدم وكلمة المرور", Toast.LENGTH_SHORT).show();
            return;
        }

        if (authService.authenticateUser(username, password)) {
            Toast.makeText(this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
            startActivity(new android.content.Intent(this, ContactsActivity.class));
            finish();
        } else {
            Toast.makeText(this, "فشل تسجيل الدخول", Toast.LENGTH_SHORT).show();
        }
    }
}
