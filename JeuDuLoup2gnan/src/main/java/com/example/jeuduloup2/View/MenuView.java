package com.example.jeuduloup2.View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.example.jeuduloup2.View.GrilleView;

import static javafx.application.Application.launch;


public class MenuView extends Application {
    private StackPane creerBoutonImage(Text text, Runnable action) {
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        text.setStyle("-fx-fill: white;");

        StackPane container = new StackPane(text);
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
        StackPane demarrer = creerBoutonImage(new Text("Démarrer"), () -> {
            new GrilleView().start(new Stage());
            primaryStage.close();
        });
        StackPane options = creerBoutonImage(new Text("Options"), () -> {});
        StackPane quitter = creerBoutonImage(new Text("Quitter"), () -> {primaryStage.close();});
        VBox vBox1 = new VBox(demarrer, options, quitter);
        vBox1.setSpacing(20);
        vBox1.setAlignment(Pos.CENTER);
        Text titre = new Text("Mange Moi \nSi Tu Peux !");
        titre.setStyle("-fx-fill: white;");
        Font lobster = Font.loadFont(getClass().getResourceAsStream("/fonts/Unkempt-Bold.ttf"), 80);        titre.setFont(lobster);
        VBox vBox2 = new VBox(titre);
        Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));
        ImageView logoImageView = new ImageView(logo);
        logoImageView.setFitWidth(300);
        logoImageView.setFitHeight(300);
        VBox vBox3 = new VBox(logoImageView);
        vBox3.setAlignment(Pos.BASELINE_RIGHT);
        HBox hBox = new HBox(vBox1, vBox2, vBox3);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(300);
        Image fond = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/fond.png"));
        ImageView background = new ImageView(fond);
        background.setFitWidth(1920);
        background.setFitHeight(1080);
        StackPane root = new StackPane(background, hBox);
        StackPane.setAlignment(vBox1, Pos.CENTER);
        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Jeu du loup");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setTitle("Jeu du loup");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}