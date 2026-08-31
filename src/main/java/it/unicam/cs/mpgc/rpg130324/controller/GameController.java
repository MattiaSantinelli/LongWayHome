package it.unicam.cs.mpgc.rpg130324.controller;

import it.unicam.cs.mpgc.rpg130324.model.Eroe;
import it.unicam.cs.mpgc.rpg130324.model.Nemico;
import it.unicam.cs.mpgc.rpg130324.view.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class GameController {

    private final Stage stage;
    private String nomeGiocatore;

    private Eroe eroe;
    private Timeline timerAttaccoNemico;
    private boolean inDifesa = false;

    public GameController(Stage stage) {
        this.stage = stage;
    }

    public void avviaGioco() {
        WelcomeView welcomeView = new WelcomeView(stage);

        welcomeView.setOnIniziaListener(nome -> {
            this.nomeGiocatore = nome;
            this.eroe = new Eroe(nomeGiocatore);
            avviaCombattimentoDrago(); // Carichiamo la mappa di gioco
        });

        welcomeView.mostra();
    }

    /**
     * Logica di combattimento per la schermata dei goblin.
     * Verrà richiamata in seguito.
     * */
    private void avviaCombattimentoGoblin() {
        Nemico goblin = new Nemico("Goblin", 50, 10);

        Image imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgGoblin = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgGoblin.png")));

        SchermataCombattimentoGoblin vistaGoblin = new SchermataCombattimentoGoblin(stage, imgEroe, imgGoblin, eroe, goblin);

        // Azione ATTACCA
        vistaGoblin.setOnAttaccaListener(() -> {
            goblin.subisciDanno(eroe.getForzaAttacco());
            vistaGoblin.aggiornaGrafica();

            if (goblin.getHpAttuali() <= 0) {
                if (timerAttaccoNemico != null) {
                    timerAttaccoNemico.stop();
                }
                System.out.println("VITTORIA! Ritorno alla mappa di gioco...");
                mostraMappaDiGioco(); // <--- Ritorna alla mappa di gioco
            }
        });

        // Azione DIFENDI
        vistaGoblin.setOnDifendiListener(() -> {
            inDifesa = true;
            System.out.println("Ti stai difendendo!");
        });

        // Attacco automatico del Goblin ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (goblin.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    System.out.println("Danno bloccato!");
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(goblin.getForzaAttacco());
                    vistaGoblin.aggiornaGrafica();

                    if (eroe.getHpAttuali() <= 0) {
                        System.out.println("GAME OVER!");
                        timerAttaccoNemico.stop();
                    }
                }
            }
        }));

        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaGoblin.mostra();
    }

    /**
     * Logica di combattimento per la schermata dei giganti.
     * Verrà richiamata in seguito.
     * */
    private void avviaCombattimentoGigante() {
        Nemico gigante = new Nemico("Gigante", 120, 20);

        Image imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgGigante = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgGigante.png")));

        SchermataCombattimentoGigante vistaGigante = new SchermataCombattimentoGigante(stage, imgEroe, imgGigante, eroe, gigante);

        // Azione ATTACCA
        vistaGigante.setOnAttaccaListener(() -> {
            gigante.subisciDanno(eroe.getForzaAttacco());
            vistaGigante.aggiornaGrafica();

            if (gigante.getHpAttuali() <= 0) {
                if (timerAttaccoNemico != null) {
                    timerAttaccoNemico.stop();
                }
                System.out.println("VITTORIA! Ritorno alla mappa di gioco...");
                mostraMappaDiGioco(); // <--- Ritorna alla mappa di gioco
            }
        });

        // Azione DIFENDI
        vistaGigante.setOnDifendiListener(() -> {
            inDifesa = true;
            System.out.println("Ti stai difendendo!");
        });

        // Attacco automatico del Gigante ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (gigante.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    System.out.println("Danno bloccato!");
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(gigante.getForzaAttacco());
                    vistaGigante.aggiornaGrafica();

                    if (eroe.getHpAttuali() <= 0) {
                        System.out.println("GAME OVER!");
                        timerAttaccoNemico.stop();
                    }
                }
            }
        }));

        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaGigante.mostra();
    }

    /**
     * Logica di combattimento per la schermata delle streghe.
     * Verrà richiamata in seguito.
     * */
    private void avviaCombattimentoStrega() {
        Nemico strega = new Nemico("Strega", 80, 30);

        Image imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgStrega = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgStrega.png")));

        SchermataCombattimentoStrega vistaStrega = new SchermataCombattimentoStrega(stage, imgEroe, imgStrega, eroe, strega);

        // Azione ATTACCA
        vistaStrega.setOnAttaccaListener(() -> {
            strega.subisciDanno(eroe.getForzaAttacco());
            vistaStrega.aggiornaGrafica();

            if (strega.getHpAttuali() <= 0) {
                if (timerAttaccoNemico != null) {
                    timerAttaccoNemico.stop();
                }
                System.out.println("VITTORIA! Ritorno alla mappa di gioco...");
                mostraMappaDiGioco(); // <--- Ritorna alla mappa di gioco
            }
        });

        // Azione DIFENDI
        vistaStrega.setOnDifendiListener(() -> {
            inDifesa = true;
            System.out.println("Ti stai difendendo!");
        });

        // Attacco automatico della Strega ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (strega.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    System.out.println("Danno bloccato!");
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(strega.getForzaAttacco());
                    vistaStrega.aggiornaGrafica();

                    if (eroe.getHpAttuali() <= 0) {
                        System.out.println("GAME OVER!");
                        timerAttaccoNemico.stop();
                    }
                }
            }
        }));

        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaStrega.mostra();
    }

    /**
     * Logica di combattimento per la schermata dei maghi.
     * Verrà richiamata in seguito.
     * */
    private void avviaCombattimentoMago() {
        Nemico mago = new Nemico("Mago", 80, 30);

        Image imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgMago = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgMago.png")));

        SchermataCombattimentoMago vistaMago = new SchermataCombattimentoMago(stage, imgEroe, imgMago, eroe, mago);

        // Azione ATTACCA
        vistaMago.setOnAttaccaListener(() -> {
            mago.subisciDanno(eroe.getForzaAttacco());
            vistaMago.aggiornaGrafica();

            if (mago.getHpAttuali() <= 0) {
                if (timerAttaccoNemico != null) {
                    timerAttaccoNemico.stop();
                }
                System.out.println("VITTORIA! Ritorno alla mappa di gioco...");
                mostraMappaDiGioco(); // <--- Ritorna alla mappa di gioco
            }
        });

        // Azione DIFENDI
        vistaMago.setOnDifendiListener(() -> {
            inDifesa = true;
            System.out.println("Ti stai difendendo!");
        });

        // Attacco automatico del Mago ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (mago.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    System.out.println("Danno bloccato!");
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(mago.getForzaAttacco());
                    vistaMago.aggiornaGrafica();

                    if (eroe.getHpAttuali() <= 0) {
                        System.out.println("GAME OVER!");
                        timerAttaccoNemico.stop();
                    }
                }
            }
        }));

        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaMago.mostra();
    }

    /**
     * Logica di combattimento per la schermata dei draghi.
     * Verrà richiamata in seguito.
     * */
    private void avviaCombattimentoDrago() {
        Nemico drago = new Nemico("Drago", 200, 40);

        Image imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgDrago = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgDrago.png")));

        SchermataCombattimentoDrago vistaDrago = new SchermataCombattimentoDrago(stage, imgEroe, imgDrago, eroe, drago);

        // Azione ATTACCA
        vistaDrago.setOnAttaccaListener(() -> {
            drago.subisciDanno(eroe.getForzaAttacco());
            vistaDrago.aggiornaGrafica();

            if (drago.getHpAttuali() <= 0) {
                if (timerAttaccoNemico != null) {
                    timerAttaccoNemico.stop();
                }
                System.out.println("VITTORIA! Ritorno alla mappa di gioco...");
                mostraMappaDiGioco(); // <--- Ritorna alla mappa di gioco
            }
        });

        // Azione DIFENDI
        vistaDrago.setOnDifendiListener(() -> {
            inDifesa = true;
            System.out.println("Ti stai difendendo!");
        });

        // Attacco automatico del Drago ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (drago.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    System.out.println("Danno bloccato!");
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(drago.getForzaAttacco());
                    vistaDrago.aggiornaGrafica();

                    if (eroe.getHpAttuali() <= 0) {
                        System.out.println("GAME OVER!");
                        timerAttaccoNemico.stop();
                    }
                }
            }
        }));

        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaDrago.mostra();
    }

    /**
     * Transizione verso la schermata di gioco (mappa)
     */
    private void mostraMappaDiGioco() {
        GameView gameView = new GameView(stage, nomeGiocatore);
        gameView.mostra();
    }

    public String getNomeGiocatore() {
        return nomeGiocatore;
    }
}