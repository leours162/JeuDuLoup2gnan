package com.example.jeuduloup2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private final int TAILLE_CASE = 60;
    private String modePlacement = null;
    private boolean moutonPlace = false;
    private boolean loupPlace = false;

    @Override
    public void start(Stage primaryStage) {
        Grille grille = new Grille(10, 10);
        grille.miseEnPlace();

        // Chargement des images
        Image imgFondHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/FondHerbe.png"));
        Image imgHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
        Image imgRocher = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/rocher.png"));
        Image imgMouton = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png"));
        Image imgLoup = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png"));

        GridPane grid = new GridPane();
        grid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        int nbLignes = grille.getNbLignes();
        int nbColonnes = grille.getNbColonnes();

        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(TAILLE_CASE, TAILLE_CASE);

                for (Elements e : grille.getElements()) {
                    if (e.getX() == j && e.getY() == i && e instanceof Herbe) {
                        ImageView herbe = new ImageView(imgHerbe);
                        herbe.setFitWidth(TAILLE_CASE);
                        herbe.setFitHeight(TAILLE_CASE);
                        cell.getChildren().add(herbe);
                    }
                }

                for (Elements e : grille.getElements()) {
                    if (e.getX() == j && e.getY() == i && e instanceof Rocher) {
                        ImageView rocher = new ImageView(imgRocher);
                        rocher.setFitWidth(TAILLE_CASE);
                        rocher.setFitHeight(TAILLE_CASE);
                        cell.getChildren().add(rocher);
                    }
                }

                final int x = j;
                final int y = i;

                cell.setOnMouseClicked(event -> {
                    boolean estRocher = grille.getElements().stream()
                            .anyMatch(e -> e.getX() == x && e.getY() == y && e instanceof Rocher);

                    boolean animalDéjàPrésent = grille.getElements().stream()
                            .anyMatch(e -> e.getX() == x && e.getY() == y && (e instanceof Mouton || e instanceof Loup));
                    if (animalDéjàPrésent && !"supprimer".equals(modePlacement)) return;

                    if (modePlacement == null) return;

                    if (modePlacement.equals("mouton") && !moutonPlace) {
                        cell.getChildren().clear();
                        grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && !(e instanceof Rocher));

                        ImageView mouton = new ImageView(imgMouton);
                        mouton.setFitWidth(TAILLE_CASE);
                        mouton.setFitHeight(TAILLE_CASE);
                        cell.getChildren().add(mouton);

                        grille.getElements().add(new Mouton(x, y));
                        moutonPlace = true;
                        modePlacement = null;

                    } else if (modePlacement.equals("loup") && !loupPlace) {
                        cell.getChildren().clear();
                        grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && !(e instanceof Rocher));

                        ImageView loup = new ImageView(imgLoup);
                        loup.setFitWidth(TAILLE_CASE);
                        loup.setFitHeight(TAILLE_CASE);
                        cell.getChildren().add(loup);

                        grille.getElements().add(new Loup(x, y));
                        loupPlace = true;
                        modePlacement = null;

                    } else if (modePlacement.equals("supprimer")) {
                        if (!estRocher) {
                            grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && (e instanceof Mouton || e instanceof Loup));

                            ImageView fondHerbe = new ImageView(imgHerbe);
                            fondHerbe.setFitWidth(TAILLE_CASE);
                            fondHerbe.setFitHeight(TAILLE_CASE);
                            cell.getChildren().clear();
                            cell.getChildren().add(fondHerbe);

                            if (moutonPlace && grille.getElements().stream().noneMatch(e -> e instanceof Mouton)) {
                                moutonPlace = false;
                            }
                            if (loupPlace && grille.getElements().stream().noneMatch(e -> e instanceof Loup)) {
                                loupPlace = false;
                            }
                        }



                    } else if (modePlacement.equals("sortie")) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);

                        if (estRocher && estBordure) {
                            grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && e instanceof Rocher);
                            cell.getChildren().clear();

                            ImageView herbe = new ImageView(imgHerbe);
                            herbe.setFitWidth(TAILLE_CASE);
                            herbe.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(herbe);

                            grille.getElements().add(new Herbe(x, y));
                            modePlacement = null;
                        }
                    }
                });

                grid.add(cell, j, i);
            }
        }

        // Boutons
        Button boutonMouton = new Button("Placer le mouton");
        boutonMouton.setOnAction(e -> modePlacement = "mouton");

        Button boutonLoup = new Button("Placer le loup");
        boutonLoup.setOnAction(e -> modePlacement = "loup");

        Button boutonSupprimer = new Button("Supprimer");
        boutonSupprimer.setOnAction(e -> modePlacement = "supprimer");

        Button boutonSortie = new Button("Placer la sortie");
        boutonSortie.setOnAction(e -> modePlacement = "sortie");

        VBox menu = new VBox(20, boutonMouton, boutonLoup, boutonSupprimer, boutonSortie);
        menu.setAlignment(Pos.CENTER);

        HBox rootContent = new HBox(50, grid, menu);
        rootContent.setAlignment(Pos.CENTER);

        ImageView background = new ImageView(imgFondHerbe);
        background.setFitWidth(1920);
        background.setFitHeight(1080);

        StackPane root = new StackPane(background, rootContent);
        StackPane.setAlignment(rootContent, Pos.CENTER);

        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Placement du mouton, du loup et de la sortie");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}