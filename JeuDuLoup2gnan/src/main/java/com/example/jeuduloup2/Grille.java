package com.example.jeuduloup2;

import java.util.ArrayList;

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

    public Elements getElement(int x, int y) {
        return elements[x][y];
    }

    public int getNbLignes() {
        return nbLignes;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }

    public void miseEnPlace() {
        for (int x = 0; x < nbColonnes; x++) {
            elements[x][0] = new Rocher(x, 0);
            elements[x][nbLignes - 1] = new Rocher(x, nbLignes - 1);
        }

        for (int y = 1; y < nbLignes - 1; y++) {
            elements[0][y] = new Rocher(0, y);
            elements[nbColonnes - 1][y] = new Rocher(nbColonnes - 1, y);
        }

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

        for (int y = 0; y < nbLignes; y++) {
            for (int x = 0; x < nbColonnes; x++) {
                if (elements[x][y] != null && elements[x][y].isAccessible()) {
                    explorer(x, y, visite);
                    return verifierConnexite(visite);
                }
            }
        }

        return false;
    }

    private void explorer(int x, int y, boolean[][] visite) {
        if (x < 0 || x >= nbColonnes || y < 0 || y >= nbLignes) return;
        if (visite[x][y]) return;
        if (!elements[x][y].isAccessible()) return;

        visite[x][y] = true;

        explorer(x + 1, y, visite);
        explorer(x - 1, y, visite);
        explorer(x, y + 1, visite);
        explorer(x, y - 1, visite);
    }
    private boolean verifierConnexite(boolean[][] visite) {
        for (int y = 0; y < nbLignes; y++) {
            for (int x = 0; x < nbColonnes; x++) {
                if (elements[x][y] != null && elements[x][y].isAccessible() && !visite[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }
    public void remplacer(int x, int y, Elements e) {
        elements[x][y] = e;
    }
    public int[][] lesDeplacements(Animal e) {
        int v = e.getVitesse();

        if (e instanceof Mouton) {
            Mouton mouton = (Mouton) e;
            v = mouton.getVitesse();
        }

        int x = e.getX();
        int y = e.getY();

        int maxDeplacements = 0;
        if (v >= 1) maxDeplacements += 4;     // 4 cases adjacentes
        if (v >= 2) maxDeplacements += 8;     // 8 cases supplémentaires à distance 2
        if (v >= 3) maxDeplacements += 12;    // 12 cases supplémentaires à distance 3
        if (v >= 4) maxDeplacements += 16;    // 16 cases supplémentaires à distance 4

        int[][] res = new int[maxDeplacements][2];
        int index = 0;

        if (v >= 1) {
            int[][] res1 = lesVoisins(x, y);
            for (int i = 0; i < 4; i++) {
                res[index][0] = res1[i][0];
                res[index][1] = res1[i][1];
                index++;
            }
        }

        if (v >= 2) {
            int[][] res2 = lesVoisin2(x, y);
            for (int i = 0; i < 8; i++) {
                res[index][0] = res2[i][0];
                res[index][1] = res2[i][1];
                index++;
            }
        }

        if (v >= 3) {
            int[][] res3 = lesVoisin3(x, y);
            for (int i = 0; i < 12; i++) {
                res[index][0] = res3[i][0];
                res[index][1] = res3[i][1];
                index++;
            }
        }

        if (v >= 4) {
            int[][] res4 = lesVoisin4(x, y);
            for (int i = 0; i < 16; i++) {
                res[index][0] = res4[i][0];
                res[index][1] = res4[i][1];
                index++;
            }
        }

        ArrayList<int[]> coordonneesValides = new ArrayList<>();

        for (int i = 0; i < maxDeplacements; i++) {
            int[] coord = res[i];
            int targetX = coord[0];
            int targetY = coord[1];

            if (targetX >= 0 && targetX < nbColonnes && targetY >= 0 && targetY < nbLignes &&
                    elements[targetX][targetY].isAccessible()) {

                if (e instanceof Mouton && elements[targetX][targetY] instanceof Sortie) {
                    coordonneesValides.add(coord);
                }
                else if (e instanceof Loup && elements[targetX][targetY] instanceof Mouton) {
                    coordonneesValides.add(coord);
                }
                else if (elements[targetX][targetY].isAccessible() && cheminLibre(x, y, targetX, targetY)) {
                    coordonneesValides.add(coord);
                }
            }
        }

        int[][] res2 = new int[coordonneesValides.size()][2];
        for (int i = 0; i < coordonneesValides.size(); i++) {
            res2[i] = coordonneesValides.get(i);
        }
        return res2;
    }
    public int[][] lesVoisins(int x, int y) {
        int[][] res = new int[4][2];
        res[0][0] = x;
        res[0][1] = y+1;
        res[1][0] = x+1;
        res[1][1] = y;
        res[2][0] = x-1;
        res[2][1] = y;
        res[3][0] = x;
        res[3][1] = y-1;
        return res;
    }
    public int[][] lesVoisin2(int x, int y) {
        int[][] res = new int[8][2];
        res[0][0] = x;
        res[0][1] = y+2;
        res[1][0] = x;
        res[1][1] = y-2;
        res[2][0] = x+2;
        res[2][1] = y;
        res[3][0] = x-2;
        res[3][1] = y;
        res[4][0] = x-1;
        res[4][1] = y-1;
        res[5][0] = x-1;
        res[5][1] = y+1;
        res[6][0] = x+1;
        res[6][1] = y+1;
        res[7][0] = x+1;
        res[7][1] = y-1;
        return res;
    }
    public int[][] lesVoisin3(int x, int y) {
        int[][] res = new int[12][2];
        res[0][0] = x;
        res[0][1] = y+3;
        res[1][0] = x+1;
        res[1][1] = y+2;
        res[2][0] = x-1;
        res[2][1] = y+2;
        res[3][0] = x+1;
        res[3][1] = y-2;
        res[4][0] = x-1;
        res[4][1] = y-2;
        res[5][0] = x+2;
        res[5][1] = y+1;
        res[6][0] = x-2;
        res[6][1] = y+1;
        res[7][0] = x-2;
        res[7][1] = y-1;
        res[8][0] = x+2;
        res[8][1] = y-1;
        res[9][0] = x-3;
        res[9][1] = y;
        res[10][0] = x+3;
        res[10][1] = y;
        res[11][0] = x;
        res[11][1] = y-3;
        return res;
    }
    public int[][] lesVoisin4(int x, int y) {
        int[][] res = new int[16][2];
        res[0][0] = x;
        res[0][1] = y+4;

        res[1][0] = x;
        res[1][1] = y-4;

        res[2][0] = x+1;
        res[2][1] = y-3;

        res[3][0] = x+1;
        res[3][1] = y+3;

        res[4][0] = x-1;
        res[4][1] = y+3;

        res[5][0] = x-1;
        res[5][1] = y-3;

        res[6][0] = x+2;
        res[6][1] = y+2;

        res[7][0] = x+2;
        res[7][1] = y-2;

        res[8][0] = x-2;
        res[8][1] = y-2;

        res[9][0] = x-2;
        res[9][1] = y+2;

        res[10][0] = x+3;
        res[10][1] = y+1;

        res[11][0] = x+3;
        res[11][1] = y-1;

        res[12][0] = x-3;
        res[12][1] = y-1;

        res[13][0] = x-3;
        res[13][1] = y+1;

        res[14][0] = x+4;
        res[14][1] = y;

        res[15][0] = x-4;
        res[15][1] = y;
        return res;
    }
    private boolean cheminLibre(int x1, int y1, int x2, int y2) {

        if ((Math.abs(x2 - x1) <= 1 && Math.abs(y2 - y1) <= 1)) {
            return true;
        }


        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;


        int currentX = x1;
        int currentY = y1;

        while (currentX != x2 || currentY != y2) {

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                currentX += sx;
            }
            if (e2 < dx) {
                err += dx;
                currentY += sy;
            }


            if (currentX != x2 || currentY != y2) {

                if (currentX < 0 || currentX >= nbColonnes ||
                        currentY < 0 || currentY >= nbLignes ||
                        !elements[currentX][currentY].isAccessible()) {
                    return false;
                }
            }
        }


        return true;}

    public boolean seDeplacer(Animal a, int x, int y) {
        if (a instanceof Mouton) {
            Mouton mouton = (Mouton) a;
            int dx = Math.abs(x - a.getX());
            int dy = Math.abs(y - a.getY());
            int distance = dx + dy;  // Distance de Manhattan

            if (distance > mouton.getVitesse()) {
                return false;
            }
        }

        int[][] deplacements = lesDeplacements(a);
        boolean possible = false;
        for (int i = 0; i < deplacements.length; i++) {
            if (deplacements[i][0] == x && deplacements[i][1] == y) {
                possible = true;
                break;
            }
        }

        if (possible) {
            a.bouger(x, y);
        }
        return possible;
    }

    public void setNbLignes(int nbLignes) {
        this.nbLignes = nbLignes;
    }

    public void setNbColonnes(int nbColonnes) {
        this.nbColonnes = nbColonnes;
    }
}





