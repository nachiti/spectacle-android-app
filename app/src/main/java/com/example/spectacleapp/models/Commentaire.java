package com.example.spectacleapp.models;

import java.util.Date;

public class Commentaire {
    private String pseudonyme;
    private double note;
    private String texte;
    private Date date;

    public Commentaire() {
    }

    public Commentaire(String pseudonyme, double note, String texte) {
        this.pseudonyme = pseudonyme;
        this.note = note;
        this.texte = texte;
        this.date = new Date();
    }

    public String getPseudonyme() {
        return pseudonyme;
    }


    public double getNote() {
        return note;
    }

    public String getTexte() {
        return texte;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "pseudonyme='" + pseudonyme + '\'' +
                ", note=" + note +
                ", texte='" + texte + '\'' +
                ", date=" + date +
                '}';
    }
}
