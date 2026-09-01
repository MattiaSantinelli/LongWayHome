package it.unicam.cs.mpgc.rpg130324.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Schermata mostrata quando l'Eroe raggiunge con successo la Casa.
 */
public class WinView {

    private final Stage stage;
    private final String nomeGiocatore;
    private final long tempoImpiegatoSecondi;
    private final int nemiciSconfitti;

    // Bottoni dichiarati come campi della classe
    private Button btnGiocaAncora;
    private Button btnFine;

    public WinView(Stage stage, String nomeGiocatore, long tempoImpiegatoSecondi, int nemiciSconfitti) {
        this.stage = stage;
        this.nomeGiocatore = nomeGiocatore;
        this.tempoImpiegatoSecondi = tempoImpiegatoSecondi;
        this.nemiciSconfitti = nemiciSconfitti;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Vittoria!");

        // Contenitore di sfondo (posizionato a sinistra come nel banner dell'immagine)
        StackPane rootPane = new StackPane();
        rootPane.setAlignment(Pos.CENTER_LEFT);

        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/WinView_background.png")));
            rootPane.setBackground(new Background(new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            )));
        } catch (Exception e) {
            rootPane.setStyle("-fx-background-color: #120300;");
        }

        // BANNER PRINCIPALE (Riquadro Scuro con bordo dorato)
        VBox bannerBox = new VBox(20);
        bannerBox.setAlignment(Pos.CENTER);
        bannerBox.setMaxWidth(320);
        bannerBox.setMaxHeight(250);
        bannerBox.setStyle(
                "-fx-background-color: rgba(20, 18, 15, 0.92); " +
                        "-fx-border-color: #B8860B; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-padding: 30px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 15, 0, 0, 0);"
        );

        // Titolo VITTORIA!
        Label titleLabel = new Label("VITTORIA!");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 38));
        titleLabel.setTextFill(Color.web("#FFD700")); // Oro
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 8, 0, 0, 0);");

        // Messaggio di congratulazioni
        Label subtitleLabel = new Label("Congratulazioni " + nomeGiocatore + "!");
        subtitleLabel.setFont(Font.font("Georgia", 14));
        subtitleLabel.setTextFill(Color.web("#E0E0E0"));

        // TABELLA STATISTICHE (GridPane)
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(40);
        statsGrid.setVgap(15);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setStyle("-fx-padding: 15px 0px;");

        // Formattazione tempo MM:SS
        long minuti = tempoImpiegatoSecondi / 60;
        long secondi = tempoImpiegatoSecondi % 60;
        String tempoFormattato = String.format("%02d:%02d", minuti, secondi);

        // Riga 1: Tempo impiegato
        Label lblTempoTitle = new Label("Tempo impiegato");
        lblTempoTitle.setFont(Font.font("Georgia", 16));
        lblTempoTitle.setTextFill(Color.web("#CCCCCC"));

        Label lblTempoVal = new Label(tempoFormattato);
        lblTempoVal.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblTempoVal.setTextFill(Color.WHITE);

        // Riga 2: Nemici sconfitti
        Label lblNemiciTitle = new Label("Nemici sconfitti");
        lblNemiciTitle.setFont(Font.font("Georgia", 16));
        lblNemiciTitle.setTextFill(Color.web("#CCCCCC"));

        Label lblNemiciVal = new Label(String.valueOf(nemiciSconfitti));
        lblNemiciVal.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblNemiciVal.setTextFill(Color.WHITE);

        // Aggiunta alla griglia (Colonna, Riga)
        statsGrid.add(lblTempoTitle, 0, 0);
        statsGrid.add(lblTempoVal, 1, 0);
        statsGrid.add(lblNemiciTitle, 0, 1);
        statsGrid.add(lblNemiciVal, 1, 1);

        // PULSANTI
        btnGiocaAncora = new Button("GIOCA ANCORA");
        btnGiocaAncora.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnGiocaAncora.setPrefWidth(220);

        // Stili CSS base (Stato normale e hover)
        String styleVerdeNormale = "-fx-background-color: #1b5e20; -fx-text-fill: white; -fx-border-color: #4caf50; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand;";
        String styleVerdeHover = "-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-border-color: #81c784; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(76, 175, 80, 0.6), 10, 0, 0, 0);";

        btnGiocaAncora.setStyle(styleVerdeNormale);
        btnGiocaAncora.setOnMouseEntered(e -> btnGiocaAncora.setStyle(styleVerdeHover));
        btnGiocaAncora.setOnMouseExited(e -> btnGiocaAncora.setStyle(styleVerdeNormale));

        btnFine = new Button("FINE");
        btnFine.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        btnFine.setPrefWidth(220);

        // Stili CSS base (Stato normale e hover)
        String styleRossoNormale = "-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-border-color: #f44336; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand;";
        String styleRossoHover = "-fx-background-color: #c62828; -fx-text-fill: white; -fx-border-color: #ef5350; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(244, 67, 54, 0.6), 10, 0, 0, 0);";

        btnFine.setStyle(styleRossoNormale);
        btnFine.setOnMouseEntered(e -> btnFine.setStyle(styleRossoHover));
        btnFine.setOnMouseExited(e -> btnFine.setStyle(styleRossoNormale));

        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnGiocaAncora, btnFine);

        // Inserimento elementi nel banner
        bannerBox.getChildren().addAll(titleLabel, subtitleLabel, statsGrid, buttonBox);

        // Posizionamento del banner nell'interfaccia con del margine dal bordo sinistro
        StackPane.setMargin(bannerBox, new javafx.geometry.Insets(0, 0, 0, 60));
        rootPane.getChildren().add(bannerBox);

        Scene scene = new Scene(rootPane, 900, 650);
        stage.setScene(scene);
    }

    /***
     * Permette al Controller di definire la logica di riavvio del gioco.
     */
    public void setOnGiocaAncoraListener(Runnable azione) {
        btnGiocaAncora.setOnAction(e -> azione.run());
    }

    /**
     * Consente al Controller di definire l'azione di chiusura dell'applicazione.
     */
    public void setOnFineListener(Runnable azione) {
        btnFine.setOnAction(e -> azione.run());
    }

    public void mostra() {
        stage.show();
    }
}
