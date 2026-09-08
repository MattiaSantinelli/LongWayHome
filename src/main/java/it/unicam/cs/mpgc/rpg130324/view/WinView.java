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
    private final String namePlayer;
    private final long timeElapsedSeconds;
    private final int defeatedEnemies;

    // Bottoni dichiarati come campi della classe
    private Button playAgainBtn;
    private Button endBtn;

    public WinView(Stage stage, String namePlayer, long timeElapsedSeconds, int defeatedEnemies) {
        this.stage = stage;
        this.namePlayer = namePlayer;
        this.timeElapsedSeconds = timeElapsedSeconds;
        this.defeatedEnemies = defeatedEnemies;
        initializeInterface();
    }

    /**
     * Inizializza la struttura del layout JavaFx (sfondo, titolo, banner, etichette)
     */
    private void initializeInterface() {
        stage.setTitle("LONG WAY HOME - Vittoria!");

        // Contenitore di sfondo
        StackPane rootPane = new StackPane();
        rootPane.setAlignment(Pos.CENTER_LEFT);

        // Sfondo di gioco
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

        // Banner principale
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
        Label subtitleLabel = new Label("Congratulazioni " + namePlayer + "!");
        subtitleLabel.setFont(Font.font("Georgia", 14));
        subtitleLabel.setTextFill(Color.web("#E0E0E0"));

        // Tabella statistiche
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(40);
        statsGrid.setVgap(15);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setStyle("-fx-padding: 15px 0px;");

        // Formattazione tempo MM:SS
        long minutes = timeElapsedSeconds / 60;
        long seconds = timeElapsedSeconds % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);

        // Tempo impiegato
        Label lblTimeTitle = new Label("Tempo impiegato");
        lblTimeTitle.setFont(Font.font("Georgia", 16));
        lblTimeTitle.setTextFill(Color.web("#CCCCCC"));

        Label lblTimeVal = new Label(formattedTime);
        lblTimeVal.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblTimeVal.setTextFill(Color.WHITE);

        // Nemici sconfitti
        Label lblEnemyTitle = new Label("Nemici sconfitti");
        lblEnemyTitle.setFont(Font.font("Georgia", 16));
        lblEnemyTitle.setTextFill(Color.web("#CCCCCC"));

        Label lblEnemyVal = new Label(String.valueOf(defeatedEnemies));
        lblEnemyVal.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblEnemyVal.setTextFill(Color.WHITE);

        // Aggiunta alla griglia
        statsGrid.add(lblTimeTitle, 0, 0);
        statsGrid.add(lblTimeVal, 1, 0);
        statsGrid.add(lblEnemyTitle, 0, 1);
        statsGrid.add(lblEnemyVal, 1, 1);

        // Bottoni
        playAgainBtn = new Button("GIOCA ANCORA");
        playAgainBtn.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        playAgainBtn.setPrefWidth(220);

        // Stili CSS base (normale e hover)
        String normalGreenStyle = "-fx-background-color: #1b5e20; -fx-text-fill: white; -fx-border-color: #4caf50; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand;";
        String hoverGreenStyle = "-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-border-color: #81c784; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(76, 175, 80, 0.6), 10, 0, 0, 0);";

        playAgainBtn.setStyle(normalGreenStyle);
        playAgainBtn.setOnMouseEntered(e -> playAgainBtn.setStyle(hoverGreenStyle));
        playAgainBtn.setOnMouseExited(e -> playAgainBtn.setStyle(normalGreenStyle));

        endBtn = new Button("FINE");
        endBtn.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        endBtn.setPrefWidth(220);

        // Stili CSS base (normale e hover)
        String normalRedStyle = "-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-border-color: #f44336; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand;";
        String hoverRedStyle = "-fx-background-color: #c62828; -fx-text-fill: white; -fx-border-color: #ef5350; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(244, 67, 54, 0.6), 10, 0, 0, 0);";

        endBtn.setStyle(normalRedStyle);
        endBtn.setOnMouseEntered(e -> endBtn.setStyle(hoverRedStyle));
        endBtn.setOnMouseExited(e -> endBtn.setStyle(normalRedStyle));

        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playAgainBtn, endBtn);

        // Inserimento elementi nel banner
        bannerBox.getChildren().addAll(titleLabel, subtitleLabel, statsGrid, buttonBox);

        // Posizionamento del banner nell'interfaccia con del margine dal bordo sinistro
        StackPane.setMargin(bannerBox, new javafx.geometry.Insets(0, 0, 0, 60));
        rootPane.getChildren().add(bannerBox);

        Scene scene = new Scene(rootPane, 900, 650);
        stage.setScene(scene);
    }

    // METODI PER IL CONTROLLER
    /***
     * Permette al Controller di definire la logica di riavvio del gioco.
     */
    public void setOnPlayAgainListener(Runnable action) {
        playAgainBtn.setOnAction(e -> action.run());
    }

    /**
     * Consente al Controller di definire l'azione di chiusura dell'applicazione.
     */
    public void setOnEndListener(Runnable action) {
        endBtn.setOnAction(e -> action.run());
    }

    public void show() {
        stage.show();
    }
}
