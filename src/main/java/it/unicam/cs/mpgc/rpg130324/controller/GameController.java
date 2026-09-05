package it.unicam.cs.mpgc.rpg130324.controller;

import it.unicam.cs.mpgc.rpg130324.model.entity.Eroe;
import it.unicam.cs.mpgc.rpg130324.model.entity.Nemico;
import it.unicam.cs.mpgc.rpg130324.model.persistence.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg130324.model.persistence.GestoreSalvataggio;
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
    private String nomeGiocatore; // Campo per memorizzare il nome del giocatore

    // Variabili per schermata di combattimento tra personaggi
    private Eroe eroe;
    private Timeline timerAttaccoNemico;
    private boolean inDifesa = false;

    // Timer per lo scorrere del tempo di gioco e per potenziare i nemici
    private Timeline timerPotenziamentoNemici;
    private int livelloPotenziamentoNemici = 0;

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
     * Avvia l'applicazione mostrando la prima schermata (WelcomeView).
     */
    public void avviaGioco() {
        WelcomeView welcomeView = new WelcomeView(stage);

        // Il controller ascolta l'evento del pulsante classifica
        welcomeView.setOnClassificaListener(() -> {
            Scene scenaIniziale = stage.getScene();
            List<DatiSalvataggio> listaSalvataggi = GestoreSalvataggio.caricaTuttiSalvataggi();
            ClassificaView classificaView = new ClassificaView(listaSalvataggi, () -> stage.setScene(scenaIniziale));
            stage.setScene(classificaView.getScene());
        });

        // Il controller ascolta l'evento della schermata e gestisce il passaggio di stato
        welcomeView.setOnIniziaListener(nome -> {
            this.nomeGiocatore = nome;
            this.eroe = new Eroe(nomeGiocatore);
            this.tempoPartita = System.currentTimeMillis();
            // Avvia il loop dei 30 secondi per potenziare i nemici futuri
            avviaTimerPotenziamentoNemici();
            mostraMappaDiGioco();
        });
        welcomeView.mostra();
    }

    /**
     * Incrementa il livello di difficoltà/potenziamento dei nemici ogni 30 secondi.
     */
    private void avviaTimerPotenziamentoNemici() {
        if (timerPotenziamentoNemici != null) timerPotenziamentoNemici.stop();
        livelloPotenziamentoNemici = 0;

        timerPotenziamentoNemici = new Timeline(new KeyFrame(Duration.seconds(30), event -> {
            livelloPotenziamentoNemici++;
        }));
        timerPotenziamentoNemici.setCycleCount(Timeline.INDEFINITE);
        timerPotenziamentoNemici.play();
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
        fermaTuttiITimer();
        EndView endView = new EndView(stage, nomeGiocatore, getTempoTrascorsoSecondi(), nemiciSconfitti);
        endView.setOnGiocaAncoraListener(this::riavviaPartita);
        endView.setOnFineListener(stage::close);
        endView.mostra();
    }

    /**
     * Ferma i timer attivi e mostra la schermata di WinView.
     */
    private void gestisciVittoria() {
        fermaTuttiITimer();

        // Salva i dati su file JSON
        GestoreSalvataggio.salvaPartita(nomeGiocatore, getTempoTrascorsoSecondi(), nemiciSconfitti);

        WinView winView = new WinView(stage, nomeGiocatore, getTempoTrascorsoSecondi(), nemiciSconfitti);
        winView.setOnGiocaAncoraListener(this::riavviaPartita);
        winView.setOnFineListener(stage::close);
        winView.mostra();
    }

    /**
     * Ferma tutti i timer attivi (attacco nemico e potenziamento nemici).
     */
    private void fermaTuttiITimer() {
        if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
        if (timerPotenziamentoNemici != null) timerPotenziamentoNemici.stop();
    }

    /**
     * Riavvia la partita.
     */
    private void riavviaPartita() {
        this.nemiciSconfitti = 0;
        this.eroeRiga = 0;
        this.eroeColonna = 0;

        inizializzaMappaGioco(); // Ripristina la mappa di gioco
        avviaGioco(); // Torna all'inizio
    }

    /**
     * Transizione verso la schermata di gioco.
     */
    private void mostraMappaDiGioco() {
        GameView gameView = new GameView(stage, nomeGiocatore);
        gameView.posizionaNemici(mappaGioco);
        gameView.setOnMovimentoListener(direzione -> gestisciMovimento(direzione, gameView));
        gameView.mostra();
    }

    /**
     * Calcola le nuove coordinate dell'eroe in base alla direzione e aggiorna la matrice.
     */
    private void gestisciMovimento(String direzione, GameView gameView) {
        int nuovaRiga = eroeRiga;
        int nuovaColonna = eroeColonna;

        switch (direzione) {
            case "SU" -> nuovaRiga--;
            case "GIU" -> nuovaRiga++;
            case "SINISTRA" -> nuovaColonna--;
            case "DESTRA" -> nuovaColonna++;
        }

        // Controllo dei confini della mappa (10x10)
        if (nuovaRiga < 0 || nuovaRiga >= 10 || nuovaColonna < 0 || nuovaColonna >= 10) {
            return;
        }

        String destinazione = mappaGioco[nuovaRiga][nuovaColonna];

        // Rimuove l'eroe dalla vecchia posizione della matrice
        mappaGioco[eroeRiga][eroeColonna] = "";
        // Aggiorna le coordinate dell'eroe
        this.eroeRiga = nuovaRiga;
        this.eroeColonna = nuovaColonna;
        // Inserisce l'eroe nella nuova posizione della matrice
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
     * Gestisce il combattimento tra l'eroe e i nemici.
     */
    private void avviaCombattimento(Nemico nemico, String percorsoImgNemico, double intervalloAttaccoSecondi) {
        // Applica il potenziamento basato sui blocchi da 30 secondi trascorsi
        if (livelloPotenziamentoNemici > 0) {
            int extraHp = livelloPotenziamentoNemici * 15;      // +15 HP per ogni 30s
            int extraDanno = livelloPotenziamentoNemici * 10;    // +10 Danno per ogni 30s
            nemico.potenziati(extraHp, extraDanno);
        }

        Image imgEroe = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imgEroe.png")));
        Image imgNemico = new Image(Objects.requireNonNull(getClass().getResourceAsStream(percorsoImgNemico)));

        SchermataCombattimento vistaCombattimento = new SchermataCombattimento(stage, imgEroe, imgNemico, eroe, nemico);

        // Azione ATTACCA
        vistaCombattimento.setOnAttaccaListener(() -> {
            nemico.subisciDanno(eroe.getForzaAttacco());
            vistaCombattimento.aggiornaGrafica();

            if (nemico.getHpAttuali() <= 0) {
                if (timerAttaccoNemico != null) timerAttaccoNemico.stop();
                nemiciSconfitti++;

                // POTENZIAMENTO EROE: Ogni 3 nemici sconfitti
                if (nemiciSconfitti % 3 == 0) {
                    eroe.potenziati(20, 5); // +20 HP Massimi, +5 Attacco
                }

                mostraMappaDiGioco();
            }
        });

        // Azione DIFENDI
        vistaCombattimento.setOnDifendiListener(() -> inDifesa = true);

        // Attacco automatico del nemico
        timerAttaccoNemico = new Timeline(new KeyFrame(Duration.seconds(intervalloAttaccoSecondi), event -> {
            if (nemico.getHpAttuali() > 0 && eroe.getHpAttuali() > 0) {
                if (inDifesa) {
                    inDifesa = false;
                } else {
                    eroe.subisciDanno(nemico.getForzaAttacco());
                    vistaCombattimento.aggiornaGrafica();
                    if (eroe.getHpAttuali() <= 0) {
                        gestisciGameOver();
                    }
                }
            }
        }));
        timerAttaccoNemico.setCycleCount(Timeline.INDEFINITE);
        timerAttaccoNemico.play();

        vistaCombattimento.mostra();
    }

    private void avviaCombattimentoGoblin() {
        Nemico goblin = new Nemico("Goblin", 50, 10);
        avviaCombattimento(goblin, "/imgGoblin.png", 1.0);
    }

    private void avviaCombattimentoGigante() {
        Nemico gigante = new Nemico("Gigante", 120, 20);
        avviaCombattimento(gigante, "/imgGigante.png", 1.0);
    }

    private void avviaCombattimentoStrega() {
        Nemico strega = new Nemico("Strega", 80, 30);
        avviaCombattimento(strega, "/imgStrega.png", 1.0);
    }

    private void avviaCombattimentoMago() {
        Nemico mago = new Nemico("Mago", 80, 30);
        avviaCombattimento(mago, "/imgMago.png", 1.0);
    }

    private void avviaCombattimentoDrago() {
        Nemico drago = new Nemico("Drago", 200, 40);
        avviaCombattimento(drago, "/imgDrago.png", 0.5);
    }


    /**
     * Popola la matrice logica con le stringhe corrispondenti alle posizioni
     * iniziali dei personaggi e della casa di arrivo.
     */
    private void inizializzaMappaGioco() {
        // Pulizia preliminare della matrice
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                mappaGioco[r][c] = "";
            }
        }

        // Posizioni fisse prescritte
        mappaGioco[0][0] = "Eroe";
        mappaGioco[9][9] = "Casa";
        mappaGioco[8][8] = "Mago";
        mappaGioco[8][9] = "Drago";
        mappaGioco[9][8] = "Drago";

        // Preparazione del pool totale contenente sia i 32 nemici che le 64 celle vuote
        List<String> poolElementi = new ArrayList<>();

        for (int i = 0; i < 9; i++) poolElementi.add("Goblin");
        for (int i = 0; i < 9; i++) poolElementi.add("Gigante");
        for (int i = 0; i < 7; i++) poolElementi.add("Strega");
        for (int i = 0; i < 7; i++) poolElementi.add("Mago");

        // Aggiunta delle 64 celle vuote
        for (int i = 0; i < 64; i++) {
            poolElementi.add("");
        }

        // Mescolamento casuale dell'interno pool di 96 elementi
        Collections.shuffle(poolElementi);

        // Assegnazione degli elementi rimescolati alle celle disponibili
        int indexPool = 0;
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                // Se la cella non è occupata dai ruoli fissi
                if (mappaGioco[r][c].isEmpty()) {
                    mappaGioco[r][c] = poolElementi.get(indexPool);
                    indexPool++;
                }
            }
        }
    }

    public String getNomeGiocatore() {
        return nomeGiocatore;
    }
}