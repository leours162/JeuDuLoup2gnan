package com.example.jeuduloup2;

import com.example.jeuduloup2.Animal;

public class Mouton extends Animal {
    private int nbCactus;
    private int nbHerbe;
    private int nbMarguerite;
    public Mouton(int x, int y) {
        super(x, y);
        this.nbCactus = 0;
        this.nbHerbe = 0;
        this.nbMarguerite = 0;
    }

    public Mouton(int nbCactus, int nbHerbe, int nbMarguerite) {
        this.nbCactus = nbCactus;
        this.nbHerbe = nbHerbe;
        this.nbMarguerite = nbMarguerite;
    }

    public Mouton() {
        this.nbCactus = 0;
        this.nbHerbe = 0;
        this.nbMarguerite = 0;
    }

    public int getNbCactus() {
        return nbCactus;
    }

    public int getNbMarguerite() {
        return nbMarguerite;
    }

    public int getNbHerbe() {
        return nbHerbe;
    }

    public void Manger (){

    }
}
