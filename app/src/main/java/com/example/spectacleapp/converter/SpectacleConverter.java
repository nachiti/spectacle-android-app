package com.example.spectacleapp.converter;

import com.example.spectacleapp.models.Spectacle;
import com.example.spectacleapp.models.feature.SpectacleGeometry;
import com.example.spectacleapp.models.feature.SpectacleProperties;
import com.example.spectacleapp.models.feature.SpectacleFeature;
import com.example.spectacleapp.models.feature.SpectacleFeatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpectacleConverter {

    public static SpectacleFeatures convertToSpectacleFeature(List<Spectacle> spectacles){
        SpectacleFeature spectacleFeature;
        List<SpectacleFeature> features = new ArrayList<>();
        for (Spectacle s:spectacles) {
                spectacleFeature = new SpectacleFeature(
                        new SpectacleProperties(
                                s.getId(),
                                s.getTitre(),
                                s.getPhotosUrl(),
                                s.getTypeSpectacle().toString(),
                                s.getAdresse(),
                                s.getDateHeure().toString(),
                                s.getPrix(),
                                s.getDescription(),
                                s.isAccesHadicap(),
                                s.getInterExter().toString()),
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
