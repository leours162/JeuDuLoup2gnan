package com.example.jeuduloup2;

public class Grille {
    private Elements[][] elements;
    private int nbLignes;
    private int nbColonnes;

    public Grille(int nbLignes, int nbColonnes) {
        this.nbLignes = nbLignes + 2; // Ajout des bordures
        this.nbColonnes = nbColonnes + 2;
        this.elements = new Elements[this.nbColonnes][this.nbLignes];
    }


    public void setElement(int x, int y, Elements e) {
        elements[x][y] = e;
    }
    public Elements getElement(int x, int y){
        return elements[x][y];
    }

    public int getNbLignes() {
        return nbLignes;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }

    public void miseEnPlace() {
        // Bordures haut et bas
        for (int x = 0; x < nbColonnes; x++) {
            elements[x][0] = new Rocher(x, 0);
            elements[x][nbLignes - 1] = new Rocher(x, nbLignes - 1);
        }

        // Bordures gauche et droite
        for (int y = 1; y < nbLignes - 1; y++) {
            elements[0][y] = new Rocher(0, y);
            elements[nbColonnes - 1][y] = new Rocher(nbColonnes - 1, y);
        }

        // Remplissage intérieur avec de l'herbe
        for (int y = 1; y < nbLignes - 1; y++) {
            for (int x = 1; x < nbColonnes - 1; x++) {
                elements[x][y] = new Herbe(x, y);
            }
        }
    }

    public void setSortie(int x, int y) {
        elements[x][y] = new Sortie(x, y);
    }

    public boolean sortieValide() {
        for (int x = 0; x < nbColonnes; x++) {
            for (int y = 0; y < nbLignes; y++) {
                if (elements[x][y] instanceof Sortie) {
                    boolean surBord = (x == 0 || x == nbColonnes - 1 || y == 0 || y == nbLignes - 1);
                    boolean enCoin = (x == 0 && y == 0) ||
                            (x == 0 && y == nbLignes - 1) ||
                            (x == nbColonnes - 1 && y == 0) ||
                            (x == nbColonnes - 1 && y == nbLignes - 1);
                    if (surBord && !enCoin) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean estConnexe() {
        boolean[][] visite = new boolean[nbColonnes][nbLignes];

        // Trouver une case Herbe pour démarrer
        for (int y = 0; y < nbLignes; y++) {
            for (int x = 0; x < nbColonnes; x++) {
                if (elements[x][y] instanceof Herbe) {
                    // Lancer le DFS
                    explorer(x, y, visite);
                    // Vérifier ensuite si tout est visité
                    return verifierConnexite(visite);
                }
            }
        }

        // Aucune case Herbe trouvée
        return false;
    }

    private void explorer(int x, int y, boolean[][] visite) {
        if (x < 0 || x >= nbColonnes || y < 0 || y >= nbLignes) return;
        if (visite[x][y]) return;
        if (!(elements[x][y] instanceof Herbe || elements[x][y] instanceof Sortie)) return;

        visite[x][y] = true;

        explorer(x + 1, y, visite);
        explorer(x - 1, y, visite);
        explorer(x, y + 1, visite);
        explorer(x, y - 1, visite);
    }

    private boolean verifierConnexite(boolean[][] visite) {
        for (int y = 0; y < nbLignes; y++) {
            for (int x = 0; x < nbColonnes; x++) {
                if ((elements[x][y] instanceof Herbe || elements[x][y] instanceof Sortie) && !visite[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }
    private void remplacer (int x, int y,Elements e) {
        elements[x][y] = e;
    }
}
