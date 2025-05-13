package com.example.jeuduloup2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private final int TAILLE_CASE = 60;
    private String modePlacement = null;
    private boolean moutonPlace = false;
    private boolean loupPlace = false;
    private boolean existSortie = false;

    private StackPane creerBoutonImage(Image image, Runnable action) {
        ImageView icone = new ImageView(image);
        icone.setFitWidth(140);
        icone.setFitHeight(140);

        StackPane container = new StackPane(icone);
        container.setPrefSize(150, 150);
        container.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-border-color: white;
            -fx-border-width: 2px;
            -fx-border-radius: 15px;
            -fx-background-radius: 15px;
            -fx-cursor: hand;
        """);

        container.setOnMouseClicked(e -> action.run());
        return container;
    }

    private void afficherMessage(String contenu, VBox messageBox) {
        Text text = new Text(contenu);
        text.setFont(new Font("Arial", 17));
        text.setStyle("-fx-fill: white;");
        messageBox.getChildren().add(text);
    }

    @Override
    public void start(Stage primaryStage) {
        Grille grille = new Grille(10, 10);
        grille.miseEnPlace();

        StackPane container = new StackPane();
        container.setPrefSize(150, 500);
        container.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-border-color: white;
            -fx-border-width: 2px;
            -fx-border-radius: 15px;
            -fx-background-radius: 15px;
            -fx-cursor: hand;
        """);

        VBox messageBox = new VBox(5);
        messageBox.setAlignment(Pos.TOP_LEFT);
        container.getChildren().add(messageBox);

        VBox cont = new VBox(container);
        cont.setAlignment(Pos.CENTER_RIGHT);
        cont.setPrefSize(170, 500);

        Image imgFondHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/FondHerbe.png"));
        Image imgHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
        Image imgRocher = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/rocher.png"));
        Image imgMouton = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png"));
        Image imgLoup = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png"));
        Image imgMarguerite = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/marguerite.png"));
        Image imgCactus = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/cactus.png"));
        Image imgSupprimer = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/supprimer.png"));
        Image imgSortie = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/sortie.png"));

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
                    boolean estRocher = grille.getElements().stream().anyMatch(e -> e.getX() == x && e.getY() == y && e instanceof Rocher);
                    boolean estLoup = grille.getElements().stream().anyMatch(e -> e.getX() == x && e.getY() == y && e instanceof Loup);
                    boolean estMouton = grille.getElements().stream().anyMatch(e -> e.getX() == x && e.getY() == y && e instanceof Mouton);

                    if (modePlacement == null) return;

                    if ("mouton".equals(modePlacement) && !moutonPlace) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (estRocher) {
                            afficherMessage("Il y a un rocher !", messageBox);
                        } else if (estLoup) {
                            afficherMessage("Il y a un loup !", messageBox);
                        } else if (!estBordure) {
                            cell.getChildren().clear();
                            grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && !(e instanceof Rocher));
                            ImageView mouton = new ImageView(imgMouton);
                            mouton.setFitWidth(TAILLE_CASE);
                            mouton.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(mouton);
                            grille.getElements().add(new Mouton(x, y));
                            moutonPlace = true;
                            modePlacement = null;
                        }
                    } else if ("loup".equals(modePlacement) && !loupPlace) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (estRocher) {
                            afficherMessage("Il y a un rocher !", messageBox);
                        } else if (estMouton) {
                            afficherMessage("Il y a un mouton !", messageBox);
                        } else if (!estBordure) {
                            cell.getChildren().clear();
                            grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && !(e instanceof Rocher));
                            ImageView loup = new ImageView(imgLoup);
                            loup.setFitWidth(TAILLE_CASE);
                            loup.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(loup);
                            grille.getElements().add(new Loup(x, y));
                            loupPlace = true;
                            modePlacement = null;
                        }
                    } else if ("marguerite".equals(modePlacement)) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (!estRocher && !estBordure) {
                            cell.getChildren().clear();
                            grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && !(e instanceof Rocher));
                            ImageView marguerite = new ImageView(imgMarguerite);
                            marguerite.setFitWidth(TAILLE_CASE);
                            marguerite.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(marguerite);
                            grille.getElements().add(new Marguerite(x, y));
                        }
                    } else if ("cactus".equals(modePlacement)) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (!estRocher && !estBordure) {
                            cell.getChildren().clear();
                            grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && !(e instanceof Rocher));
                            ImageView cactus = new ImageView(imgCactus);
                            cactus.setFitWidth(TAILLE_CASE);
                            cactus.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(cactus);
                            grille.getElements().add(new Cactus(x, y));
                        }
                    } else if ("supprimer".equals(modePlacement)) {
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
                    } else if ("sortie".equals(modePlacement)) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (existSortie) {
                            boolean sortieEncoreValide = grille.sortieValide();
                            if (sortieEncoreValide) {
                                modePlacement = null;
                                return;
                            }
                        }
                        if (estRocher && estBordure) {
                            grille.getElements().removeIf(e -> e.getX() == x && e.getY() == y && e instanceof Rocher);
                            cell.getChildren().clear();
                            ImageView herbe = new ImageView(imgHerbe);
                            herbe.setFitWidth(TAILLE_CASE);
                            herbe.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(herbe);
                            grille.getElements().add(new Herbe(x, y));
                            modePlacement = null;
                            existSortie = true;
                        }
                    }
                });

                grid.add(cell, j, i);
            }
        }

        StackPane boutonMouton = creerBoutonImage(imgMouton, () -> modePlacement = "mouton");
        StackPane boutonLoup = creerBoutonImage(imgLoup, () -> modePlacement = "loup");
        StackPane boutonMarguerite = creerBoutonImage(imgMarguerite, () -> modePlacement = "marguerite");
        StackPane boutonCactus = creerBoutonImage(imgCactus, () -> modePlacement = "cactus");
        StackPane boutonSupprimer = creerBoutonImage(imgSupprimer, () -> modePlacement = "supprimer");
        StackPane boutonSortie = creerBoutonImage(imgSortie, () -> modePlacement = "sortie");

        VBox menu = new VBox(20, boutonMouton, boutonLoup, boutonMarguerite);
        menu.setAlignment(Pos.CENTER);
        VBox menu1 = new VBox(20, boutonCactus, boutonSupprimer, boutonSortie);
        menu1.setAlignment(Pos.CENTER);
        menu.setSpacing(50);
        menu1.setSpacing(50);
        HBox toutmenu = new HBox(20, menu, menu1);
        toutmenu.setAlignment(Pos.CENTER);
        HBox rootContent = new HBox(50, cont, grid, toutmenu);
        rootContent.setSpacing(200);
        rootContent.setAlignment(Pos.CENTER);

        ImageView background = new ImageView(imgFondHerbe);
        background.setFitWidth(1920);
        background.setFitHeight(1080);

        StackPane root = new StackPane(background, rootContent);
        StackPane.setAlignment(rootContent, Pos.CENTER);

        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Jeu du loup");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
