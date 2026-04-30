package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "cinefast_session_pref_v3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView logo = findViewById(R.id.imgLogo);
        TextView appName = findViewById(R.id.tvAppName);

        Animation rotateFade = AnimationUtils.loadAnimation(this, R.anim.rotate_fade);
        Animation fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        logo.startAnimation(rotateFade);
        appName.startAnimation(fadeAnim);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            if (prefs.getBoolean("logged_in", false)) {
                // Session exists — go directly to DrawerActivity
                Intent intent = new Intent(MainActivity.this, DrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                // No session — show onboarding
                startActivity(new Intent(MainActivity.this, OnboardingActivity.class));
            }
            finish();
        }, 3000);
    }
}