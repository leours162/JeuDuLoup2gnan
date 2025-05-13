package com.example.jeuduloup2;

public class Cactus extends Vegetaux{
    private int nutrition;

    public Cactus(int vitesse) {
        super();
    }

    @Override
    public int getNutrition() {
        return nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }
}

