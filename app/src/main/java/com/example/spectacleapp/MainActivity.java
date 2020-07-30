package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private Animation animation;
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageViewLogo = findViewById(R.id.splash_logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.transition);
        imageViewLogo.startAnimation(animation);
        SystemClock.sleep(SPLASH_TIME_OUT);
        Intent mapIntent = new Intent(MainActivity.this,MapActivity.class);
        startActivity(mapIntent);
        finish();
    }
}