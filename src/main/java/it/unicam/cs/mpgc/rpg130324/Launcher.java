package it.unicam.cs.mpgc.rpg130324;

import it.unicam.cs.mpgc.rpg130324.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher {

    public static void main(String[] args) {
        App.main(args);
    }

    // Deve essere public e static per permettere a JavaFX di istanziarla
    public static class App extends Application {

        public App() {
            // Costruttore no-arg esplicito per la reflection di JavaFX
        }

        @Override
        public void start(Stage primaryStage) {
            // Inizializza il Controller e avvia il flusso del gioco
            GameController gameController = new GameController(primaryStage);
            gameController.startGame();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
}