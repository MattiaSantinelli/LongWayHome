package it.unicam.cs.mpgc.rpg130324;

import it.unicam.cs.mpgc.rpg130324.view.WelcomeView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        WelcomeView menu = new WelcomeView(primaryStage);

        menu.setOnIniziaListener(nome -> {
            System.out.println("Nome inserito: " + nome);
            // Qui passeremo alla schermata di gioco principale (es. SchermataGiocoView)
        });

        menu.mostra();
    }

    public static void main(String[] args) {
        launch(args);
    }
}