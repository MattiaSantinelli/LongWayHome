package it.unicam.cs.mpgc.rpg130324.model.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Gestisce la persistenza dei dati di gioco (salvataggio e caricamento)
 * utilizzando un file JSON situato nella root del progetto.
 */
public class GestoreSalvataggio {

    // Nome del file JSON dove verranno salvati i dati
    private static final String FILE_SALVATAGGIO = "salvataggio.json";

    // Istanza di Gson configurata per formattare il JSON in modo leggibile (Pretty Printing)
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Salva i dati della partita corrente aggiungendoli a quelli esistenti.
     *
     * @param nomeGiocatore   il nome inserito dall'utente
     * @param tempoTrascorso  il tempo giocato in secondi
     * @param nemiciSconfitti il numero totale di nemici sconfitti
     */
    public static void salvaPartita(String nomeGiocatore, long tempoTrascorso, int nemiciSconfitti) {
        // 1. Carica i salvataggi già esistenti per non sovrascriverli
        List<DatiSalvataggio> listaSalvataggi = caricaTuttiSalvataggi();

        // 2. Crea il nuovo oggetto con i dati della partita attuale
        DatiSalvataggio nuovoSalvataggio = new DatiSalvataggio(nomeGiocatore, tempoTrascorso, nemiciSconfitti);

        // 3. Aggiunge il nuovo salvataggio alla lista
        listaSalvataggi.add(nuovoSalvataggio);

        // 4. Ordina la classifica:
        //    - Prima per nemici sconfitti (dal più alto al più basso)
        //    - A parità di nemici, per tempo trascorso (dal più breve al più lungo)
        listaSalvataggi.sort(Comparator
                .comparingInt(DatiSalvataggio::getNemiciSconfitti).reversed()
                .thenComparingLong(DatiSalvataggio::getTempoTrascorso));

        // 5. Scrive l'intera lista aggiornata sul file JSON
        try (Writer writer = new FileWriter(FILE_SALVATAGGIO)) {
            gson.toJson(listaSalvataggi, writer);
            System.out.println("Salvataggio completato con successo in: " + FILE_SALVATAGGIO);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carica tutti i salvataggi presenti nel file JSON.
     *
     * @return una List di DatiSalvataggio, oppure una lista vuota se il file non esiste o è vuoto.
     */
    public static List<DatiSalvataggio> caricaTuttiSalvataggi() {
        File file = new File(FILE_SALVATAGGIO);

        // Se il file non esiste ancora (es. prima partita in assoluto), restituisce una lista vuota
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            // Utilizziamo TypeToken per gestire la deserializzazione di un tipo generico (List<DatiSalvataggio>)
            Type tipoLista = new TypeToken<ArrayList<DatiSalvataggio>>() {}.getType();
            List<DatiSalvataggio> lista = gson.fromJson(reader, tipoLista);

            // Se il file era vuoto, gson restituisce null; in tal caso restituiamo una lista vuota
            return (lista != null) ? lista : new ArrayList<>();

        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file di salvataggio: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}