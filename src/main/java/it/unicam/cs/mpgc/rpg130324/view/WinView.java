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

    private Button playAgainBtn;
    private Button endBtn;

    public WinView(Stage stage, String namePlayer, long timeElapsedSeconds, int defeatedEnemies) {
        this.stage = stage;
        this.namePlayer = namePlayer;
        this.timeElapsedSeconds = timeElapsedSeconds;
        this.defeatedEnemies = defeatedEnemies;
        initializeInterface();
    }

    private void initializeInterface() {
        stage.setTitle("LONG WAY HOME - Vittoria!");

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

        VBox bannerBox = new VBox(20);
        bannerBox.setAlignment(Pos.CENTER);
        bannerBox.setMaxWidth(320);
        bannerBox.setMaxHeight(250);
        bannerBox.getStyleClass().add("dark-panel");
        bannerBox.setStyle("-fx-padding: 30px;");

        Label titleLabel = new Label("VITTORIA!");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 38));
        titleLabel.setTextFill(Color.web("#FFD700"));
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 8, 0, 0, 0);");

        Label subtitleLabel = new Label("Congratulazioni " + namePlayer + "!");
        subtitleLabel.setFont(Font.font("Georgia", 14));
        subtitleLabel.setTextFill(Color.web("#E0E0E0"));

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(40);
        statsGrid.setVgap(15);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setStyle("-fx-padding: 15px 0px;");

        long minutes = timeElapsedSeconds / 60;
        long seconds = timeElapsedSeconds % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);

        Label lblTimeTitle = new Label("Tempo impiegato");
        lblTimeTitle.setFont(Font.font("Georgia", 16));
        lblTimeTitle.setTextFill(Color.web("#CCCCCC"));

        Label lblTimeVal = new Label(formattedTime);
        lblTimeVal.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblTimeVal.setTextFill(Color.WHITE);

        Label lblEnemyTitle = new Label("Nemici sconfitti");
        lblEnemyTitle.setFont(Font.font("Georgia", 16));
        lblEnemyTitle.setTextFill(Color.web("#CCCCCC"));

        Label lblEnemyVal = new Label(String.valueOf(defeatedEnemies));
        lblEnemyVal.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        lblEnemyVal.setTextFill(Color.WHITE);

        statsGrid.add(lblTimeTitle, 0, 0);
        statsGrid.add(lblTimeVal, 1, 0);
        statsGrid.add(lblEnemyTitle, 0, 1);
        statsGrid.add(lblEnemyVal, 1, 1);

        playAgainBtn = new Button("GIOCA ANCORA");
        playAgainBtn.getStyleClass().add("btn-success");
        playAgainBtn.setPrefWidth(220);

        endBtn = new Button("FINE");
        endBtn.getStyleClass().add("btn-danger");
        endBtn.setPrefWidth(220);

        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(playAgainBtn, endBtn);

        bannerBox.getChildren().addAll(titleLabel, subtitleLabel, statsGrid, buttonBox);

        StackPane.setMargin(bannerBox, new javafx.geometry.Insets(0, 0, 0, 60));
        rootPane.getChildren().add(bannerBox);

        Scene scene = new Scene(rootPane, 900, 650);
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