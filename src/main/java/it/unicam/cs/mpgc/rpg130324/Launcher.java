package it.unicam.cs.mpgc.rpg130324;

import it.unicam.cs.mpgc.rpg130324.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inizializza il Controller e avvia il flusso del gioco
        GameController gameController = new GameController(primaryStage);
        gameController.avviaGioco();
    }

    public static void main(String[] args) {
        launch(args);
    }
}