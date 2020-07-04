package com.example.spectacleapp.models.feature;

import java.util.List;

public class SpectacleProperties {
    public String id;
    public String titre;
    public List<String> photosUrl;
    public String typeSpectacle;
    public String adresse;
    public String dateHeure;
    public double prix;
    public String description;
    public boolean accesHandicap;
    public String interExter;
    public boolean selected;
    public boolean loading;
    public long loadingProgress;
    public boolean favourite;
    public double zoom;
    public long bearing;
    public long tilt;

    public SpectacleProperties(String id, String titre, List<String> photosUrl, String typeSpectacle, String adresse, String dateHeure, double prix, String description, boolean accesHandicap, String interExter) {
        this.id = id;
        this.titre = titre;
        this.photosUrl = photosUrl;
        this.typeSpectacle = typeSpectacle;
        this.adresse = adresse;
        this.dateHeure = dateHeure;
        this.prix = prix;
        this.description = description;
        this.accesHandicap = accesHandicap;
        this.interExter = interExter;
        this.selected = false;
        this.loading = false;
        this.loadingProgress = 4;
        this.favourite = false;
        this.zoom = 10;
        this.bearing = 12;
        this.tilt = 12;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public List<String> getPhotosUrl() {
        return photosUrl;
    }

    public void setPhotosUrl(List<String> photosUrl) {
        this.photosUrl = photosUrl;
    }

    public String getTypeSpectacle() {
        return typeSpectacle;
    }

    public void setTypeSpectacle(String typeSpectacle) {
        this.typeSpectacle = typeSpectacle;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(String dateHeure) {
        this.dateHeure = dateHeure;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getAccesHandicap() {
        return accesHandicap;
    }

    public void setAccesHandicap(boolean accesHandicap) {
        this.accesHandicap = accesHandicap;
    }

    public String getInterExter() {
        return interExter;
    }

    public void setInterExter(String interExter) {
        this.interExter = interExter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public long getLoadingProgress() {
        return loadingProgress;
    }

    public void setLoadingProgress(long loadingProgress) {
        this.loadingProgress = loadingProgress;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public long getBearing() {
        return bearing;
    }

    public void setBearing(long bearing) {
        this.bearing = bearing;
    }

    public long getTilt() {
        return tilt;
    }

    public void setTilt(long tilt) {
        this.tilt = tilt;
    }
}
