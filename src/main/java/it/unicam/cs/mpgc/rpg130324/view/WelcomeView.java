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

/**
 * Schermata iniziale JavaFX e inserimento nome del giocatore.
 */
public class WelcomeView {

    private final Stage stage;
    private TextField campoNome;
    private Button bottoneInizia;
    private Button btnClassifica;

    public WelcomeView(Stage stage) {
        this.stage = stage;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Benvenuto");

        BorderPane mainLayout = new BorderPane();

        // --- Gestione Sfondo su Layout Principale (Immagine con Fallback su Gradiente) ---
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/WelcomeView_background.png")));
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            mainLayout.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            // Se l'immagine non è presente in src/main/resources, applica il gradiente scuro
            mainLayout.setStyle("-fx-background: linear-gradient(to bottom, #2B0B00, #120300);");
        }

        // Aggiunta della barra superiore integrata (Classifica, Aiuto)
        HBox barraSuperiore = creaBarraPulsanti();
        mainLayout.setTop(barraSuperiore);

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);

        // TITOLO GIOCO (Giallo con Glow Rosso)
        Label titoloLabel = new Label("LONG WAY HOME");
        titoloLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 52));
        titoloLabel.setTextFill(Color.web("#FFC107"));
        titoloLabel.setStyle("-fx-effect: dropshadow(three-pass-box, #D32F2F, 15, 0.5, 0, 0);");

        // ISTRUZIONI (Arancione Chiaro)
        Label istruzioneLabel = new Label("Inserisci il tuo nome per iniziare l'avventura");
        istruzioneLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 18));
        istruzioneLabel.setTextFill(Color.web("#FFB74D"));

        // CAMPO DI TESTO
        campoNome = new TextField();
        campoNome.setMaxWidth(320);
        campoNome.setPrefHeight(45);
        campoNome.setAlignment(Pos.CENTER);
        campoNome.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        campoNome.setStyle(
                "-fx-background-color: #1E1E1E; " +
                        "-fx-text-fill: #FFB74D; " +
                        "-fx-border-color: #E65100; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        // BOTTONE INIZIA (Stile Glow & Bordo Dorato)
        bottoneInizia = new Button("INIZIA");
        bottoneInizia.setPrefSize(180, 50);
        bottoneInizia.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

        // Stile dinamico bottone al passaggio del mouse
        applicaStilePulsanteBase(bottoneInizia);
        bottoneInizia.setOnMouseEntered(e -> applicaStilePulsanteHover(bottoneInizia));
        bottoneInizia.setOnMouseExited(e -> applicaStilePulsanteBase(bottoneInizia));

        // Aggiunta degli elementi al layout
        root.getChildren().addAll(titoloLabel, istruzioneLabel, campoNome, bottoneInizia);

        mainLayout.setCenter(root);

        Scene scene = new Scene(mainLayout, 800, 600);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    /**
     * Crea la barra superiore perfettamente unita allo sfondo generale della schermata.
     */
    private HBox creaBarraPulsanti() {
        HBox barra = new HBox(12);
        barra.setAlignment(Pos.CENTER_LEFT);
        // Padding interno per distanziare i pulsanti dal bordo superiore e sinistro
        barra.setStyle("-fx-background-color: transparent; -fx-padding: 20px 0 0 20px;");

        // Bottone Classifica
        btnClassifica = new Button("🏆 Classifica");
        preparaPulsanteTopBar(btnClassifica);

        // Bottone Aiuto
        Button btnAiuto = new Button("❓ Aiuto");
        preparaPulsanteTopBar(btnAiuto);
        btnAiuto.setOnAction(e -> {
            Alert infoAiuto = new Alert(Alert.AlertType.INFORMATION);
            infoAiuto.setTitle("Aiuto");
            infoAiuto.setHeaderText("Guida di gioco");
            infoAiuto.setContentText("Raggiungi la casa in fondo al percorso per vincere la partita!");
            infoAiuto.showAndWait();
        });

        barra.getChildren().addAll(btnClassifica, btnAiuto);

        return barra;
    }

    /**
     * Applica font e comportamenti hover al singolo pulsante della barra superiore.
     */
    private void preparaPulsanteTopBar(Button btn) {
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        btn.setPrefHeight(35);
        applicaStileTopBarBase(btn);
        btn.setOnMouseEntered(e -> applicaStileTopBarHover(btn));
        btn.setOnMouseExited(e -> applicaStileTopBarBase(btn));
    }

    /**
     * Caratteristiche base per i pulsanti della barra superiore (Storico e Aiuto).
     */
    private void applicaStileTopBarBase(Button btn) {
        btn.setStyle(
                "-fx-background-color: rgba(20, 10, 5, 0.65); " +
                        "-fx-border-color: #BF360C; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-text-fill: #FFB74D; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1); " +
                        "-fx-cursor: hand;"
        );
    }

    /**
     * Caratteristiche hover per i pulsanti della barra superiore (Storico e Aiuto).
     */
    private void applicaStileTopBarHover(Button btn) {
        btn.setStyle(
                "-fx-background-color: rgba(45, 15, 8, 0.85); " +
                        "-fx-border-color: #FFC107; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-text-fill: #FFE082; " +
                        "-fx-effect: dropshadow(three-pass-box, #E64A19, 8, 0.3, 0, 0); " +
                        "-fx-cursor: hand;"
        );
    }

    /**
     * Caratteristiche base bottone (come appare quando non ci si passa sopra con il mouse).
     */
    private void applicaStilePulsanteBase(Button btn) {
        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #E64A19, #BF360C); " +
                        "-fx-border-color: #FFC107; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-text-fill: #FFD54F; " +
                        "-fx-effect: dropshadow(three-pass-box, #FF5722, 10, 0.4, 0, 0); " +
                        "-fx-cursor: hand;"
        );
    }

    /**
     * Caratteristiche avanzate bottone (come appare quando ci si passa sopra con il mouse).
     */
    private void applicaStilePulsanteHover(Button btn) {
        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FF5722, #D32F2F); " +
                        "-fx-border-color: #FFE082; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 8px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-effect: dropshadow(three-pass-box, #FF9800, 18, 0.7, 0, 0); " +
                        "-fx-cursor: hand;"
        );
    }

    /**
     * Collega l'azione del pulsante Classifica alla logica del Controller.
     */
    public void setOnClassificaListener(Runnable callback) {
        btnClassifica.setOnAction(e -> callback.run());
    }

    /**
     * Collega l'azione del pulsante INIZIA alla logica di callback.
     */
    public void setOnIniziaListener(Consumer<String> callback) {
        bottoneInizia.setOnAction(e -> {
            String nome = campoNome.getText().trim();
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

    public void mostra() {
        stage.show();
    }

}