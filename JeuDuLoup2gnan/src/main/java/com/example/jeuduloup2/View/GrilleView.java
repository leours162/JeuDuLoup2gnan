package com.example.jeuduloup2.View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import com.example.jeuduloup2.Grille;
import com.example.jeuduloup2.Elements;
import com.example.jeuduloup2.Mouton;
import com.example.jeuduloup2.Loup;
import com.example.jeuduloup2.Rocher;
import com.example.jeuduloup2.Sortie;
import com.example.jeuduloup2.Marguerite;
import com.example.jeuduloup2.Cactus;
import com.example.jeuduloup2.Herbe;
import com.example.jeuduloup2.View.JeuView;
import java.io.File;
import java.io.IOException;

public class GrilleView extends Application {

    private StackPane creerBoutonImage(Image image, Runnable action) {
        ImageView icone = new ImageView(image);
        icone.setFitWidth(140);
        icone.setFitHeight(140);

        StackPane container = new StackPane(icone);
        container.setPrefSize(300, 150);
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

    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();

        Image fond = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/fond.png"));
        ImageView background = new ImageView(fond);
        background.setFitWidth(1920);
        background.setFitHeight(1080);

        Text titre = new Text("Créez La Grille");
        Font fontTitre = Font.loadFont(getClass().getResourceAsStream("/fonts/Unkempt-Bold.ttf"), 80);
        titre.setFont(fontTitre);
        titre.setStyle("-fx-fill: white;");

        Text longLabel = new Text("Longueur");
        longLabel.setStyle("-fx-fill: white; -fx-font-weight: bold; -fx-font-size: 30px;");
        Spinner<Integer> spinnerLongueur = new Spinner<>();
        spinnerLongueur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 12, 8));

        Text hautLabel = new Text("Hauteur");
        hautLabel.setStyle("-fx-fill: white; -fx-font-weight: bold; -fx-font-size: 30px;");
        Spinner<Integer> spinnerHauteur = new Spinner<>();
        spinnerHauteur.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 12, 8));

        Button valider = new Button("Valider");
        valider.setStyle("-fx-font-size: 25px; -fx-background-color: white; -fx-text-fill: black;");
        valider.setOnAction(e -> {
            int largeur = spinnerLongueur.getValue();
            int hauteur = spinnerHauteur.getValue();
            MiseEnPlaceView jeu = new MiseEnPlaceView();
            jeu.setgrille(largeur,hauteur);
            jeu.start(new Stage());
            stage.close();
        });

        Button importer = new Button("Importer");
        importer.setStyle("-fx-font-size: 25px; -fx-background-color: white; -fx-text-fill: black;");
        importer.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importer une grille");
            File fichier = fileChooser.showOpenDialog(stage);

            if (fichier != null) {
                try {
                    JeuView jeu = new JeuView();
                    jeu.setFichierGrille(fichier);
                    jeu.setGrilleimportee();
                    jeu.start(new Stage());
                    stage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);

        Image imgRetour = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/retour.png"));
        StackPane boutonRetour = creerBoutonImage(imgRetour, () -> {new MenuView().start(new Stage());
        stage.close();});

        HBox barreHaut = new HBox(250, boutonRetour, titre, logoView);
        barreHaut.setAlignment(Pos.CENTER);
        barreHaut.setPadding(new Insets(20));

        VBox centre = new VBox(20, longLabel, spinnerLongueur, hautLabel, spinnerHauteur, valider, importer);
        centre.setAlignment(Pos.CENTER);

        BorderPane layout = new BorderPane();
        layout.setTop(barreHaut);
        layout.setCenter(centre);
        layout.setPadding(new Insets(30));

        StackPane root = new StackPane(background, layout);
        Scene scene = new Scene(root, 1920, 1080);

        stage.setTitle("Création de la grille");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
