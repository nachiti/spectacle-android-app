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

import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.models.Utilisateur;
import com.example.spectacleapp.service.ServiceGenerator;
import com.example.spectacleapp.service.SpectacleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageButton imageButtonLoginClose;
    private EditText editTextLoginUsername;
    private EditText editTextLoginPassword;
    private Button buttonLoginSubmit;
    private Button buttonLoginInscripton;
    private SharedPreferences sharedPreferencesUser;
    private SharedPreferences sharedPreferencesFavoris;
    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FAVORIS = "favoris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageButtonLoginClose = findViewById(R.id.btn_login_close);
        editTextLoginUsername = findViewById(R.id.login_pseudonyme);
        editTextLoginPassword = findViewById(R.id.login_password);
        buttonLoginSubmit = findViewById(R.id.btn_login_connexion);
        buttonLoginInscripton = findViewById(R.id.btn_login_inscription);

        sharedPreferencesUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferencesFavoris = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE);

        buttonLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLoginUsername.getText().toString().isEmpty()||
                        editTextLoginPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Veuillez remplir tous les champs svp !",Toast.LENGTH_LONG).show();
                }else {
                    String username = editTextLoginUsername.getText().toString().trim();
                    String password = editTextLoginPassword.getText().toString().trim();

                    ServiceGenerator
                            .createService(SpectacleService.class,username,password)
                            .recupererUnUtilisateur(username).enqueue(new Callback<Utilisateur>() {
                        @Override
                        public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                            if (response.isSuccessful()){

                                Toast.makeText(LoginActivity.this, "Votre êtes connecté !", Toast.LENGTH_SHORT).show();
                                //Store user
                                SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
                                editorUser.clear();
                                editorUser.putString(USERNAME,username);
                                editorUser.putString(PASSWORD,password);
                                editorUser.commit();

                                //Store Spectacles favoris
                                if (response.body() != null && response.body().getSpectaclesFavoris() !=null) {
                                    List<Spectacle> spectacles = response.body().getSpectaclesFavoris();
                                    SharedPreferences.Editor editorFavoris = sharedPreferencesFavoris.edit();
                                    editorFavoris.clear();
                                    for (Spectacle spectacle: spectacles){
                                        editorFavoris.putString(spectacle.getId(),spectacle.getId());
                                    }
                                    editorFavoris.commit();
                                }

                                startActivity(new Intent(LoginActivity.this,MapActivity.class));
                                LoginActivity.this.finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "identification incorrect !", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Utilisateur> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "identification incorrect !", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        buttonLoginInscripton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(LoginActivity.this,InscriptionActivity.class)));
            }
        });
    }

    public void onClickBtnCloseLogin(View view){
        super.onBackPressed();
    }
}