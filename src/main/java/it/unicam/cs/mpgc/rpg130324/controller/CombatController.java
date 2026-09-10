package it.unicam.cs.mpgc.rpg130324.controller;

import it.unicam.cs.mpgc.rpg130324.model.entity.Enemy;
import it.unicam.cs.mpgc.rpg130324.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg130324.model.entity.Hero;
import it.unicam.cs.mpgc.rpg130324.view.CombatView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class CombatController {

    private final Stage stage;
    private final Hero hero;
    private final Enemy enemy;
    private final EnemyType enemyType;
    private Timeline enemyAttackTimer;
    private boolean isDefending = false;

    public CombatController(Stage stage, Hero hero, EnemyType enemyType, int enemyBuffLevel) {
        this.stage = stage;
        this.hero = hero;
        this.enemyType = enemyType;
        this.enemy = enemyType.createEnemy();

        if (enemyBuffLevel > 0) {
            enemy.applyBuffed(enemyBuffLevel * 15, enemyBuffLevel * 10);
        }
    }

    public void startCombat(Runnable onVictory, Runnable onGameOver) {
        // Carica le due immagini richieste dal costruttore di CombatView
        Image imgHero = null;
        Image imgEnemy = null;

        try {
            imgHero = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
            imgEnemy = new Image(Objects.requireNonNull(getClass().getResourceAsStream(enemyType.getImagePath())));
        } catch (Exception ignored) {
            // Se le risorse falliscono, l'interfaccia gestirà l'assenza dello sprite senza crashare
        }

        // Invocazione corretta del costruttore con i 5 parametri previsti
        CombatView combatView = new CombatView(stage, imgHero, imgEnemy, hero, enemy);

        combatView.setOnAttackListener(() -> {
            enemy.takeDamage(hero.getAttackPower());
            combatView.updateUI();

            if (enemy.getCurrentHp() <= 0) {
                stopTimer();
                if (onVictory != null) onVictory.run();
            }
        });

        combatView.setOnDefendListener(() -> isDefending = true);

        enemyAttackTimer = new Timeline(new KeyFrame(Duration.seconds(enemyType.getAttackIntervalSeconds()), e -> {
            if (enemy.getCurrentHp() > 0 && hero.getCurrentHp() > 0) {
                if (isDefending) {
                    isDefending = false;
                } else {
                    hero.takeDamage(enemy.getAttackPower());
                    combatView.updateUI();
                    if (hero.getCurrentHp() <= 0) {
                        stopTimer();
                        if (onGameOver != null) onGameOver.run();
                    }
                }
            }
        }));
        enemyAttackTimer.setCycleCount(Timeline.INDEFINITE);
        enemyAttackTimer.play();

        combatView.show();
    }

    public void stopTimer() {
        if (enemyAttackTimer != null) {
            enemyAttackTimer.stop();
        }
    }
}