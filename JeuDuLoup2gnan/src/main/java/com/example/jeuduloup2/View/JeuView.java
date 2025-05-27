package com.example.jeuduloup2.View;

import com.example.jeuduloup2.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class JeuView extends Application {
    private boolean grilleimportee = false;
    private static Grille grille;
    private int tour = 1;
    private int cactusMangé = 0;
    private int margueritéMangée = 0;
    private int herbeMangée = 0;

    private File fichierGrille;

    private HashMap<Integer, Mouton> moutons = new HashMap<>();

    private Label tourLabel;
    private Label cptHerbe;
    private Label cptCactus;
    private Label cptMarguerite;
    private Label vitesseLabel;

    public boolean gagne = false;
    public boolean perdu = false;
    public static boolean animal = true;

    private int animalSelectedX = -1;
    private int animalSelectedY = -1;
    private boolean animalSelected = false;
    private Label messageLabel;

    private GridPane grid;
    private StackPane[][] cellules;
    private double tailleCellule;

    private Stage currentStage;

    // Images pour éviter de les recharger
    private Image imgMarguerite, imgCactus, imgHerbe, imgRocher, imgMouton, imgLoup, imgSortie;

    public static void setGrille(Grille grille) {
        JeuView.grille = grille;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.currentStage = primaryStage;

        if (grilleimportee && fichierGrille != null) {
            System.out.println("Chargement de la grille importée depuis : " + fichierGrille.getAbsolutePath());
            chargerGrilleDepuisFichier();
        }

        if (grille == null) {
            throw new IllegalStateException("La grille n'est pas initialisée !");
        }
        // Debug pour vérifier les dimensions
        System.out.println("Dimensions de la grille: " + grille.getNbColonnes() + " colonnes x " + grille.getNbLignes() + " lignes");

        // CALCUL ADAPTATIF DES TAILLES
        calculerTailleCellule();

        // Charger toutes les images une seule fois
        chargerImages();

        // Initialiser l'interface
        initialiserInterface();

        // Initialiser les moutons
        initializeMoutons(grille);

        // Créer la grille d'affichage
        creerGrilleAffichage();

        // Créer l'interface complète
        Scene scene = creerScenePrincipale();

        primaryStage.setTitle("Jeu - Mange moi si tu peux");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Vérifier les conditions de fin
        verifierConditionsFinDeJeu();
    }

    private void chargerGrilleDepuisFichier() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fichierGrille));
        String line;
        int maxLignes = 0;
        int maxColonnes = 0;

        // Première passe : déterminer les dimensions
        while ((line = reader.readLine()) != null) {
            maxLignes++;
            maxColonnes = Math.max(maxColonnes, line.length());
        }
        reader.close();

        // Créer la grille avec les bonnes dimensions
        grille = new Grille(maxColonnes, maxLignes);

        // Deuxième passe : remplir la grille
        reader = new BufferedReader(new FileReader(fichierGrille));
        int y = 0;

        while ((line = reader.readLine()) != null && y < grille.getNbLignes()) {
            if (line.length() > 0 && line.charAt(0) == '\uFEFF') {
                line = line.substring(1); // supprime BOM s'il existe au début
            }
            System.out.println("Ligne " + y + " longueur = " + line.length() + " / attendu = " + grille.getNbColonnes());
            for (int x = 0; x < grille.getNbColonnes(); x++) {
                char c = x < line.length() ? line.charAt(x) : 'h';

                Elements element = creerElementDepuisChar(c, x, y);
                grille.remplacer(x, y, element);
            }
            y++;
        }
        reader.close();
    }

    private Elements creerElementDepuisChar(char c, int x, int y) {
        switch (Character.toLowerCase(c)) {
            case 'm': return new Mouton(x, y);
            case 'l': return new Loup(x, y);
            case 'x': return new Rocher(x, y);
            case 's': return new Sortie(x, y);
            case 'f': return new Marguerite(x, y);
            case 'c': return new Cactus(x, y);
            case 'h': return new Herbe(x, y);
            default: return new Herbe(x, y);
        }
    }

    private void calculerTailleCellule() {
        int nbColonnes = grille.getNbColonnes();
        int nbLignes = grille.getNbLignes();

        // Espace disponible pour la grille (en laissant de la place pour les autres éléments)
        double espaceDisponibleLargeur = 1920 - 600; // 600px pour les compteurs et logo
        double espaceDisponibleHauteur = 1080 - 200;  // 200px pour les marges

        // Calculer la taille optimale des cellules
        double tailleCelluleLargeur = espaceDisponibleLargeur / nbColonnes;
        double tailleCelluleHauteur = espaceDisponibleHauteur / nbLignes;

        // Prendre la plus petite taille pour garder les cellules carrées
        tailleCellule = Math.min(tailleCelluleLargeur, tailleCelluleHauteur);

        // Limites min/max pour la lisibilité
        tailleCellule = Math.max(30, Math.min(80, tailleCellule));
    }

    private void chargerImages() {
        imgMarguerite = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/marguerite.png"));
        imgCactus = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/cactus.png"));
        imgHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
        imgRocher = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/rocher.png"));
        imgMouton = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png"));
        imgLoup = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png"));
        imgSortie = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
    }

    private void initialiserInterface() {
        // Adapter les tailles de police
        double facteurEchelle = tailleCellule / 60.0;
        double tailleTitre = Math.max(30, Math.min(60, 60 * facteurEchelle));
        double tailleTexte = Math.max(16, Math.min(24, 24 * facteurEchelle));
        double tailleCompteur = Math.max(25, Math.min(50, 50 * facteurEchelle));

        tourLabel = new Label("Tour " + tour);
        tourLabel.setFont(Font.font("Arial", FontWeight.BOLD, tailleTitre));
        tourLabel.setStyle("-fx-text-fill: white;");

        messageLabel = new Label("Sélectionne un " + (animal ? "loup" : "mouton"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, tailleTexte));
        messageLabel.setStyle("-fx-text-fill: white;");

        vitesseLabel = new Label("Vitesse mouton: 2");
        vitesseLabel.setFont(Font.font("Arial", FontWeight.BOLD, tailleTexte));
        vitesseLabel.setStyle("-fx-text-fill: white;");

        cptHerbe = new Label(String.valueOf(herbeMangée));
        cptCactus = new Label(String.valueOf(cactusMangé));
        cptMarguerite = new Label(String.valueOf(margueritéMangée));

        cptMarguerite.setStyle("-fx-text-fill: white;");
        cptMarguerite.setFont(Font.font("Arial", FontWeight.BOLD, tailleCompteur));
        cptCactus.setStyle("-fx-text-fill: white;");
        cptCactus.setFont(Font.font("Arial", FontWeight.BOLD, tailleCompteur));
        cptHerbe.setStyle("-fx-text-fill: white;");
        cptHerbe.setFont(Font.font("Arial", FontWeight.BOLD, tailleCompteur));
    }

    private void creerGrilleAffichage() {
        grid = new GridPane();
        cellules = new StackPane[grille.getNbColonnes()][grille.getNbLignes()];
        System.out.println("Ajout de la cellule : x=" + grille.getNbLignes() + ", y=" + grille.getNbColonnes());

        System.out.println("Création du tableau cellules: " + grille.getNbColonnes() + "x" + grille.getNbLignes());

        // Parcourir ligne par ligne, colonne par colonne
        for (int x = 0; x < grille.getNbColonnes(); x++) {
            for (int y = 0; y < grille.getNbLignes(); y++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(tailleCellule, tailleCellule);
                cellules[x][y] = cell;

                // Créer les références finales pour les listeners
                final int finalX = x;
                final int finalY = y;

                Elements e = grille.getElement(x, y);
                ajouterImageDansCell(cell, e);

                cell.setOnMouseClicked(event -> {
                    handleCellClick(finalX, finalY);
                });

                // Ajouter la cellule à la grille d'affichage (colonne, ligne)
                grid.add(cell, x, y);
            }
        }

        grid.setAlignment(Pos.CENTER);
    }

    private void ajouterImageDansCell(StackPane cell, Elements e) {
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
            Rectangle exitMarker = new Rectangle(tailleCellule, tailleCellule);
            exitMarker.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.3));
            cell.getChildren().add(exitMarker);
        }

        if (imageView != null) {
            imageView.setFitWidth(tailleCellule);
            imageView.setFitHeight(tailleCellule);
            cell.getChildren().add(imageView);
        }
    }

    private Scene creerScenePrincipale() {
        double facteurEchelle = tailleCellule / 60.0;

        StackPane tourPane = new StackPane(tourLabel);
        tourPane.setAlignment(Pos.BOTTOM_RIGHT);
        tourPane.setPadding(new Insets(15 * facteurEchelle));

        // Adapter la taille des icônes des compteurs
        double tailleIcone = Math.max(50, Math.min(100, 100 * facteurEchelle));

        ImageView imgMarg = new ImageView(imgMarguerite);
        ImageView imgCact = new ImageView(imgCactus);
        ImageView imgHerb = new ImageView(imgHerbe);

        imgMarg.setFitWidth(tailleIcone);
        imgMarg.setFitHeight(tailleIcone);
        imgCact.setFitWidth(tailleIcone);
        imgCact.setFitHeight(tailleIcone);
        imgHerb.setFitWidth(tailleIcone);
        imgHerb.setFitHeight(tailleIcone);

        HBox végétaux = new HBox(imgCact, cptCactus, imgMarg, cptMarguerite, imgHerb, cptHerbe);
        végétaux.setAlignment(Pos.CENTER);
        végétaux.setSpacing(30 * facteurEchelle);

        VBox compteurs = new VBox(végétaux);

        // Adapter la taille du logo
        double tailleLogo = Math.max(100, Math.min(200, 200 * facteurEchelle));
        Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));
        ImageView logoJeu = new ImageView(logo);
        logoJeu.setFitHeight(tailleLogo);
        logoJeu.setFitWidth(tailleLogo);

        VBox logotour = new VBox(logoJeu, tourPane, messageLabel, vitesseLabel);
        logotour.setSpacing(20 * facteurEchelle);
        logotour.setAlignment(Pos.CENTER);

        // Adapter l'espacement général
        HBox tout = new HBox(compteurs, grid, logotour);
        tout.setSpacing(Math.max(50, Math.min(150, 150 * facteurEchelle)));
        tout.setAlignment(Pos.CENTER);

        Image imgFondHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/FondHerbe.png"));
        ImageView background = new ImageView(imgFondHerbe);
        background.setFitWidth(1920);
        background.setFitHeight(1080);

        StackPane root = new StackPane(background, tout);
        return new Scene(root, 1920, 1080);
    }

    private void verifierConditionsFinDeJeu() {
        if (gagne) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("VOUS AVEZ GAGNE");
            alert.setHeaderText(null);
            alert.setContentText("VICTOIREEEEE !");
            alert.showAndWait();
            currentStage.close();
        }
        if (perdu) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("VOUS AVEZ PERDU");
            alert.setHeaderText(null);
            alert.setContentText("DEFAITE ...");
            alert.showAndWait();
            currentStage.close();
        }
    }

    private void initializeMoutons(Grille grille) {
        moutons.clear();
        for (int y = 0; y < grille.getNbLignes(); y++) {
            for (int x = 0; x < grille.getNbColonnes(); x++) {
                Elements element = grille.getElement(x, y);
                if (element instanceof Mouton) {
                    Mouton mouton = (Mouton) element;
                    int key = x * 1000 + y;
                    moutons.put(key, mouton);
                }
            }
        }
        System.out.println("Moutons initialisés: " + moutons.size());
    }

    private void updateMoutonPosition(int oldX, int oldY, int newX, int newY, Mouton mouton) {
        int oldKey = oldX * 1000 + oldY;
        int newKey = newX * 1000 + newY;
        moutons.remove(oldKey);
        moutons.put(newKey, mouton);
    }

    private void handleCellClick(int x, int y) {
        if (gagne || perdu) {
            return;
        }

        System.out.println("Clic sur cellule: (" + x + ", " + y + ")");
        Elements element = grille.getElement(x, y);

        if (!animalSelected) {
            if ((animal && element instanceof Loup) || (!animal && element instanceof Mouton)) {
                animalSelectedX = x;
                animalSelectedY = y;
                animalSelected = true;

                messageLabel.setText("Sélectionnez une destination");
                afficherDeplacementsPossibles((Animal) element);
            }
        } else {
            Elements animalElement = grille.getElement(animalSelectedX, animalSelectedY);
            if (animalElement instanceof Animal) {
                Animal animalCourant = (Animal) animalElement;
                boolean deplacementReussi = grille.seDeplacer(animalCourant, x, y);

                if (deplacementReussi) {
                    Elements destination = grille.getElement(x, y);

                    if (!animal && destination instanceof Loup) {
                        perdu = true;
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        afficherFinDeJeu("VOUS AVEZ PERDU", "Le mouton a été mangé par le loup !");
                        return;
                    }

                    if (!animal && destination instanceof Sortie) {
                        gagne = true;
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        afficherFinDeJeu("VOUS AVEZ GAGNE", "VICTOIREEEEE !");
                        return;
                    }

                    if (animal && destination instanceof Mouton) {
                        perdu = true;
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        afficherFinDeJeu("VOUS AVEZ PERDU", "DEFAITE ...");
                        return;
                    }

                    mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);

                    if (animalCourant instanceof Mouton) {
                        traiterMoutonMange((Mouton) animalCourant, destination, x, y);
                    }

                    incrementerTour();
                    animal = !animal;
                    messageLabel.setText("Sélectionnez le " + (animal ? "loup" : "mouton"));
                }

                animalSelected = false;
                animalSelectedX = -1;
                animalSelectedY = -1;
                effacerDeplacementsPossibles();
            }
        }
    }

    private void traiterMoutonMange(Mouton mouton, Elements destination, int x, int y) {
        if (destination instanceof Vegetaux) {
            Vegetaux vegetaux = (Vegetaux) destination;
            mouton.manger(vegetaux);

            if (vegetaux.getNutrition() == 2) {
                herbeMangée++;
                cptHerbe.setText(String.valueOf(herbeMangée));
            } else if (vegetaux.getNutrition() == 4) {
                margueritéMangée++;
                cptMarguerite.setText(String.valueOf(margueritéMangée));
            } else if (vegetaux.getNutrition() == 1) {
                cactusMangé++;
                cptCactus.setText(String.valueOf(cactusMangé));
            }

            updateMoutonPosition(animalSelectedX, animalSelectedY, x, y, mouton);
            vitesseLabel.setText("Vitesse mouton: " + mouton.getVitesse());
        }
    }

    private void afficherFinDeJeu(String titre, String message) {
        new MenuView().start(new Stage());
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        currentStage.close();
    }

    private void afficherDeplacementsPossibles(Animal animal) {
        int[][] deplacements = grille.lesDeplacements(animal);
        effacerDeplacementsPossibles();

        for (int[] deplacement : deplacements) {
            int x = deplacement[0];
            int y = deplacement[1];
            if (x >= 0 && x < grille.getNbColonnes() && y >= 0 && y < grille.getNbLignes()) {
                Rectangle highlight = new Rectangle(tailleCellule, tailleCellule);

                if (animal instanceof Loup) {
                    highlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.3));
                    Elements elementCible = grille.getElement(x, y);
                    if (elementCible instanceof Mouton) {
                        highlight.setFill(Color.PURPLE.deriveColor(0, 1, 1, 0.5));
                    }
                } else {
                    highlight.setFill(Color.GREEN.deriveColor(0, 1, 1, 0.3));
                    Elements elementCible = grille.getElement(x, y);
                    if (elementCible instanceof Sortie) {
                        highlight.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.5));
                    }
                }

                cellules[x][y].getChildren().add(highlight);
            }
        }
    }

    private void effacerDeplacementsPossibles() {
        for (int x = 0; x < grille.getNbColonnes(); x++) {
            for (int y = 0; y < grille.getNbLignes(); y++) {
                if (cellules[x][y].getChildren().size() > 1) {
                    cellules[x][y].getChildren().remove(1, cellules[x][y].getChildren().size());
                }
            }
        }
    }

    private void mettreAJourInterface(int oldX, int oldY, int newX, int newY, Animal animal) {
        ImageView animalView;
        if (animal instanceof Loup) {
            animalView = new ImageView(imgLoup);
        } else {
            animalView = new ImageView(imgMouton);
        }
        animalView.setFitWidth(tailleCellule);
        animalView.setFitHeight(tailleCellule);

        StackPane oldCell = cellules[oldX][oldY];
        StackPane newCell = cellules[newX][newY];

        oldCell.getChildren().clear();

        // Générer un nouveau végétal aléatoire
        int rand = (int)(Math.random() * 3);
        Elements vegetal;

        if (rand == 0) {
            vegetal = new Herbe(oldX, oldY);
        } else if (rand == 1) {
            vegetal = new Cactus(oldX, oldY);
        } else {
            vegetal = new Marguerite(oldX, oldY);
        }

        ImageView vegetalView = null;
        if (vegetal instanceof Herbe) {
            vegetalView = new ImageView(imgHerbe);
        } else if (vegetal instanceof Cactus) {
            vegetalView = new ImageView(imgCactus);
        } else if (vegetal instanceof Marguerite) {
            vegetalView = new ImageView(imgMarguerite);
        }

        if (vegetalView != null) {
            vegetalView.setFitWidth(tailleCellule);
            vegetalView.setFitHeight(tailleCellule);
            oldCell.getChildren().add(vegetalView);
        }

        grille.remplacer(oldX, oldY, vegetal);

        newCell.getChildren().clear();
        newCell.getChildren().add(animalView);

        if (animal instanceof Loup) {
            grille.remplacer(newX, newY, new Loup(newX, newY));
        } else if (animal instanceof Mouton) {
            Mouton mouton = (Mouton) animal;
            grille.remplacer(newX, newY, mouton);
            updateMoutonPosition(oldX, oldY, newX, newY, mouton);
        }
    }

    public static void setAnimal(boolean b) {
        animal = b;
    }

    private void incrementerTour() {
        tour++;
        tourLabel.setText("Tour " + tour);
    }

    public void setGrilleimportee() {
        this.grilleimportee = true;
    }

    public void setFichierGrille(File fichier) {
        this.fichierGrille = fichier;
    }

    public static void main(String[] args) {
        launch(args);
    }
}