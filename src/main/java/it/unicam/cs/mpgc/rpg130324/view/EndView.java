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

    private Button playAgainBtn;
    private Button endBtn;

    public EndView(Stage stage, String namePlayer, long timeElapsedSeconds, int defeatedEnemies) {
        this.stage = stage;
        this.namePlayer = namePlayer;
        this.timeElapsedSeconds = timeElapsedSeconds;
        this.defeatedEnemies = defeatedEnemies;
        initializeInterface();
    }

    private void initializeInterface() {
        stage.setTitle("LONG WAY HOME - Game Over");

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);

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

        Label titleLabel = new Label("GAME OVER");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.web("#D32F2F"));
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        Label playerLabel = new Label("Sei stato sconfitto, " + namePlayer + "!");
        playerLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        playerLabel.setTextFill(Color.web("#FFB74D"));

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

        playAgainBtn = new Button("GIOCA ANCORA");
        playAgainBtn.getStyleClass().add("btn-success");
        playAgainBtn.setPrefWidth(190);

        endBtn = new Button("FINE");
        endBtn.getStyleClass().add("btn-danger");
        endBtn.setPrefWidth(190);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playAgainBtn, endBtn);

        root.getChildren().addAll(titleLabel, playerLabel, statsBox, buttonBox);

        Scene scene = new Scene(root, 800, 650);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        stage.setScene(scene);
    }

    public void setOnPlayAgainListener(Runnable action) {
        playAgainBtn.setOnAction(e -> action.run());
    }

    public void setOnEndListener(Runnable action) {
        endBtn.setOnAction(e -> action.run());
    }

    public void show() {
        stage.show();
    }
}