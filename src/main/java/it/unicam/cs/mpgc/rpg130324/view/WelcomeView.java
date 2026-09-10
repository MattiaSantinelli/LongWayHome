package it.unicam.cs.mpgc.rpg130324.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class WelcomeView {

    private final Stage stage;
    private TextField nameField;
    private Button startButton;
    private Button leaderboardButton;

    public WelcomeView(Stage stage) {
        this.stage = stage;
        initializeInterface();
    }

    /**
     * Inizializza la struttura del layout JavaFx (sfondo, top bar, etichette, bottoni)
     */
    private void initializeInterface() {
        stage.setTitle("LONG WAY HOME - Benvenuto");

        BorderPane mainLayout = new BorderPane();

        // Sfondo di gioco
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/image/WelcomeView_background.png")));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            mainLayout.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            mainLayout.setStyle("-fx-background: linear-gradient(to bottom, #2B0B00, #120300);");
        }
        // Bottoni top bar
        HBox topBar = createButtonBar();
        mainLayout.setTop(topBar);

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);

        // Titolo gioco
        Label labelTitle = new Label("LONG WAY HOME");
        labelTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 52));
        labelTitle.setTextFill(Color.web("#FFC107"));
        labelTitle.setStyle("-fx-effect: dropshadow(three-pass-box, #D32F2F, 15, 0.5, 0, 0);");

        // Istruzioni
        Label instructionLabel = new Label("Inserisci il tuo nome per iniziare l'avventura");
        instructionLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 18));
        instructionLabel.setTextFill(Color.web("#FFB74D"));

        // Campo di testo
        nameField = new TextField();
        nameField.setMaxWidth(320);
        nameField.setPrefHeight(45);
        nameField.setAlignment(Pos.CENTER);
        nameField.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        nameField.setStyle(
                "-fx-background-color: #1E1E1E; " +
                        "-fx-text-fill: #FFB74D; " +
                        "-fx-border-color: #E65100; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        // Bottone INIZIA
        startButton = new Button("INIZIA");
        startButton.setPrefSize(180, 50);
        startButton.getStyleClass().add("primary-button");

        root.getChildren().addAll(labelTitle, instructionLabel, nameField, startButton);
        mainLayout.setCenter(root);

        Scene scene = new Scene(mainLayout, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
    }

    private HBox createButtonBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: transparent; -fx-padding: 20px 0 0 20px;");

        leaderboardButton = new Button("🏆 Classifica");
        setupTopBarButton(leaderboardButton);

        Button btnHelp = new Button("❓ Aiuto");
        setupTopBarButton(btnHelp);
        btnHelp.setOnAction(e -> {
            Alert infoHelp = new Alert(Alert.AlertType.INFORMATION);
            infoHelp.setTitle("Aiuto");
            infoHelp.setHeaderText("Guida di gioco");
            infoHelp.setContentText(
                    "\uD83C\uDFAE MOVIMENTO\n" + " - All'interno del gioco è possibile muoversi attraverso l'uso delle frecce direzionali o i tasti WASD.\n\n" +
                    "⚔ COMBATTIMENTO\n" + " - Bottone ATTACCA: Attraverso questo bottone l'eroe infligge i danni al nemico.\n " +
                            " - Bottone DIFENDI: Attraverso questo bottone l'eroe si difende dai danni che i nemici infliggono ogni 0.5/1 secondo di gioco.\n\n" +
                    "\uD83C\uDFAF OBIETTIVO\n" + " - Il gioco termina quando l'Eroe riesce a raggiungere la Casa! Buona fortuna!"
            );
            // Permette alla finestra Alert di ridimensionarsi automaticamente in base al contenuto
            infoHelp.setResizable(true);
            infoHelp.getDialogPane().setPrefWidth(500);
            infoHelp.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

            infoHelp.showAndWait();
        });
        bar.getChildren().addAll(leaderboardButton, btnHelp);
        return bar;
    }

    private void setupTopBarButton(Button btn) {
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        btn.setPrefHeight(35);

        // Stile base (stato normale)
        String baseStyle =
                "-fx-background-color: rgba(20, 10, 5, 0.75); " +
                        "-fx-border-color: #BF360C; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-text-fill: #FFB74D; " +
                        "-fx-cursor: hand;";

        // Stile al passaggio del mouse (hover con glow e schiarimento testo)
        String hoverStyle =
                "-fx-background-color: rgba(50, 15, 5, 0.90); " +
                        "-fx-border-color: #FF5722; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-text-fill: #FFE0B2; " +
                        "-fx-effect: dropshadow(three-pass-box, #FF5722, 12, 0.6, 0, 0); " +
                        "-fx-cursor: hand;";

        btn.setStyle(baseStyle);

        // Gestione dinamica dell'effetto Hover
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    public void setOnLeaderboardListener(Runnable callback) {
        leaderboardButton.setOnAction(e -> callback.run());
    }

    public void setOnStartListener(Consumer<String> callback) {
        startButton.setOnAction(e -> {
            String nome = nameField.getText().trim();
            if (nome.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("ATTENZIONE!");
                alert.setHeaderText(null);
                alert.setContentText("Nome inserito non valido, il nome del giocatore non può essere vuoto. Perfavore riprova!");
                alert.showAndWait();
            } else {
                callback.accept(nome);
            }
        });
    }

    public void show() {
        stage.show();
    }
}