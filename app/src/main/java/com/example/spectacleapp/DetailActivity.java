package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.spectacleapp.adapters.SpectacleImagesAdapter;
import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.service.ServiceGenerator;
import com.example.spectacleapp.service.SpectacleService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

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
    private TextInputEditText textInputEditTextPseudo;
    private RatingBar ratingBarNote;
    private Button buttonSubmitComment;

    private boolean ALREADY_ADDED_TO_WISHLIST = false;

    private String spectacleId;
    public static SpectacleService spectacleService = ServiceGenerator.createService(SpectacleService.class);
    private Spectacle spectacle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectacle_detail);

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
        textViewDate = findViewById(R.id.tv_date);
        textViewHeure = findViewById(R.id.tv_heure);
        textViewInterExter = findViewById(R.id.tv_inter_exter);
        textViewAccesHandicap = findViewById(R.id.tv_acces_handicap);
        //init view of description layout
        textViewDescription = findViewById(R.id.tv_description);
        //init view of Add comment form layout
        textInputEditTextPseudo = findViewById(R.id.tiet_pseudonyme);
        ratingBarNote = findViewById(R.id.rb_note);
        buttonSubmitComment = findViewById(R.id.btn_submit_comment);

        //Load data from api
        Intent intent = getIntent();
        spectacleId = intent.getStringExtra("spectacleId");
        new LoadSpectacleDataFromApiTask(this).execute(spectacleId);
    }

    private static class LoadSpectacleDataFromApiTask extends AsyncTask<String, Void, Spectacle> {

        private WeakReference<DetailActivity> activityRef;

        LoadSpectacleDataFromApiTask(DetailActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected Spectacle doInBackground(String... params) {
            Spectacle newSpectacle = null;
            DetailActivity activity = activityRef.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            String id = params[0];
            Call<Spectacle> callSync = spectacleService.getSpectacleById(Integer.parseInt(id));
            try {
                retrofit2.Response<Spectacle> response = callSync.execute();
                newSpectacle = response.body();
            } catch (Exception e) {
                System.out.println("@Error spectacleService.getSpectacleById(" + id + "):" + e);
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
                imageUrls.add(ServiceGenerator.API_IMAGE + s.getPhotosUrl().get(i));
            }
            SpectacleImagesAdapter spectacleImagesAdapter = new SpectacleImagesAdapter(activity, imageUrls);
            activity.viewPagerImages.setAdapter(spectacleImagesAdapter);
            activity.tabLayoutViewpagerIndicator.setupWithViewPager(activity.viewPagerImages, true);
            activity.textViewTitle.setText(s.getTitre());


            activity.textViewAverageNote.setText("todo");
            activity.textViewTotalComment.setText("todo");
            String prix = String.valueOf(s.getPrix());
            if (prix.equals("0") || prix.equals("0.0") || prix.equals("0.00")){
                prix = "Gratuit";
            }else {
                prix = prix + "€";
            }
            activity.textViewPrix.setText(prix);
            activity.floatingActionButtonAddToWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity.ALREADY_ADDED_TO_WISHLIST) {
                        activity.ALREADY_ADDED_TO_WISHLIST = false;
                        activity.floatingActionButtonAddToWishlist.setSupportImageTintList(
                                ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    } else {
                        activity.ALREADY_ADDED_TO_WISHLIST = true;
                        activity.floatingActionButtonAddToWishlist.setSupportImageTintList(
                                activity.getResources().getColorStateList(R.color.colorPrimary));
                        //TODO add favorite
                    }
                }
            });
            activity.floatingActionButtonShowItineraire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO show itineraire
                }
            });
            //Remplissage de la vue detail layout
            activity.textViewType.setText(String.valueOf(s.getTypeSpectacle()));
            activity.textViewAdresse.setText(s.getAdresse());
            String dateHeure = s.getDateHeure().toString();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
            Date parseDateHeure = format.parse(dateHeure);
            String[] newDateHeure = parseDateHeure.toString().split(" ");
            activity.textViewDate.setText(newDateHeure[0]);
            activity.textViewHeure.setText(newDateHeure[1]);
            activity.textViewInterExter.setText(s.getInterExter().toString());
            activity.textViewAccesHandicap.setText(s.isAccesHadicap() ? "Oui" : "Non");
            //init view of description layout
            activity.textViewDescription.setText(s.getDescription());
            //init view of Add comment form layout
        /*   TODO     activity.textInputEditTextPseudo
        activity.ratingBarNote
        activity.buttonSubmitComment*/
        }

    }


}