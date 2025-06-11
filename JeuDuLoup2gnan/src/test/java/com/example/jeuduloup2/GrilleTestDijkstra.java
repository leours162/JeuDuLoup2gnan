package com.example.jeuduloup2;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class GrilleTestDijkstra {

    private Grille creerGrilleBasique() {
        Grille g = new Grille(5, 5);
        g.miseEnPlace();
        return g;
    }

    @Test
    public void testCheminSimple() {
        Grille g = creerGrilleBasique();
        Mouton mouton = new Mouton(1, 1);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(1, 1, 3, 3, mouton);
        assertFalse(chemin.isEmpty());
        assertEquals(1, chemin.get(0)[0]);
        assertEquals(1, chemin.get(0)[1]);
        assertEquals(3, chemin.get(chemin.size() - 1)[0]);
        assertEquals(3, chemin.get(chemin.size() - 1)[1]);
    }

    @Test
    public void testDepartEgalArrivee() {
        Grille g = creerGrilleBasique();
        Loup loup = new Loup(2, 2);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(2, 2, 2, 2, loup);
        assertEquals(1, chemin.size());
        assertEquals(2, chemin.get(0)[0]);
        assertEquals(2, chemin.get(0)[1]);
    }

    @Test
    public void testAucunCheminCauseRocher() {
        Grille g = creerGrilleBasique();
        g.setElement(2, 1, new Rocher(2, 1));
        g.setElement(2, 2, new Rocher(2, 2));
        g.setElement(2, 3, new Rocher(2, 3));
        g.setElement(1, 1, new Rocher(1, 1));
        g.setElement(1, 3, new Rocher(1, 3));
        g.setElement(3, 1, new Rocher(3, 1));
        g.setElement(3, 3, new Rocher(3, 3));

        Mouton mouton = new Mouton(1, 2);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(1, 2, 3, 2, mouton);
        assertTrue(chemin.isEmpty());
    }

    @Test
    public void testCibleInaccessible() {
        Grille g = creerGrilleBasique();
        g.setElement(3, 3, new Rocher(3, 3));
        Mouton mouton = new Mouton(1, 1);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(1, 1, 3, 3, mouton);
        assertTrue(chemin.isEmpty());
    }

    @Test
    public void testLoupVersSortieInterdit() {
        Grille g = creerGrilleBasique();
        g.setElement(3, 3, new Sortie(3, 3));
        Loup loup = new Loup(1, 1);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(1, 1, 3, 3, loup);
        assertTrue(chemin.isEmpty());
    }

    @Test
    public void testMoutonVersSortieOK() {
        Grille g = creerGrilleBasique();
        g.setElement(3, 3, new Sortie(3, 3));
        Mouton mouton = new Mouton(1, 1);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(1, 1, 3, 3, mouton);
        assertFalse(chemin.isEmpty());
    }

    @Test
    public void testDepartBloqueParRochers() {
        Grille g = creerGrilleBasique();
        int x = 2, y = 2;
        g.setElement(x + 1, y, new Rocher(x + 1, y));
        g.setElement(x - 1, y, new Rocher(x - 1, y));
        g.setElement(x, y + 1, new Rocher(x, y + 1));
        g.setElement(x, y - 1, new Rocher(x, y - 1));
        Mouton mouton = new Mouton(x, y);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(x, y, 3, 3, mouton);
        assertTrue(chemin.isEmpty());
    }

    @Test
    public void testCheminAvecDetour() {
        Grille g = creerGrilleBasique();
        // On crée un petit labyrinthe avec un passage étroit
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                if (!(i == 1 || j == 3)) {
                    g.setElement(i, j, new Rocher(i, j));
                }
            }
        }
        Mouton mouton = new Mouton(1, 1);
        ArrayList<int[]> chemin = g.cheminLePlusCourt(1, 1, 3, 3, mouton);
        assertFalse(chemin.isEmpty());
    }
}
