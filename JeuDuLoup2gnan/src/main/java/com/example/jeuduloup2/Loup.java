package com.example.jeuduloup2;

public class Loup extends Animal {
    private int vitesse;

    public Loup(int x, int y) {
        super(x, y);
        this.vitesse = 3; // valeur par défaut si tu veux
    }

    public int getVitesse() { return vitesse; }
}
