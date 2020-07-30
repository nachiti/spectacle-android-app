package com.example.spectacleapp.models;

import java.util.List;

public class Utilisateur {
    private String username;
    private String password;
    private List<Spectacle> spectaclesFavoris;

    public Utilisateur() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Spectacle> getSpectaclesFavoris() {
        return spectaclesFavoris;
    }

    public void setSpectaclesFavoris(List<Spectacle> spectaclesFavoris) {
        this.spectaclesFavoris = spectaclesFavoris;
    }
}
