package com.example.jeuduloup2.View;

import com.example.jeuduloup2.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JeuView extends Application {
    private boolean grilleimportee = false;
    private File fichierGrille;

    private boolean automatiser = false;

    private static Grille grille;
    private int tour = 1;
    private int cactusMangé = 0;
    private int margueritéMangée = 0;
    private int herbeMangée = 0;

    private HashMap<Integer, Mouton> moutons = new HashMap<>();

    private Label tourLabel;
    private Label cptHerbe;
    private Label cptCactus;
    private Label cptMarguerite;
    private Label vitesseLabel;

    public boolean gagne = false;
    public boolean perdu = false;
    public static boolean animal = true;

    private int[] coLoup = new int[2];
    private int[] coMouton = new int[2];
    private int[] coSortie = new int[2];
    private boolean fuite;

    private int animalSelectedX = -1;
    private int animalSelectedY = -1;
    private boolean animalSelected = false;
    private Label messageLabel;

    private GridPane grid;
    private StackPane[][] cellules;

    private Stage currentStage;


    public static void setGrille(Grille grille) {
        JeuView.grille = grille;
    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        // Vérifie si la on a importé la grille ou pas
        if (grilleimportee) {
            BufferedReader reader = new BufferedReader(new FileReader(fichierGrille));
            String line;
            java.util.List<String> lignes = new java.util.ArrayList<>();
            int maxColonnes = 0;
            while ((line = reader.readLine()) != null) {
                lignes.add(line);
                maxColonnes = Math.max(maxColonnes, line.length());
            }
            reader.close();
            int maxLignes = lignes.size();
            grille = new Grille(maxLignes, maxColonnes);
            // Lecture du fichier puis on créer la grille a partir de ca
            for (int y = 0; y < maxLignes; y++) {
                String ligneCourante = lignes.get(y);
                for (int x = 0; x < maxColonnes; x++) {
                    char c;
                    if (x < ligneCourante.length()) {
                        c = ligneCourante.charAt(x);
                    } else {
                        c = ' ';
                    }
                    // Pour chaque lettre, on remplace par l'élément qui correspond dans la grille
                    switch (Character.toLowerCase(c)) {
                        case 'm':
                            grille.remplacer(x, y, new Mouton(x, y));
                            break;
                        case 'l':
                            grille.remplacer(x, y, new Loup(x, y));
                            coLoup[0] = x;
                            coLoup[1] = y;
                            break;
                        case 'x':
                            grille.remplacer(x, y, new Rocher(x, y));
                            break;
                        case 's':
                            grille.remplacer(x, y, new Sortie(x, y));
                            coSortie[0] = x;
                            coSortie[1] = y;
                            break;
                        case 'f':
                            grille.remplacer(x, y, new Marguerite(x, y));
                            break;
                        case 'c':
                            grille.remplacer(x, y, new Cactus(x, y));
                            break;
                        case 'h':
                            grille.remplacer(x, y, new Herbe(x, y));
                            break;
                        case ' ':
                        case '.':
                        default:
                            break;
                    }
                }
            }
        }

        // Initialisation de tout l'affichage
        this.currentStage = primaryStage;
        grid = new GridPane();
        cellules = new StackPane[grille.getNbColonnes()][grille.getNbLignes()];

        tourLabel = new Label("Tour " + tour);
        tourLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        tourLabel.setStyle("-fx-text-fill: white;");

        messageLabel = new Label("Sélectionne un " + (animal ? "loup" : "mouton"));
        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        messageLabel.setStyle("-fx-text-fill: white;");

        vitesseLabel = new Label("Vitesse mouton: 2");
        vitesseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        vitesseLabel.setStyle("-fx-text-fill: white;");

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

        imgMarg.setFitWidth(50);
        imgMarg.setFitHeight(50);
        imgCact.setFitWidth(50);
        imgCact.setFitHeight(50);
        imgHerb.setFitWidth(50);
        imgHerb.setFitHeight(50);

        Button Automatiser = new Button("Automatiser");
        Automatiser.setStyle("-fx-font-size: 25px; -fx-background-color: white; -fx-text-fill: black;");
        Automatiser.setOnAction(e -> {
            System.out.println("Bouton Automatiser cliqué");
            try {
                if (animal) {
                    Loup loup = (Loup) grille.getElement(coLoup[0], coLoup[1]);
                    Sortie sortie = (Sortie) grille.getElement(coSortie[0], coSortie[1]);
                    setAutomatiserDijkstra(loup, sortie);
                } else {
                    Mouton mouton = (Mouton) grille.getElement(coMouton[0], coMouton[1]);
                    Sortie sortie = (Sortie) grille.getElement(coSortie[0], coSortie[1]);
                    setAutomatiserDijkstra(mouton, sortie);
                }
            } catch (Exception ex) {
                ex.printStackTrace(); // important
            }
        });

        HBox végétaux = new HBox(imgCact, cptCactus, imgMarg, cptMarguerite, imgHerb, cptHerbe);
        végétaux.setAlignment(Pos.CENTER);
        végétaux.setSpacing(30);

        VBox compteurs = new VBox(végétaux);

        // Apres avoir créer la grille ou dans GrilleView ou l'avoir importée, on créer l'affichage de la grille
        for (int i = 0; i < grille.getNbLignes(); i++) {
            for (int j = 0; j < grille.getNbColonnes(); j++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(50, 50);
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
                    Rectangle exitMarker = new Rectangle(50, 50);
                    exitMarker.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.3));
                    cell.getChildren().add(exitMarker);
                }

                if (imageView != null) {
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    cell.getChildren().add(imageView);
                }
                cell.setOnMouseClicked(event -> {
                    handleCellClick(x, y);
                });

                grid.add(cell, j, i);
            }
        }

        grid.setAlignment(Pos.CENTER);
        ImageView logoJeu = new ImageView(logo);
        logoJeu.setFitHeight(150);
        logoJeu.setFitWidth(150);

        VBox logotour = new VBox(logoJeu, tourPane, messageLabel, vitesseLabel,Automatiser);
        logotour.setSpacing(20);
        logotour.setAlignment(Pos.CENTER);

        HBox tout = new HBox(compteurs, grid, logotour);
        tout.setSpacing(100);

        ImageView background = new ImageView(imgFondHerbe);
        background.setFitWidth(1920);
        background.setFitHeight(1080);

        StackPane root = new StackPane(background, tout);

        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Jeu - Mange moi si tu peux");
        primaryStage.setScene(scene);
        primaryStage.show();

        // On vérifie les conditions si on a gagné ou pas
        if (gagne){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("VOUS AVEZ GAGNE");
            alert.setHeaderText(null);
            alert.setContentText("VICTOIREEEEE !");
            alert.showAndWait();
            primaryStage.close();
        }
        if (perdu){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("VOUS AVEZ PERDU");
            alert.setHeaderText(null);
            alert.setContentText("DEFAITE ...");
            alert.showAndWait();
            primaryStage.close();
        }
    }


    private void initializeMoutons(Grille grille) {
        for (int i = 0; i < grille.getNbLignes(); i++) {
            for (int j = 0; j < grille.getNbColonnes(); j++) {
                Elements element = grille.getElement(j, i);
                if (element instanceof Mouton) {
                    Mouton mouton = (Mouton) element;
                    int key = j * 1000 + i;
                    moutons.put(key, mouton);
                }
            }
        }
    }


    private void updateMoutonPosition(int oldX, int oldY, int newX, int newY, Mouton mouton) {
        int oldKey = oldX * 1000 + oldY;
        int newKey = newX * 1000 + newY;
        moutons.remove(oldKey);
        moutons.put(newKey, mouton);
    }

    // On gere tout le jeu ici
    private void handleCellClick(int x, int y) {
        if (gagne || perdu) {
            return;
        }

        Elements element = grille.getElement(x, y);

        // Des qu'on selectionne un animal
        if (!animalSelected) {
            if ((animal && element instanceof Loup) || (!animal && element instanceof Mouton)) {
                animalSelectedX = x;
                animalSelectedY = y;
                animalSelected = true;

                messageLabel.setText("Sélectionnez \n une destination");
                messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 35));


                // On affiche en mettant en valeur tous les déplacements possible pour lui selon l'animal qu'on a selectionné
                afficherDeplacementsPossibles((Animal) element);
            } else {
                String animalAttendu = animal ? "loup" : "mouton";
                messageLabel.setText("Sélectionnez le \n" + animalAttendu + " (pas " + element.getClass().getSimpleName().toLowerCase() + ")");
                messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 35));
            }
        } else {
            Elements animalElement = grille.getElement(animalSelectedX, animalSelectedY);
            if (animalElement instanceof Animal) {
                Animal animalCourant = (Animal) animalElement;
                boolean deplacementReussi = grille.seDeplacer(animalCourant, x, y);

                // Des qu'on déplacé l'animal
                if (deplacementReussi) {
                    Elements destination = grille.getElement(x, y);

                    // On vérifie ou il va pour savoir si il a gagné, perdu ou ce qu'il a mangé
                    if (!animal && destination instanceof Loup) {
                        perdu = true;
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        new MenuView().start(new Stage());
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("VOUS AVEZ PERDU");
                        alert.setHeaderText(null);
                        alert.setContentText("Le mouton a été mangé par le loup !");
                        alert.showAndWait();
                        currentStage.close();
                        return;
                    }

                    if (!animal && destination instanceof Sortie) {
                        gagne = true;
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        new MenuView().start(new Stage());
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("VOUS AVEZ GAGNE");
                        alert.setHeaderText(null);
                        alert.setContentText("VICTOIREEEEE !");
                        alert.showAndWait();
                        currentStage.close();
                        return;
                    }

                    if (animal && destination instanceof Mouton) {
                        perdu = true;
                        mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);
                        new MenuView().start(new Stage());
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("VOUS AVEZ PERDU");
                        alert.setHeaderText(null);
                        alert.setContentText("DEFAITE ...");
                        alert.showAndWait();
                        currentStage.close();
                        return;
                    }

                    mettreAJourInterface(animalSelectedX, animalSelectedY, x, y, animalCourant);

                    if (animalCourant instanceof Mouton) {
                        Mouton mouton = (Mouton) animalCourant;

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

                    animal = !animal;
                    messageLabel.setText("Sélectionnez le \n" + (animal ? "loup" : "mouton"));
                    messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 35));

                } else {
                    messageLabel.setText("Déplacement impossible ! Sélectionnez le \n" + (animal ? "loup" : "mouton"));
                    messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 35));
                }

                animalSelected = false;
                animalSelectedX = -1;
                animalSelectedY = -1;
                effacerDeplacementsPossibles();
            }
        }
    }

    // Méthode qui permet de mettre en valeur les déplacements possible de l'animal en fonction de ce qu'il a mangé et de l'animal
    private void afficherDeplacementsPossibles(Animal animal) {
        int[][] deplacements = grille.lesDeplacements(animal);
        // On commence par effacer tous les déplacements pour éviter les bugs et les superpositions
        effacerDeplacementsPossibles();

        for (int[] deplacement : deplacements) {
            int x = deplacement[0];
            int y = deplacement[1];
            if (x >= 0 && x < grille.getNbColonnes() && y >= 0 && y < grille.getNbLignes()) {
                Rectangle highlight = new Rectangle(50, 50);
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

    // Elle sert tout simplement a enlever la mise en valeur des cases ou l'animal pouvait se déplacer
    private void effacerDeplacementsPossibles() {
        for (int i = 0; i < grille.getNbColonnes(); i++) {
            for (int j = 0; j < grille.getNbLignes(); j++) {
                if (cellules[i][j].getChildren().size() > 1) {
                    cellules[i][j].getChildren().remove(1, cellules[i][j].getChildren().size());
                }
            }
        }
    }

    // Cette fonction permet à chaque fois qu'on l'appelle de mettre à jour l'interface, a chaque déplacement cela met a jour la grille
    private void mettreAJourInterface(int oldX, int oldY, int newX, int newY, Animal animal) {
        ImageView animalView;
        if (animal instanceof Loup) {
            animalView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png")));
        } else {
            animalView = new ImageView(new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png")));
        }
        animalView.setFitWidth(50);
        animalView.setFitHeight(50);

        StackPane oldCell = cellules[oldX][oldY];
        StackPane newCell = cellules[newX][newY];

        oldCell.getChildren().clear();

        int rand = (int)(Math.random() * 3); // 0, 1 ou 2
        Elements vegetal;

        if (rand == 0) {
            vegetal = new Herbe(oldX, oldY);
        } else if (rand == 1) {
            vegetal = new Cactus(oldX, oldY);
        } else {
            vegetal = new Marguerite(oldX, oldY);
        }

        Image imageVegetal = null;
        if (vegetal instanceof Herbe) {
            imageVegetal = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
        } else if (vegetal instanceof Cactus) {
            imageVegetal = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/cactus.png"));
        } else if (vegetal instanceof Marguerite) {
            imageVegetal = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/marguerite.png"));
        }

        if (imageVegetal != null) {
            ImageView vegetalView = new ImageView(imageVegetal);
            vegetalView.setFitWidth(50);
            vegetalView.setFitHeight(50);
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
        incrementerTour();
    }

    public void setAutomatiserDijkstra (Animal animal, Sortie s) throws InterruptedException {
       if (animal instanceof Mouton) {
           List cell = grille.cheminLePlusCourt(animal.getX(),animal.getY(), s.getX(),s.getY(), animal);
           System.out.println(cell);
           for (int i = 0; i < cell.size(); i++) {
               Rectangle highlight = new Rectangle(50, 50);
               highlight.setFill(Color.GREEN.deriveColor(0, 1, 1, 0.5));
               Thread.sleep(500);
           }
           if (cell.size() <= 1) return;
           int[] deplacement = (int[]) cell.get(1);

           handleCellClick(deplacement[0],deplacement[1]);
           coMouton = (int[]) cell.get(animal.getVitesse());
       }
       else if (animal instanceof Loup) {
           List cell = grille.cheminLePlusCourt(animal.getX(),animal.getY(), coMouton[0],coMouton[1], animal);
           for (int i = 0; i < cell.size(); i++) {
               Rectangle highlight = new Rectangle(50, 50);
               highlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
               Thread.sleep(500);
           }
           if (cell.size() <= 1) return;
           int[] deplacement = (int[]) cell.get(1);

           handleCellClick(deplacement[0],deplacement[1]);
           coLoup = (int[]) cell.get(animal.getVitesse());
       }
    }

    public void setAutomatiserAetoile (Animal animal, Sortie s) throws InterruptedException {
        System.out.println("Entrée dans setAutomatiserAetoile");
        if (animal instanceof Mouton) {
            List cell = grille.aEtoile(animal.getX(),animal.getY(), s.getX(),s.getY());
            System.out.println(cell);
            for (int i = 0; i < cell.size(); i++) {
                Rectangle highlight = new Rectangle(50, 50);
                highlight.setFill(Color.GREEN.deriveColor(0, 1, 1, 0.5));
                Thread.sleep(500);
            }
            int[] deplacement = (int[]) cell.get(animal.getVitesse());
            handleCellClick(deplacement[0],deplacement[1]);
            coMouton = (int[]) cell.get(animal.getVitesse());
        }
        else if (animal instanceof Loup) {
            List cell = grille.aEtoile(animal.getX(),animal.getY(), coMouton[0],coMouton[1]);
            System.out.println(cell);
            for (int i = 0; i < cell.size(); i++) {
                Rectangle highlight = new Rectangle(50, 50);
                highlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
                Thread.sleep(500);
            }
            int[] deplacement = (int[]) cell.get(animal.getVitesse());
            handleCellClick(deplacement[0],deplacement[1]);
            coLoup = (int[]) cell.get(animal.getVitesse());
        }
    }

    public static void setAnimal(boolean b){
        animal = b;
    }

    // On incrémente a chaque déplacement les tours pour l'affichage
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

    public void setCoMouton(int x, int y){
        coMouton[0] = x;
        coMouton[1] = y;
    }

    public void setCoLoup(int x, int y){
        coLoup[0] = x;
        coLoup[1] = y;
    }

    public void setCoSortie(int x, int y){
        coSortie[0] = x;
        coSortie[1] = y;
    }

    public static void main(String[] args) {
        launch(args);
    }
}