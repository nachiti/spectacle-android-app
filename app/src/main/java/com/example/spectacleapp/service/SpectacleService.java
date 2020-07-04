package com.example.spectacleapp.service;

import com.example.spectacleapp.models.Commentaire;
import com.example.spectacleapp.models.Spectacle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpectacleService {

    //Recuper toutes les spectacles
    @GET("spectacles")
    Call<List<Spectacle>> getAllSpectacles();

    @GET("spectacles/{id}")
    Call<Spectacle> getSpectacleById(@Path("id") Integer id);

    @GET("spectacles/seachBy")
    Call<List<Spectacle>> getSpectaclesByCriteria(@Query("ville")  String ville,
                                 @Query("type") String type,
                                 @Query("prixMin")Double prixMin,
                                 @Query("prixMax") Double prixMax,
                                 @Query("accesHandicap")Boolean accesHandicap);

    //add commentaire in spectacle
    @POST("Commentaires")
    Call<Commentaire> addCommentaire(@Body Commentaire commentaire);

    //get list imagesName of spectacle
    @GET(value = "spectacles/{id}/images")
    Call<List<String>> getImagesnameOfSpectacle(@Path("id") Integer id);

}
