package it.unicam.cs.mpgc.rpg130324.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * Schermata iniziale JavaFX per il benvenuto e l'inserimento del nome del giocatore.
 */
public class WelcomeView {

    private final Stage stage;
    private TextField campoNome;
    private Button bottoneInizia;

    public WelcomeView(Stage stage) {
        this.stage = stage;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Benvenuto");

        // Layout principale con sfondo scuro a gradiente (Arancione Bruciato -> Nero)
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background: linear-gradient(to bottom, #2B0B00, #120300);");

        // 1. Titolo del gioco (Giallo Aranciato con ombreggiatura Rossa)
        Label titoloLabel = new Label("LONG WAY HOME");
        titoloLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 52));
        titoloLabel.setTextFill(Color.web("#FFC107")); // Giallo ambra
        titoloLabel.setStyle("-fx-effect: dropshadow(three-pass-box, #D32F2F, 15, 0.5, 0, 0);"); // Glow Rosso

        // 2. Istruzione per il giocatore (Arancione Chiaro)
        Label istruzioneLabel = new Label("Inserisci il tuo nome per iniziare l'avventura");
        istruzioneLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 18));
        istruzioneLabel.setTextFill(Color.web("#FFB74D")); // Arancione chiaro

        // 3. Campo di testo per il nome (Bordo Arancione e Testo Arancione Scuro)
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

        // 4. Pulsante INIZIA (Rosso/Arancione con Testo Giallo)
        bottoneInizia = new Button("INIZIA");
        bottoneInizia.setPrefSize(180, 50);
        bottoneInizia.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        bottoneInizia.setTextFill(Color.web("#FFFDE7")); // Giallo chiarissimo
        bottoneInizia.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FF5722, #D32F2F); " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 3);"
        );

        // Effetto Hover sul pulsante
        bottoneInizia.setOnMouseEntered(e ->
                bottoneInizia.setStyle("-fx-background-color: linear-gradient(to bottom, #FF7043, #E53935); -fx-background-radius: 8px; -fx-cursor: hand;")
        );
        bottoneInizia.setOnMouseExited(e ->
                bottoneInizia.setStyle("-fx-background-color: linear-gradient(to bottom, #FF5722, #D32F2F); -fx-background-radius: 8px; -fx-cursor: hand;")
        );

        // Aggiunta degli elementi al layout
        root.getChildren().addAll(titoloLabel, istruzioneLabel, campoNome, bottoneInizia);

        // Creazione della scena
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setResizable(false);
    }

    /**
     * Collega l'azione del pulsante INIZIA alla logica di callback.
     */
    public void setOnIniziaListener(Consumer<String> callback) {
        bottoneInizia.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attenzione");
                alert.setHeaderText(null);
                alert.setContentText("Per favore, inserisci un nome valido!");
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