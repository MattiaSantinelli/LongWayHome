package it.unicam.cs.mpgc.rpg130324.controller;

import it.unicam.cs.mpgc.rpg130324.view.GameView;
import it.unicam.cs.mpgc.rpg130324.view.WelcomeView;
import javafx.stage.Stage;

public class GameController {

    private final Stage stage;
    private String nomeGiocatore;

    public GameController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Avvia l'applicazione mostrando la prima schermata (WelcomeView).
     */
    public void avviaGioco() {
        WelcomeView welcomeView = new WelcomeView(stage);

        // Il Controller ascolta l'evento della schermata e gestisce il passaggio di stato
        welcomeView.setOnIniziaListener(nome -> {
            this.nomeGiocatore = nome;
            System.out.println("Benvenuto " + nomeGiocatore + "! Avvio della partita...");
            mostraMappaDiGioco();
        });

        welcomeView.mostra();
    }

    /**
     * Transizione verso la schermata da gioco
     */
    private void mostraMappaDiGioco() {
        GameView gameView = new GameView(stage, nomeGiocatore);
        gameView.mostra();
    }

    public String getNomeGiocatore() {
        return nomeGiocatore;
    }
}
