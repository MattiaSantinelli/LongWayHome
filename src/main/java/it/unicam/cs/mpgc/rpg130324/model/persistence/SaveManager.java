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
public class SaveManager {

    // Nome del file JSON dove verranno salvati i dati
    private static final String SAVE_FILE_PATH = "salvataggio.json";

    // Istanza di Gson configurata per formattare il JSON in modo leggibile
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Salva i dati della partita corrente aggiungendoli a quelli esistenti.
     */
    public static void saveGame(String namePlayer, long gameTime, int defeatedEnemies) {
        // Carica i salvataggi già esistenti per non sovrascriverli
        List<SaveData> saveDataList = loadAllSaves();

        // Crea il nuovo oggetto con i dati della partita attuale
        SaveData newSave = new SaveData(namePlayer, gameTime, defeatedEnemies);

        // Aggiunge il nuovo salvataggio alla lista
        saveDataList.add(newSave);

        // Ordina la classifica:
        //   - Prima per nemici sconfitti (dal più alto al più basso)
        //   - A parità di nemici, per tempo trascorso (dal più breve al più lungo)
        saveDataList.sort(Comparator
                .comparingInt(SaveData::getDefeatedEnemies).reversed()
                .thenComparingLong(SaveData::getGameTime));

        // Scrive l'intera lista aggiornata sul file JSON
        try (Writer writer = new FileWriter(SAVE_FILE_PATH)) {
            gson.toJson(saveDataList, writer);
            System.out.println("Salvataggio completato con successo in: " + SAVE_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carica tutti i salvataggi presenti nel file JSON.
     */
    public static List<SaveData> loadAllSaves() {
        File file = new File(SAVE_FILE_PATH);

        // Se il file non esiste ancora, restituisce una lista vuota
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            // Utilizziamo TypeToken per gestire la deserializzazione di un tipo generico
            Type listType = new TypeToken<ArrayList<SaveData>>() {}.getType();
            List<SaveData> list = gson.fromJson(reader, listType);

            // Se il file era vuoto, gson restituisce null; in tal caso restituiamo una lista vuota
            return (list != null) ? list : new ArrayList<>();

        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file di salvataggio: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}