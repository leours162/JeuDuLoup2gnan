package com.example.jeuduloup2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AetoileTest {
    @BeforeEach
    void setUp() {
        Grille grille=new Grille(13,16);
        grille.miseEnPlace();
        grille.remplacer(2,1,new Rocher(2,1));
        grille.remplacer(3,1,new Rocher(3,1));
        grille.remplacer(2,2,new Rocher(2,2));
        grille.remplacer(3,2,new Rocher(3,2));
        grille.remplacer(5,2,new Rocher(5,2));
        grille.remplacer(7,2,new Rocher(7,2));
        grille.remplacer(2,4,new Rocher(2,4));
        grille.remplacer(3,4,new Rocher(3,4));
        grille.remplacer(5,4,new Rocher(5,4));
        grille.remplacer(6,4,new Rocher(6,4));

    }

    @Test
    void aEtoile() {
    }
}