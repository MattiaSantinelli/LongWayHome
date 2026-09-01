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
    private String nomeGiocatore; // Campo per memorizzare il nome del giocatore

    // Variabili per schermate di combattimento tra personaggi
    private Eroe eroe;
    private Timeline timerAttaccoNemico;
    private boolean inDifesa = false;

    // Variabili per creazione mappa da gioco
    private int eroeRiga = 0;
    private int eroeColonna = 0;
    private final String[][] mappaGioco = new String[10][10];

    // Variabili per statistiche della partita
    private long tempoPartita;
    private int nemiciSconfitti = 0;

    public GameController(Stage stage) {
        this.stage = stage;
        inizializzaMappaGioco();
    }

    /**
     * Avvia l'applicazione mostrando la prima schermata (WelcomeView)
     **/
    public void avviaGioco() {
        WelcomeView welcomeView = new WelcomeView(stage);
        // Il controller ascolta l'evento della schermata e gestisce il passaggio di stato
        welcomeView.setOnIniziaListener(nome -> {
            this.nomeGiocatore = nome;
            this.eroe = new Eroe(nomeGiocatore);
            this.tempoPartita = System.currentTimeMillis(); // Avvia il cronometro
            mostraMappaDiGioco();
        });

        welcomeView.mostra();
    }

    /**
     * Calcola i secondi trascorsi dall'inizio della partita.
     */
    private long getTempoTrascorsoSecondi() {
        return (System.currentTimeMillis() - tempoPartita) / 1000;
    }

    /**
    * Ferma i timer attivi e mostra la schermata di EndView.
    */
    private void gestisciGameOver() {
        if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
        EndView endView = new EndView(stage, nomeGiocatore, getTempoTrascorsoSecondi(), nemiciSconfitti);
        endView.setOnGiocaAncoraListener(() ->{
            // Riavvio della partita
            this.riavviaPartita();
        });
        endView.setOnFineListener(() -> {
            // Chiudo il gioco
            stage.close();
        });
        endView.mostra();
    }

    /**
     * Ferma i timer attivi e mostra la schermata di WinView.
     */
    private void gestisciVittoria() {
        if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
        WinView winView = new WinView(stage, nomeGiocatore, getTempoTrascorsoSecondi(), nemiciSconfitti);
        winView.setOnGiocaAncoraListener(() ->{
            // Riavvio della partita
            this.riavviaPartita();
        });
        winView.setOnFineListener(() -> {
            // Chiudo il gioco
            stage.close();
        });
        winView.mostra();
    }

    private void riavviaPartita() {
        this.nemiciSconfitti = 0;
        this.eroeRiga = 0;
        this.eroeColonna = 0;

        inizializzaMappaGioco(); // Ripristina la mappa
        avviaGioco();            // Torna all'inizio
    }

    /**
     * Transizione verso la schermata di gioco (mappa)
     **/
    private void mostraMappaDiGioco() {
        GameView gameView = new GameView(stage, nomeGiocatore);
        gameView.posizionaNemici(mappaGioco);
        gameView.setOnMovimentoListener(direzione -> gestisciMovimento(direzione, gameView));
        gameView.mostra();
    }

    /**
     * Calcola le nuove coordinate dell'eroe in base alla direzione e aggiorna la matrice.
     **/
    private void gestisciMovimento(String direzione, GameView gameView) {
        int nuovaRiga = eroeRiga;
        int nuovaColonna = eroeColonna;

        switch (direzione) {
            case "SU" -> nuovaRiga--;
            case "GIU" -> nuovaRiga++;
            case "SINISTRA" -> nuovaColonna--;
            case "DESTRA" -> nuovaColonna++;
        }

        // Controllo dei confini della mappa
        if (nuovaRiga < 0 || nuovaRiga >= 10 || nuovaColonna < 0 || nuovaColonna >= 10) {
            return;
        }

        String destinazione = mappaGioco[nuovaRiga][nuovaColonna];

        // Rimuove l'Eroe dalla vecchia posizione nella matrice
        mappaGioco[eroeRiga][eroeColonna] = "";

        // Aggiorna le coordinate dell'eroe
        this.eroeRiga = nuovaRiga;
        this.eroeColonna = nuovaColonna;

        // Inserisce l'Eroe nella nuova posizione nella matrice
        mappaGioco[eroeRiga][eroeColonna] = "Eroe";

        // Aggiorna la vista della mappa
        gameView.posizionaNemici(mappaGioco);

        // Se nella casella di arrivo c'era un nemico o la casa, gestisce l'evento
        if (destinazione != null && !destinazione.isEmpty() && !destinazione.equals("Eroe")) {
            switch (destinazione) {
                case "Goblin" -> avviaCombattimentoGoblin();
                case "Gigante" -> avviaCombattimentoGigante();
                case "Strega" -> avviaCombattimentoStrega();
                case "Mago" -> avviaCombattimentoMago();
                case "Drago" -> avviaCombattimentoDrago();
                case "Casa" -> gestisciVittoria();
            }
        }
    }

    //------------------------------------------------------------
    //                 GESTIONE COMBATTIMENTI
    //------------------------------------------------------------
    /**
     * Gestisce il combattimento tra l'eroe e il goblin.
     */
    private void avviaCombattimentoGoblin() {
        Nemico goblin = new Nemico("Goblin", 50, 10);
        Image imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgGoblin = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgGoblin.png")));

        SchermataCombattimentoGoblin vistaGoblin = new SchermataCombattimentoGoblin(stage, imgEroe, imgGoblin, eroe, goblin);

        //Azione ATTACCA
        vistaGoblin.setOnAttaccaListener(() -> {
            goblin.subisciDanno(eroe.getForzaAttacco());
            vistaGoblin.aggiornaGrafica();

            if (goblin.getHpAttuali() <= 0) {
                if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
                nemiciSconfitti++;
                // Alla vittoria il nemico è sparito e l'eroe occupa la casella
                mostraMappaDiGioco();
            }
        });

        // Azione DIFENDI
        vistaGoblin.setOnDifendiListener(() -> inDifesa = true);

        // Attacco automatico del Goblin ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (goblin.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(goblin.getForzaAttacco());
                    vistaGoblin.aggiornaGrafica();
                    if (eroe.getHpAttuali() <= 0) {
                        timerAttaccoNemico.stop();
                        gestisciGameOver();
                    }
                }
            }
        }));
        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaGoblin.mostra();
    }

    /**
     * Gestisce il combattimento tra l'eroe e il gigante.
     */
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
                if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
                nemiciSconfitti++;
                mostraMappaDiGioco();
            }
        });

        // Azione DIFENDI
        vistaGigante.setOnDifendiListener(() -> inDifesa = true);

        // Attacco automatico del Gigante ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (gigante.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(gigante.getForzaAttacco());
                    vistaGigante.aggiornaGrafica();
                    if (eroe.getHpAttuali() <= 0) {
                        timerAttaccoNemico.stop();
                        gestisciGameOver();
                    }
                }
            }
        }));
        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaGigante.mostra();
    }

    /**
     * Gestisce il combattimento tra l'eroe e la strega.
     */
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
                if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
                nemiciSconfitti++;
                mostraMappaDiGioco();
            }
        });

        //Azione DIFENDI
        vistaStrega.setOnDifendiListener(() -> inDifesa = true);

        // Attacco automatico della Strega ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (strega.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(strega.getForzaAttacco());
                    vistaStrega.aggiornaGrafica();
                    if (eroe.getHpAttuali() <= 0) {
                        timerAttaccoNemico.stop();
                        gestisciGameOver();
                    }
                }
            }
        }));
        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaStrega.mostra();
    }

    /**
     * Gestisce il combattimento tra l'eroe e il mago.
     */
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
                if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
                nemiciSconfitti++;
                mostraMappaDiGioco();
            }
        });

        //Azione DIFENDI
        vistaMago.setOnDifendiListener(() -> inDifesa = true);

        //Attacco automatico del Mago ogni secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (mago.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(mago.getForzaAttacco());
                    vistaMago.aggiornaGrafica();
                    if (eroe.getHpAttuali() <= 0) {
                        timerAttaccoNemico.stop();
                        gestisciGameOver();
                    }
                }
            }
        }));
        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaMago.mostra();
    }

    /**
     * Gestisce il combattimento tra l'eroe e il drago.
     */
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
                if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
                nemiciSconfitti++;
                mostraMappaDiGioco();
            }
        });

        // Azione DIFENDI
        vistaDrago.setOnDifendiListener(() -> inDifesa = true);

        // Attacco automatico del Drago ogni mezzo secondo
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (drago.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(drago.getForzaAttacco());
                    vistaDrago.aggiornaGrafica();
                    if (eroe.getHpAttuali() <= 0) {
                        timerAttaccoNemico.stop();
                        gestisciGameOver();
                    }
                }
            }
        }));
        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaDrago.mostra();
    }

    /**
     * Popola la matrice logica con le stringhe corrispondenti alle posizioni
     * iniziali dei personaggi e della casa di arrivo.
     */
    private void inizializzaMappaGioco() {
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                mappaGioco[r][c] = "";
            }
        }

        // Posizione iniziale dell'Eroe
        mappaGioco[0][0] = "Eroe";

        // GOBLIN
        mappaGioco[0][5] = "Goblin";
        mappaGioco[1][2] = "Goblin";
        mappaGioco[1][8] = "Goblin";
        mappaGioco[2][0] = "Goblin";
        mappaGioco[2][5] = "Goblin";
        mappaGioco[3][1] = "Goblin";
        mappaGioco[3][8] = "Goblin";
        mappaGioco[4][6] = "Goblin";
        mappaGioco[5][3] = "Goblin";
        mappaGioco[7][5] = "Goblin";

        // GIGANTE
        mappaGioco[0][3] = "Gigante";
        mappaGioco[1][6] = "Gigante";
        mappaGioco[2][2] = "Gigante";
        mappaGioco[2][4] = "Gigante";
        mappaGioco[4][0] = "Gigante";
        mappaGioco[6][1] = "Gigante";
        mappaGioco[7][0] = "Gigante";
        mappaGioco[8][2] = "Gigante";
        mappaGioco[9][6] = "Gigante";

        // STREGA
        mappaGioco[2][9] = "Strega";
        mappaGioco[3][3] = "Strega";
        mappaGioco[5][5] = "Strega";
        mappaGioco[7][1] = "Strega";
        mappaGioco[7][3] = "Strega";
        mappaGioco[7][7] = "Strega";
        mappaGioco[8][5] = "Strega";

        // MAGO
        mappaGioco[5][2] = "Mago";
        mappaGioco[5][8] = "Mago";
        mappaGioco[6][6] = "Mago";
        mappaGioco[8][8] = "Mago";
        mappaGioco[9][0] = "Mago";
        mappaGioco[9][3] = "Mago";

        // DRAGO
        mappaGioco[8][9] = "Drago";
        mappaGioco[9][8] = "Drago";

        // CASA
        mappaGioco[9][9] = "Casa";
    }

    public String getNomeGiocatore() {
        return nomeGiocatore;
    }
}