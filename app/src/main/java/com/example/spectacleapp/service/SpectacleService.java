package com.example.spectacleapp.service;

import com.example.spectacleapp.models.Commentaire;
import com.example.spectacleapp.models.Search;
import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.models.Utilisateur;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpectacleService {

    /**
     * Récupérer toutes les spectacles
     * @return List<Spectacle>
     */
    @GET("public/spectacles")
    Call<List<Spectacle>> getAllSpectacles();

    /**
     * Recuper toutes les spectacles par different critere
     * @param search
     * @return
     */
    @POST("public/spectacles/search")
    Call<List<Spectacle>> getAllSpectaclesBySeach(@Body Search search);

    /**
     * Récupérer un spectacle par id
     * @param id id spectacle
     * @return Spectacle
     */
    @GET("public/spectacles/{id}")
    Call<Spectacle> getSpectacleById(@Path("id") Integer id);

/*    @GET("spectacles/seachBy")
    Call<List<Spectacle>> getSpectaclesByCriteria(@Query("ville")  String ville,
                                 @Query("type") String type,
                                 @Query("prixMin")Double prixMin,
                                 @Query("prixMax") Double prixMax,
                                 @Query("accesHandicap")Boolean accesHandicap);*/

    /**
     * Ajouter un commentaire à un spectacle
     * @param id idSpectacle
     * @param commentaire
     * @return
     */
    @Headers("Content-Type: application/json")
    @POST("/user/spectacles/{idSpectacle}/commentaires")
    Call<Commentaire> addCommentaire(@Path("idSpectacle")Integer id, @Body Commentaire commentaire);

    /**
     * Récupérer la liste des noms des images d'un spectacle
     * @param id id spectacle
     * @return
     */
    @GET(value = "public/spectacles/{id}/images")
    Call<List<String>> getImagesnameOfSpectacle(@Path("id") Integer id);

    /**
     * Récupérer la liste des commentaires d'un spectacle
     * @param id idSpectacle
     * @return
     */
    @GET("public/spectacles/{id}/commentaires")
    Call<List<Commentaire>> getAllCommentairesOfSpectacleById(@Path("id") Integer id);


    @POST()
    Call <Utilisateur> login(@Body Utilisateur utilisateur);

    /**
     * Ajouter un nouveau utilisateur
     * @param utilisateur
     * @return
     */
    @POST(value = "public/createUser")
    Call <Utilisateur> creerUnUtilisateur(@Body Utilisateur utilisateur);

    /**
     *  Modifier un utilisateur
     * @param utilisateur
     * @param username
     * @return
     */
    @POST(value = "user/update/{username}")
    Call <Utilisateur> modifierUnUtilisateur(@Body Utilisateur utilisateur, @Path("username") String username);

    /**
     * Supprimer un utilisateur
     * @param username
     */
    @DELETE(value = "user/delete/{username}")
    Call<Response> supprimerUnUtilisateur(@Path("username") String username);

    /**
     * Recuperer un utilisateur
     * @param username
     * @return
     */
    @GET(value = "user/{username}")
    Call <Utilisateur> recupererUnUtilisateur(@Path("username") String username);

    /**
     * Ajouter un spectacle au favoris d'un utilisateur
     * @param username
     * @param id id spectacle
     * @return
     */
    @POST(value = "user/{username}/addSpectacleFavoris/{id}")
    Call <Utilisateur> ajouterSpectacleAuFavoris( @Path(value = "username") String username,
                                              @Path(value = "id") long id);

    /**
     * Supprimer un spectacle du favoris d'un utilisateur
     * @param username
     * @param id id spectacle
     * @return
     */
    @POST(value = "user/{username}/deleteSpectacleFavoris/{id}")
    Call <Utilisateur> supprimerSpectacleDuFavoris( @Path(value = "username") String username,
                                                   @Path(value = "id") long id);
}
