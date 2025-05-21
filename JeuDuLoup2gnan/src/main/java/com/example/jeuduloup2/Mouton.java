package com.example.jeuduloup2;

public class Mouton extends Animal {
    private int nbCactus;
    private int nbHerbe;
    private int nbMarguerite;

    public Mouton(int x, int y) {
        super(2, x, y);
        this.nbCactus = 0;
        this.nbHerbe = 0;
        this.nbMarguerite = 0;
    }

    public int getNbCactus() {
        return nbCactus;
    }

    public void setNbCactus(int nbCactus) {
        this.nbCactus = nbCactus;
    }

    public int getNbHerbe() {
        return nbHerbe;
    }

    public void setNbHerbe(int nbHerbe) {
        this.nbHerbe = nbHerbe;
    }

    public int getNbMarguerite() {
        return nbMarguerite;
    }

    public void setNbMarguerite(int nbMarguerite) {
        this.nbMarguerite = nbMarguerite;
    }

    public void manger(Vegetaux v) {
        this.setVitesse(v.getNutrition());
        if (v.getNutrition() == 1) {
            this.vitesse=1;
            nbCactus++;
        } else if (v.getNutrition() == 2) {
            this.vitesse=2;
            nbHerbe++;
        } else if (v.getNutrition() == 4) {
            this.vitesse=4;
            nbMarguerite++;
        }
    }
}
