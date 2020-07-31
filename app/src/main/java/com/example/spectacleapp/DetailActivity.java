package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spectacleapp.adapters.CommentaireAdapter;
import com.example.spectacleapp.adapters.SpectacleImagesAdapter;
import com.example.spectacleapp.models.Commentaire;
import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.models.Utilisateur;
import com.example.spectacleapp.service.NetworkService;
import com.example.spectacleapp.service.ServiceGenerator;
import com.example.spectacleapp.service.SpectacleService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    //image layout
    private ViewPager viewPagerImages;
    private TabLayout tabLayoutViewpagerIndicator;
    private TextView textViewTitle;
    private TextView textViewAverageNote;
    private TextView textViewTotalComment;
    private TextView textViewPrix;
    private FloatingActionButton floatingActionButtonAddToWishlist;
    private FloatingActionButton floatingActionButtonShowItineraire;
    //detail layout
    private TextView textViewType;
    private TextView textViewAdresse;
    private TextView textViewDate;
    private TextView textViewHeure;
    private TextView textViewInterExter;
    private TextView textViewAccesHandicap;
    //description layout
    private TextView textViewDescription;
    //Add comment form layout
    private LinearLayout linearLayoutConnection;
    private Button buttonSeconnecter;
    private Button buttonSinscrire;
    private LinearLayout linearLayoutComment;
    private RatingBar ratingBarNote;
    private TextInputEditText textInputEditTextCommentaire;
    private Button buttonSubmitComment;
    //List view for comments
    private TextView textViewTitreListComments;
    private RecyclerView recyclerViewCommentaires;
    private List<Commentaire> commentaireList;
    private CommentaireAdapter commentaireAdapter;

    private boolean ALREADY_ADDED_TO_WISHLIST = false;

    private String spectacleId;
    public static SpectacleService spectacleService = NetworkService.createService(SpectacleService.class);
    private Spectacle spectacle;

    private static SharedPreferences sharedPreferencesUser;
    private static SharedPreferences sharedPreferencesFavoris;
    private static final String USER = "user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String FAVORIS = "favoris";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //init view of image layout
        viewPagerImages = findViewById(R.id.vp_images);
        tabLayoutViewpagerIndicator = findViewById(R.id.tl_viewpager_indicator);
        textViewTitle = findViewById(R.id.tv_title);
        ;
        textViewAverageNote = findViewById(R.id.tv_average_note);
        textViewTotalComment = findViewById(R.id.tv_total_comment);
        textViewPrix = findViewById(R.id.tv_prix);
        floatingActionButtonAddToWishlist = findViewById(R.id.fabtn_add_to_wishlist);
        floatingActionButtonShowItineraire = findViewById(R.id.fabtn_show_itineraire);
        //init view of detail layout
        textViewType = findViewById(R.id.tv_type);
        textViewAdresse = findViewById(R.id.tv_adresse);
        textViewDate = findViewById(R.id.tv_c_date);
        textViewHeure = findViewById(R.id.tv_heure);
        textViewInterExter = findViewById(R.id.tv_inter_exter);
        textViewAccesHandicap = findViewById(R.id.tv_acces_handicap);
        //init view of description layout
        textViewDescription = findViewById(R.id.tv_description);
        //init view of Add comment form layout
        linearLayoutConnection  = findViewById(R.id.linearLayout_connexion);
        buttonSeconnecter = findViewById(R.id.btn_seconnecter);
        buttonSinscrire= findViewById(R.id.btn_sinscrire);
        linearLayoutComment= findViewById(R.id.linearLayout_add_comment);
        ratingBarNote = findViewById(R.id.rb_note);
        textInputEditTextCommentaire = findViewById(R.id.tiet_commentaire);
        buttonSubmitComment = findViewById(R.id.btn_submit_comment);
        //init listview comments
        textViewTitreListComments = findViewById(R.id.tv_titre_list_comments);
        recyclerViewCommentaires = findViewById(R.id.recycleview_commentaires);

        sharedPreferencesUser = getSharedPreferences(USER, Context.MODE_PRIVATE);
        sharedPreferencesFavoris = getSharedPreferences(FAVORIS, Context.MODE_PRIVATE);

        if(sharedPreferencesUser.contains(USERNAME)){
            linearLayoutConnection.setVisibility(View.GONE);
            linearLayoutComment.setVisibility(View.VISIBLE);
        }

        //Load data from api
        Intent intent = getIntent();
        spectacleId = intent.getStringExtra("spectacleId");
        new LoadSpectacleDataFromApiTask(this).execute(spectacleId);

        //get typed data of comment
        buttonSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pseudonymeInput =  sharedPreferencesUser.getString(USERNAME,"");
                String commentaireInput = textInputEditTextCommentaire.getText().toString().trim();
                double note = ratingBarNote.getRating();
                if (!pseudonymeInput.isEmpty() && note!= 0 && !commentaireInput.isEmpty()) {

                    sendNewComments(new Commentaire(pseudonymeInput, note, commentaireInput));
                } else {
                    Toast.makeText(DetailActivity.this, "Le champ pseudonyme, etoile et commentaire ne doivent pas être vide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onButtonClickSeconnecter(View view){
        startActivity(new Intent(DetailActivity.this,LoginActivity.class));
    }

    public void onButtonClickSinscrire(View view){
        startActivity(new Intent(DetailActivity.this,InscriptionActivity.class));
    }

    public void sendNewComments(Commentaire commentaire) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        System.out.println("Commentaire: "+commentaire);
        String u = sharedPreferencesUser.getString(USERNAME, "");
        String p = sharedPreferencesUser.getString(PASSWORD, "");
        ServiceGenerator.createService(SpectacleService.class,u,p)
        .addCommentaire(Integer.parseInt(spectacleId), commentaire)
                .enqueue(new Callback<Commentaire>() {
                    @Override
                    public void onResponse(Call<Commentaire> call, Response<Commentaire> response) {

                        if (response.isSuccessful()) {
                            commentaireList.add(0, commentaire);
                            recyclerViewCommentaires.scrollToPosition(0);
                            commentaireAdapter.notifyItemInserted(0);
                            //clear form
                            ratingBarNote.setRating(0);
                            textInputEditTextCommentaire.setText("");
                            textInputEditTextCommentaire.clearFocus();
                            updateAverageNoteAndNbrComments(commentaireList);
                            recyclerViewCommentaires.setFocusable(true);
                        }{
                            System.out.println("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
                            System.out.println(response.code() + response.message() + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Commentaire> call, Throwable t) {
                        System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                        System.out.println("Commentaire: "+commentaire);
                        System.out.println("error insertion: " + t.getMessage());
                    }
                });

    }

    public void updateAverageNoteAndNbrComments(List<Commentaire> commentaires){
        DecimalFormat formatter = new DecimalFormat("#0.0");
        double d = calculateAverageNote(commentaires);
        String averageNoteFormat = (d > 0) ? formatter.format(d) : "0.0";
        textViewAverageNote.setText(averageNoteFormat);
        String textTotalComment = "(" + commentaires.size() + ") commentaires ";
        textViewTotalComment.setText(textTotalComment);
        textViewTitreListComments.setText(commentaires.size() +" commentaires - note total : " +averageNoteFormat+"/5");
    }

    public double calculateAverageNote(List<Commentaire> commentaires) {
        double sumNote = 0;
        for (int i = 0; i < commentaires.size(); i++) {
            sumNote = sumNote + commentaires.get(i).getNote();
        }
        return sumNote / commentaires.size();
    }

    private static class LoadSpectacleDataFromApiTask extends AsyncTask<String, Void, Spectacle> {

        private WeakReference<DetailActivity> activityRef;

        LoadSpectacleDataFromApiTask(DetailActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected Spectacle doInBackground(String... params) {
            Spectacle newSpectacle = null;
            List<Commentaire> commentaires = null;
            DetailActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            String id = params[0];
            Call<Spectacle> callSyncSpectacle = spectacleService.getSpectacleById(Integer.parseInt(id));
            try {
                retrofit2.Response<Spectacle> response = callSyncSpectacle.execute();
                newSpectacle = response.body();
            } catch (Exception e) {
                System.out.println("@Error spectacleService.getSpectacleById(" + id + "):" + e);
            }

            Call<List<Commentaire>> callSyncComment = spectacleService.getAllCommentairesOfSpectacleById(Integer.parseInt(id));
            try {
                retrofit2.Response<List<Commentaire>> response = callSyncComment.execute();
                commentaires = response.body();
            } catch (Exception e) {
                System.out.println("@Error spectacleService.getAllCommentairesOfSpectacleById(" + id + "):" + e);
            }
            activity.commentaireList = commentaires;
            newSpectacle.setCommentaires(commentaires);

            if(activity.sharedPreferencesFavoris.contains(newSpectacle.getId())){
                newSpectacle.setFavourite(true);
            }

            return newSpectacle;
        }

        @Override
        protected void onPostExecute(Spectacle newSpectacle) {
            super.onPostExecute(newSpectacle);
            DetailActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.spectacle = newSpectacle;
            try {
                showLoadedDatafromApi(activity, newSpectacle);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public void showLoadedDatafromApi(DetailActivity activity, Spectacle s) throws ParseException {
            //Remplissage de la vue image layout
            List<String> imageUrls = new ArrayList<>();
            for (int i = 0; i < s.getPhotosUrl().size(); i++) {
                imageUrls.add(NetworkService.API_IMAGE + s.getPhotosUrl().get(i));
            }
            SpectacleImagesAdapter spectacleImagesAdapter = new SpectacleImagesAdapter(activity, imageUrls);
            activity.viewPagerImages.setAdapter(spectacleImagesAdapter);
            activity.tabLayoutViewpagerIndicator.setupWithViewPager(activity.viewPagerImages, true);
            activity.textViewTitle.setText(s.getTitre());

            activity.updateAverageNoteAndNbrComments(s.getCommentaires());

            String prix = String.valueOf(s.getPrix());
            if (prix.equals("0") || prix.equals("0.0") || prix.equals("0.00")) {
                prix = "Gratuit";
            } else {
                prix = prix + " €";
            }
            activity.textViewPrix.setText(prix);
            if(s.isFavourite() || sharedPreferencesFavoris.contains(s.getId())){
                activity.ALREADY_ADDED_TO_WISHLIST = true;
                activity.floatingActionButtonAddToWishlist.setSupportImageTintList(
                        activity.getResources().getColorStateList(R.color.colorPrimary));
            }

            //Remplissage de la vue detail layout
            activity.textViewType.setText(String.valueOf(s.getTypeSpectacle()));
            activity.textViewAdresse.setText(s.getAdresse());
            Date dateHeure = s.getDateHeure();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            String parseDateHeure = format.format(dateHeure);
            String[] newDateHeure = parseDateHeure.toString().split(" ");
            activity.textViewDate.setText(newDateHeure[0]);
            activity.textViewHeure.setText(newDateHeure[1]);
            activity.textViewInterExter.setText(s.getInterExter().toString());
            activity.textViewAccesHandicap.setText(s.isAccesHadicap() ? "Oui" : "Non");
            //init view of description layout
            activity.textViewDescription.setText(s.getDescription());


            //init list of comments
            activity.commentaireAdapter = new CommentaireAdapter(s.getCommentaires());
            activity.recyclerViewCommentaires.setLayoutManager(
                    new LinearLayoutManager(activity.getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false));
            activity.recyclerViewCommentaires.setAdapter(activity.commentaireAdapter);


        }
    }


    public void onfloatingActionButtonClickAddToWishlist(View view) {
        //si il est connecté
        if (sharedPreferencesUser.contains(USERNAME)) {
            if (ALREADY_ADDED_TO_WISHLIST) { // si selectioné alors supprimer au favoris
                ALREADY_ADDED_TO_WISHLIST = false;
                floatingActionButtonAddToWishlist.setSupportImageTintList(
                        ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                int id = Integer.parseInt(spectacleId);

                    String u = sharedPreferencesUser.getString(USERNAME, "");
                    String p = sharedPreferencesUser.getString(PASSWORD, "");
                    ServiceGenerator.createService(SpectacleService.class, u, p)
                            .supprimerSpectacleDuFavoris(u, id).enqueue(new Callback<Utilisateur>() {
                        @Override
                        public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                            if (response.isSuccessful()) {

                                SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                                editor.remove(spectacleId);
                                editor.commit();
                                spectacle.setFavourite(false);
                                Toast.makeText(DetailActivity.this, "succes!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailActivity.this, "Error : " + response.code() + response.message(), Toast.LENGTH_SHORT).show();
                                System.out.println("Erreur de suppression du favoris :" + response.code() + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Utilisateur> call, Throwable t) {
                            Toast.makeText(DetailActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            System.out.println("Erreur de suppression du favoris :" + t.getMessage());
                        }
                    });

            }
            else { // si  non selectioné alors ajouter au favoris
                ALREADY_ADDED_TO_WISHLIST = true;
                floatingActionButtonAddToWishlist.setSupportImageTintList(
                        getResources().getColorStateList(R.color.colorPrimary));
                int id = Integer.parseInt(spectacleId);

                String u = sharedPreferencesUser.getString(USERNAME, "");
                String p = sharedPreferencesUser.getString(PASSWORD, "");
                ServiceGenerator.createService(SpectacleService.class, u, p).ajouterSpectacleAuFavoris(u, id)
                        .enqueue(new Callback<Utilisateur>() {
                            @Override
                            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                                if (response.isSuccessful()) {
                                    SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                                    editor.putString(spectacleId, spectacleId);
                                    editor.commit();
                                    spectacle.setFavourite(true);
                                    System.out.println("8888888888888888 SUCESS Ajout favoris 88888888888888888");
                                } else {
                                    Toast.makeText(DetailActivity.this, "Error : " + response.code() + response.message(), Toast.LENGTH_SHORT).show();
                                    System.out.println("Erreur Ajout favoris :" + response.code() + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Utilisateur> call, Throwable t) {
                                Toast.makeText(DetailActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                System.out.println("Erreur Ajout favoris :" + t.getMessage());
                            }
                        });
            }
        } else {
            startActivity(new Intent(DetailActivity.this, LoginActivity.class));
        }
    }


    public void onfloatingActionButtonClickItineraire(View view) {
        Intent intent = new Intent(DetailActivity.this, MapActivity.class);
        startActivity(intent.putExtra("showItineraireSpectacleId", spectacleId));
    }

    @Override
    protected void onStop() {
        super.onStop();
        linearLayoutConnection.setVisibility(View.VISIBLE);
        linearLayoutComment.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        linearLayoutConnection.setVisibility(View.VISIBLE);
        linearLayoutComment.setVisibility(View.GONE);
    }
}