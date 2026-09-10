package it.unicam.cs.mpgc.rpg130324.view;

import it.unicam.cs.mpgc.rpg130324.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg130324.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg130324.model.entity.Hero;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Objects;

public class CombatView {

    private final Stage stage;
    private Image imgHero;
    private Image imgEnemy;

    private final Hero hero;
    private final Enemy enemy;
    private final EnemyType enemyType;

    private ProgressBar heroHpBar;
    private Label heroHpLabel;
    private ProgressBar enemyHpBar;
    private Label enemyHpLabel;

    private Button attackBtn;
    private Button defendBtn;

    public CombatView(Stage stage, Hero hero, Enemy enemy, EnemyType enemyType) {
        this.stage = stage;
        this.hero = hero;
        this.enemy = enemy;
        this.enemyType = enemyType;
        loadResources();
        initializeInterface();
    }

    /**
     * Carica le immagini necessarie in modo incapsulato all'interno della View.
     */
    private void loadResources() {
        try {
            this.imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/imgEroe.png")));
            this.imgEnemy = new Image(Objects.requireNonNull(getClass().getResourceAsStream(enemyType.getImagePath())));
        } catch (Exception ignored) {
            // Se il caricamento fallisce, l'interfaccia gestisce le immagini come null
        }
    }

    /**
     * Inizializza la struttura del layout JavaFX (sfondo, etichette, barre HP e bottoni).
     */
    private void initializeInterface() {
        stage.setTitle("LONG WAY HOME - Scontro con " + enemy.getName() + "!");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15, 30, 15, 30));

        // Sfondo di gioco
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/GameView_background.png")));
            root.setBackground(new Background(new BackgroundImage(
                    bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            )));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #120300;");
        }

        // Titolo in alto al centro
        Label labelTitle = new Label("COMBATTIMENTO!");
        labelTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        labelTitle.setTextFill(Color.web("#FF5722"));
        labelTitle.setStyle("-fx-effect: dropshadow(three-pass-box, #2B0B00, 10, 0.5, 0, 0);");

        BorderPane.setMargin(labelTitle, new Insets(20, 0, 0, 0));
        BorderPane.setAlignment(labelTitle, Pos.CENTER);
        root.setTop(labelTitle);

        // Contenitore centrale: Eroe VS Nemico
        HBox combatBox = new HBox(40);
        combatBox.setAlignment(Pos.CENTER);

        VBox heroBox = createHeroBox();

        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 42));
        vsLabel.setTextFill(Color.web("#FF5722"));
        vsLabel.setStyle(
                "-fx-background-color: rgba(30, 10, 5, 0.9); " +
                        "-fx-border-color: #FF5722; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-padding: 8px 18px;"
        );

        VBox enemyBox = createEnemyBox();

        combatBox.getChildren().addAll(heroBox, vsLabel, enemyBox);
        root.setCenter(combatBox);

        // Sezione bottoni azione
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        BorderPane.setMargin(buttonBox, new Insets(0, 0, 70, 0));

        attackBtn = createButton("ATTACCA", "#D32F2F", "#FF5722");
        defendBtn = createButton("DIFENDI", "#FF9800", "#FFC107");

        buttonBox.getChildren().addAll(attackBtn, defendBtn);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 800, 650);
        stage.setScene(scene);
    }

    private VBox createHeroBox() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);

        ImageView sprite = new ImageView(imgHero);
        sprite.setFitWidth(220);
        sprite.setFitHeight(220);
        sprite.setPreserveRatio(true);
        sprite.setStyle("-fx-effect: dropshadow(three-pass-box, #FF5722, 22, 0.7, 0, 0);");

        Label labelName = new Label(hero.getName());
        labelName.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        labelName.setTextFill(Color.web("#FF5722"));

        heroHpBar = new ProgressBar((double) hero.getCurrentHp() / hero.getMaxHp());
        heroHpBar.setPrefWidth(180);
        heroHpBar.setPrefHeight(16);
        heroHpBar.setStyle(
                "-fx-accent: #D32F2F; " +
                        "-fx-control-inner-background: rgba(20, 5, 0, 0.8); " +
                        "-fx-border-color: #FF5722; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        heroHpLabel = new Label(hero.getCurrentHp() + " / " + hero.getMaxHp() + " HP");
        heroHpLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        heroHpLabel.setTextFill(Color.WHITE);

        box.getChildren().addAll(sprite, labelName, heroHpBar, heroHpLabel);
        return box;
    }

    private VBox createEnemyBox() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER); // Mantiene il blocco ben centrato verticalmente nella schermata

        String glowColor = enemyType.getGlowColor();

        ImageView sprite = new ImageView(imgEnemy);
        sprite.setFitWidth(220);
        sprite.setFitHeight(220);
        sprite.setPreserveRatio(true);
        sprite.setStyle("-fx-effect: dropshadow(three-pass-box, " + glowColor + ", 22, 0.7, 0, 0);");

        // Contenitore a dimensione fissa per garantire l'allineamento dei testi sottostanti
        StackPane imageContainer = new StackPane(sprite);
        imageContainer.setPrefSize(220, 220);
        imageContainer.setMinSize(220, 220);
        imageContainer.setMaxSize(220, 220);
        imageContainer.setAlignment(Pos.CENTER); // Centra l'immagine all'interno del riquadro fisso

        Label labelName = new Label(enemy.getName());
        labelName.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        labelName.setTextFill(Color.web(glowColor));

        enemyHpBar = new ProgressBar((double) enemy.getCurrentHp() / enemy.getMaxHp());
        enemyHpBar.setPrefWidth(180);
        enemyHpBar.setPrefHeight(16);
        enemyHpBar.setStyle(
                "-fx-accent: " + glowColor + "; " +
                        "-fx-control-inner-background: rgba(20, 5, 0, 0.8); " +
                        "-fx-border-color: " + glowColor + "; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        enemyHpLabel = new Label(enemy.getCurrentHp() + " / " + enemy.getMaxHp() + " HP");
        enemyHpLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        enemyHpLabel.setTextFill(Color.WHITE);

        // Aggiungiamo imageContainer al posto di sprite
        box.getChildren().addAll(imageContainer, labelName, enemyHpBar, enemyHpLabel);
        return box;
    }

    private Button createButton(String text, String glowColor, String hoverColor) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(160);
        btn.setPrefHeight(50);

        String baseStyle = "-fx-background-color: rgba(30, 10, 5, 0.85); " +
                "-fx-border-color: " + glowColor + "; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 8px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;";
        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(baseStyle + "-fx-border-color: " + hoverColor + "; -fx-effect: dropshadow(three-pass-box, " + hoverColor + ", 12, 0.6, 0, 0);"));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));

        return btn;
    }

    /**
     * Aggiorna le barre HP.
     */
    public void updateUI() {
        heroHpBar.setProgress((double) hero.getCurrentHp() / hero.getMaxHp());
        heroHpLabel.setText(hero.getCurrentHp() + " / " + hero.getMaxHp() + " HP");

        enemyHpBar.setProgress((double) enemy.getCurrentHp() / enemy.getMaxHp());
        enemyHpLabel.setText(enemy.getCurrentHp() + " / " + enemy.getMaxHp() + " HP");
    }

    public void setOnAttackListener(Runnable action) {
        this.attackBtn.setOnAction(e -> action.run());
    }

    public void setOnDefendListener(Runnable action) {
        defendBtn.setOnAction(e -> action.run());
    }

    public void show() {
        stage.show();
    }
}