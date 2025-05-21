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

        // Trouver une case accessible pour démarrer
        for (int y = 0; y < nbLignes; y++) {
            for (int x = 0; x < nbColonnes; x++) {
                if (elements[x][y] != null && elements[x][y].isAccessible()) {
                    explorer(x, y, visite);
                    return verifierConnexite(visite);
                }
            }
        }

        // Aucune case accessible trouvée
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
        int v= e.getVitesse();
        int x= e.getX();
        int y= e.getY();
        int colonnes= this.getNbColonnes();
        int lignes= this.getNbLignes();
        int[][] res = new int[v*(2+v*2)][2];
        if (v==1){
            res=lesVoisins(x,y);
            return res;
        }
        else if (v==2){
            res=lesVoisins(x,y);
            int[][] res2= lesVoisins(x+1,y);
            int[][] res3=lesVoisins(x,y+1);
            int[][] res4=lesVoisins(x-1,y);
            int[][] res5=lesVoisins(x,y-1);
            for (int i=0;i<=2;i++){
                for (int j=0;j<=2;j++){
                    res[i+2][j]=res2[i][j];
                    res[i+4][j]=res3[i][j];
                    res[i+6][j]=res4[i][j];
                    res[i+8][j]=res5[i][j];
                }
            }
        }
        else if (v==3){
            res=lesVoisins(x,y);
            int[][] res2= lesVoisins(x+1,y);
            int[][] res3=lesVoisins(x,y+1);
            int[][] res4=lesVoisins(x-1,y);
            int[][] res5=lesVoisins(x,y-1);
            for (int i=0;i<=2;i++){
                for (int j=0;j<=2;j++){
                    res[i+2][j]=res2[i][j];
                    res[i+4][j]=res3[i][j];
                    res[i+6][j]=res4[i][j];
                    res[i+8][j]=res5[i][j];
                }
            }
            int[][] res12= lesVoisins(x+2,y);
            int[][] res13=lesVoisins(x,y+2);
            int[][] res14=lesVoisins(x-1,y+1);
            int[][] res15=lesVoisins(x-1,y-1);
            int[][] res16=lesVoisins(x+1,y-1);
            int[][] res17=lesVoisins(x+1,y+1);
            int[][] res18=lesVoisins(x,y-2);
            int[][] res19=lesVoisins(x,y+2);
            for (int i=0;i<=2;i++){
                for (int j=0;j<=2;j++){
                    res[i+10][j]=res12[i][j];
                    res[i+12][j]=res13[i][j];
                    res[i+14][j]=res14[i][j];
                    res[i+16][j]=res15[i][j];
                    res[i+18][j]=res16[i][j];
                    res[i+19][j]=res17[i][j];
                    res[i+20][j]=res18[i][j];
                    res[i+21][j]=res19[i][j];
                }
            }
        }
        else if (v==4){
            res=lesVoisins(x,y);
            int[][] res2= lesVoisins(x+1,y);
            int[][] res3=lesVoisins(x,y+1);
            int[][] res4=lesVoisins(x-1,y);
            int[][] res5=lesVoisins(x,y-1);
            for (int i=0;i<=2;i++){
                for (int j=0;j<=2;j++){
                    res[i+2][j]=res2[i][j];
                    res[i+4][j]=res3[i][j];
                    res[i+6][j]=res4[i][j];
                    res[i+8][j]=res5[i][j];
                }
            }
            int[][] res12= lesVoisins(x+2,y);
            int[][] res13=lesVoisins(x,y+2);
            int[][] res14=lesVoisins(x-1,y+1);
            int[][] res15=lesVoisins(x-1,y-1);
            int[][] res16=lesVoisins(x+1,y-1);
            int[][] res17=lesVoisins(x+1,y+1);
            int[][] res18=lesVoisins(x,y-2);
            int[][] res19=lesVoisins(x,y+2);
            for (int i=0;i<=2;i++){
                for (int j=0;j<=2;j++){
                    res[i+10][j]=res12[i][j];
                    res[i+12][j]=res13[i][j];
                    res[i+14][j]=res14[i][j];
                    res[i+16][j]=res15[i][j];
                    res[i+18][j]=res16[i][j];
                    res[i+20][j]=res17[i][j];
                    res[i+22][j]=res18[i][j];
                    res[i+24][j]=res19[i][j];
                }
            }
            int[][] res22= lesVoisins(x+3,y);
            int[][] res23=lesVoisins(x,y+3);
            int[][] res24=lesVoisins(x-1,y+2);
            int[][] res25=lesVoisins(x-1,y-2);
            int[][] res26=lesVoisins(x+1,y-2);
            int[][] res27=lesVoisins(x+1,y+2);
            int[][] res28=lesVoisins(x+2,y-1);
            int[][] res29=lesVoisins(x+2,y+1);
            int[][] res30=lesVoisins(x-2,y-1);
            int[][] res31=lesVoisins(x-2,y+1);
            int[][] res32=lesVoisins(x,y+3);
            int[][] res33=lesVoisins(x,y-3);
            for (int i=0;i<=2;i++){
                for (int j=0;j<=2;j++){
                    res[i+26][j]=res22[i][j];
                    res[i+28][j]=res23[i][j];
                    res[i+30][j]=res24[i][j];
                    res[i+32][j]=res25[i][j];
                    res[i+34][j]=res26[i][j];
                    res[i+36][j]=res27[i][j];
                    res[i+38][j]=res28[i][j];
                    res[i+40][j]=res29[i][j];
                    res[i+42][j]=res30[i][j];
                    res[i+44][j]=res31[i][j];
                    res[i+46][j]=res32[i][j];
                    res[i+48][j]=res33[i][j];
                }
            }

        }
        return res;

    }
    public int[][] lesVoisins(int x, int y) {
        int[][] res = new int[4][2];
        res[0][0] = x;
        res[0][1] = y+1;
        res[1][0] = x+1;
        res[1][1] = y-1;
        return res;
    }
}




