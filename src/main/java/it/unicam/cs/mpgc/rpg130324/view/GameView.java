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
import java.util.function.Consumer;

public class GameView {

    private final Stage stage;
    private final int RIGHE = 10;
    private final int COLONNE = 10;
    private final String nomeGiocatore; // Campo per memorizzare il nome del giocatore

    // Mappa da gioco
    private final StackPane[][] grigliaCelle = new StackPane[RIGHE][COLONNE];

    // Immagini degli elementi di gioco
    private Image imgEroe;
    private Image imgGoblin;
    private Image imgGigante;
    private Image imgStrega;
    private Image imgMago;
    private Image imgDrago;
    private Image imgCasa;

    // Callback per notificare il GameController quando l'utente preme un tasto
    private Consumer<String> onMovimentoListener;

    public GameView(Stage stage, String nomeGiocatore) {
        this.stage = stage;
        this.nomeGiocatore = nomeGiocatore;
        inizializzaInterfaccia();
        caricaImmagini();
    }

    /**
     * Inizializza la struttura del layout JavaFX (sfondo, etichette e griglia)
     */
    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Mappa di Gioco");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        // Sfondo con fallback
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

        // Etichette con istruzioni da gioco
        Label infoLabel = new Label("Usa le Frecce Direzionali o WASD per muoverti");
        infoLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        infoLabel.setTextFill(Color.web("#FFB74D"));

        // Griglia della scacchiera
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        gridPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        gridPane.setStyle("-fx-background-color: rgba(43, 11, 0, 0.6); -fx-padding: 10px; -fx-border-color: #E65100; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Creazione mappa da gioco
        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                StackPane cella = new StackPane();
                cella.setPrefSize(50, 50);

                if ((r + c) % 2 == 0) {
                    cella.setStyle("-fx-background-color: rgba(30, 30, 30, 0.45); -fx-border-color: rgba(230, 81, 0, 0.3);");
                } else {
                    cella.setStyle("-fx-background-color: rgba(10, 10, 10, 0.55); -fx-border-color: rgba(230, 81, 0, 0.3);");
                }

                grigliaCelle[r][c] = cella;
                gridPane.add(cella, c, r);
            }
        }

        root.getChildren().addAll(infoLabel, gridPane);

        Scene scene = new Scene(root, 800, 650);
        scene.setOnKeyPressed(this::gestisciPressioneTasto);

        stage.setScene(scene);
        stage.setResizable(false);
    }

    /**
     * Carica tutte le immagini dei personaggi dalla cartella resources.
     */
    private void caricaImmagini() {
        try { imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png"))); } catch (Exception ignored) {}
        try { imgGoblin = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgGoblin.png"))); } catch (Exception ignored) {}
        try { imgGigante = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgGigante.png"))); } catch (Exception ignored) {}
        try { imgStrega = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgStrega.png"))); } catch (Exception ignored) {}
        try { imgMago = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgMago.png"))); } catch (Exception ignored) {}
        try { imgDrago = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgDrago.png"))); } catch (Exception ignored) {}
        try { imgCasa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgCasa.png"))); } catch (Exception ignored) {}
    }

    /**
     * Intercetta la pressione dei tasti e invia la direzione al Controller.
     */
    private void gestisciPressioneTasto(KeyEvent event) {
        if (onMovimentoListener == null) return;

        switch (event.getCode()) {
            case UP, W -> onMovimentoListener.accept("SU");
            case DOWN, S -> onMovimentoListener.accept("GIU");
            case LEFT, A -> onMovimentoListener.accept("SINISTRA");
            case RIGHT, D -> onMovimentoListener.accept("DESTRA");
            default -> {}
        }
    }

    /**
     * Permette al Controller di registrare una funzione di callback per il movimento.
     */
    public void setOnMovimentoListener(Consumer<String> listener) {
        this.onMovimentoListener = listener;
    }

    /**
     * Pulisce e ridisegna la mappa intera partendo dalla matrice.
     */
    public void posizionaNemici(String[][] mappaGioco) {
        // Svuota i contenuti precedenti delle celle
        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                grigliaCelle[r][c].getChildren().clear();
            }
        }

        // Disegna gli elementi aggiornati
        for (int r = 0; r < RIGHE; r++) {
            for (int c = 0; c < COLONNE; c++) {
                String elemento = mappaGioco[r][c];
                if (elemento == null || elemento.isEmpty()) continue;

                switch (elemento) {
                    case "Eroe" -> posizionaSingoloElemento(imgEroe, "-fx-effect: dropshadow(three-pass-box, #FF5722, 12, 0.6, 0, 0);", r, c);
                    case "Goblin" -> posizionaSingoloElemento(imgGoblin, "-fx-effect: dropshadow(three-pass-box, #8BC34A, 10, 0.5, 0, 0);", r, c);
                    case "Gigante" -> posizionaSingoloElemento(imgGigante, "-fx-effect: dropshadow(three-pass-box, #FFC107, 16, 0.7, 0, 0);", r, c);
                    case "Strega" -> posizionaSingoloElemento(imgStrega, "-fx-effect: dropshadow(three-pass-box, #880E4F, 16, 0.7, 0, 0);", r, c);
                    case "Mago" -> posizionaSingoloElemento(imgMago, "-fx-effect: dropshadow(three-pass-box, #1E88E5, 16, 0.7, 0, 0);", r, c);
                    case "Drago" -> posizionaSingoloElemento(imgDrago, "-fx-effect: dropshadow(three-pass-box, #B71C1C, 16, 0.7, 0, 0);", r, c);
                    case "Casa" -> posizionaSingoloElemento(imgCasa, null, r, c);
                }
            }
        }
    }

    /**
     * Helper interno per creare un'ImageView con l'effetto di luce e posizionarla sulla cella.
     */
    private void posizionaSingoloElemento(Image img, String stile, int riga, int colonna) {
        if (img == null) return;
        ImageView iv = new ImageView(img);
        iv.setFitWidth(40);
        iv.setFitHeight(40);
        iv.setPreserveRatio(true);
        if (stile != null) iv.setStyle(stile);
        grigliaCelle[riga][colonna].getChildren().add(iv);
    }

    public void mostra() {
        stage.show();
    }
}