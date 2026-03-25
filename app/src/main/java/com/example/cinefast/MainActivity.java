package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, OnboardingActivity.class);
                startActivity(intent);

                finish();
            }
        }, 5000);
    }
}