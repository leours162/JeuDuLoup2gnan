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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class JeuView extends Application {

    private static Grille grille;
    private int tour = 1;
    private int cactusMangé = 0;
    private int margueritéMangée = 0;
    private int herbeMangée = 0;

    private Label tourLabel;
    private Label cptHerbe;
    private Label cptCactus;
    private Label cptMarguerite;

    public boolean gagne = false;
    public boolean perdu = false;


    public static void setGrille(Grille grille) {
        JeuView.grille = grille;
    }


    @Override
    public void start(Stage primaryStage) {
        tourLabel = new Label("Tour " + tour);
        tourLabel.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        tourLabel.setStyle("-fx-text-fill: white;");
        StackPane tourPane = new StackPane(tourLabel);
        tourPane.setAlignment(Pos.BOTTOM_RIGHT);
        tourPane.setPadding(new Insets(15));

        Button boutonTourSuivant = new Button("Tour suivant");
        boutonTourSuivant.setOnAction(e -> incrementerTour());


        Image imgMarguerite = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/marguerite.png"));
        Image imgCactus = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/cactus.png"));
        Image imgHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
        Image imgRocher = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/rocher.png"));
        Image imgMouton = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png"));
        Image imgLoup = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png"));
        Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));
        Image imgFondHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/FondHerbe.png"));

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

        GridPane grid = new GridPane();
        for (int i = 0; i < grille.getNbLignes(); i++) {
            for (int j = 0; j < grille.getNbColonnes(); j++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(60, 60);


                Elements e = grille.getElement(j, i);

                if (e instanceof Rocher) {
                    ImageView rocher = new ImageView(imgRocher);
                    rocher.setFitWidth(60);
                    rocher.setFitHeight(60);
                    cell.getChildren().add(rocher);
                } else if (e instanceof Herbe) {
                    ImageView herbe = new ImageView(imgHerbe);
                    herbe.setFitWidth(60);
                    herbe.setFitHeight(60);
                    cell.getChildren().add(herbe);
                } else if (e instanceof Mouton) {
                    ImageView mouton = new ImageView(imgMouton);
                    mouton.setFitWidth(60);
                    mouton.setFitHeight(60);
                    cell.getChildren().add(mouton);
                } else if (e instanceof Loup) {
                    ImageView loup = new ImageView(imgLoup);
                    loup.setFitWidth(60);
                    loup.setFitHeight(60);
                    cell.getChildren().add(loup);
                } else if (e instanceof Marguerite) {
                    ImageView marguerite = new ImageView(imgMarguerite);
                    marguerite.setFitWidth(60);
                    marguerite.setFitHeight(60);
                    cell.getChildren().add(marguerite);
                }
                else if (e instanceof Cactus) {
                    ImageView cactus = new ImageView(imgCactus);
                    cactus.setFitWidth(60);
                    cactus.setFitHeight(60);
                    cell.getChildren().add(cactus);
                } else if (e instanceof Sortie) {
                    ImageView sortie = new ImageView(imgHerbe);
                    sortie.setFitWidth(60);
                    sortie.setFitHeight(60);
                    cell.getChildren().add(sortie);
                }
                grid.add(cell, j, i);
            }
        }
        grid.setAlignment(Pos.CENTER);
        ImageView logoJeu = new ImageView(logo);
        logoJeu.setFitHeight(200);
        logoJeu.setFitWidth(200);

        VBox logotour = new VBox(logoJeu, tourPane);
        logotour.setSpacing(650);

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


        while ((!(gagne))||(!(perdu))){
            grille.lesDeplacements(ParamsView.get)
        }
    }



    private void incrementerTour() {
        tour++;
        tourLabel.setText("Tour " + tour);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


