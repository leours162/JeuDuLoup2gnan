package com.example.jeuduloup2;

import com.example.jeuduloup2.Elements;

public abstract class Animal extends Elements {
    protected int vitesse;
    protected boolean vivant;

    public Animal (int vitesse,int x, int y) {
        super(x,y);
        this.vitesse = vitesse;
        this.vivant = true;
    }

    public Animal(){
        this.vitesse = 0;
    }

    public int getVitesse(){
        return this.vitesse;
    }
    public void setVitesse(int vitesse){
        this.vitesse = vitesse;
    }
    public boolean estVivant(){
        return this.vivant;
    }
    public void mourir(boolean vivant){
        this.vivant = false;
    }


    public void bouger (int x, int y){
        this.x = x;
        this.y = y;
    }
}
