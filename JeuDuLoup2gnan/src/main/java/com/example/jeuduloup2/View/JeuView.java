    package com.example.jeuduloup2.View;

    import com.example.jeuduloup2.*;
    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
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
    import java.io.File;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Optional;
    import java.util.Random;

    public class JeuView extends Application {
        private boolean grilleimportee = false;
        private File fichierGrille;

        private boolean automatiser = false;
        private boolean simulationEnCours = false;

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

        private int animalSelectedX = -1;
        private int animalSelectedY = -1;
        private boolean animalSelected = false;
        private Label messageLabel;

        private GridPane grid;
        private StackPane[][] cellules;

        private Stage currentStage;
        private Random random = new Random();

        // Variables pour la simulation automatique
        private Loup loupInstance;
        private Mouton moutonInstance;
        private Sortie sortieInstance;

        public static void setGrille(Grille grille) {
            JeuView.grille = grille;
        }

        @Override
        public void start(Stage primaryStage) throws IOException, InterruptedException {
            // Code d'initialisation existant...
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

                for (int y = 0; y < maxLignes; y++) {
                    String ligneCourante = lignes.get(y);
                    for (int x = 0; x < maxColonnes; x++) {
                        char c;
                        if (x < ligneCourante.length()) {
                            c = ligneCourante.charAt(x);
                        } else {
                            c = ' ';
                        }

                        switch (Character.toLowerCase(c)) {
                            case 'm':
                                moutonInstance = new Mouton(x, y);  // ✅ Instance unique
                                grille.remplacer(x, y, moutonInstance);
                                coMouton[0] = x;
                                coMouton[1] = y;
                                break; // ❌ AJOUT DU BREAK MANQUANT
                            case 'l':
                                loupInstance = new Loup(x, y);      // ✅ Instance unique
                                grille.remplacer(x, y, loupInstance);
                                coLoup[0] = x;
                                coLoup[1] = y;
                                break; // ❌ AJOUT DU BREAK MANQUANT
                            case 'x':
                                grille.remplacer(x, y, new Rocher(x, y));
                                break;
                            case 's':
                                sortieInstance = new Sortie(x, y);
                                grille.remplacer(x, y, sortieInstance);
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
                                // Laisser vide ou mettre de l'herbe par défaut
                                grille.remplacer(x, y, new Herbe(x, y));
                                break;
                        }
                    }
                }
            }

            // Initialiser les instances d'animaux
            initialiserAnimaux();

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

            // Images...
            Image imgMarguerite = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/marguerite.png"));
            Image imgCactus = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/cactus.png"));
            Image imgHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));
            Image imgRocher = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/rocher.png"));
            Image imgMouton = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/mouton.png"));
            Image imgLoup = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/loup.png"));
            Image logo = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/logo.png"));
            Image imgFondHerbe = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/FondHerbe.png"));
            Image imgSortie = new Image(getClass().getResourceAsStream("/com/example/jeuduloup2/herbe.png"));

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
                if (!simulationEnCours) {
                    lancerSimulationAutomatique();
                }
            });

            HBox végétaux = new HBox(imgCact, cptCactus, imgMarg, cptMarguerite, imgHerb, cptHerbe);
            végétaux.setAlignment(Pos.CENTER);
            végétaux.setSpacing(30);

            VBox compteurs = new VBox(végétaux);

            // Création de la grille d'affichage
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
                        if (!simulationEnCours) {
                            handleCellClick(x, y);
                        }
                    });

                    grid.add(cell, j, i);
                }
            }

            grid.setAlignment(Pos.CENTER);
            ImageView logoJeu = new ImageView(logo);
            logoJeu.setFitHeight(150);
            logoJeu.setFitWidth(150);

            VBox logotour = new VBox(logoJeu, tourPane, messageLabel, vitesseLabel, Automatiser);
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

            if (gagne) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("VOUS AVEZ GAGNE");
                alert.setHeaderText(null);
                alert.setContentText("VICTOIREEEEE !");
                alert.showAndWait();
                primaryStage.close();
            }
            if (perdu) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("VOUS AVEZ PERDU");
                alert.setHeaderText(null);
                alert.setContentText("DEFAITE ...");
                alert.showAndWait();
                primaryStage.close();
            }
        }

        // NOUVELLE MÉTHODE : Initialiser les instances d'animaux
        private void initialiserAnimaux() {
            for (int y = 0; y < grille.getNbLignes(); y++) {
                for (int x = 0; x < grille.getNbColonnes(); x++) {
                    Elements element = grille.getElement(x, y);
                    if (element instanceof Loup) {
                        loupInstance = (Loup) element;
                        coLoup[0] = x;
                        coLoup[1] = y;
                    } else if (element instanceof Mouton) {
                        moutonInstance = (Mouton) element;
                        coMouton[0] = x;
                        coMouton[1] = y;
                    } else if (element instanceof Sortie) {
                        sortieInstance = (Sortie) element;
                        coSortie[0] = x;
                        coSortie[1] = y;
                    }
                }
            }
        }

        // NOUVELLE MÉTHODE : Lancer la simulation automatique
        private void lancerSimulationAutomatique() {
            simulationEnCours = true;
            messageLabel.setText("Simulation automatique\nen cours...");

            Thread simulationThread = new Thread(() -> {
                try {
                    while (!gagne && !perdu && tour < 200) { // Limite de 200 tours

                        // 1. Vérifier vision et changer modes
                        boolean loupVoitMouton = peutVoir(loupInstance, moutonInstance);
                        boolean moutonVoitLoup = peutVoir(moutonInstance, loupInstance);

                        Platform.runLater(() -> {
                            tourLabel.setText("Tour " + tour);
                        });

                        // 2. Déplacer le loup
                        deplacerAnimalAutomatique(loupInstance, loupVoitMouton ? moutonInstance : null);

                        // Vérifier si le loup a attrapé le mouton
                        if (loupInstance.getX() == moutonInstance.getX() && loupInstance.getY() == moutonInstance.getY()) {
                            perdu = true;
                            Platform.runLater(() -> afficherResultatFinal("Loup"));
                            break;
                        }

                        Thread.sleep(500); // Pause pour voir l'animation

                        // 3. Déplacer le mouton
                        deplacerAnimalAutomatique(moutonInstance, moutonVoitLoup ? sortieInstance : null);

                        // Vérifier si le mouton a atteint la sortie
                        if (moutonInstance.getX() == sortieInstance.getX() && moutonInstance.getY() == sortieInstance.getY()) {
                            gagne = true;
                            Platform.runLater(() -> afficherResultatFinal("Mouton"));
                            break;
                        }

                        // Vérifier collision après déplacement du mouton
                        if (loupInstance.getX() == moutonInstance.getX() && loupInstance.getY() == moutonInstance.getY()) {
                            perdu = true;
                            Platform.runLater(() -> afficherResultatFinal("Loup"));
                            break;
                        }

                        tour++;
                        Thread.sleep(500);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    simulationEnCours = false;
                }
            });

            simulationThread.setDaemon(true);
            simulationThread.start();
        }

        // NOUVELLE MÉTHODE : Vérifier si un animal peut voir un autre
        private boolean peutVoir(Animal observateur, Elements cible) {
            int distance = Math.abs(observateur.getX() - cible.getX()) + Math.abs(observateur.getY() - cible.getY());
            return distance <= 5; // Distance Manhattan <= 5
        }

        // NOUVELLE MÉTHODE : Déplacer un animal automatiquement
        private void deplacerAnimalAutomatique(Animal animal, Elements cible) throws InterruptedException {
            int[] nouvellePosition;

            if (cible != null) {
                // Mode chasse/fuite : utiliser Dijkstra avec affichage du chemin
                nouvellePosition = obtenirPositionAvecDijkstraEtAffichage(animal, cible);
            } else {
                // Mode aléatoire
                nouvellePosition = obtenirPositionAleatoire(animal);
            }

            if (nouvellePosition != null) {
                // SAUVEGARDER les anciennes coordonnées AVANT de déplacer l'animal
                int oldX = animal.getX();
                int oldY = animal.getY();

                // Déplacer l'animal
                animal.bouger(nouvellePosition[0], nouvellePosition[1]);

                // Puis mise à jour asynchrone de l'interface avec les bonnes coordonnées
                Platform.runLater(() -> {
                    mettreAJourInterfaceAutomatique(animal, oldX, oldY, nouvellePosition[0], nouvellePosition[1]);
                });

                // Attendre que l'interface soit mise à jour
                Thread.sleep(100);
            }
        }

        // NOUVELLE MÉTHODE : Obtenir position avec Dijkstra et affichage du calcul en temps réel
        private int[] obtenirPositionAvecDijkstraEtAffichage(Animal animal, Elements cible) throws InterruptedException {
            // Utiliser notre propre implémentation de Dijkstra avec visualisation
            ArrayList<int[]> chemin = calculerCheminAvecVisualisation(animal, cible);

            if (chemin.size() > 1) {
                // Afficher le chemin final en couleur différente
                Platform.runLater(() -> {
                    // Effacer les cases de réflexion
                    effacerDeplacementsPossibles();

                    // Afficher le chemin final
                    for (int i = 1; i < chemin.size(); i++) {
                        int[] pos = chemin.get(i);
                        int x = pos[0];
                        int y = pos[1];

                        if (x >= 0 && x < grille.getNbColonnes() && y >= 0 && y < grille.getNbLignes()) {
                            Rectangle highlight = new Rectangle(50, 50);

                            if (animal instanceof Loup) {
                                highlight.setFill(Color.RED.deriveColor(0, 1, 1, 0.6)); // Rouge vif pour le chemin final
                            } else {
                                highlight.setFill(Color.BLUE.deriveColor(0, 1, 1, 0.6)); // Bleu vif pour le chemin final
                            }

                            cellules[x][y].getChildren().add(highlight);
                        }
                    }
                });

                // Attendre pour que l'utilisateur voie le chemin final
                Thread.sleep(1500);

                // Effacer le chemin affiché
                Platform.runLater(() -> {
                    effacerDeplacementsPossibles();
                });

                // MODIFICATION ICI : Calculer la distance maximale selon la vitesse
                int vitesseMax = animal.getVitesse();
                int distanceMax = 0;
                int indexOptimal = 1; // Au minimum, on avance d'une case

                // Parcourir le chemin et trouver jusqu'où on peut aller avec la vitesse max
                for (int i = 1; i < chemin.size(); i++) {
                    int[] currentPos = chemin.get(i);
                    int[] startPos = chemin.get(0);

                    // Calculer la distance Manhattan depuis le départ
                    int distance = Math.abs(currentPos[0] - startPos[0]) + Math.abs(currentPos[1] - startPos[1]);

                    if (distance <= vitesseMax) {
                        distanceMax = distance;
                        indexOptimal = i;
                    } else {
                        break; // On a dépassé la vitesse max
                    }
                }

                // Si on n'a pas utilisé toute la vitesse et qu'il reste du chemin,
                // essayer d'aller plus loin en utilisant EXACTEMENT la vitesse max
                if (distanceMax < vitesseMax && indexOptimal < chemin.size() - 1) {
                    // Chercher une position qui utilise exactement la vitesse max
                    for (int i = indexOptimal + 1; i < chemin.size(); i++) {
                        int[] pos = chemin.get(i);
                        int[] startPos = chemin.get(0);
                        int distance = Math.abs(pos[0] - startPos[0]) + Math.abs(pos[1] - startPos[1]);

                        if (distance == vitesseMax) {
                            return pos;
                        } else if (distance > vitesseMax) {
                            break;
                        }
                    }
                }

                return chemin.get(indexOptimal);
            }

            return null;
        }

        // NOUVELLE MÉTHODE : Dijkstra avec visualisation du processus de calcul
        private ArrayList<int[]> calculerCheminAvecVisualisation(Animal animal, Elements cible) throws InterruptedException {
            int departX = animal.getX();
            int departY = animal.getY();
            int arriveeX = cible.getX();
            int arriveeY = cible.getY();

            int[][] distances = new int[grille.getNbColonnes()][grille.getNbLignes()];
            boolean[][] dejaVisite = new boolean[grille.getNbColonnes()][grille.getNbLignes()];
            int[][][] precedent = new int[grille.getNbColonnes()][grille.getNbLignes()][2];
            int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

            // Initialisation
            for (int x = 0; x < grille.getNbColonnes(); x++) {
                for (int y = 0; y < grille.getNbLignes(); y++) {
                    distances[x][y] = Integer.MAX_VALUE;
                    precedent[x][y][0] = -1;
                    precedent[x][y][1] = -1;
                }
            }
            distances[departX][departY] = 0;

            // Algorithme de Dijkstra avec visualisation
            while (true) {
                int distMin = Integer.MAX_VALUE;
                int caseX = -1, caseY = -1;

                // Chercher la case non visitée avec la plus petite distance
                for (int x = 0; x < grille.getNbColonnes(); x++) {
                    for (int y = 0; y < grille.getNbLignes(); y++) {
                        if (!dejaVisite[x][y] && distances[x][y] < distMin) {
                            distMin = distances[x][y];
                            caseX = x;
                            caseY = y;
                        }
                    }
                }

                if (caseX == -1 || (caseX == arriveeX && caseY == arriveeY)) break;

                // VISUALISATION : Montrer la case en cours d'exploration
                final int currentX = caseX;
                final int currentY = caseY;
                Platform.runLater(() -> {
                    if (currentX >= 0 && currentX < grille.getNbColonnes() &&
                            currentY >= 0 && currentY < grille.getNbLignes()) {
                        Rectangle explorationHighlight = new Rectangle(50, 50);

                        if (animal instanceof Loup) {
                            explorationHighlight.setFill(Color.ORANGE.deriveColor(0, 1, 1, 0.4)); // Orange pour réflexion loup
                        } else {
                            explorationHighlight.setFill(Color.CYAN.deriveColor(0, 1, 1, 0.4)); // Cyan pour réflexion mouton
                        }

                        cellules[currentX][currentY].getChildren().add(explorationHighlight);
                    }
                });

                // Petite pause pour voir la réflexion
                Thread.sleep(100);

                dejaVisite[caseX][caseY] = true;

                // Explorer les voisins
                for (int[] direction : directions) {
                    int voisinX = caseX + direction[0];
                    int voisinY = caseY + direction[1];

                    if (voisinX < 0 || voisinY < 0 || voisinX >= grille.getNbColonnes() || voisinY >= grille.getNbLignes())
                        continue;

                    Elements voisin = grille.getElement(voisinX, voisinY);
                    if (voisin == null || !voisin.isAccessible()) continue;

                    if (animal instanceof Loup && voisin instanceof Sortie) continue;

                    int cout = getCoutDeplacement(voisin, animal);
                    int nouvelleDistance = distances[caseX][caseY] + cout;

                    if (nouvelleDistance < distances[voisinX][voisinY]) {
                        distances[voisinX][voisinY] = nouvelleDistance;
                        precedent[voisinX][voisinY][0] = caseX;
                        precedent[voisinX][voisinY][1] = caseY;
                    }
                }
            }

            // Reconstruction du chemin
            ArrayList<int[]> chemin = new ArrayList<>();
            if (distances[arriveeX][arriveeY] == Integer.MAX_VALUE) return chemin;

            int x = arriveeX, y = arriveeY;
            while (x != departX || y != departY) {
                chemin.add(0, new int[]{x, y});
                int precX = precedent[x][y][0];
                int precY = precedent[x][y][1];
                x = precX;
                y = precY;
            }
            chemin.add(0, new int[]{departX, departY});

            return chemin;
        }

        // NOUVELLE MÉTHODE : Calculer coût de déplacement
        private int getCoutDeplacement(Elements e, Animal a) {
            if (e instanceof Herbe) return 1;
            if (e instanceof Sortie) return 1;
            if (e instanceof Rocher) return Integer.MAX_VALUE;
            return 1;
        }

        // NOUVELLE MÉTHODE : Obtenir position aléatoire
        private int[] obtenirPositionAleatoire(Animal animal) {
            int[][] deplacementsPossibles = grille.lesDeplacements(animal);
            int vitesseMax = animal.getVitesse();

            // Filtrer pour ne garder que les déplacements à vitesse maximale
            ArrayList<int[]> deplacementsVitesseMax = new ArrayList<>();

            for (int[] deplacement : deplacementsPossibles) {
                int distance = Math.abs(deplacement[0] - animal.getX()) + Math.abs(deplacement[1] - animal.getY());
                if (distance == vitesseMax) {
                    deplacementsVitesseMax.add(deplacement);
                }
            }

            // Si aucun déplacement à vitesse max n'est possible, prendre le plus loin possible
            if (deplacementsVitesseMax.isEmpty()) {
                int distanceMaxTrouvee = 0;
                for (int[] deplacement : deplacementsPossibles) {
                    int distance = Math.abs(deplacement[0] - animal.getX()) + Math.abs(deplacement[1] - animal.getY());
                    if (distance > distanceMaxTrouvee) {
                        distanceMaxTrouvee = distance;
                        deplacementsVitesseMax.clear();
                        deplacementsVitesseMax.add(deplacement);
                    } else if (distance == distanceMaxTrouvee) {
                        deplacementsVitesseMax.add(deplacement);
                    }
                }
            }

            if (!deplacementsVitesseMax.isEmpty()) {
                int indexAleatoire = random.nextInt(deplacementsVitesseMax.size());
                return deplacementsVitesseMax.get(indexAleatoire);
            }

            return null;
        }

        // NOUVELLE MÉTHODE : Mettre à jour l'interface pour la simulation automatique
        // NOUVELLE MÉTHODE : Mettre à jour l'interface pour la simulation automatique
        private void mettreAJourInterfaceAutomatique(Animal animal, int oldX, int oldY, int newX, int newY) {
            // 1. EFFACER L'ANCIENNE POSITION ET FAIRE REPOUSSER UN VÉGÉTAL
            StackPane oldCell = cellules[oldX][oldY];
            oldCell.getChildren().clear();

            // Créer un nouveau végétal aléatoire pour remplacer l'animal
            Vegetaux nouveauVegetal = genererVegetalAleatoire(oldX, oldY);
            grille.remplacer(oldX, oldY, nouveauVegetal);

            // Afficher le nouveau végétal
            ImageView vegetalView = creerImageViewPourElement(nouveauVegetal);
            if (vegetalView != null) {
                oldCell.getChildren().add(vegetalView);
            }

            // 2. GÉRER L'ALIMENTATION DU MOUTON (si applicable)
            if (animal instanceof Mouton) {
                Mouton mouton = (Mouton) animal;
                Elements elementDestination = grille.getElement(newX, newY);

                // Si le mouton arrive sur un végétal, il le mange
                if (elementDestination instanceof Vegetaux) {
                    mouton.manger((Vegetaux) elementDestination);

                    // Mettre à jour les compteurs
                    if (elementDestination instanceof Herbe) {
                        herbeMangée++;
                        cptHerbe.setText(String.valueOf(herbeMangée));
                    } else if (elementDestination instanceof Marguerite) {
                        margueritéMangée++;
                        cptMarguerite.setText(String.valueOf(margueritéMangée));
                    } else if (elementDestination instanceof Cactus) {
                        cactusMangé++;
                        cptCactus.setText(String.valueOf(cactusMangé));
                    }

                    vitesseLabel.setText("Vitesse mouton: " + mouton.getVitesse());
                }
            }

            // 3. METTRE L'ANIMAL DANS LA GRILLE À SA NOUVELLE POSITION
            grille.remplacer(newX, newY, animal);

            // 4. METTRE À JOUR L'AFFICHAGE DE LA NOUVELLE POSITION
            StackPane newCell = cellules[newX][newY];
            newCell.getChildren().clear();

            // Afficher l'animal à sa nouvelle position
            ImageView animalView = creerImageViewPourElement(animal);
            if (animalView != null) {
                newCell.getChildren().add(animalView);
            }

            // 5. SI C'EST LA SORTIE, REMETTRE LE MARQUEUR DORÉ
            if (newX == coSortie[0] && newY == coSortie[1]) {
                Rectangle exitMarker = new Rectangle(50, 50);
                exitMarker.setFill(Color.GOLD.deriveColor(0, 1, 1, 0.3));
                newCell.getChildren().add(exitMarker);
            }

            // 6. METTRE À JOUR LES COORDONNÉES DE RÉFÉRENCE
            if (animal instanceof Loup) {
                coLoup[0] = newX;
                coLoup[1] = newY;
            } else if (animal instanceof Mouton) {
                coMouton[0] = newX;
                coMouton[1] = newY;
            }
        }

        // NOUVELLE MÉTHODE : Créer ImageView pour un élément
        private ImageView creerImageViewPourElement(Elements element) {
            String imagePath = null;

            if (element instanceof Loup) {
                imagePath = "/com/example/jeuduloup2/loup.png";
            } else if (element instanceof Mouton) {
                imagePath = "/com/example/jeuduloup2/mouton.png";
            } else if (element instanceof Herbe) {
                imagePath = "/com/example/jeuduloup2/herbe.png";
            } else if (element instanceof Cactus) {
                imagePath = "/com/example/jeuduloup2/cactus.png";
            } else if (element instanceof Marguerite) {
                imagePath = "/com/example/jeuduloup2/marguerite.png";
            } else if (element instanceof Rocher) {
                imagePath = "/com/example/jeuduloup2/rocher.png";
            }

            if (imagePath != null) {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                return imageView;
            }

            return null;
        }

        // NOUVELLE MÉTHODE : Générer un végétal aléatoire
        private Vegetaux genererVegetalAleatoire(int x, int y) {
            int type = random.nextInt(3);
            switch (type) {
                case 0: return new Herbe(x, y);
                case 1: return new Cactus(x, y);
                case 2: return new Marguerite(x, y);
                default: return new Herbe(x, y);
            }
        }

        // NOUVELLE MÉTHODE : Afficher le résultat final
        private void afficherResultatFinal(String vainqueur) {
            simulationEnCours = false;

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Simulation terminée");
            alert.setHeaderText("Résultat de la simulation automatique");

            String contenu = "Vainqueur: " + vainqueur + "\n" +
                    "Nombre de tours: " + tour + "\n" +
                    "Végétaux mangés par le mouton:\n" +
                    "- Herbe: " + herbeMangée + "\n" +
                    "- Cactus: " + cactusMangé + "\n" +
                    "- Marguerite: " + margueritéMangée;

            alert.setContentText(contenu);
            alert.setOnCloseRequest(e -> {
                new MenuView().start(new Stage());
                currentStage.close();
            });
            alert.showAndWait();
        }

        // ===== MÉTHODES EXISTANTES (inchangées) =====

        private void initializeMoutons(Grille grille) {
            for (int i = 0; i < grille.getNbLignes(); i++) {
                for (int j = 0; j < grille.getNbColonnes(); j++) {
                    Elements element = grille.getElement(j, i);
                    if (element instanceof Mouton) {
                        Mouton mouton = (Mouton) element;
                        int key = j * 1000 + i;
                        moutons.put(Integer.valueOf(key), mouton);
                    }
                }
            }
        }

        private void updateMoutonPosition(int oldX, int oldY, int newX, int newY, Mouton mouton) {
            int oldKey = oldX * 1000 + oldY;
            int newKey = newX * 1000 + newY;
            moutons.remove(Optional.of(oldKey));
            moutons.put(Integer.valueOf(newKey), mouton);
        }

        private void handleCellClick(int x, int y) {
            if (gagne || perdu || simulationEnCours) {
                return;
            }

            Elements element = grille.getElement(x, y);

            if (!animalSelected) {
                if ((animal && element instanceof Loup) || (!animal && element instanceof Mouton)) {
                    animalSelectedX = x;
                    animalSelectedY = y;
                    animalSelected = true;

                    messageLabel.setText("Sélectionnez \n une destination");
                    messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 35));

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

                    if (deplacementReussi) {
                        Elements destination = grille.getElement(x, y);

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

        private void afficherDeplacementsPossibles(Animal animal) {
            int[][] deplacements = grille.lesDeplacements(animal);
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

        private void effacerDeplacementsPossibles() {
            for (int i = 0; i < grille.getNbColonnes(); i++) {
                for (int j = 0; j < grille.getNbLignes(); j++) {
                    if (cellules[i][j].getChildren().size() > 1) {
                        cellules[i][j].getChildren().remove(1, cellules[i][j].getChildren().size());
                    }
                }
            }
        }

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

            int rand = (int)(Math.random() * 3);
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

        public void setCoMouton(int x, int y) {
            coMouton[0] = x;
            coMouton[1] = y;
        }

        public void setCoLoup(int x, int y) {
            coLoup[0] = x;
            coLoup[1] = y;
        }

        public void setCoSortie(int x, int y) {
            coSortie[0] = x;
            coSortie[1] = y;
        }

        public static void main(String[] args) {
            launch(args);
        }
    }