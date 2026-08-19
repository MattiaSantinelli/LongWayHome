package it.unicam.cs.mpgc.rpg130324;

import it.unicam.cs.mpgc.rpg130324.view.GameView;
import it.unicam.cs.mpgc.rpg130324.view.WelcomeView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Crea la schermata iniziale
        WelcomeView welcomeView = new WelcomeView(primaryStage);

        // 2. Quando l'utente inserisce il nome e clicca "INIZIA", passa a GameView
        welcomeView.setOnIniziaListener(nomeGiocatore -> {
            System.out.println("Benvenuto " + nomeGiocatore + "! Avvio della partita...");

            // Crea e mostra la schermata con la scacchiera
            GameView gameView = new GameView(primaryStage);
            gameView.mostra();
        });

        // 3. Mostra prima la schermata di benvenuto
        welcomeView.mostra();
    }

    public static void main(String[] args) {
        launch(args);
    }
}