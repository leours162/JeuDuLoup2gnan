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

    public void manger(int nutrition) {
        this.setVitesse(nutrition);
        if (nutrition == 1) {
            nbCactus++;
        } else if (nutrition == 2) {
            nbHerbe++;
        } else if (nutrition == 4) {
            nbMarguerite++;
        }
    }
}
