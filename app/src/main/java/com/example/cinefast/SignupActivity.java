package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "cinefast_session_pref_v3";

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Sign up button
        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> attemptSignup());
    }

    private void attemptSignup() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
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
        if (password.length() < 8) {
            etPassword.setError("Password must be at least 8 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Firebase Auth registration
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();

                    // Store user data in Firebase Realtime Database
                    mDatabase.child("users").child(uid).child("name").setValue(name);
                    mDatabase.child("users").child(uid).child("email").setValue(email);

                    // Save session
                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                            .edit()
                            .putBoolean("logged_in", true)
                            .putString("uid", uid)
                            .apply();

                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to home
                    Intent intent = new Intent(this, DrawerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Signup failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
