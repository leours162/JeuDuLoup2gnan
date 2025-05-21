package com.example.jeuduloup2.View;

import com.example.jeuduloup2.View.SoundManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
public class ParamsView extends Application {



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

    @Override
    public void start(Stage stage) {


        Image fond = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/fond.png"));
        ImageView background = new ImageView(fond);
        background.setFitWidth(1920);
        background.setFitHeight(1080);

        Text titre = new Text("Options");
        Font fontTitre = Font.loadFont(getClass().getResourceAsStream("/fonts/Unkempt-Bold.ttf"), 100);
        titre.setFont(fontTitre);
        titre.setStyle("-fx-fill: white;");

        Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);

        Image imgRetour = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/retour.png"));
        StackPane boutonRetour = creerBoutonImage(imgRetour, () -> {
            new MenuView().start(new Stage());
            stage.close();
        });

        HBox barreHaut = new HBox(250, boutonRetour, titre, logoView);
        barreHaut.setAlignment(Pos.CENTER);
        barreHaut.setPadding(new Insets(20));

        Text titreAudio = new Text("Audio");
        titreAudio.setStyle("-fx-fill: white; -fx-font-weight: bold; -fx-font-size: 40px;");


        Slider sliderMusique = new Slider(0, 1, SoundManager.getVolume());
        sliderMusique.valueProperty().addListener((obs, oldVal, newVal) ->
                SoundManager.setVolume(newVal.doubleValue())
        );

        VBox blocAudio = new VBox(10, titreAudio, sliderMusique);
        blocAudio.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 30px;");
        blocAudio.setAlignment(Pos.CENTER);
        blocAudio.setPadding(new Insets(20));
        blocAudio.setPrefWidth(400);
        blocAudio.setPrefHeight(500);

        Text Systeme = new Text("Système");
        Systeme.setStyle("-fx-fill: white; -fx-font-weight: bold; -fx-font-size: 40px;");

        Text systeme = new Text("Qui joue en premier ?");
        systeme.setStyle("-fx-fill: white; -fx-font-size: 60px;");
        ToggleGroup groupe = new ToggleGroup();

        RadioButton Loup = new RadioButton("Loup");
        Loup.setToggleGroup(groupe);
        Loup.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        Loup.setOnMouseClicked(e -> JeuView.setAnimal(true));

        RadioButton Mouton = new RadioButton("Mouton");
        Mouton.setToggleGroup(groupe);
        Mouton.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        Mouton.setOnMouseClicked(e -> JeuView.setAnimal(false));

        VBox blocSysteme = new VBox(10, Systeme, systeme, Loup, Mouton);
        blocSysteme.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 30px;");
        blocSysteme.setAlignment(Pos.CENTER);
        blocSysteme.setPadding(new Insets(20));
        blocSysteme.setPrefWidth(400);
        blocSysteme.setPrefHeight(250);


        HBox centre = new HBox(blocAudio,blocSysteme);
        centre.setAlignment(Pos.CENTER);
        centre.setSpacing(80);


        BorderPane layout = new BorderPane();
        layout.setTop(barreHaut);
        layout.setCenter(centre);
        layout.setPadding(new Insets(30));

        StackPane root = new StackPane(background, layout);
        Scene scene = new Scene(root, 1920, 1080);

        stage.setTitle("Paramètres");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
