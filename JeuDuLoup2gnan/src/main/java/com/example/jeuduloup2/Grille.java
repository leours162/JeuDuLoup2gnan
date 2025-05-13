package com.example.jeuduloup2;

import java.util.ArrayList;

public class Grille {
    // Changer en matrice /!\ helloapplication va devoir changer
    private ArrayList<Elements> elements= new ArrayList<>();
    private int nbLignes;
    private int nbColonnes;

    public Grille(int nbLignes, int nbColonnes) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
    }

    public ArrayList<Elements> getElements()
    {
        //nul, a changer (plutot get element x y comme matrice )
        return elements;
    }
    public int getNbLignes() {
        return nbLignes;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }

    public void miseEnPlace() {
        this.nbLignes += 2;
        this.nbColonnes += 2;


        for (int i = 0; i < this.nbColonnes; i++) {
            elements.add(new Rocher(i, 0)); // haut
            elements.add(new Rocher(i, nbLignes - 1));
        }
        for (int i = 1; i < this.nbLignes - 1; i++) {
            elements.add(new Rocher(0, i)); // gauche
            elements.add(new Rocher(nbColonnes - 1, i));
        }


        for (int y = 1; y < this.nbLignes - 2; y++) {
            for (int x = 1; x < this.nbColonnes - 2; x++) {
                elements.add(new Herbe(x, y));
            }
        }
    }

    public void setSortie(int x, int y){
        for (int i=0; i<this.elements.size(); i++){
            if (this.elements.get(i).getX() == x && this.elements.get(i).getY() == y){
                this.elements.set(i,new Sortie(x,y));
            }
        }
    }
    public boolean sortieValide(){
        for (int i=0; i<this.elements.size();i++){
            if (this.elements.get(i) instanceof Sortie){
                if ((this.elements.get(i).getX()==0 || this.elements.get(i).getX()==this.nbColonnes)&&(this.elements.get(i).getY()==0 || this.elements.get(i).getY()==nbLignes )){
                    if ((this.elements.get(i).getX()==0 && this.elements.get(i).getY()==0) ||(this.elements.get(i).getX()==0 && this.elements.get(i).getY()==nbColonnes) || (this.elements.get(i).getX()==nbLignes && this.elements.get(i).getY()==nbColonnes) ||(this.elements.get(i).getX()==nbColonnes && this.elements.get(i).getY()==0)){
                        return false;
                    }
                }

            }
        }
        return true;
    }
}
