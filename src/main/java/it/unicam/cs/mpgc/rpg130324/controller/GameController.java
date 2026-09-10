package it.unicam.cs.mpgc.rpg130324.controller;

import it.unicam.cs.mpgc.rpg130324.model.GameMap;
import it.unicam.cs.mpgc.rpg130324.model.entity.CellType;
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
            LeaderboardView leaderboardViewView = new LeaderboardView(savedGames, () -> stage.setScene(initialScene));
            stage.setScene(leaderboardViewView.getScene());
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
        // La logica di verifica e spostamento è ora incapsulata in GameMap (Model)
        CellType destination = gameMap.moveHero(direction);

        if (destination == null) {
            return; // Movimento non valido/muro
        }

        gameView.enemyPosition(gameMap.getGrid());

        if (destination == CellType.HOUSE) {
            handleVictory();
            return;
        }

        EnemyType enemyType = EnemyType.fromName(destination.name());
        if (enemyType != null) {
            startCombatSequence(enemyType);
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
        SaveManager.saveGame(playerName, getElapsedTimeSeconds(), defeatedEnemies);
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
        gameMap.initializeMap();
        startGame();
    }
}