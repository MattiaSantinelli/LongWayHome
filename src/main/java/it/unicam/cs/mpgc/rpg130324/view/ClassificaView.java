package it.unicam.cs.mpgc.rpg130324.view;

import it.unicam.cs.mpgc.rpg130324.persistence.DatiSalvataggio;
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

public class ClassificaView {

    private final BorderPane mainLayout;
    private final Button btnIndietro;

    public ClassificaView(List<DatiSalvataggio> listaDati, Runnable azioneIndietro) {
        this.mainLayout = new BorderPane();
        this.btnIndietro = new Button("INDIETRO");

        inizializzaInterfaccia(listaDati, azioneIndietro);
    }

    private void inizializzaInterfaccia(List<DatiSalvataggio> lista, Runnable azioneIndietro) {
        // --- Gestione Sfondo ---
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

        // TITOLO SCHERMATA
        Label titoloLabel = new Label("🏆 CLASSIFICA GIOCATORI");
        titoloLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 42));
        titoloLabel.setTextFill(Color.web("#FFC107"));
        titoloLabel.setStyle("-fx-effect: dropshadow(three-pass-box, #D32F2F, 15, 0.5, 0, 0);");

        // CONTENITORE LISTA SALVATAGGI
        VBox listaContenuto = new VBox(12);
        listaContenuto.setAlignment(Pos.TOP_CENTER);
        listaContenuto.setStyle("-fx-padding: 10px;");

        if (lista == null || lista.isEmpty()) {
            Label noDataLabel = new Label("Nessun salvataggio presente.");
            noDataLabel.setFont(Font.font("Georgia", FontWeight.NORMAL, 18));
            noDataLabel.setTextFill(Color.web("#FFB74D"));
            listaContenuto.getChildren().add(noDataLabel);
        } else {
            int posizione = 1;
            for (DatiSalvataggio dati : lista) {
                String testo = String.format("%d. %s  —  Nemici sconfitti: %d  |  Tempo: %ds",
                        posizione++, dati.getNomeGiocatore(), dati.getNemiciSconfitti(), dati.getTempoTrascorso());

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
                listaContenuto.getChildren().add(rigaLabel);
            }
        }

        // SCROLL PANE PER LA LISTA
        ScrollPane scrollPane = new ScrollPane(listaContenuto);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxWidth(650);
        scrollPane.setMaxHeight(340);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");

        // CSS SCROLLBAR DARK/RPG
        String cssScrollbar =
                ".scroll-bar:vertical { -fx-background-color: rgba(20, 10, 5, 0.5); -fx-pref-width: 10px; -fx-background-radius: 5px; } " +
                        ".scroll-bar:vertical .thumb { -fx-background-color: #E65100; -fx-background-radius: 5px; } " +
                        ".scroll-bar:vertical .thumb:hover { -fx-background-color: #FFC107; } " +
                        ".scroll-bar:vertical .track, .scroll-bar:vertical .track-background { -fx-background-color: transparent; } " +
                        ".scroll-bar:vertical .increment-button, .scroll-bar:vertical .decrement-button { -fx-padding: 0; -fx-background-color: transparent; } " +
                        ".scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow { -fx-shape: null; -fx-padding: 0; }";

        mainLayout.getStylesheets().add("data:text/css," + cssScrollbar.replaceAll("\n", ""));

        // BOTTONE INDIETRO
        btnIndietro.setPrefSize(160, 45);
        btnIndietro.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        applicaStileIndietroBase(btnIndietro);
        btnIndietro.setOnMouseEntered(e -> applicaStileIndietroHover(btnIndietro));
        btnIndietro.setOnMouseExited(e -> applicaStileIndietroBase(btnIndietro));

        btnIndietro.setOnAction(e -> azioneIndietro.run());

        root.getChildren().addAll(titoloLabel, scrollPane, btnIndietro);
        mainLayout.setCenter(root);
    }

    public Scene getScene() {
        return new Scene(mainLayout, 800, 600);
    }

    private void applicaStileIndietroBase(Button btn) {
        btn.setStyle("-fx-background-color: linear-gradient(to bottom, #E64A19, #BF360C); -fx-border-color: #FFC107; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-text-fill: #FFD54F; -fx-effect: dropshadow(three-pass-box, #FF5722, 10, 0.4, 0, 0); -fx-cursor: hand;");
    }

    private void applicaStileIndietroHover(Button btn) {
        btn.setStyle("-fx-background-color: linear-gradient(to bottom, #FF5722, #D32F2F); -fx-border-color: #FFE082; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-text-fill: #FFFFFF; -fx-effect: dropshadow(three-pass-box, #FF9800, 18, 0.7, 0, 0); -fx-cursor: hand;");
    }
}