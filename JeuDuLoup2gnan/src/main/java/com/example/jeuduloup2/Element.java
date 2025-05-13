package com.example.jeuduloup2;

public abstract class Element {
    protected int x;
    protected int y;
    protected boolean accessible;


    public Element(int x, int y) {
        this.x = x;
        this.y = y;
        this.accessible = true;
    }
    public Element() {
        this.x = 0;
        this.y = 0;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    };
    public void setY(int y) {
        this.y = y;
    }
    public boolean isAccessible() {
        return accessible;
    }
    public void pasDacces () {
        this.accessible = false;
    }
}

