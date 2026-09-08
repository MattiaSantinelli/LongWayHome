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
 * Schermata mostrata quando l'Eroe viene sconfitto in combattimento.
 */
public class EndView {

    private final Stage stage;
    private final String namePlayer;
    private final long timeElapsedSeconds;
    private final int defeatedEnemies;

    // Bottoni dichiarati come campi della classe
    private Button playAgainBtn;
    private Button endBtn;

    public EndView(Stage stage, String namePlayer, long timeElapsedSeconds, int defeatedEnemies) {
        this.stage = stage;
        this.namePlayer = namePlayer;
        this.timeElapsedSeconds = timeElapsedSeconds;
        this.defeatedEnemies = defeatedEnemies;
        initializeInterface();
    }

    /**
     *  Inizializza la struttura del layout JavaFx (sfondo, etichette, bottoni)
     */
    private void initializeInterface() {
        stage.setTitle("LONG WAY HOME - Game Over");

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);

        // Sfondo di gioco
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
        root.setStyle(root.getStyle() + " -fx-padding: 30px;");

        // Titolo GAME OVER
        Label titleLabel = new Label("GAME OVER");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.web("#D32F2F")); // Rosso scuro
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        // Nome giocatore
        Label playerLabel = new Label("Sei stato sconfitto, " + namePlayer + "!");
        playerLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        playerLabel.setTextFill(Color.web("#FFB74D"));

        // Riquadro con le statistiche
        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setMaxWidth(400);
        statsBox.setStyle("-fx-background-color: rgba(20, 5, 5, 0.85); -fx-padding: 20px; -fx-border-color: #B71C1C; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        long minutes = timeElapsedSeconds / 60;
        long seconds = timeElapsedSeconds % 60;
        String tempoFormattato = String.format("%02d:%02d", minutes, seconds);

        Label timeLabel = new Label("Tempo di gioco: " + tempoFormattato);
        timeLabel.setFont(Font.font("Georgia", 18));
        timeLabel.setTextFill(Color.WHITE);

        Label enemyLabel = new Label("Nemici sconfitti: " + defeatedEnemies);
        enemyLabel.setFont(Font.font("Georgia", 18));
        enemyLabel.setTextFill(Color.WHITE);

        statsBox.getChildren().addAll(timeLabel, enemyLabel);

        // Bottoni
        playAgainBtn = new Button("GIOCA ANCORA");
        playAgainBtn.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        playAgainBtn.setPrefWidth(190);

        // Stili CSS base (normale e hover)
        String normalGreenStyle = "-fx-background-color: #1b5e20; -fx-text-fill: white; -fx-border-color: #4caf50; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand;";
        String hoverGreenStyle = "-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-border-color: #81c784; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(76, 175, 80, 0.6), 10, 0, 0, 0);";

        playAgainBtn.setStyle(normalGreenStyle);
        playAgainBtn.setOnMouseEntered(e -> playAgainBtn.setStyle(hoverGreenStyle));
        playAgainBtn.setOnMouseExited(e -> playAgainBtn.setStyle(normalGreenStyle));

        endBtn = new Button("FINE");
        endBtn.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        endBtn.setPrefWidth(190);

        // Stili CSS base (normale e hover)
        String normaleRedStyle = "-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-border-color: #f44336; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand;";
        String hoverRedStyle = "-fx-background-color: #c62828; -fx-text-fill: white; -fx-border-color: #ef5350; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 10px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(244, 67, 54, 0.6), 10, 0, 0, 0);";

        endBtn.setStyle(normaleRedStyle);
        endBtn.setOnMouseEntered(e -> endBtn.setStyle(hoverRedStyle));
        endBtn.setOnMouseExited(e -> endBtn.setStyle(normaleRedStyle));

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playAgainBtn, endBtn);

        root.getChildren().addAll(titleLabel, playerLabel, statsBox, buttonBox);

        Scene scene = new Scene(root, 800, 650);
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
