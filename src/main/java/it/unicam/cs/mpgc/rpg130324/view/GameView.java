package it.unicam.cs.mpgc.rpg130324.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameView {

    private final Stage stage;
    private final int RIGHE = 10;
    private final int COLONNE = 10;

    // Matrice grafica delle celle
    private final StackPane[][] grigliaCelle = new StackPane[RIGHE][COLONNE];

    // Posizione iniziale dell'eroe (riga 0, colonna 0)
    private int eroeRiga = 0;
    private int eroeColonna = 0;

    // Rappresentazione grafica dell'eroe (un cerchio dorato)
    private Circle pedinaEroe;

    public GameView(Stage stage) {
        this.stage = stage;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Mappa di Gioco");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #120300; -fx-padding: 20px;");

        // Info Giocatore
        Label infoLabel = new Label("Usa le Frecce Direzionali o WASD per muoverti");
        infoLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        infoLabel.setTextFill(Color.web("#FFB74D"));

        // Griglia della Scacchiera
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(3); // Spazio orizzontale tra le caselle
        gridPane.setVgap(3); // Spazio verticale tra le caselle
        gridPane.setStyle("-fx-background-color: #2B0B00; -fx-padding: 10px; -fx-border-color: #E65100; -fx-border-width: 2px;");

        // Generazione delle caselle
        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                StackPane cella = new StackPane();
                cella.setPrefSize(50, 50);

                // Colore alternato per l'effetto scacchiera
                if ((r + c) % 2 == 0) {
                    cella.setStyle("-fx-background-color: #1E1E1E;");
                } else {
                    cella.setStyle("-fx-background-color: #2A2A2A;");
                }

                grigliaCelle[r][c] = cella;
                gridPane.add(cella, c, r); // In GridPane: Colonna (X), Riga (Y)
            }
        }

        // Creazione dell'eroe (Cerchio Giallo con Glow Arancione)
        pedinaEroe = new Circle(18, Color.web("#FFC107"));
        pedinaEroe.setStyle("-fx-effect: dropshadow(three-pass-box, #FF5722, 10, 0.5, 0, 0);");

        // Posiziona l'eroe sulla casella iniziale (0,0)
        aggiornaPosizioneEroe();

        root.getChildren().addAll(infoLabel, gridPane);

        Scene scene = new Scene(root, 800, 650);

        // Gestione movimento da Tastiera
        scene.setOnKeyPressed(this::gestisciPressioneTasto);

        stage.setScene(scene);
        stage.setResizable(false);
    }

    /**
     * Gestisce i comandi di movimento dell'eroe mantenendolo dentro i confini della griglia.
     */
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
            default -> { return; } // Ignora altri tasti
        }

        aggiornaPosizioneEroe();
    }

    /**
     * Sposta la pedina grafica dell'eroe nella nuova casella corrente.
     */
    private void aggiornaPosizioneEroe() {
        // Rimuove la pedina da qualsiasi casella si trovasse prima
        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                grigliaCelle[r][c].getChildren().remove(pedinaEroe);
            }
        }
        // Inserisce la pedina nella nuova casella
        grigliaCelle[eroeRiga][eroeColonna].getChildren().add(pedinaEroe);
    }

    public void mostra() {
        stage.show();
    }
}