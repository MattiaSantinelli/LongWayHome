package it.unicam.cs.mpgc.rpg130324.view;

import it.unicam.cs.mpgc.rpg130324.model.persistence.SaveData;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Objects;

public class LeaderboardView {

    private final BorderPane mainLayout;
    private final Button backBtn;

    public LeaderboardView(List<SaveData> dataList, Runnable backAction) {
        this.mainLayout = new BorderPane();
        this.backBtn = new Button("INDIETRO");
        initializeInterface(dataList, backAction);
    }

    private void initializeInterface(List<SaveData> list, Runnable backAction) {
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/WelcomeView_background.png")));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            mainLayout.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            mainLayout.setStyle("-fx-background: linear-gradient(to bottom, #2B0B00, #120300);");
        }

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 30px;");

        Label titleLabel = new Label("🏆 CLASSIFICA GIOCATORI");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 42));
        titleLabel.setTextFill(Color.web("#FFC107"));
        titleLabel.setStyle("-fx-effect: dropshadow(three-pass-box, #D32F2F, 15, 0.5, 0, 0);");

        VBox contentList = new VBox(12);
        contentList.setAlignment(Pos.TOP_CENTER);
        contentList.setStyle("-fx-padding: 10px;");

        if (list == null || list.isEmpty()) {
            Label noDataLabel = new Label("Nessun salvataggio presente.");
            noDataLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 18));
            noDataLabel.setTextFill(Color.web("#FFB74D"));
            contentList.getChildren().add(noDataLabel);
        } else {
            int posizione = 1;
            for (SaveData dati : list) {
                String testo = String.format("%d. %s  —  Nemici sconfitti: %d  |  Tempo: %ds",
                        posizione++, dati.getNamePlayer(), dati.getDefeatedEnemies(), dati.getGameTime());

                Label rigaLabel = new Label(testo);
                rigaLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
                rigaLabel.setTextFill(Color.web("#FFE082"));
                rigaLabel.setStyle(
                        "-fx-background-color: rgba(20, 10, 5, 0.75); " +
                                "-fx-border-color: #E65100; " +
                                "-fx-border-width: 1px; " +
                                "-fx-border-radius: 6px; " +
                                "-fx-background-radius: 6px; " +
                                "-fx-padding: 12px 20px;"
                );
                rigaLabel.setMaxWidth(620);
                contentList.getChildren().add(rigaLabel);
            }
        }

        ScrollPane scrollPane = new ScrollPane(contentList);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxWidth(650);
        scrollPane.setMaxHeight(340);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");

        String cssScrollbar =
                ".scroll-bar:vertical { -fx-background-color: rgba(20, 10, 5, 0.5); -fx-pref-width: 10px; -fx-background-radius: 5px; } " +
                        ".scroll-bar:vertical .thumb { -fx-background-color: #E65100; -fx-background-radius: 5px; } " +
                        ".scroll-bar:vertical .thumb:hover { -fx-background-color: #FFC107; } " +
                        ".scroll-bar:vertical .track, .scroll-bar:vertical .track-background { -fx-background-color: transparent; } " +
                        ".scroll-bar:vertical .increment-button, .scroll-bar:vertical .decrement-button { -fx-padding: 0; -fx-background-color: transparent; } " +
                        ".scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow { -fx-shape: null; -fx-padding: 0; }";

        mainLayout.getStylesheets().add("data:text/css," + cssScrollbar.replaceAll("\n", ""));

        backBtn.setPrefSize(160, 45);
        backBtn.getStyleClass().add("primary-button");
        backBtn.setOnAction(e -> backAction.run());

        root.getChildren().addAll(titleLabel, scrollPane, backBtn);
        mainLayout.setCenter(root);
    }

    public Scene getScene() {
        Scene scene = new Scene(mainLayout, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        return scene;
    }
}