package com.example.spectacleapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spectacleapp.models.Commentaire;
import com.example.spectacleapp.service.ServiceGenerator;
import com.example.spectacleapp.service.SpectacleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferencesUser;
    private SharedPreferences sharedPreferencesFavoris;
    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FAVORIS = "favoris";

    private TextView textViewPseudoNyme;
    private Button buttonModifier;
    private Button buttonDeconnecter;
    private Button buttonSupprimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);
        sharedPreferencesUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferencesFavoris = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE);

        textViewPseudoNyme = findViewById(R.id.tv_compte_pseudonyme);
        buttonModifier = findViewById(R.id.btn_compte_modifier);
        buttonDeconnecter = findViewById(R.id.btn_compte_deconnecter);
        buttonSupprimer = findViewById(R.id.btn_compte_supprimer);

        textViewPseudoNyme.setText("Bienvenue "
                +sharedPreferencesUser.getString(USERNAME,"")
                +"!");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void onClickButtonModifierCompte(View view){
        Intent intent = new Intent(CompteActivity.this, InscriptionActivity.class);
        intent.putExtra("modiferUser",true);
        startActivity(intent);

    }
    public void onClickButtonDeconnecter(View v){
        SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
        editorUser.clear();
        SharedPreferences.Editor editorFavoris = sharedPreferencesFavoris.edit();
        editorFavoris.clear();
        startActivity(new Intent(CompteActivity.this, MapActivity.class));
        Toast.makeText(this, "Vous êtes bien deconnecté !", Toast.LENGTH_SHORT).show();
    }

    public void onClickButtonSupprimer(View v){

        SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
        editorUser.clear();
        SharedPreferences.Editor editorFavoris = sharedPreferencesFavoris.edit();
        editorFavoris.clear();
        startActivity(new Intent(CompteActivity.this, MapActivity.class));
        Toast.makeText(this, "Vous êtes bien deconnecté !", Toast.LENGTH_SHORT).show();
    }

    public void showConfirmDialog(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirmer la suppression compte !");
        alertDialogBuilder.setIcon(R.drawable.ic_info_red);
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir supprimer votre compte ?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            //Delete User
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                String u = sharedPreferencesUser.getString(USERNAME, "");
                String p = sharedPreferencesUser.getString(PASSWORD, "");
                ServiceGenerator.createService(SpectacleService.class,u,p).supprimerUnUtilisateur(u)
                        .enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(Call<Response> call, Response<Response> response) {
                                if (response.isSuccessful()) {
                                    onClickButtonDeconnecter(view);
                                    Toast.makeText(CompteActivity.this, "Votre compte est bien supprimer!", Toast.LENGTH_SHORT).show();
                                }else {
                                    System.out.println("Erreur de suppression de compte : " + response.code()+response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {
                                System.out.println("Erreur de suppression de compte : " + t.getMessage());
                            }
                        });



            }
        });
        alertDialogBuilder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CompteActivity.this,"Vous avez cliqué sur Non",Toast.LENGTH_SHORT).show();
            }
        });
    }
}