package com.example.jeuduloup2;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;


import java.util.ArrayList;

public class Grille {
    private Elements[][] elements;
    private int nbLignes;
    private int nbColonnes;

    public Grille(int nbLignes, int nbColonnes) {
        this.nbLignes = nbLignes + 2;
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

    // Méthode servant a créer une grille basique remplie d'herbe et entourée de rochers
    public void miseEnPlace() {
        // Creer le contour de rochers
        for (int x = 0; x < nbColonnes; x++) {
            elements[x][0] = new Rocher(x, 0);
            elements[x][nbLignes - 1] = new Rocher(x, nbLignes - 1);
        }

        for (int y = 1; y < nbLignes - 1; y++) {
            elements[0][y] = new Rocher(0, y);
            elements[nbColonnes - 1][y] = new Rocher(nbColonnes - 1, y);
        }
        // Remplie la grille d'herbe
        for (int y = 1; y < nbLignes - 1; y++) {
            for (int x = 1; x < nbColonnes - 1; x++) {
                elements[x][y] = new Herbe(x, y);
            }
        }
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
    // Méthode our changer une element en un autre a l'aide de ses coordonées
    public void remplacer(int x, int y, Elements e) {
        elements[x][y] = e;
    }

    //Créé la liste des déplacements possibles
    public int[][] lesDeplacements(Animal e) {
        // recupère la vitesse de l'animal
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

            if (targetX >= 0 && targetX < this.elements.length &&
                    targetY >= 0 && targetY < this.elements[0].length &&
                    this.elements[targetX][targetY] != null &&
                    this.elements[targetX][targetY].isAccessible()) {

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
            int distance = dx + dy;

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


    public ArrayList<int[]> cheminLePlusCourt(int departX, int departY, int arriveeX, int arriveeY, Animal animal) {
        int[][] distances = new int[nbColonnes][nbLignes];                     // Distance minimale depuis la case de départ
        boolean[][] dejaVisite = new boolean[nbColonnes][nbLignes];           // Marque les cases déjà explorées
        int[][][] precedent = new int[nbColonnes][nbLignes][2];               // Pour reconstituer le chemin final
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};               // Déplacements possibles : haut, droite, bas, gauche

        // Initialisation : toutes les distances sont infinies sauf le point de départ
        for (int x = 0; x < nbColonnes; x++) {
            Arrays.fill(distances[x], Integer.MAX_VALUE);
        }
        distances[departX][departY] = 0;

        // Algorithme de Dijkstra (version simple sans file de priorité)
        while (true) {
            int distMin = Integer.MAX_VALUE;
            int caseX = -1, caseY = -1;

            // On cherche la case non encore visitée ayant la plus petite distance
            for (int x = 0; x < nbColonnes; x++) {
                for (int y = 0; y < nbLignes; y++) {
                    if (!dejaVisite[x][y] && distances[x][y] < distMin) {
                        distMin = distances[x][y];
                        caseX = x;
                        caseY = y;
                    }
                }
            }

            // Si aucune case n'a été trouvée ou si on a atteint la cible, on sort de la boucle
            if (caseX == -1 || (caseX == arriveeX && caseY == arriveeY)) break;

            dejaVisite[caseX][caseY] = true;

            // Pour chaque direction, on regarde les voisins accessibles
            for (int[] direction : directions) {
                int voisinX = caseX + direction[0];
                int voisinY = caseY + direction[1];

                // Vérification des limites de la grille
                if (voisinX < 0 || voisinY < 0 || voisinX >= nbColonnes || voisinY >= nbLignes)
                    continue;

                Elements voisin = elements[voisinX][voisinY];
                if (voisin == null || !voisin.isAccessible()) continue;

                // Le loup ne peut pas aller sur une case de sortie
                if (animal instanceof Loup && voisin instanceof Sortie) continue;

                // Calcul du coût pour aller sur la case voisine
                int cout = getCoutDeplacement(voisin, animal);
                int nouvelleDistance = distances[caseX][caseY] + cout;

                if (nouvelleDistance < distances[voisinX][voisinY]) {
                    distances[voisinX][voisinY] = nouvelleDistance;
                    precedent[voisinX][voisinY][0] = caseX;
                    precedent[voisinX][voisinY][1] = caseY;
                }
            }
        }

        // Reconstruction du chemin depuis la cible vers le départ
        ArrayList<int[]> chemin = new ArrayList<>();
        if (distances[arriveeX][arriveeY] == Integer.MAX_VALUE) return chemin; // Aucun chemin trouvé

        int x = arriveeX, y = arriveeY;
        while (x != departX || y != departY) {
            chemin.add(0, new int[]{x, y});
            int precX = precedent[x][y][0];
            int precY = precedent[x][y][1];
            x = precX;
            y = precY;
        }
        chemin.add(0, new int[]{departX, departY});

        return chemin;
    }

private int getCoutDeplacement(Elements e, Animal a) {
    // On peut adapter selon le type d’élément
    if (e instanceof Herbe) return 1;
    if (e instanceof Sortie) return 1;
    if (e instanceof Rocher) return Integer.MAX_VALUE; // Devrait être inaccessible
    return 1;
}
    public int manhattan(int debx , int deby, int finx , int finy){
        return Math.abs(debx-finx) + Math.abs(deby-finy);
    }

    public int[][] heuristic(int x , int y){
        int[][] heuristic = new int[nbLignes][nbColonnes];
        for (int i = 0; i < nbLignes; i++){
            for (int j = 0; j < nbColonnes; j++){
                heuristic[i][j] = manhattan(i, j, x, y);
            }
        }
        return heuristic;}
    public int[][] heuristique2(int x, int y) {
        int rows = elements.length;
        int cols = elements[0].length;

        int[][] distances = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                distances[i][j] = -1;
            }
        }


        ArrayList<int[]> currentLevel = new ArrayList<>();

        ArrayList<int[]> nextLevel = new ArrayList<>();


        currentLevel.add(new int[]{x, y});
        distances[x][y] = 0;

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int currentDistance = 0;

        while (!currentLevel.isEmpty()) {

            for (int i = 0; i < currentLevel.size(); i++) {
                int[] current = currentLevel.get(i);
                int currentX = current[0];
                int currentY = current[1];
                for (int[] dir : directions) {
                    int newX = currentX + dir[0];
                    int newY = currentY + dir[1];
                    if (newX >= 0 && newX < rows &&
                            newY >= 0 && newY < cols &&
                            elements[newX][newY] != null &&  // protection ajoutée ici
                            elements[newX][newY].isAccessible() &&
                            distances[newX][newY] == -1) {

                        distances[newX][newY] = currentDistance + 1;
                        nextLevel.add(new int[]{newX, newY});
                    }
                }
            }
            currentLevel.clear();
            currentLevel.addAll(nextLevel);
            nextLevel.clear();
            currentDistance++;
        }

        return distances;
    }
    public ArrayList<int[]> aEtoile(int depx, int depy, int finx, int finy) {
        int[][] heuristic = heuristique2(depx, depy);
        for (int i = 0; i < heuristic.length; i++){
            for (int j = 0; j < heuristic[0].length; j++){
                if (heuristic[i][j] == -1){
                    heuristic[i][j]=999999999;
                }
            }
        }
        ArrayList<int[]> leChemin = new ArrayList<>();

        int currentX = depx;
        int currentY = depy;


        leChemin.add(new int[]{currentX, currentY});


        while (currentX != finx || currentY != finy) {
            int bestX = currentX;
            int bestY = currentY;
            int bestHeuristic = Integer.MAX_VALUE;



            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};


            for (int[] dir : directions) {
                int newX = currentX + dir[0];
                int newY = currentY + dir[1];

                if (newX >= 0 && newX < nbLignes && newY >= 0 && newY < nbColonnes) {

                    if (heuristic[newX][newY] < bestHeuristic) {
                        bestHeuristic = heuristic[newX][newY];
                        bestX = newX;
                        bestY = newY;
                    }
                }
            }
            currentX = bestX;
            currentY = bestY;
            leChemin.add(new int[]{currentX, currentY});
        }
        return leChemin;
    }

}





