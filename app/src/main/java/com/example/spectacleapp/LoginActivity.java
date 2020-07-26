package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity {

    private ImageButton imageButtonLoginClose;
    private EditText editTextLoginUsername;
    private EditText editTextLoginPassword;
    private Button buttonLoginSubmit;
    private Button buttonLoginInscripton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageButtonLoginClose = findViewById(R.id.btn_login_close);
        editTextLoginUsername = findViewById(R.id.login_pseudonyme);
        editTextLoginPassword = findViewById(R.id.login_password);
        buttonLoginSubmit = findViewById(R.id.btn_login_connexion);
        buttonLoginInscripton = findViewById(R.id.btn_login_inscription);
    }
}