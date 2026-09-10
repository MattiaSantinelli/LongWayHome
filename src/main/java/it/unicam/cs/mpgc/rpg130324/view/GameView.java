package it.unicam.cs.mpgc.rpg130324.view;

import it.unicam.cs.mpgc.rpg130324.model.entity.EnemyType;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class GameView {

    private final Stage stage;
    private final int ROW = 10;
    private final int COLUMN = 10;
    private final String namePlayer;

    // Matrice delle celle della griglia
    private final StackPane[][] gridCells = new StackPane[ROW][COLUMN];

    // Cache per riutilizzare gli stessi nodi ImageView (elimina il lag al 100%)
    private final ImageView[][] cellImageViews = new ImageView[ROW][COLUMN];

    // Cache per le risorse grafiche
    private final Map<String, Image> imageCache = new HashMap<>();

    private Consumer<String> onMovimentoListener;

    public GameView(Stage stage, String namePlayer) {
        this.stage = stage;
        this.namePlayer = namePlayer;
        loadImages();
        initializeInterface();
    }

    private void initializeInterface() {
        stage.setTitle("LONG WAY HOME - Mappa di Gioco");

        VBox root = new VBox(20);
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
        root.setStyle(root.getStyle() + " -fx-padding: 20px;");

        Label infoLabel = new Label("Usa le Frecce Direzionali o WASD per muoverti");
        infoLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        infoLabel.setTextFill(Color.web("#FFB74D"));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        gridPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        gridPane.setStyle("-fx-background-color: rgba(43, 11, 0, 0.6); -fx-padding: 10px; -fx-border-color: #E65100; -fx-border-width: 2px; -fx-border-radius: 5px;");

        // Creazione fissa della struttura visuale (eseguita UNA SOLA VOLTA all'avvio)
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(50, 50);

                if ((r + c) % 2 == 0) {
                    cell.setStyle("-fx-background-color: rgba(30, 30, 30, 0.45); -fx-border-color: rgba(230, 81, 0, 0.3);");
                } else {
                    cell.setStyle("-fx-background-color: rgba(10, 10, 10, 0.55); -fx-border-color: rgba(230, 81, 0, 0.3);");
                }

                // Inseriamo un'ImageView permanente per ogni cella
                ImageView iv = new ImageView();
                iv.setFitWidth(40);
                iv.setFitHeight(40);
                iv.setPreserveRatio(true);

                cell.getChildren().add(iv);
                cellImageViews[r][c] = iv;

                gridCells[r][c] = cell;
                gridPane.add(cell, c, r);
            }
        }

        root.getChildren().addAll(infoLabel, gridPane);

        Scene scene = new Scene(root, 800, 650);
        scene.setOnKeyPressed(this::handleKeyPress);

        stage.setScene(scene);
        stage.setResizable(false);
    }

    private void loadImages() {
        try {
            imageCache.put("Eroe", new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png"))));
            imageCache.put("Casa", new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgCasa.png"))));
        } catch (Exception ignored) {}

        for (EnemyType enemy : EnemyType.values()) {
            try {
                Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(enemy.getImagePath())));
                imageCache.put(enemy.getName(), img);
            } catch (Exception ignored) {}
        }
    }

    private void handleKeyPress(KeyEvent event) {
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
     * Ridisegna la mappa mantenendo GLI STESSI GLOW ORIGINALI ma senza lag.
     */
    public void enemyPosition(String[][] mappaGioco) {
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COLUMN; c++) {
                String elemento = mappaGioco[r][c];
                ImageView iv = cellImageViews[r][c];

                // Cella vuota: pulisce l'immagine e lo stile
                if (elemento == null || elemento.isEmpty()) {
                    iv.setImage(null);
                    iv.setStyle("");
                    continue;
                }

                EnemyType enemy = EnemyType.fromName(elemento);

                if (enemy != null) {
                    Image imgEnemy = imageCache.get(enemy.getName());
                    if (imgEnemy != null) {
                        iv.setImage(imgEnemy);
                        // RIPRISTINATO IL TUO GLOW CSS ORIGINALE
                        iv.setStyle("-fx-effect: dropshadow(three-pass-box, " + enemy.getGlowColor() + ", 12, 0.6, 0, 0);");
                    }
                } else {
                    switch (elemento) {
                        case "Eroe" -> {
                            iv.setImage(imageCache.get("Eroe"));
                            // RIPRISTINATO IL TUO GLOW EROE ORIGINALE
                            iv.setStyle("-fx-effect: dropshadow(three-pass-box, #FF5722, 12, 0.6, 0, 0);");
                        }
                        case "Casa" -> {
                            iv.setImage(imageCache.get("Casa"));
                            iv.setStyle(""); // Nessun glow per la casa
                        }
                    }
                }
            }
        }
    }

    public void setOnMoveListener(Consumer<String> listener) {
        this.onMovimentoListener = listener;
    }

    public void show() {
        stage.show();
    }
}