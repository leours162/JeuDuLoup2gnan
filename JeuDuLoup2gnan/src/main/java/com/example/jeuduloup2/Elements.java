package com.example.jeuduloup2;

public abstract class Elements {
    protected int x;
    protected int y;
    protected boolean accessible;

    
    public Elements(int x, int y) {
        this.x = x;
        this.y = y;
        this.accessible = true; 
    }

    
    public Elements() {
        this.x = 0;
        this.y = 0;
        this.accessible = true;
    }

    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAccessible() {
        return accessible;
    }

   
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    
    public void rendreInaccessible() {
        this.accessible = false;
    }
}
