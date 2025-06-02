package com.example.jeuduloup2;

public class Bord {
    private int indiceDepart;
    private int indiceArrive;
    private int longueur;

    public Bord(int indiceDepart, int indiceArrive, int longueur) {
        this.indiceDepart = indiceDepart;
        this.indiceArrive = indiceArrive;
        this.longueur = longueur;
    }

    public int getIndiceDepart() {
        return indiceDepart;
    }

    public int getIndiceArrive() {
        return indiceArrive;
    }

    public int getLongueur() {
        return longueur;
    }

    // Renvoie l'autre extrémité de l'arête
    public int getIndexVoisin(int indiceNoeud) {
        return (this.indiceDepart == indiceNoeud) ? this.indiceArrive : this.indiceDepart;
    }
}

