package com.example.spectacleapp.models;

import java.io.Serializable;
import java.util.List;

public class Search implements Serializable {
    private String ville;
    private List<TypeSpectacle> typeSpectacleList;
    private int prixMax;
    private int prixMin;
    private List<InterExter> interExterList;
    private List<Boolean> accesHandicapList;

    public Search() {
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public List<TypeSpectacle> getTypeSpectacleList() {
        return typeSpectacleList;
    }

    public void setTypeSpectacleList(List<TypeSpectacle> typeSpectacleList) {
        this.typeSpectacleList = typeSpectacleList;
    }

    public int getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(int prixMin) {
        this.prixMin = prixMin;
    }

    public int getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(int prixMax) {
        this.prixMax = prixMax;
    }

    public List<InterExter> getInterExterList() {
        return interExterList;
    }

    public void setInterExterList(List<InterExter> interExterList) {
        this.interExterList = interExterList;
    }

    public List<Boolean> getAccesHandicapList() {
        return accesHandicapList;
    }

    public void setAccesHandicapList(List<Boolean> accesHandicapList) {
        this.accesHandicapList = accesHandicapList;
    }

    @Override
    public String toString() {
        return "Search{" +
                "ville='" + ville + '\'' +
                ", type=" + typeSpectacleList +
                ", prixMin=" + prixMin +
                ", prixMax=" + prixMax +
                ", interExter=" + interExterList +
                ", AccesHandicap=" + accesHandicapList +
                '}';
    }
}

