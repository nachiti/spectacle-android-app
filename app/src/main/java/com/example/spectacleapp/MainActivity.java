package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewLogo;
    private Animation animation;
    private static int SPLASH_TIME_OUT = 5000;

    private SharedPreferences sharedPreferencesUser;
    private SharedPreferences sharedPreferencesFavoris;
    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FAVORIS = "favoris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      /*  imageViewLogo = findViewById(R.id.splash_logo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.transition);
        imageViewLogo.startAnimation(animation);*/
        sharedPreferencesUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferencesFavoris = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesUser.edit();
        editor.clear();
        SharedPreferences.Editor editor2 = sharedPreferencesFavoris.edit();
        editor.clear();
        SystemClock.sleep(SPLASH_TIME_OUT);
        Intent mapIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(mapIntent);
        finish();
    }
}