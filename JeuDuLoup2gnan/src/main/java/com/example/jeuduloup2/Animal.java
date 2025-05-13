package com.example.jeuduloup2;

public class Animal extends Element {
    protected int vitesse;
    protected boolean vivant;
    protected int x, y;

    public Animal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Animal (int vitesse){
        super();
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


}
