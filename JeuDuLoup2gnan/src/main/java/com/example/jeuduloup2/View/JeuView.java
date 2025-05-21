package com.example.jeuduloup2.View;

import com.example.jeuduloup2.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.HashMap;

public class JeuView extends Application {

    private static Grille grille;
    private int tour = 1;
    private int cactusMangé = 0;
    private int margueritéMangée = 0;
    private int herbeMangée = 0;

    // Map pour garder trace des moutons et leurs vitesses
    private HashMap<Integer, Mouton> moutons = new HashMap<>();

    private Label tourLabel;
    private Label cptHerbe;
    private Label cptCactus;
    private Label cptMarguerite;
    private Label vitesseLabel; // Nouveau label pour afficher la vitesse actuelle

    public boolean gagne = false;
    public boolean perdu = false;
    public static boolean animal = true; // true = loup, false = mouton

    // Variables pour le déplacement par clic
    private int animalSelectedX = -1;
    private int animalSelectedY = -1;
    private boolean animalSelected = false;
    private Label messageLabel;

    private GridPane grid;
    private StackPane[][] cellules; // Pour stocker les références aux cellules de la grille


    public static void setGrille(Grille grille) {
        JeuView.grille = grille;
    }

    @Override
    public void start(Stage primaryStage) {
        grid = new GridPane();
        cellules = new StackPane[grille.getNbColonnes()][grille.getNbLignes()];

        tourLabel = new Label("Tour " + tour);
        tourLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        tourLabel.setStyle("-fx-text-fill: white;");

        messageLabel = new Label("Sélectionne un " + (animal ? "loup" : "mouton"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        messageLabel.setStyle("-fx-text-fill: white;");

        // Ajout du label pour la vitesse du mouton
        vitesseLabel = new Label("Vitesse mouton: 2");
        vitesseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        vitesseLabel.setStyle("-fx-text-fill: white;");

        // On initialise un mouton pour garder trace de sa vitesse
        initializeMoutons(grille);

        StackPane tourPane = new StackPane(tourLabel);
        tourPane.setAlignment(Pos.BOTTOM_RIGHT);
        tourPane.setPadding(new Insets(15));


        Image imgMarguerite = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/marguerite.png"));
        Image imgCactus = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/cactus.png"));
        Image imgHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
        Image imgRocher = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/rocher.png"));
        Image imgMouton = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png"));
        Image imgLoup = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png"));
        Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));
        Image imgFondHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/FondHerbe.png"));
        Image imgSortie = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png")); // Utilisez une image spécifique pour la sortie si disponible

        cptHerbe = new Label(String.valueOf(herbeMangée));
        cptCactus = new Label(String.valueOf(cactusMangé));
        cptMarguerite = new Label(String.valueOf(margueritéMangée));

        cptMarguerite.setStyle("-fx-text-fill: white;");
        cptMarguerite.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        cptCactus.setStyle("-fx-text-fill: white;");
        cptCactus.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        cptHerbe.setStyle("-fx-text-fill: white;");
        cptHerbe.setFont(Font.font("Arial", FontWeight.BOLD, 50));


        ImageView imgMarg = new ImageView(imgMarguerite);
        ImageView imgCact = new ImageView(imgCactus);
        ImageView imgHerb = new ImageView(imgHerbe);

        imgMarg.setFitWidth(100);
        imgMarg.setFitHeight(100);
        imgCact.setFitWidth(100);
        imgCact.setFitHeight(100);
        imgHerb.setFitWidth(100);
        imgHerb.setFitHeight(100);


        HBox végétaux = new HBox(imgCact, cptCactus, imgMarg, cptMarguerite, imgHerb, cptHerbe);
        végétaux.setAlignment(Pos.CENTER);
        végétaux.setSpacing(30);

        VBox compteurs = new VBox(végétaux);

        // Initialisation des cellules
        for (int i = 0; i < grille.getNbLignes(); i++) {
            for (int j = 0; j < grille.getNbColonnes(); j++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(60, 60);
                cellules[j][i] = cell;

                final int x = j;
                final int y = i;

                Elements e = grille.getElement(j, i);
                ImageView imageView = null;

                if (e instanceof Rocher) {
                    imageView = new ImageView(imgRocher);
                } else if (e instanceof Herbe) {
                    imageView = new ImageView(imgHerbe);
                } else if (e instanceof Mouton) {
                    imageView = new ImageView(imgMouton);
                } else if (e instanceof Loup) {
                    imageView = new ImageView(imgLoup);
                } else if (e instanceof Marguerite) {
                    imageView = new ImageView(imgMarguerite);
                } else if (e instanceof Cactus) {
                    imageView = new ImageView(imgCactus);
                } else if (e instanceof Sortie) {
                    imageView = new ImageView(imgSortie);
                    // Ajout d'un rectangle pour marquer visuellement la sortie
                    Rectangle exitMarker = new Rectangle(60, 60);
                    exitMarker.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.3));
                    cell.getChildren().add(exitMarker);
                }

                if (imageView != null) {
                    imageView.setFitWidth(60);
                    imageView.setFitHeight(60);
                    cell.getChildren().add(imageView);
                }

                // Ajout des gestionnaires d'événements pour la sélection et le déplacement
                cell.setOnMouseClicked(event -> {
                    handleCellClick(x, y);
                });

                grid.add(cell, j, i);
            }
        }

        grid.setAlignment(Pos.CENTER);
        ImageView logoJeu = new ImageView(logo);
        logoJeu.setFitHeight(200);
        logoJeu.setFitWidth(200);

        VBox logotour = new VBox(logoJeu, tourPane, messageLabel, vitesseLabel); // Ajout du label de vitesse
        logotour.setSpacing(20);
        logotour.setAlignment(Pos.CENTER);

        HBox tout = new HBox(compteurs, grid, logotour);
        tout.setSpacing(150);

        ImageView background = new ImageView(imgFondHerbe);
        background.setFitWidth(1920);
        background.setFitHeight(1080);

        StackPane root = new StackPane(background, tout);

        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Jeu - Mange moi si tu peux");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour initialiser la HashMap des moutons
    private void initializeMoutons(Grille grille) {
        // Parcourir la grille pour trouver les moutons
        for (int i = 0; i < grille.getNbLignes(); i++) {
            for (int j = 0; j < grille.getNbColonnes(); j++) {
                Elements element = grille.getElement(j, i);
                if (element instanceof Mouton) {
                    Mouton mouton = (Mouton) element;
                    // Utiliser une clé unique basée sur la position
                    int key = j * 1000 + i;
                    moutons.put(key, mouton);
                }
            }
        }
    }

    // Méthode pour obtenir le mouton à une position donnée
    private Mouton getMouton(int x, int y) {
        int key = x * 1000 + y;
        return moutons.get(key);
    }

    // Méthode pour mettre à jour la position d'un mouton dans la HashMap
    private void updateMoutonPosition(int oldX, int oldY, int newX, int newY, Mouton mouton) {
        int oldKey = oldX * 1000 + oldY;
        int newKey = newX * 1000 + newY;
        moutons.remove(oldKey);
        moutons.put(newKey, mouton);
    }
    private void handleCellClick(int x, int y) {
        if (gagne || perdu) {
            return; // Le jeu est terminé, ignore les clics
        }

        Elements element = grille.getElement(x, y);

        // Si aucun animal n'est sélectionné, vérifie si l'utilisateur a cliqué sur un animal
        if (!animalSelected) {
            if ((animal && element instanceof Loup) || (!animal && element instanceof Mouton)) {
                // Sélectionne l'animal
                animalSelectedX = x;
                animalSelectedY = y;
                animalSelected = true;

                // Met à jour le message
                messageLabel.setText("Sélectionne une destination");

                // Affiche les déplacements possibles
                afficherDeplacementsPossibles((Animal) element);
            }
        } else {
            // Un animal est déjà sélectionné, tente de le déplacer
            Elements animalElement = grille.getElement(animalSelectedX, animalSelectedY);
            if (animalElement instanceof Animal) {
                Animal animalCourant = (Animal) animalElement;
                boolean deplacementReussi = grille.seDeplacer(animalCourant, x, y);

                if (deplacementReussi) {
                    // Sauvegarde des informations sur la cellule de destination avant déplacement
                    Elements destination = grille.getElement(x, y);

                    // Vérifie si le mouton atteint la sortie
                    if (!animal && destination instanceof Sortie) {
                        gagne = true;
                        messageLabel.setText("Victoire ! Le mouton a atteint la sortie !");
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        return;
                    }

                    // Vérifie si le loup mange le mouton (si on déplace un loup sur un mouton)
                    if (animal && destination instanceof Mouton) {
                        perdu = true;
                        messageLabel.setText("Défaite ! Le loup a mangé le mouton !");
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        return;
                    }

                    // Mise à jour de l'interface graphique après déplacement
                    mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);

                    // Si l'animal est un mouton sur une plante comestible, met à jour les compteurs et la vitesse
                    if (animalCourant instanceof Mouton) {
                        Mouton mouton = (Mouton) animalCourant;

                        if (destination instanceof Vegetaux) {
                            Vegetaux vegetaux = (Vegetaux) destination;
                            // Appliquer la méthode manger qui mettra à jour la vitesse et les compteurs
                            mouton.manger(vegetaux);

                            // Met à jour les compteurs affichés selon le type de végétal
                            if (vegetaux.getNutrition() == 2) { // Herbe
                                herbeMangée++;
                                cptHerbe.setText(String.valueOf(herbeMangée));
                            } else if (vegetaux.getNutrition() == 4) { // Marguerite
                                margueritéMangée++;
                                cptMarguerite.setText(String.valueOf(margueritéMangée));
                            } else if (vegetaux.getNutrition() == 1) { // Cactus
                                cactusMangé++;
                                cptCactus.setText(String.valueOf(cactusMangé));
                            }

                            // On met à jour le mouton dans notre HashMap
                            updateMoutonPosition(animalSelectedX, animalSelectedY, x, y, mouton);

                            // Mise à jour du label de vitesse
                            vitesseLabel.setText("Vitesse mouton: " + mouton.getVitesse());
                        }
                    }

                    // Passe au tour suivant
                    incrementerTour();
                    animal = !animal; // Alterne entre loup et mouton
                    messageLabel.setText("Sélectionne un " + (animal ? "loup" : "mouton"));
                }

                // Réinitialise la sélection et efface les cases de déplacement possibles
                animalSelected = false;
                animalSelectedX = -1;
                animalSelectedY = -1;
                effacerDeplacementsPossibles();
            }
        }
    }

    // Affiche les cases où l'animal peut se déplacer
    private void afficherDeplacementsPossibles(Animal animal) {
        int[][] deplacements = grille.lesDeplacements(animal);

        // Réinitialise d'abord toutes les cellules à leur état normal
        effacerDeplacementsPossibles();

        // Ajoute un rectangle semi-transparent pour les cases de déplacement possibles
        for (int[] deplacement : deplacements) {
            int x = deplacement[0];
            int y = deplacement[1];

            // Vérifie que les coordonnées sont dans les limites de la grille
            if (x >= 0 && x < grille.getNbColonnes() && y >= 0 && y < grille.getNbLignes()) {
                Rectangle highlight = new Rectangle(60, 60);
                // Couleur différente selon le type d'animal
                if (animal instanceof Loup) {
                    highlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.3));

                    // Pour le loup, mettre en évidence spéciale si un mouton est accessible
                    Elements elementCible = grille.getElement(x, y);
                    if (elementCible instanceof Mouton) {
                        highlight.setFill(Color.PURPLE.deriveColor(0, 1, 1, 0.5));
                    }
                } else { // Mouton
                    highlight.setFill(Color.GREEN.deriveColor(0, 1, 1, 0.3));

                    // Pour le mouton, mettre en évidence spéciale si une sortie est accessible
                    Elements elementCible = grille.getElement(x, y);
                    if (elementCible instanceof Sortie) {
                        highlight.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.5));
                    }
                }

                cellules[x][y].getChildren().add(highlight);
            }
        }
    }

    // Efface les mises en évidence des cases de déplacement
    private void effacerDeplacementsPossibles() {
        for (int i = 0; i < grille.getNbColonnes(); i++) {
            for (int j = 0; j < grille.getNbLignes(); j++) {
                // Supprime tous les rectangles de mise en évidence (les enfants après le premier)
                if (cellules[i][j].getChildren().size() > 1) {
                    cellules[i][j].getChildren().remove(1, cellules[i][j].getChildren().size());
                }
            }
        }
    }

    private void mettreAJourInterface(int oldX, int oldY, int newX, int newY, Animal animal) {
        // Crée une nouvelle image pour l'animal
        ImageView animalView;
        if (animal instanceof Loup) {
            animalView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png")));
        } else {
            animalView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png")));
        }
        animalView.setFitWidth(60);
        animalView.setFitHeight(60);

        // Récupère les cellules
        StackPane oldCell = cellules[oldX][oldY];
        StackPane newCell = cellules[newX][newY];

        // Supprime tous les enfants de l'ancienne cellule
        oldCell.getChildren().clear();

        // Ajoute une herbe à l'ancienne position
        ImageView herbeView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png")));
        herbeView.setFitWidth(60);
        herbeView.setFitHeight(60);
        oldCell.getChildren().add(herbeView);

        // Met à jour la grille logique pour l'ancienne position
        grille.remplacer(oldX, oldY, new Herbe(oldX, oldY));

        // Supprime tous les enfants de la nouvelle cellule
        newCell.getChildren().clear();

        // Ajoute l'animal à la nouvelle position
        newCell.getChildren().add(animalView);

        // Met à jour la grille logique pour la nouvelle position
        if (animal instanceof Loup) {
            grille.remplacer(newX, newY, new Loup(newX, newY));
        } else if (animal instanceof Mouton) {
            // Si c'est un mouton, préserve sa vitesse actuelle
            Mouton mouton = (Mouton) animal;
            Mouton nouveauMouton = new Mouton(newX, newY);

            // Copie des attributs du mouton existant
            nouveauMouton.setVitesse(mouton.getVitesse());
            nouveauMouton.setNbCactus(mouton.getNbCactus());
            nouveauMouton.setNbHerbe(mouton.getNbHerbe());
            nouveauMouton.setNbMarguerite(mouton.getNbMarguerite());

            // Remplace le mouton dans la grille
            grille.remplacer(newX, newY, nouveauMouton);

            // Met à jour le mouton dans notre HashMap
            updateMoutonPosition(oldX, oldY, newX, newY, nouveauMouton);
        }
    }

    public static void setAnimal(boolean b){
        animal = b;
    }

    private void incrementerTour() {
        tour++;
        tourLabel.setText("Tour " + tour);
    }

    public static void main(String[] args) {
        launch(args);
    }
}