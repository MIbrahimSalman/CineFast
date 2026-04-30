package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "cinefast_session_pref_v3";

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check existing session — skip login if already logged in
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean("logged_in", false)) {
            goToMain();
            return;
        }

        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Forgot password (no-op / Toast for now)
        findViewById(R.id.tvForgotPassword).setOnClickListener(v ->
                Toast.makeText(this, "Forgot password not implemented", Toast.LENGTH_SHORT).show());

        // Register link
        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

        // Login button
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    // Save session if Remember Me is checked
                    if (cbRememberMe.isChecked()) {
                        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                                .edit()
                                .putBoolean("logged_in", true)
                                .putString("uid", uid)
                                .apply();
                    }
                    goToMain();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void goToMain() {
        Intent intent = new Intent(this, DrawerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
