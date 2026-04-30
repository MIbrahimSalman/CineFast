package com.example.cinefast;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Stub for Commit 1 — full implementation in Commit 2.
 */
public class DrawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Temporary content view — replaced in Commit 2
        setContentView(R.layout.activity_new_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        Toast.makeText(this, "Welcome to CineFAST", Toast.LENGTH_SHORT).show();
    }
}
