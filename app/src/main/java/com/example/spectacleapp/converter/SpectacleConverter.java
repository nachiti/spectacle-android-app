package com.example.spectacleapp.converter;

import android.annotation.SuppressLint;

import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.models.feature.SpectacleGeometry;
import com.example.spectacleapp.models.feature.SpectacleProperties;
import com.example.spectacleapp.models.feature.SpectacleFeature;
import com.example.spectacleapp.models.feature.SpectacleFeatures;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SpectacleConverter {

    public static String formatDateHeure(Date date){
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }
    public static SpectacleFeatures convertToSpectacleFeature(List<Spectacle> spectacles){
        SpectacleFeature spectacleFeature;
        List<SpectacleFeature> features = new ArrayList<>();
        for (Spectacle s : spectacles) {

                spectacleFeature = new SpectacleFeature(
                        new SpectacleProperties(
                                s.getId(),
                                s.getTitre(),
                                s.getPhotosUrl(),
                                s.getTypeSpectacle().toString(),
                                s.getAdresse(),
                                formatDateHeure(s.getDateHeure()),
                                s.getPrix(),
                                s.getDescription(),
                                s.isAccesHadicap(),
                                s.getInterExter().toString(),
                                s.isFavourite()),
                        new SpectacleGeometry(Arrays.asList(
                                s.getLongitude(),
                                s.getLatitude()

                        ))
                );
        features.add(spectacleFeature);
        }
        return new SpectacleFeatures(features);
    }
}
