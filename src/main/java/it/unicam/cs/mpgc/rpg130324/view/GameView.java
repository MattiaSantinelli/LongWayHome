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
    private final String nomeGiocatore;  //campo per memorizzare il nome

    // Matrice grafica delle celle
    private final StackPane[][] grigliaCelle = new StackPane[RIGHE][COLONNE];

    // Posizione iniziale dell'eroe (riga 0, colonna 0)
    private int eroeRiga = 0;
    private int eroeColonna = 0;

    // Variabili per rappresentazione grafica pupetti
    private ImageView pedinaEroe;
    private Image imgGoblin;
    private Image imgGigante;
    private Image imgStrega;
    private Image imgMago;
    private Image imgDrago;
    private Image imgCasa;

    public GameView(Stage stage, String nomeGiocatore) {
        this.stage = stage;
        this.nomeGiocatore = nomeGiocatore;
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
            System.out.println("⚠️ Immagine 'imgEroe.png' non trovata in resources!");
        }

        // Posiziona l'eroe sulla casella iniziale (0,0)
        aggiornaPosizioneEroe();

        root.getChildren().addAll(infoLabel, gridPane);

        Scene scene = new Scene(root, 800, 650);

        // Gestione movimento da Tastiera
        scene.setOnKeyPressed(this::gestisciPressioneTasto);

        stage.setScene(scene);
        stage.setResizable(false);

        // --- CARICAMENTO SPRITE GOBLIN ---
        try {
            imgGoblin = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgGoblin.png")));
        } catch (Exception e) {
            System.out.println("⚠️ Immagine 'imgGoblin.png' non trovata in resources!");
        }

        // --- POSIZIONAMENTO GOBLIN (Riga, Colonna) ---
        posizionaGoblin(0, 5);
        posizionaGoblin(1, 2);
        posizionaGoblin(1, 8);
        posizionaGoblin(2, 0 );
        posizionaGoblin(2, 5);
        posizionaGoblin(3, 1);
        posizionaGoblin(3, 8);
        posizionaGoblin(4, 6);
        posizionaGoblin(5, 3);
        posizionaGoblin(7, 5);

        // --- CARICAMENTO SPRITE GIGANTE ---
        try {
            imgGigante = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgGigante.png")));
        } catch (Exception e) {
            System.out.println("⚠️ Immagine 'imgGigante.png' non trovata in resources!");
        }

        // --- POSIZIONAMENTO GIGANTE (Riga, Colonna) ---
        posizionaGigante(0, 3);
        posizionaGigante(1, 6);
        posizionaGigante(2, 2);
        posizionaGigante(2, 4);
        posizionaGigante(4, 0);
        posizionaGigante(6, 1);
        posizionaGigante(7, 0);
        posizionaGigante(8, 2);
        posizionaGigante(9, 6);

        // --- CARICAMENTO SPRITE STREGA ---
        try {
            imgStrega = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgStrega.png")));
        } catch (Exception e) {
            System.out.println("⚠️ Immagine 'imgStrega.png' non trovata in resources!");
        }

        // --- POSIZIONAMENTO STREGA (Riga, Colonna) ---
        posizionaStrega(2, 9);
        posizionaStrega(3, 3);
        posizionaStrega(5, 5);
        posizionaStrega(7, 1);
        posizionaStrega(7, 3);
        posizionaStrega(7, 7);
        posizionaStrega(8, 5);

        // --- CARICAMENTO SPRITE MAGO ---
        try {
            imgMago = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgMago.png")));
        } catch (Exception e) {
            System.out.println("⚠️ Immagine 'imgMago.png' non trovata in resources!");
        }

        // --- POSIZIONAMENTO MAGO (Riga, Colonna) ---
        posizionaMago( 5, 2);
        posizionaMago( 5, 8);
        posizionaMago( 6, 6);
        posizionaMago( 9, 0);
        posizionaMago( 9, 3);
        posizionaMago( 8, 8);

        // --- CARICAMENTO SPRITE DRAGO ---
        try {
            imgDrago = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgDrago.png")));
        } catch (Exception e) {
            System.out.println("⚠️ Immagine 'imgDrago.png' non trovata in resources!");
        }

        // --- POSIZIONAMENTO DRAGO (Riga, Colonna) ---
        posizionaDrago( 8, 9);
        posizionaDrago( 9, 8);

        // --- CARICAMENTO SPRITE CASA ---
        try {
            imgCasa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgCasa.png")));
        } catch (Exception e) {
            System.out.println("⚠️ Immagine 'imgCasa.png' non trovata in resources!");
        }

        // --- POSIZIONAMENTO CASA (Riga, Colonna) ---
        posizionaCasa( 9, 9);

        //Comandi test classe SchermataCombattimentoGoblin
        SchermataCombattimentoDrago scontro = new SchermataCombattimentoDrago(stage, pedinaEroe.getImage(), imgDrago, nomeGiocatore);
        scontro.mostra();
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

    private void posizionaGoblin(int riga, int colonna) {
        if (imgGoblin == null) return;

        ImageView pedinaGoblin = new ImageView(imgGoblin);
        pedinaGoblin.setFitWidth(40);
        pedinaGoblin.setFitHeight(40);
        pedinaGoblin.setPreserveRatio(true);

        // Bagliore verde/giallo velenoso adatto ai goblin
        pedinaGoblin.setStyle("-fx-effect: dropshadow(three-pass-box, #8BC34A, 10, 0.5, 0, 0);");

        grigliaCelle[riga][colonna].getChildren().add(pedinaGoblin);
    }

    private void posizionaGigante(int riga, int colonna) {
        if (imgGigante == null) return;

        ImageView pedinaGigante = new ImageView(imgGigante);
        pedinaGigante.setFitWidth(40);
        pedinaGigante.setFitHeight(40);
        pedinaGigante.setPreserveRatio(true);

        // Bagliore giallo lucente adatto ai giganti
        pedinaGigante.setStyle("-fx-effect: dropshadow(three-pass-box, #FFC107, 16, 0.7, 0, 0);");

        grigliaCelle[riga][colonna].getChildren().add(pedinaGigante);
    }

    private void posizionaStrega(int riga, int colonna) {
        if (imgStrega == null) return;

        ImageView pedinaStrega = new ImageView(imgStrega);
        pedinaStrega.setFitWidth(40);
        pedinaStrega.setFitHeight(40);
        pedinaStrega.setPreserveRatio(true);

        // Bagliore viola chiaro adatto alle streghe
        pedinaStrega.setStyle("-fx-effect: dropshadow(three-pass-box, #880E4F, 16, 0.7, 0, 0);");

        grigliaCelle[riga][colonna].getChildren().add(pedinaStrega);
    }

    private void posizionaMago(int riga, int colonna) {
        if (imgMago == null) return;

        ImageView pedinaMago = new ImageView(imgMago);
        pedinaMago.setFitWidth(40);
        pedinaMago.setFitHeight(40);
        pedinaMago.setPreserveRatio(true);

        // Bagliore blu zaffiro adatto ai maghi
        pedinaMago.setStyle("-fx-effect: dropshadow(three-pass-box, #1E88E5, 16, 0.7, 0, 0);");

        grigliaCelle[riga][colonna].getChildren().add(pedinaMago);
    }

    private void posizionaDrago(int riga, int colonna) {
        if (imgDrago == null) return;

        ImageView pedinaDrago = new ImageView(imgDrago);
        pedinaDrago.setFitWidth(40);
        pedinaDrago.setFitHeight(40);
        pedinaDrago.setPreserveRatio(true);

        // Bagliore rosso velenoso adatto ai draghi
        pedinaDrago.setStyle("-fx-effect: dropshadow(three-pass-box, #B71C1C, 16, 0.7, 0, 0);");

        grigliaCelle[riga][colonna].getChildren().add(pedinaDrago);
    }

    private void posizionaCasa(int riga, int colonna) {
        if (imgCasa == null) return;

        ImageView pedinaCasa = new ImageView(imgCasa);
        pedinaCasa.setFitWidth(40);
        pedinaCasa.setFitHeight(40);
        pedinaCasa.setPreserveRatio(true);

        grigliaCelle[riga][colonna].getChildren().add(pedinaCasa);
    }

    public void mostra() {
        stage.show();
    }
}