package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageViewLogo = findViewById(R.id.splash_logo);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.transition);
        imageViewLogo.startAnimation(animation);
        SystemClock.sleep(2000);
        Intent loginIntent = new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}