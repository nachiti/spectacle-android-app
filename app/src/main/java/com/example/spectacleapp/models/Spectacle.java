package com.example.spectacleapp.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Spectacle {
    private String id;
    private String titre;
    private List<String> photosUrl;
    private TypeSpectacle typeSpectacle;
    private double latitude, longitude;
    private String adresse;
    private Date dateHeure;
    private double prix;
    private String description;
    private boolean accesHadicap;
    private InterExter interExter;
    private boolean favourite;
    private List<Commentaire> commentaires;

    public Spectacle() {
    }

    public Spectacle(String id, String titre, List<String> photosUrl, TypeSpectacle typeSpectacle, double latitude, double longitude, String adresse, Date dateHeure, double prix, String description, boolean accesHadicap, InterExter interExter) {
        this.id = id;
        this.titre = titre;
        this.photosUrl = photosUrl;
        this.typeSpectacle = typeSpectacle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresse = adresse;
        this.dateHeure = dateHeure;
        this.prix = prix;
        this.description = description;
        this.accesHadicap = accesHadicap;
        this.interExter = interExter;
        this.commentaires = new ArrayList<>();
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

    public TypeSpectacle getTypeSpectacle() {
        return typeSpectacle;
    }

    public void setTypeSpectacle(TypeSpectacle typeSpectacle) {
        this.typeSpectacle = typeSpectacle;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(Date dateHeure) {
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

    public boolean isAccesHadicap() {
        return accesHadicap;
    }

    public void setAccesHadicap(boolean accesHadicap) {
        this.accesHadicap = accesHadicap;
    }

    public InterExter getInterExter() {
        return interExter;
    }

    public void setInterExter(InterExter interExter) {
        this.interExter = interExter;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    @Override
    public String toString() {
        return "Spectacle{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", photosUrl=" + photosUrl +
                ", typeSpectacle=" + typeSpectacle +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", adresse='" + adresse + '\'' +
                ", dateHeure=" + dateHeure +
                ", prix=" + prix +
                ", description='" + description + '\'' +
                ", accesHadicap=" + accesHadicap +
                ", interExter=" + interExter +
                ", favourite=" + favourite +
                ", commentaires=" + commentaires +
                '}';
    }
}



