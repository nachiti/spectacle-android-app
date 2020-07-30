package com.example.spectacleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.spectacleapp.models.InterExter;
import com.example.spectacleapp.models.Search;
import com.example.spectacleapp.models.TypeSpectacle;
import com.google.android.material.textfield.TextInputEditText;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextVille;
    private CheckBox checkBoxMusique;
    private CheckBox checkBoxTheatre;
    private CheckBox checkBoxHumour;
    private CheckBox checkBoxDanse;
    private RangeSeekBar rangeSeekBarPrix;
    private CheckBox checkBoxInterieur;
    private CheckBox checkBoxExterieur;
    private CheckBox checkBoxHandicapOui;
    private CheckBox checkBoxHandicapNon;
    private Button buttonValider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initViewElements();
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this,MapActivity.class);
                Search search = getValueOfViewElements();
                intent.putExtra("search",search);
                FilterActivity.this.startActivity(intent);
            }
        });


    }

    public void initViewElements(){
        textInputEditTextVille = findViewById(R.id.tiet_ville);
        checkBoxMusique = findViewById(R.id.cb_musique);
        checkBoxTheatre = findViewById(R.id.cb_theatre);
        checkBoxHumour = findViewById(R.id.cb_humour);
        checkBoxDanse = findViewById(R.id.cb_danse);
        rangeSeekBarPrix = findViewById(R.id.rsb_prix);
        rangeSeekBarPrix.setRangeValues(0,200);
        checkBoxInterieur = findViewById(R.id.cb_interierur);
        checkBoxExterieur = findViewById(R.id.cb_exterieur);
        checkBoxHandicapOui = findViewById(R.id.cb_handicap_oui);
        checkBoxHandicapNon = findViewById(R.id.cb_handicap_non);
        buttonValider = findViewById(R.id.btn_submit_seach);
    }

    public Search getValueOfViewElements(){

        Search search = new Search();
        List<TypeSpectacle> typeChecked = new ArrayList<>();
        List<InterExter> interExterChecked = new ArrayList<>();
        List<Boolean> acceshandicapChecked = new ArrayList<>();

        String ville = textInputEditTextVille.getText().toString();
        search.setVille(ville);

        if(checkBoxMusique.isChecked()) typeChecked.add(TypeSpectacle.Musique);
        if(checkBoxTheatre.isChecked()) typeChecked.add(TypeSpectacle.Theatre);
        if(checkBoxHumour.isChecked()) typeChecked.add(TypeSpectacle.Humour);
        if(checkBoxDanse.isChecked()) typeChecked.add(TypeSpectacle.Danse);
        if (!typeChecked.isEmpty())search.setTypeSpectacleList(typeChecked);

        int min = (int) rangeSeekBarPrix.getSelectedMinValue();
        int max = (int) rangeSeekBarPrix.getSelectedMaxValue();
        search.setPrixMin(min);
        search.setPrixMax(max);

        if(checkBoxInterieur.isChecked()) interExterChecked.add(InterExter.Interieur);
        if(checkBoxExterieur.isChecked()) interExterChecked.add(InterExter.Exterieur);
        if (!interExterChecked.isEmpty())search.setInterExterList(interExterChecked);

        if (checkBoxHandicapOui.isChecked()) acceshandicapChecked.add(true);
        if(checkBoxHandicapNon.isChecked()) acceshandicapChecked.add(false);
        if (!acceshandicapChecked.isEmpty())search.setAccesHandicapList(acceshandicapChecked);
        return search;
    }

/*    public void rangeSeekBarChangeListener(){
        rangeSeekBarPrix.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                int prixMin = (int) bar.getAbsoluteMinValue();
                int prixMax = (int) bar.getAbsoluteMaxValue();
               *//* search.setPrixMin(prixMin);
                search.setPrixMax(prixMax);*//*
                Toast.makeText(getApplicationContext(),"min="+prixMin +"\n"+"max="+prixMax,Toast.LENGTH_LONG).show();
            }
        });
    }*/
}