package com.example.jeuduloup2;

import com.example.jeuduloup2.Element;

public class Vegetaux extends Element {
    private int nutrition;
    private boolean libre;

    public Vegetaux(int nutrition,int x,int y) {
        super(x,y);
        this.nutrition = nutrition;
        this.libre = true;
    }

    public Vegetaux(){
        this.nutrition = 0;
        this.libre = false;
    }

    public int getNutrition() {
        return nutrition;
    }

    public boolean isLibre() {
        return libre;
    }}
