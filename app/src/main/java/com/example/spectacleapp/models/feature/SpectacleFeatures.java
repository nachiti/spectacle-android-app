package com.example.spectacleapp.models.feature;

import java.util.List;

public class SpectacleFeatures {
    public String type;
    public List<SpectacleFeature> features = null;
    public SpectacleFeatures(List<SpectacleFeature> features) {
        this.type = "features";
        this.features = features;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<SpectacleFeature> getFeatures() {
        return features;
    }
    public void setFeatures(List<SpectacleFeature> features) {
        this.features = features;
    }
}
