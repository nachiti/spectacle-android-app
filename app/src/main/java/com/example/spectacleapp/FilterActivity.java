package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class FilterActivity extends AppCompatActivity {

    private RangeSeekBar rangeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }
}