package com.example.jeuduloup2;

public class Vegetaux extends Elements {
    protected int nutrition;
    protected boolean libre;

    public Vegetaux(int nutrition, int x, int y) {
        super(x, y);
        this.nutrition = nutrition;
        this.libre = true;
    }

    public Vegetaux() {
        super();
        this.nutrition = 0;
        this.libre = false;
    }

    public int getNutrition() {
        return nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public boolean isLibre() {
        return libre;
    }

    public void setLibre(boolean libre) {
        this.libre = libre;
    }
}
