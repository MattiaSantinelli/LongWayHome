package it.unicam.cs.mpgc.rpg130324.controller;



import it.unicam.cs.mpgc.rpg130324.model.GameMap;

import it.unicam.cs.mpgc.rpg130324.model.entity.EnemyType;

import it.unicam.cs.mpgc.rpg130324.model.entity.Hero;

import it.unicam.cs.mpgc.rpg130324.model.persistence.SaveData;

import it.unicam.cs.mpgc.rpg130324.model.persistence.SaveManager;

import it.unicam.cs.mpgc.rpg130324.view.*;

import javafx.animation.KeyFrame;

import javafx.animation.Timeline;

import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.util.Duration;



import java.util.List;



public class GameController {



    private final Stage stage;

    private String playerName;



    private Hero hero;

    private final GameMap gameMap;



    private int heroRow = 0;

    private int heroColumn = 0;



    private Timeline enemyBuffTimer;

    private int enemyBuffLevel = 0;

    private long gameTime;

    private int defeatedEnemies = 0;



    private CombatController activeCombatController;



    public GameController(Stage stage) {

        this.stage = stage;

        this.gameMap = new GameMap();

    }



    public void startGame() {

        WelcomeView welcomeView = new WelcomeView(stage);



        welcomeView.setOnLeaderboardListener(() -> {

            Scene initialScene = stage.getScene();

            List<SaveData> savedGames = SaveManager.loadAllSaves();

            LeaderboardView classificaView = new LeaderboardView(savedGames, () -> stage.setScene(initialScene));

            stage.setScene(classificaView.getScene());

        });



        welcomeView.setOnStartListener(name -> {

            this.playerName = name;

            this.hero = new Hero(playerName);

            this.gameTime = System.currentTimeMillis();

            startEnemyBuffTimer();

            showGameMap();

        });



        welcomeView.show();

    }



    private void startEnemyBuffTimer() {

        if (enemyBuffTimer != null) {

            enemyBuffTimer.stop();

        }

        enemyBuffLevel = 0;

        enemyBuffTimer = new Timeline(new KeyFrame(Duration.seconds(30), event -> enemyBuffLevel++));

        enemyBuffTimer.setCycleCount(Timeline.INDEFINITE);

        enemyBuffTimer.play();

    }



    private long getElapsedTimeSeconds() {

        return (System.currentTimeMillis() - gameTime) / 1000;

    }



    private void showGameMap() {

        GameView gameView = new GameView(stage, playerName);

        gameView.enemyPosition(gameMap.getGrid());

        gameView.setOnMoveListener(direction -> handleMovement(direction, gameView));

        gameView.show();

    }



    private void handleMovement(String direction, GameView gameView) {

        int newRow = heroRow;

        int newColumn = heroColumn;



        switch (direction) {

            case "SU" -> newRow--;

            case "GIU" -> newRow++;

            case "SINISTRA" -> newColumn--;

            case "DESTRA" -> newColumn++;

        }



        if (!gameMap.isValidPosition(newRow, newColumn)) {

            return;

        }



        String destination = gameMap.getCell(newRow, newColumn);



        gameMap.setCell(heroRow, heroColumn, "");

        this.heroRow = newRow;

        this.heroColumn = newColumn;

        gameMap.setCell(heroRow, heroColumn, "Eroe");



        gameView.enemyPosition(gameMap.getGrid());



        if (destination != null && !destination.isEmpty() && !destination.equals("Eroe")) {

            if (destination.equals("Casa")) {

                handleVictory();

                return;

            }



            EnemyType enemyType = EnemyType.fromName(destination);

            if (enemyType != null) {

                startCombatSequence(enemyType);

            }

        }

    }



    private void startCombatSequence(EnemyType enemyType) {

        activeCombatController = new CombatController(stage, hero, enemyType, enemyBuffLevel);

        activeCombatController.startCombat(

                this::handleCombatVictory,

                this::handleGameOver

        );

    }



    private void handleCombatVictory() {

        defeatedEnemies++;

        if (defeatedEnemies % 3 == 0) {

            hero.applyBuffed(20, 5);

        }

        showGameMap();

    }



    private void handleGameOver() {

        stopAllTimers();

        EndView endView = new EndView(stage, playerName, getElapsedTimeSeconds(), defeatedEnemies);

        endView.setOnPlayAgainListener(this::restartGame);

        endView.setOnEndListener(stage::close);

        endView.show();

    }



    private void handleVictory() {

        stopAllTimers();

        SaveManager.salvaPartita(playerName, getElapsedTimeSeconds(), defeatedEnemies);

        WinView winView = new WinView(stage, playerName, getElapsedTimeSeconds(), defeatedEnemies);

        winView.setOnPlayAgainListener(this::restartGame);

        winView.setOnEndListener(stage::close);

        winView.show();

    }



    private void stopAllTimers() {

        if (enemyBuffTimer != null) enemyBuffTimer.stop();

        if (activeCombatController != null) activeCombatController.stopTimer();

    }



    private void restartGame() {

        this.defeatedEnemies = 0;

        this.heroRow = 0;

        this.heroColumn = 0;



        gameMap.initializeMap();

        startGame();

    }

}

