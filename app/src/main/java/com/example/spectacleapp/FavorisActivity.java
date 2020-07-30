package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.spectacleapp.adapters.FavorisAdapter;
import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.models.Utilisateur;
import com.example.spectacleapp.service.ServiceGenerator;
import com.example.spectacleapp.service.SpectacleService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavorisActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavoris;
    private LinearLayout linearLayoutFavorisVide;
    private FavorisAdapter adapterFavoris;
    private RecyclerView.LayoutManager layoutManagerFavoris;
    private List<Spectacle> spectacleList;
    private SharedPreferences sharedPreferencesUser;
    private SharedPreferences sharedPreferencesFavoris;
    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FAVORIS = "favoris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);

        sharedPreferencesUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferencesFavoris = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE);
        linearLayoutFavorisVide = findViewById(R.id.ly_favoris_vide);
        recyclerViewFavoris = findViewById(R.id.rv_favoris);
        recyclerViewFavoris.setHasFixedSize(true);
        layoutManagerFavoris = new LinearLayoutManager(this);
        recyclerViewFavoris.setLayoutManager(layoutManagerFavoris);
        adapterFavoris = new FavorisAdapter();
        //recyclerViewFavoris.setAdapter(adapterFavoris);
        getAllFavorisSpectacle();




        adapterFavoris.setOnItemClickListener(new FavorisAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // faire appel à l'activity detail spectacle
               String id = spectacleList.get(position).getId();
                Intent intent = new Intent(FavorisActivity.this,DetailActivity.class);
                intent.putExtra("spectacleId",id);
                startActivity(intent);

            }

            @Override
            public void onDeleteClick(int position) {
                //TODO remove spectacle from favoris api
                String idspectacle = spectacleList.get(position).getId();
                spectacleList.get(position).setFavourite(false);
                spectacleList.remove(position);
                adapterFavoris.notifyItemRemoved(position);
                int id = Integer.parseInt(idspectacle);
                String u = sharedPreferencesUser.getString(USERNAME, "");
                String p = sharedPreferencesUser.getString(PASSWORD, "");

                ServiceGenerator.createService(SpectacleService.class, u, p)
                        .supprimerSpectacleDuFavoris(
                                sharedPreferencesUser.getString(USERNAME, ""), id)
                        .enqueue(new Callback<Utilisateur>() {
                            @Override
                            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                                editor.remove(idspectacle);
                                editor.commit();
                                Toast.makeText(FavorisActivity.this, "succes !", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Utilisateur> call, Throwable t) {
                                Toast.makeText(FavorisActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                                System.out.println("Erreur de suppression du favoris :" + t.getMessage());
                            }
                        });
                if(spectacleList.isEmpty()){
                    linearLayoutFavorisVide.setVisibility(View.VISIBLE);
                    recyclerViewFavoris.setVisibility(View.GONE);
                }
            }
        });
    }

    public void getAllFavorisSpectacle() {
        if (sharedPreferencesUser.contains(USERNAME)) {
            String u = sharedPreferencesUser.getString(USERNAME, "");
            String p = sharedPreferencesUser.getString(PASSWORD, "");
            Call <Utilisateur> utilisateurCall = ServiceGenerator.createService(SpectacleService.class,u,p)
                    .recupererUnUtilisateur(u);
            utilisateurCall.enqueue(new Callback<Utilisateur>() {
                @Override
                public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                    if(response.isSuccessful()){
                        if(response.body().getSpectaclesFavoris()!= null){
                            linearLayoutFavorisVide.setVisibility(View.GONE);
                            recyclerViewFavoris.setVisibility(View.VISIBLE);
                            spectacleList = response.body().getSpectaclesFavoris();
                            adapterFavoris.setSpectaclesFavoris(spectacleList);
                            recyclerViewFavoris.setAdapter(adapterFavoris);
                        }
                    }else {
                        System.out.println("Error de recuperation de favoris" + response.code()+response.message());
                        Toast.makeText(FavorisActivity.this, "Error de recuperation de favoris", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Utilisateur> call, Throwable t) {
                    System.out.println("Error de recuperation de favoris" + t.getMessage());
                    Toast.makeText(FavorisActivity.this, "Error de recuperation de favoris", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}