package com.example.spectacleapp.models.feature;

public class SpectacleFeature {
    public String type;
    public SpectacleProperties properties;
    public SpectacleGeometry geometry;

    public SpectacleFeature(SpectacleProperties properties, SpectacleGeometry geometry) {
        this.type = "Feature";
        this.properties = properties;
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SpectacleProperties getProperties() {
        return properties;
    }

    public void setProperties(SpectacleProperties properties) {
        this.properties = properties;
    }

    public SpectacleGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(SpectacleGeometry geometry) {
        this.geometry = geometry;
    }
}


