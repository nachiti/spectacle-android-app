package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class InscriptionActivity extends AppCompatActivity {

    private ImageButton imageButtonInscriptionClose;
    private EditText editTextInscriptionUsername;
    private EditText editTextInscriptionPassword1;
    private EditText editTextInscriptionPassword2;
    private Button buttonInscriptionSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        imageButtonInscriptionClose = findViewById(R.id.btn_inscription_close);
        editTextInscriptionUsername = findViewById(R.id.inscription_pseudonyme);
        editTextInscriptionPassword1 = findViewById(R.id.inscription_password1);
        editTextInscriptionPassword2 = findViewById(R.id.inscription_password2);
        buttonInscriptionSubmit = findViewById(R.id.btn_inscription_enregistrer);
    }
}