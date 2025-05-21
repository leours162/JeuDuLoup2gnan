package com.example.jeuduloup2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

class GrilleTestConnexe {

    private Grille grille;
    private Elements[][] elements;
    private int nbLignes = 5;
    private int nbColonnes = 5;

    @BeforeEach
    void setUp() throws Exception {
        grille = new Grille(nbColonnes, nbLignes);

        Field elementsField = Grille.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        elements = (Elements[][]) elementsField.get(grille);

        Field nbLignesField = Grille.class.getDeclaredField("nbLignes");
        nbLignesField.setAccessible(true);
        nbLignesField.set(grille, nbLignes);

        Field nbColonnesField = Grille.class.getDeclaredField("nbColonnes");
        nbColonnesField.setAccessible(true);
        nbColonnesField.set(grille, nbColonnes);
    }

    @Test
    void testEstConnexe_AucunElementAccessible() {
        elements[1][1] = new Rocher(1, 1);

        for (int y = 0; y < nbLignes; y++) {
            for (int x = 0; x < nbColonnes; x++) {
                if (!(x == 1 && y == 1)) {
                    elements[x][y] = null;
                }
            }
        }

        boolean resultat = grille.estConnexe();
        assertFalse(resultat);
    }

    @Test
    void testEstConnexe_ElementAccessibleTrouve() {
        elements[1][1] = new Herbe(1, 1);

        for (int y = 0; y < nbLignes; y++) {
            for (int x = 0; x < nbColonnes; x++) {
                if (!(x == 1 && y == 1)) {
                    elements[x][y] = new Rocher(x, y);
                }
            }
        }

        boolean resultat = grille.estConnexe();
        assertTrue(resultat);
    }
}
