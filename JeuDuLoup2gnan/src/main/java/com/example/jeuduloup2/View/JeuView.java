package com.example.jeuduloup2.View;

import com.example.jeuduloup2.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JeuView extends Application {
    private final int TAILLE_CASE = 60;
    private String modePlacement = null;
    private boolean moutonPlace = false;
    private boolean loupPlace = false;
    private boolean existSortie = false;
    private Elements e;
    public int nbColonnes;
    public int nbLignes;

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

    public void setgrille(int x, int y){
        this.nbColonnes = x;
        this.nbLignes = y;
    }

    private void afficherMessage(String contenu, VBox messageBox) {
        Text text = new Text(contenu);
        text.setFont(new Font("Arial", 17));
        text.setStyle("-fx-fill: red;");
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



        Image imgFondHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/FondHerbe.png"));
        Image imgHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
        Image imgRocher = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/rocher.png"));
        Image imgMouton = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png"));
        Image imgLoup = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png"));
        Image imgMarguerite = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/marguerite.png"));
        Image imgCactus = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/cactus.png"));
        Image imgSupprimer = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/supprimer.png"));
        Image imgSortie = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/sortie.png"));
        Image imgRetour = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/retour.png"));
        Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));

        GridPane grid = new GridPane();
        Text titre = new Text("Créez le labyrinthe");
        titre.setStyle("-fx-fill: white;");
        Font lobster = Font.loadFont(getClass().getResourceAsStream("/fonts/Unkempt-Bold.ttf"), 80);        titre.setFont(lobster);
        VBox titregrille = new VBox(titre, grid);
        grid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);


        int nbLignes = this.nbLignes;
        int nbColonnes = this.nbColonnes;

        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(TAILLE_CASE, TAILLE_CASE);


                Elements e = grille.getElement(j, i);

                if (e instanceof Rocher) {
                    ImageView rocher = new ImageView(imgRocher);
                    rocher.setFitWidth(TAILLE_CASE);
                    rocher.setFitHeight(TAILLE_CASE);
                    cell.getChildren().add(rocher);
                } else if (e instanceof Herbe) {
                    ImageView herbe = new ImageView(imgHerbe);
                    herbe.setFitWidth(TAILLE_CASE);
                    herbe.setFitHeight(TAILLE_CASE);
                    cell.getChildren().add(herbe);
                }

                final int x = j;
                final int y = i;

                cell.setOnMouseClicked(event -> {
                    boolean estRocher = ((grille.getElement(x,y)) instanceof Rocher);
                    boolean estLoup = ((grille.getElement(x,y)) instanceof Loup);
                    boolean estMouton = ((grille.getElement(x,y)) instanceof Mouton);
                    boolean estSortie = ((grille.getElement(x,y)) instanceof Sortie);
                        if (modePlacement == null) return;

                    if ("mouton".equals(modePlacement) && !moutonPlace) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (estRocher) {
                            afficherMessage("Il y a un rocher !", messageBox);
                        } else if (estLoup) {
                            afficherMessage("Il y a un loup !", messageBox);
                        } else if (!estBordure) {
                            cell.getChildren().clear();
                            grille.remplacer(x,y,new Mouton(x,y));
                            ImageView mouton = new ImageView(imgMouton);
                            mouton.setFitWidth(TAILLE_CASE);
                            mouton.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(mouton);
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
                            grille.remplacer(x,y,new Loup(x,y));
                            ImageView loup = new ImageView(imgLoup);
                            loup.setFitWidth(TAILLE_CASE);
                            loup.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(loup);
                            loupPlace = true;
                            modePlacement = null;
                        }
                    } else if ("marguerite".equals(modePlacement)) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (estBordure) {
                            afficherMessage("Il y a un rocher !", messageBox);
                        }
                        if (estLoup) {
                            afficherMessage("Il y a un loup !", messageBox);
                        }
                        else if (estMouton) {
                            afficherMessage("Il y a un mouton !", messageBox);
                        }
                        else if (!estRocher && !estBordure) {
                            cell.getChildren().clear();
                            grille.remplacer(x,y,new Marguerite(x,y));
                            ImageView marguerite = new ImageView(imgMarguerite);
                            marguerite.setFitWidth(TAILLE_CASE);
                            marguerite.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(marguerite);
                        }
                    } else if ("cactus".equals(modePlacement)) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (estBordure) {
                            afficherMessage("Il y a un rocher !", messageBox);
                        }
                        if (estLoup) {
                            afficherMessage("Il y a un loup !", messageBox);
                        }
                        else if (estMouton) {
                            afficherMessage("Il y a un mouton !", messageBox);
                        }
                        else if (!estRocher) {
                            cell.getChildren().clear();
                            grille.remplacer(x,y,new Mouton(x,y));
                            ImageView cactus = new ImageView(imgCactus);
                            cactus.setFitWidth(TAILLE_CASE);
                            cactus.setFitHeight(TAILLE_CASE);
                            cell.getChildren().add(cactus);
                        }
                    }   else if ("rocher".equals(modePlacement)) {
                            boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                            if (estLoup) {
                                afficherMessage("Il y a un loup !", messageBox);
                            }
                            else if (estMouton) {
                                afficherMessage("Il y a un mouton !", messageBox);
                            }
                            else if (!estBordure) {
                                cell.getChildren().clear();
                                grille.remplacer(x,y,new Rocher(x,y));
                                ImageView rocher = new ImageView(imgRocher);
                                rocher.setFitWidth(TAILLE_CASE);
                                rocher.setFitHeight(TAILLE_CASE);
                                cell.getChildren().add(rocher);
                            }
                    } else if ("supprimer".equals(modePlacement)) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        Elements element = grille.getElement(x,y);
                        if (estSortie){
                            grille.remplacer(x,y,new Rocher(x,y));
                            ImageView rocher = new ImageView(imgRocher);
                            rocher.setFitWidth(TAILLE_CASE);
                            rocher.setFitHeight(TAILLE_CASE);
                            cell.getChildren().clear();
                            cell.getChildren().add(rocher);
                            existSortie = false;
                        }
                        else if (estBordure) {
                            afficherMessage("Pas la bordure!", messageBox);
                        }
                        else {
                                grille.remplacer(x,y,new Herbe(x,y));
                                ImageView fondHerbe = new ImageView(imgHerbe);
                                fondHerbe.setFitWidth(TAILLE_CASE);
                                fondHerbe.setFitHeight(TAILLE_CASE);
                                cell.getChildren().clear();
                                cell.getChildren().add(fondHerbe);

                                if (element instanceof Mouton) {
                                    moutonPlace = false;
                                }
                                if (element instanceof Loup) {
                                    loupPlace = false;
                                }
                        }
                    } else if ("sortie".equals(modePlacement)) {
                        boolean estBordure = (x == 0 || y == 0 || x == nbColonnes - 1 || y == nbLignes - 1);
                        if (existSortie){
                            afficherMessage("Il y a déjà une sortie !", messageBox);
                        }
                        else if (estRocher && estBordure) {
                            if ((x == 0 && y == 0) || (x==nbColonnes-1 && y == 0) || (x == 0 && y == nbLignes-1) || (x == nbLignes-1 && y == nbColonnes-1)){
                                afficherMessage("Pas les coins !", messageBox);
                            }else if (grille.getElement(x,y) instanceof Rocher) {
                                grille.remplacer(x,y,new Sortie(x,y));
                                cell.getChildren().clear();
                                ImageView herbe = new ImageView(imgHerbe);
                                herbe.setFitWidth(TAILLE_CASE);
                                herbe.setFitHeight(TAILLE_CASE);
                                cell.getChildren().add(herbe);
                                modePlacement = null;
                                existSortie = true;
                                this.e=grille.getElement(x,y);
                            }
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
        StackPane boutonRocher = creerBoutonImage(imgRocher, () -> modePlacement = "rocher");
        StackPane boutonRetour = creerBoutonImage(imgRetour, () -> {
            new GrilleView().start(new Stage());
            primaryStage.close();
        });

        Text demarrer = new Text("Démarrer");
        demarrer.setFont(new Font("Arial", 40));
        demarrer.setStyle("-fx-fill: white;");
        StackPane demarrerbouton = new StackPane(demarrer);
        demarrerbouton.setPrefSize(150, 150);
        demarrerbouton.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-border-color: white;
            -fx-border-width: 2px;
            -fx-border-radius: 15px;
            -fx-background-radius: 15px;
            -fx-cursor: hand;
        """);



        VBox menu = new VBox(20, boutonMouton, boutonLoup, boutonMarguerite);
        menu.setAlignment(Pos.CENTER);
        VBox menu1 = new VBox(20, boutonCactus, boutonSupprimer, boutonSortie);
        menu1.setAlignment(Pos.CENTER);
        VBox menu2 = new VBox(20, boutonRocher);
        menu2.setAlignment(Pos.CENTER);
        menu.setSpacing(50);
        menu1.setSpacing(50);
        HBox toutmenu = new HBox(20, menu, menu1,menu2);
        toutmenu.setAlignment(Pos.CENTER);
        ImageView vlogo = new ImageView(logo);
        vlogo.setFitWidth(300);
        vlogo.setFitHeight(300);
        VBox droite = new VBox(20, vlogo, toutmenu);
        VBox cont = new VBox(boutonRetour,container,demarrerbouton);
        cont.setSpacing(30);
        cont.setAlignment(Pos.CENTER_RIGHT);
        cont.setPrefSize(170, 500);
        titregrille.setAlignment(Pos.CENTER);
        titregrille.setSpacing(30);
        HBox rootContent = new HBox(50, cont, titregrille, droite);
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
