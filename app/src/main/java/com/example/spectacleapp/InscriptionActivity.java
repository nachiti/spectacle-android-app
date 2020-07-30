package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.spectacleapp.models.Utilisateur;
import com.example.spectacleapp.service.NetworkService;
import com.example.spectacleapp.service.SpectacleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {

    private ImageButton imageButtonInscriptionClose;
    private EditText editTextInscriptionUsername;
    private EditText editTextInscriptionPassword1;
    private EditText editTextInscriptionPassword2;
    private Button buttonInscriptionSubmit;
    private SharedPreferences sharedPreferencesUser;
    private SharedPreferences sharedPreferencesFavoris;
    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FAVORIS = "favoris";
    private boolean modiferUser;

    public static SpectacleService spectacleService = NetworkService.createService(SpectacleService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Intent intent = getIntent();
        modiferUser = intent.getBooleanExtra("modiferUser",false);
        imageButtonInscriptionClose = findViewById(R.id.btn_inscription_close);
        editTextInscriptionUsername = findViewById(R.id.inscription_pseudonyme);
        editTextInscriptionPassword1 = findViewById(R.id.inscription_password1);
        editTextInscriptionPassword2 = findViewById(R.id.inscription_password2);
        buttonInscriptionSubmit = findViewById(R.id.btn_inscription_enregistrer);

        sharedPreferencesUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferencesFavoris = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE);
    }

    public void onClickSubmit(View view){
        if(modiferUser){
            editTextInscriptionUsername.setText(sharedPreferencesUser.getString(USERNAME,""));
            updateUser();
        }else {
            createUser();
        }
    }

    public void createUser() {
        if (editTextInscriptionUsername.getText().toString().isEmpty()||
                editTextInscriptionPassword1.getText().toString().isEmpty()||
                editTextInscriptionPassword2.getText().toString().isEmpty()){
            Toast.makeText(InscriptionActivity.this,"Veuillez remplir tous les champs svp !",Toast.LENGTH_LONG).show();
        }else {
            if(editTextInscriptionPassword2.getText().toString().trim().
                    equals(editTextInscriptionPassword2.getText().toString().trim())){
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setUsername(editTextInscriptionUsername.getText().toString());
                utilisateur.setPassword(editTextInscriptionPassword1.getText().toString());

                spectacleService.creerUnUtilisateur(utilisateur).enqueue(new Callback<Utilisateur>() {
                    @Override
                    public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                        Toast.makeText(InscriptionActivity.this, "Votre compte est crée !", Toast.LENGTH_SHORT).show();
                        InscriptionActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Utilisateur> call, Throwable t) {
                        Toast.makeText(InscriptionActivity.this, "Error !" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    public void updateUser() {
        if (editTextInscriptionUsername.getText().toString().isEmpty()||
                editTextInscriptionPassword1.getText().toString().isEmpty()||
                editTextInscriptionPassword2.getText().toString().isEmpty()){
            Toast.makeText(InscriptionActivity.this,"Veuillez remplir tous les champs svp !",Toast.LENGTH_LONG).show();
        }else {
            if(editTextInscriptionPassword2.getText().toString().trim().
                    equals(editTextInscriptionPassword2.getText().toString().trim())){
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setUsername(editTextInscriptionUsername.getText().toString());
                utilisateur.setPassword(editTextInscriptionPassword1.getText().toString());

                spectacleService.modifierUnUtilisateur(
                        utilisateur,sharedPreferencesUser.getString(USERNAME,"")).enqueue(new Callback<Utilisateur>() {
                    @Override
                    public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                        if(response.isSuccessful()){
                            SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                            editor.clear();
                            editor.putString(USERNAME,utilisateur.getUsername());
                            editor.putString(PASSWORD,utilisateur.getPassword());
                            Toast.makeText(InscriptionActivity.this, "Votre compte est mise à jour !", Toast.LENGTH_SHORT).show();
                            InscriptionActivity.this.finish();
                        }else {
                            Toast.makeText(InscriptionActivity.this, "Error !" + response.code()+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Utilisateur> call, Throwable t) {
                        Toast.makeText(InscriptionActivity.this, "Error !" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    public void onClickBtnCloseInscription(View view){
        super.onBackPressed();
    }
}