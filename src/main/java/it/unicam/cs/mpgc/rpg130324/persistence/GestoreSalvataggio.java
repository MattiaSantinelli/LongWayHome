package it.unicam.cs.mpgc.rpg130324.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestoreSalvataggio {

    // Nome del file JSON creato nella radice del progetto
    private static final String FILE_PATH = "salvataggio.json";

    /**
     * Salva i dati della partita creando/sovrascrivendo il file JSON.
     */
    public static void salvaPartita(String nomeGiocatore, long tempoTrascorso, int nemiciSconfitti) {
        DatiSalvataggio dati = new DatiSalvataggio(nomeGiocatore, tempoTrascorso, nemiciSconfitti);

        // GsonBuilder con setPrettyPrinting per rendere il JSON ben formattato e leggibile
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            gson.toJson(dati, writer);
            System.out.println("Salvataggio JSON eseguito con successo in " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file JSON: " + e.getMessage());
        }
    }

    /**
     * Carica i dati dal file salvataggio.json se presente.
     */
    public static DatiSalvataggio caricaPartita() {
        Gson gson = new Gson();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            return gson.fromJson(reader, DatiSalvataggio.class);
        } catch (IOException e) {
            System.err.println("Impossibile caricare il file JSON o file non trovato: " + e.getMessage());
            return null;
        }
    }
}
