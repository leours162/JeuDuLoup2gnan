package com.example.jeuduloup2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrilleTest {
    private Grille grille;

    @BeforeEach
    void setUp() {
        grille=new Grille(10,10);
        grille.miseEnPlace();

    }
    @AfterEach
    void tearDown() {
        grille=null;
    }

    @Test
    void lesDeplacements() {
        Mouton mouton=new Mouton(1,1);
        mouton.setVitesse(4);
        int[][] deplacements= grille.lesDeplacements(mouton);
        int[][] attendu={{1, 2}, {2, 1}, {0, 1}, {1, 0}, {1, 3}, {3, 1}, {0, 0}, {0, 2}, {2, 2}, {2, 0}, {1, 4}, {2, 3}, {0, 3}, {3, 2}, {3, 0}, {4, 1}, {1, 5}, {2, 4},{ 0, 4}, {3, 3},{4, 2}, {4, 0}, {5, 1}};
        assertEquals(4, attendu.length);

    }
    @Test
    void lesDeplacements2() {
        Mouton mouton=new Mouton(1,1);
        mouton.setVitesse(0);
        int[][] deplacements=grille.lesDeplacements(mouton);
        int[][] attendu={};
        assertEquals(4,attendu);
    }
}