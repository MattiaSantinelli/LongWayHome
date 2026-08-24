package it.unicam.cs.mpgc.rpg130324.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;

public class SchermataCombattimentoGigante {

    private final Stage stage;
    private final Image imgEroe;
    private final Image imgGigante;
    private final String nomeGiocatore; //campo per memorizzare il nome

    public SchermataCombattimentoGigante(Stage stage, Image imgEroe, Image imgGigante, String nomeGiocatore) {
        this.stage = stage;
        this.imgEroe = imgEroe;
        this.imgGigante = imgGigante;
        this.nomeGiocatore = nomeGiocatore;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Scontro con il Gigante!");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15, 30, 15, 30));

        // --- SFONDO DI GIOCO ---
        try {
            Image bgImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/GameView_background.png")));
            root.setBackground(new Background(new BackgroundImage(
                    bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            )));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #120300;");
        }

        // --- TITOLO IN ALTO AL CENTRO ---
        Label titoloLabel = new Label("COMBATTIMENTO!");
        titoloLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        titoloLabel.setTextFill(Color.web("#FF5722"));
        titoloLabel.setStyle("-fx-effect: dropshadow(three-pass-box, #2B0B00, 10, 0.5, 0, 0);");
        //posizionamento titolo
        BorderPane.setMargin(titoloLabel, new Insets(25, 0, 0, 0));
        BorderPane.setAlignment(titoloLabel, Pos.CENTER);
        root.setTop(titoloLabel);

        // --- CONTENITORE CENTRALE: EROE VS GIGANTE ---
        HBox scontroBox = new HBox(40);
        scontroBox.setAlignment(Pos.CENTER);

        // Eroe a Sinistra (Sprite ingrandita a 240px)
        VBox eroeBox = creaBoxCombattente(imgEroe, nomeGiocatore, "#FF5722");

        // Scritta "VS" con Stile del Bottone Attivo
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

        // Gigante a Destra (Sprite ingrandita a 240px)
        VBox giganteBox = creaBoxCombattente(imgGigante, "GIGANTE", "#FFC107");

        scontroBox.getChildren().addAll(eroeBox, vsLabel, giganteBox);
        root.setCenter(scontroBox);

        // --- SEZIONE BOTTONI AZIONE (RIALZATI) ---
        HBox bottoniBox = new HBox(30);
        bottoniBox.setAlignment(Pos.CENTER);
        BorderPane.setMargin(bottoniBox, new Insets(0, 0, 70, 0)); // Solleva i bottoni

        Button btnAttacca = creaBottone("ATTACCA", "#D32F2F", "#FF5722");
        Button btnDifendi = creaBottone("DIFENDI", "#FF9800", "#FFC107");

        bottoniBox.getChildren().addAll(btnAttacca, btnDifendi);
        root.setBottom(bottoniBox);

        Scene scene = new Scene(root, 800, 650);
        stage.setScene(scene);
    }

    private VBox creaBoxCombattente(Image img, String nome, String coloreGlow) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        ImageView sprite = new ImageView(img);
        sprite.setFitWidth(240);
        sprite.setFitHeight(240);
        sprite.setPreserveRatio(true);
        sprite.setStyle("-fx-effect: dropshadow(three-pass-box, " + coloreGlow + ", 22, 0.7, 0, 0);");

        Label labelNome = new Label(nome);
        labelNome.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        labelNome.setTextFill(Color.web(coloreGlow));

        box.getChildren().addAll(sprite, labelNome);
        return box;
    }

    private Button creaBottone(String testo, String coloreBordo, String coloreHover) {
        Button btn = new Button(testo);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(160);
        btn.setPrefHeight(50);

        String stileBase = "-fx-background-color: rgba(30, 10, 5, 0.85); " +
                "-fx-border-color: " + coloreBordo + "; " +
                "-fx-border-width: 2px; " +
                "-fx-border-radius: 8px; " +
                "-fx-background-radius: 8px; " +
                "-fx-cursor: hand;";

        btn.setStyle(stileBase);

        btn.setOnMouseEntered(e -> btn.setStyle(stileBase + "-fx-border-color: " + coloreHover + "; -fx-effect: dropshadow(three-pass-box, " + coloreHover + ", 12, 0.6, 0, 0);"));
        btn.setOnMouseExited(e -> btn.setStyle(stileBase));

        return btn;
    }

    public void mostra() {
        stage.show();
    }
}
