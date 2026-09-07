package it.unicam.cs.mpgc.rpg130324.controller;

import it.unicam.cs.mpgc.rpg130324.model.entity.Hero;
import it.unicam.cs.mpgc.rpg130324.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg130324.model.persistence.SaveData;
import it.unicam.cs.mpgc.rpg130324.model.persistence.SaveManager;
import it.unicam.cs.mpgc.rpg130324.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    private final Stage stage;
    private String playerName; // Campo per memorizzare il nome del giocatore

    // Variabili per schermata di combattimento tra personaggi
    private Hero hero;
    private Timeline enemyAttackTimer;
    private boolean isDefending = false;

    // Timer per lo scorrere del tempo di gioco e per potenziare i nemici
    private Timeline enemyBuffTimer;
    private int enemyBuffLevel = 0;

    // Variabili per creazione mappa da gioco
    private int heroRow = 0;
    private int heroColumn = 0;
    private final String[][] gameMap = new String[10][10];

    // Variabili per statistiche della partita
    private long gameTime;
    private int defeatedEnemies = 0;

    public GameController(Stage stage) {
        this.stage = stage;
        initializeGameMap();
    }

    /**
     * Avvia l'applicazione mostrando la prima schermata (WelcomeView).
     */
    public void startGame() {
        WelcomeView welcomeView = new WelcomeView(stage);

        // Il controller ascolta l'evento del pulsante classifica
        welcomeView.setOnLeaderboardListener(() -> {
            Scene initialScene = stage.getScene();
            List<SaveData> savedGames = SaveManager.loadAllSaves();
            LeaderboardView classificaView = new LeaderboardView(savedGames, () -> stage.setScene(initialScene));
            stage.setScene(classificaView.getScene());
        });

        // Il controller ascolta l'evento della schermata e gestisce il passaggio di stato
        welcomeView.setOnStartListener(name -> {
            this.playerName = name;
            this.hero = new Hero(playerName);
            this.gameTime = System.currentTimeMillis();
            // Avvia il loop dei 30 secondi per potenziare i nemici futuri
            startEnemyBuffTimer();
            showGameMap();
        });
        welcomeView.show();
    }

    /**
     * Incrementa il livello di difficoltà/potenziamento dei nemici ogni 30 secondi.
     */
    private void startEnemyBuffTimer() {
        if (enemyBuffTimer != null) {
            enemyBuffTimer.stop();
        }
        enemyBuffLevel = 0;

        enemyBuffTimer = new Timeline(new KeyFrame(Duration.seconds(30), event -> {
            enemyBuffLevel++;
        }));
        enemyBuffTimer.setCycleCount(Timeline.INDEFINITE);
        enemyBuffTimer.play();
    }

    /**
     * Calcola i secondi trascorsi dall'inizio della partita.
     */
    private long getElapsedTimeSeconds() {
        return (System.currentTimeMillis() - gameTime) / 1000;
    }

    /**
     * Ferma i timer attivi e mostra la schermata di EndView.
     */
    private void handleGameOver() {
        stopAllTimers();
        EndView endView = new EndView(stage, playerName, getElapsedTimeSeconds(), defeatedEnemies);
        endView.setOnPlayAgainListener(this::restartGame);
        endView.setOnEndListener(stage::close);
        endView.show();
    }

    /**
     * Ferma i timer attivi e mostra la schermata di WinView.
     */
    private void handleVictory() {
        stopAllTimers();

        // Salva i dati su file JSON
        SaveManager.salvaPartita(playerName, getElapsedTimeSeconds(), defeatedEnemies);

        WinView winView = new WinView(stage, playerName, getElapsedTimeSeconds(), defeatedEnemies);
        winView.setOnPlayAgainListener(this::restartGame);
        winView.setOnEndListener(stage::close);
        winView.show();
    }

    /**
     * Ferma tutti i timer attivi (attacco nemico e potenziamento nemici).
     */
    private void stopAllTimers() {
        if (enemyAttackTimer != null) enemyAttackTimer.stop();
        if (enemyBuffTimer != null) enemyBuffTimer.stop();
    }

    /**
     * Riavvia la partita.
     */
    private void restartGame() {
        this.defeatedEnemies = 0;
        this.heroRow = 0;
        this.heroColumn = 0;

        initializeGameMap(); // Ripristina la mappa di gioco
        startGame(); // Torna all'inizio
    }

    /**
     * Transizione verso la schermata di gioco.
     */
    private void showGameMap() {
        GameView gameView = new GameView(stage, playerName);
        gameView.enemyPosition(gameMap);
        gameView.setOnMoveListener(direction -> handleMovement(direction, gameView));
        gameView.show();
    }

    /**
     * Calcola le nuove coordinate dell'eroe in base alla direzione e aggiorna la matrice.
     */
    private void handleMovement(String direction, GameView gameView) {
        int newRow = heroRow;
        int newColumn = heroColumn;

        switch (direction) {
            case "SU" -> newRow--;
            case "GIU" -> newRow++;
            case "SINISTRA" -> newColumn--;
            case "DESTRA" -> newColumn++;
        }

        // Controllo dei confini della mappa (10x10)
        if (newRow < 0 || newRow >= 10 || newColumn < 0 || newColumn >= 10) {
            return;
        }

        String destination = gameMap[newRow][newColumn];

        // Rimuove l'eroe dalla vecchia posizione della matrice
        gameMap[heroRow][heroColumn] = "";
        // Aggiorna le coordinate dell'eroe
        this.heroRow = newRow;
        this.heroColumn = newColumn;
        // Inserisce l'eroe nella nuova posizione della matrice
        gameMap[heroRow][heroColumn] = "Eroe";
        // Aggiorna la vista della mappa
        gameView.enemyPosition(gameMap);

        // Se nella casella di arrivo c'era un nemico o la casa, gestisce l'evento
        if (destination != null && !destination.isEmpty() && !destination.equals("Eroe")) {
            switch (destination) {
                case "Goblin" -> startGoblinCombat();
                case "Gigante" -> startGiantCombat();
                case "Strega" -> startWitchCombat();
                case "Mago" -> startWizardCombat();
                case "Drago" -> startDragonCombat();
                case "Casa" -> handleVictory();
            }
        }
    }

    //------------------------------------------------------------
    //                 GESTIONE COMBATTIMENTI
    //------------------------------------------------------------

    /**
     * Gestisce il combattimento tra l'eroe e i nemici.
     */
    private void startCombat(Enemy enemy, String enemyImagePath, double attackIntervalSeconds) {
        // Applica il potenziamento basato sui blocchi da 30 secondi trascorsi
        if (enemyBuffLevel > 0) {
            int extraHp = enemyBuffLevel * 15;      // +15 HP per ogni 30s
            int extraDanno = enemyBuffLevel * 10;    // +10 Danno per ogni 30s
            enemy.isBuffed(extraHp, extraDanno);
        }

        Image imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgEnemy = new Image(Objects.requireNonNull(getClass().getResourceAsStream(enemyImagePath)));

        CombatView combatView = new CombatView(stage, imgHero, imgEnemy, hero, enemy);

        // Azione ATTACCA
        combatView.setOnAttackListener(() -> {
            enemy.takeDamage(hero.getAttackPower());
            combatView.updateUI();

            if (enemy.getCurrentHp() <= 0) {
                if (enemyAttackTimer != null) {
                    enemyAttackTimer.stop();
                }
                defeatedEnemies++;

                // POTENZIAMENTO EROE: Ogni 3 nemici sconfitti
                if (defeatedEnemies % 3 == 0) {
                    hero.isBuffed(20, 5); // +20 HP Massimi, +5 Attacco
                }

                showGameMap();
            }
        });

        // Azione DIFENDI
        combatView.setOnDefendListener(() -> isDefending = true);

        // Attacco automatico del nemico
        enemyAttackTimer = new Timeline(new KeyFrame(Duration.seconds(attackIntervalSeconds), event -> {
            if (enemy.getCurrentHp() > 0 && hero.getCurrentHp() > 0) {
                if (isDefending) {
                    isDefending = false;
                } else {
                    hero.takeDamage(enemy.getAttackPower());
                    combatView.updateUI();
                    if (hero.getCurrentHp() <= 0) {
                        handleGameOver();
                    }
                }
            }
        }));
        enemyAttackTimer.setCycleCount(Timeline.INDEFINITE);
        enemyAttackTimer.play();

        combatView.show();
    }

    private void startGoblinCombat() {
        Enemy goblin = new Enemy("Goblin", 50, 10);
        startCombat(goblin, "/imgGoblin.png", 1.0);
    }

    private void startGiantCombat() {
        Enemy gigante = new Enemy("Gigante", 120, 20);
        startCombat(gigante, "/imgGigante.png", 1.0);
    }

    private void startWitchCombat() {
        Enemy strega = new Enemy("Strega", 80, 30);
        startCombat(strega, "/imgStrega.png", 1.0);
    }

    private void startWizardCombat() {
        Enemy mago = new Enemy("Mago", 80, 30);
        startCombat(mago, "/imgMago.png", 1.0);
    }

    private void startDragonCombat() {
        Enemy drago = new Enemy("Drago", 200, 40);
        startCombat(drago, "/imgDrago.png", 0.5);
    }


    /**
     * Popola la matrice logica con le stringhe corrispondenti alle posizioni
     * iniziali dei personaggi e della casa di arrivo.
     */
    private void initializeGameMap() {
        // Pulizia preliminare della matrice
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                gameMap[r][c] = "";
            }
        }

        // Posizioni fisse prescritte
        gameMap[0][0] = "Eroe";
        gameMap[9][9] = "Casa";
        gameMap[8][8] = "Mago";
        gameMap[8][9] = "Drago";
        gameMap[9][8] = "Drago";

        // Preparazione del pool totale contenente sia i 32 nemici che le 64 celle vuote
        List<String> elementPool = new ArrayList<>();

        for (int i = 0; i < 9; i++) elementPool.add("Goblin");
        for (int i = 0; i < 9; i++) elementPool.add("Gigante");
        for (int i = 0; i < 7; i++) elementPool.add("Strega");
        for (int i = 0; i < 7; i++) elementPool.add("Mago");

        // Aggiunta delle 64 celle vuote
        for (int i = 0; i < 64; i++) {
            elementPool.add("");
        }

        // Mescolamento casuale dell'interno pool di 96 elementi
        Collections.shuffle(elementPool);

        // Assegnazione degli elementi rimescolati alle celle disponibili
        int indexPool = 0;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                // Se la cella non è occupata dai ruoli fissi
                if (gameMap[r][c].isEmpty()) {
                    gameMap[r][c] = elementPool.get(indexPool);
                    indexPool++;
                }
            }
        }
    }

    public String getPlayerName() {
        return playerName;
    }
}