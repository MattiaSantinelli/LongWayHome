package it.unicam.cs.mpgc.rpg130324.view;

import it.unicam.cs.mpgc.rpg130324.model.Eroe;
import it.unicam.cs.mpgc.rpg130324.model.Nemico;
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

public class SchermataCombattimentoMago {

    private final Stage stage;
    private final Image imgEroe;
    private final Image imgMago;

    // Riferimenti ai modelli di gioco
    private final Eroe eroe;
    private final Nemico mago;

    // Riferimenti alle Barre HP e Label salvati come campi di classe
    private ProgressBar hpBarEroe;
    private Label labelHpTextEroe;
    private ProgressBar hpBarMago;
    private Label labelHpTextMago;

    // Bottoni per il Controller
    private Button btnAttacca;
    private Button btnDifendi;

    public SchermataCombattimentoMago(Stage stage, Image imgEroe, Image imgMago, Eroe eroe, Nemico mago) {
        this.stage = stage;
        this.imgEroe = imgEroe;
        this.imgMago = imgMago;
        this.eroe = eroe;
        this.mago = mago;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        stage.setTitle("LONG WAY HOME - Scontro con il Mago!");

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
        // Posizionamento titolo
        BorderPane.setMargin(titoloLabel, new Insets(20, 0, 0, 0));
        BorderPane.setAlignment(titoloLabel, Pos.CENTER);
        root.setTop(titoloLabel);

        // --- CONTENITORE CENTRALE: EROE VS MAGO ---
        HBox scontroBox = new HBox(40);
        scontroBox.setAlignment(Pos.CENTER);

        // Box Eroe con Barra HP (Rosso/Arancio)
        VBox eroeBox = creaBoxEroe();

        // Scritta "VS" Stile bottone
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

        // Box Mago con barra HP (bluastro)
        VBox magoBox = creaBoxMago();

        scontroBox.getChildren().addAll(eroeBox, vsLabel, magoBox);
        root.setCenter(scontroBox);

        // --- SEZIONE BOTTONI AZIONE ---
        HBox bottoniBox = new HBox(30);
        bottoniBox.setAlignment(Pos.CENTER);
        BorderPane.setMargin(bottoniBox, new Insets(0, 0, 70, 0));

        btnAttacca = creaBottone("ATTACCA", "#D32F2F", "#FF5722");
        btnDifendi = creaBottone("DIFENDI", "#FF9800", "#FFC107");

        bottoniBox.getChildren().addAll(btnAttacca, btnDifendi);
        root.setBottom(bottoniBox);

        Scene scene = new Scene(root, 800, 650);
        stage.setScene(scene);
    }

    private VBox creaBoxEroe() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);

        ImageView sprite = new ImageView(imgEroe);
        sprite.setFitWidth(220);
        sprite.setFitHeight(220);
        sprite.setPreserveRatio(true);
        sprite.setStyle("-fx-effect: dropshadow(three-pass-box, #FF5722, 22, 0.7, 0, 0);");

        Label labelNome = new Label(eroe.getNome());
        labelNome.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        labelNome.setTextFill(Color.web("#FF5722"));

        hpBarEroe = new ProgressBar((double) eroe.getHpAttuali() / eroe.getHpMassimi());
        hpBarEroe.setPrefWidth(180);
        hpBarEroe.setPrefHeight(16);
        hpBarEroe.setStyle(
                "-fx-accent: #D32F2F; " +
                        "-fx-control-inner-background: rgba(20, 5, 0, 0.8); " +
                        "-fx-border-color: #FF5722; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        labelHpTextEroe = new Label(eroe.getHpAttuali() + " / " + eroe.getHpMassimi() + " HP");
        labelHpTextEroe.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        labelHpTextEroe.setTextFill(Color.WHITE);

        box.getChildren().addAll(sprite, labelNome, hpBarEroe, labelHpTextEroe);
        return box;
    }

    private VBox creaBoxMago() {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);

        ImageView sprite = new ImageView(imgMago);
        sprite.setFitWidth(220);
        sprite.setFitHeight(220);
        sprite.setPreserveRatio(true);
        sprite.setStyle("-fx-effect: dropshadow(three-pass-box, #1E88E5, 22, 0.7, 0, 0);");

        Label labelNome = new Label(mago.getNome());
        labelNome.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        labelNome.setTextFill(Color.web("#1E88E5"));

        hpBarMago = new ProgressBar((double) mago.getHpAttuali() / mago.getHpMassimi());
        hpBarMago.setPrefWidth(180);
        hpBarMago.setPrefHeight(16);
        hpBarMago.setStyle(
                "-fx-accent: #0277BD; " +
                        "-fx-control-inner-background: rgba(20, 5, 0, 0.8); " +
                        "-fx-border-color: #1E88E5; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        labelHpTextMago = new Label(mago.getHpAttuali() + " / " + mago.getHpMassimi() + " HP");
        labelHpTextMago.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        labelHpTextMago.setTextFill(Color.WHITE);

        box.getChildren().addAll(sprite, labelNome, hpBarMago, labelHpTextMago);
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

    // --- METODO DI AGGIORNAMENTO GRAFICO ---
    public void aggiornaGrafica() {
        hpBarEroe.setProgress((double) eroe.getHpAttuali() / eroe.getHpMassimi());
        labelHpTextEroe.setText(eroe.getHpAttuali() + " / " + eroe.getHpMassimi() + " HP");

        hpBarMago.setProgress((double) mago.getHpAttuali() / mago.getHpMassimi());
        labelHpTextMago.setText(mago.getHpAttuali() + " / " + mago.getHpMassimi() + " HP");
    }

    // --- METODI PER IL CONTROLLER ---
    public void setOnAttaccaListener(Runnable azione) {
        this.btnAttacca.setOnAction(e -> azione.run());
    }

    public void setOnDifendiListener(Runnable azione) {
        btnDifendi.setOnAction(e -> azione.run());
    }

    public void mostra() {
        stage.show();
    }
}