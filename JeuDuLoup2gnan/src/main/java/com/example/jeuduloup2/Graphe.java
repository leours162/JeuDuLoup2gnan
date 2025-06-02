package com.example.jeuduloup2;

import java.util.ArrayList;

public class Graphe {
    private Noeud[] nodes;
    private int nbNoeuds;
    private Bord[] bords;
    private int nbBords;

    public Graphe(Bord[] bords) {
        this.bords = bords;
        this.nbNoeuds = calculerNbNoeuds(bords);
        this.nodes = new Noeud[this.nbNoeuds];
        for (int i = 0; i < this.nbNoeuds; i++) {
            this.nodes[i] = new Noeud();
        }
        this.nbBords = bords.length;

        for (Bord bord : bords) {
            nodes[bord.getIndiceDepart()].getBords().add(bord);
            nodes[bord.getIndiceArrive()].getBords().add(bord);
        }
    }

    public void calculerDistancesMinimales() {
        nodes[0].setDistanceAvecDebut(0);
        int noeudActuel = getDistanceMinimaleDuNoeud();

        while (noeudActuel != -1) {
            ArrayList<Bord> aretes = nodes[noeudActuel].getBords();
            for (Bord bord : aretes) {
                int voisinIndex = bord.getIndexVoisin(noeudActuel);
                if (!nodes[voisinIndex].isVisite()) {
                    int tentative = nodes[noeudActuel].getDistanceAvecDebut() + bord.getLongueur();
                    if (tentative < nodes[voisinIndex].getDistanceAvecDebut()) {
                        nodes[voisinIndex].setDistanceAvecDebut(tentative);
                    }
                }
            }
            nodes[noeudActuel].setVisite(true);
            noeudActuel = getDistanceMinimaleDuNoeud();
        }
    }

    private int getDistanceMinimaleDuNoeud() {
        int indice = -1;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < nodes.length; i++) {
            if (!nodes[i].isVisite() && nodes[i].getDistanceAvecDebut() < min) {
                min = nodes[i].getDistanceAvecDebut();
                indice = i;
            }
        }
        return indice;
    }

    public void afficherResult() {
        System.out.println("Nombre de noeuds = " + nbNoeuds);
        System.out.println("Nombre d'arêtes = " + nbBords + "\n");

        for (int i = 0; i < nodes.length; i++) {
            System.out.println("Distance minimale du noeud 0 au noeud " + i + " = " + nodes[i].getDistanceAvecDebut());
        }
    }

    private int calculerNbNoeuds(Bord[] bords) {
        int max = 0;
        for (Bord bord : bords) {
            max = Math.max(max, Math.max(bord.getIndiceDepart(), bord.getIndiceArrive()));
        }
        return max + 1;
    }
}
