package com.example.spectacleapp.models.feature;

import java.util.List;

public class SpectacleGeometry {
    public String type;
    public List<Double> coordinates = null;

    public SpectacleGeometry(List<Double> coordinates) {
        this.type = "Point";
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
