package com.example.jeuduloup2;

import java.util.ArrayList;

public class Noeud {
    private int distanceAvecDebut = Integer.MAX_VALUE;
    private boolean visite;
    private ArrayList<Bord> bords = new ArrayList<>();

    public int getDistanceAvecDebut() {
        return distanceAvecDebut;
    }

    public void setDistanceAvecDebut(int distanceAvecDebut) {
        this.distanceAvecDebut = distanceAvecDebut;
    }

    public boolean isVisite() {
        return visite;
    }

    public void setVisite(boolean visite) {
        this.visite = visite;
    }

    public ArrayList<Bord> getBords() {
        return bords;
    }

    public void setBords(ArrayList<Bord> bords) {
        this.bords = bords;
    }
}

