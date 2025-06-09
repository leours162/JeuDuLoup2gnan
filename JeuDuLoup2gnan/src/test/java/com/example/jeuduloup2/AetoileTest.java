package com.example.jeuduloup2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AetoileTest {
    private Grille grille;
    @BeforeEach
    void setUp() {
        grille=new Grille(17,14);
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
        grille.remplacer(13,4,new Rocher(13,4));
        grille.remplacer(5,6,new Rocher(5,6));
        grille.remplacer(6,6,new Rocher(6,6));
        grille.remplacer(8,6,new Rocher(8,6));
        grille.remplacer(5,6,new Rocher(5,6));
        grille.remplacer(13,6,new Rocher(13,6));
        grille.remplacer(13,5,new Rocher(13,5));
        grille.remplacer(3,7,new Rocher(3,7));
        grille.remplacer(5,7,new Rocher(5,7));
        grille.remplacer(6,7,new Rocher(6,7));
        grille.remplacer(13,7,new Rocher(13,7));
        grille.remplacer(13,8,new Rocher(13,8));
        grille.remplacer(13,9,new Rocher(13,9));
        grille.remplacer(12,9,new Rocher(12,9));
        grille.remplacer(10,9,new Rocher(10,9));
        grille.remplacer(9,9,new Rocher(9,9));
        grille.remplacer(8,9,new Rocher(8,9));
        grille.remplacer(6,9,new Rocher(6,9));
        grille.remplacer(2,9,new Rocher(2,9));
        grille.remplacer(12,10,new Rocher(12,10));
        grille.remplacer(12,11,new Rocher(12,11));
        grille.remplacer(12,12,new Rocher(12,12));
        grille.remplacer(6,11,new Rocher(6,11));
        grille.remplacer(3,11,new Rocher(3,11));
        grille.remplacer(2,11,new Rocher(2,11));
        grille.remplacer(9,12,new Rocher(9,12));
        grille.remplacer(11,12,new Rocher(11,12));
    }
    @AfterEach
    void tearDown() {
        grille=null;
    }

    @Test
    void aEtoile() {
        System.out.println(grille);
        ArrayList<int[]> chemin= grille.aEtoile(17,11,1,1);
        ArrayList<int[]> reponse=new ArrayList<>();
        reponse.add(new int[]{17, 11});
        reponse.add(new int[]{16, 11});
        reponse.add(new int[]{15, 11});
        reponse.add(new int[]{14, 11});
        reponse.add(new int[]{14, 10});
        reponse.add(new int[]{14, 9});
        reponse.add(new int[]{14, 8});
        reponse.add(new int[]{14, 7});
        reponse.add(new int[]{14, 6});
        reponse.add(new int[]{14, 5});
        reponse.add(new int[]{14, 4});
        reponse.add(new int[]{14, 3});
        reponse.add(new int[]{13, 3});
        reponse.add(new int[]{12, 3});
        reponse.add(new int[]{11, 3});
        reponse.add(new int[]{10, 3});
        reponse.add(new int[]{9, 3});
        reponse.add(new int[]{8, 3});
        reponse.add(new int[]{7, 3});
        reponse.add(new int[]{6, 3});
        reponse.add(new int[]{5, 3});
        reponse.add(new int[]{4, 3});
        reponse.add(new int[]{3, 3});
        reponse.add(new int[]{2, 3});
        reponse.add(new int[]{1, 3});
        reponse.add(new int[]{1, 2});
        reponse.add(new int[]{1, 1});
        assertEquals(chemin,reponse);

    }

}