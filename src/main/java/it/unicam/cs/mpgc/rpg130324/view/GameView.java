package it.unicam.cs.mpgc.rpg130324.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Objects;

public class GameView {

    private final Stage stage;
    private final int RIGHE = 10;
    private final int COLONNE = 10;

    // Matrice grafica delle celle
    private final StackPane[][] grigliaCelle = new StackPane[RIGHE][COLONNE];

    // Posizione iniziale dell'eroe (riga 0, colonna 0)
    private int eroeRiga = 0;
    private int eroeColonna = 0;

    // Rappresentazione grafica dell'eroe (Sprite Image)
    private ImageView pedinaEroe;

    public GameView(Stage stage) {
        this.stage = stage;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Mappa di Gioco");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        // --- AGGIUNTA SFONDO CON FALLBACK ---
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/GameView_background.png")));
            root.setBackground(new Background(new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            )));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #120300;");
        }
        root.setStyle(root.getStyle() + " -fx-padding: 20px;");

        // Info Giocatore
        Label infoLabel = new Label("Usa le Frecce Direzionali o WASD per muoverti");
        infoLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        infoLabel.setTextFill(Color.web("#FFB74D"));

        // Griglia della Scacchiera (sfondo semi-trasparente per far passare la mappa)
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        gridPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        gridPane.setStyle("-fx-background-color: rgba(43, 11, 0, 0.6); -fx-padding: 10px; -fx-border-color: #E65100; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Generazione delle caselle con opacità regolata
        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                StackPane cella = new StackPane();
                cella.setPrefSize(50, 50);

                // Colori scuri trasparenti (rgba) per far intravedere l'immagine sotto
                if ((r + c) % 2 == 0) {
                    cella.setStyle("-fx-background-color: rgba(30, 30, 30, 0.45); -fx-border-color: rgba(230, 81, 0, 0.3);");
                } else {
                    cella.setStyle("-fx-background-color: rgba(10, 10, 10, 0.55); -fx-border-color: rgba(230, 81, 0, 0.3);");
                }

                grigliaCelle[r][c] = cella;
                gridPane.add(cella, c, r);
            }
        }

        // --- CREAZIONE DELL'EROE CON IMMAGINE ---
        try {
            Image heroImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
            pedinaEroe = new ImageView(heroImage);

            // Adatta l'immagine alla dimensione della casella (50x50)
            pedinaEroe.setFitWidth(40);
            pedinaEroe.setFitHeight(40);
            pedinaEroe.setPreserveRatio(true);

            // Effetto bagliore arancione sotto la sprite dell'eroe
            pedinaEroe.setStyle("-fx-effect: dropshadow(three-pass-box, #FF5722, 12, 0.6, 0, 0);");
        } catch (Exception e) {
            System.out.println("⚠️ Immagine 'hero.png' non trovata in resources!");
        }

        // Posiziona l'eroe sulla casella iniziale (0,0)
        aggiornaPosizioneEroe();

        root.getChildren().addAll(infoLabel, gridPane);

        Scene scene = new Scene(root, 800, 650);

        // Gestione movimento da Tastiera
        scene.setOnKeyPressed(this::gestisciPressioneTasto);

        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void gestisciPressioneTasto(KeyEvent event) {
        switch (event.getCode()) {
            case UP, W -> {
                if (eroeRiga > 0) eroeRiga--;
            }
            case DOWN, S -> {
                if (eroeRiga < RIGHE - 1) eroeRiga++;
            }
            case LEFT, A -> {
                if (eroeColonna > 0) eroeColonna--;
            }
            case RIGHT, D -> {
                if (eroeColonna < COLONNE - 1) eroeColonna++;
            }
            default -> { return; }
        }

        aggiornaPosizioneEroe();
    }

    private void aggiornaPosizioneEroe() {
        if (pedinaEroe == null) return;

        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                grigliaCelle[r][c].getChildren().remove(pedinaEroe);
            }
        }
        grigliaCelle[eroeRiga][eroeColonna].getChildren().add(pedinaEroe);
    }

    public void mostra() {
        stage.show();
    }
}