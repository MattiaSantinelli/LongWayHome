package it.unicam.cs.mpgc.rpg130324.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestoreSalvataggio {

    // Nome del file JSON creato nella radice del progetto
    private static final String FILE_PATH = "salvataggio.json";

    /**
     * Salva i dati della partita creando/sovrascrivendo il file JSON.
     */
    public static void salvaPartita(String nomeGiocatore, long tempoTrascorso, int nemiciSconfitti) {
        List <DatiSalvataggio> listaSalvataggi = caricaTuttiSalvataggi();
        listaSalvataggi.add(new DatiSalvataggio(nomeGiocatore, tempoTrascorso, nemiciSconfitti));
        // GsonBuilder con setPrettyPrinting per rendere il JSON ben formattato e leggibile
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            gson.toJson(listaSalvataggi, writer);
            System.out.println("Salvataggio JSON eseguito con successo in " + FILE_PATH);
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file JSON: " + e.getMessage());
        }
    }

    /**
     * Legge e restituisce l'intera lista di salvataggi.
     */
    public static List<DatiSalvataggio> caricaTuttiSalvataggi() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(); // Se il file non esiste, restituisce una lista vuota
        }

        Gson gson = new Gson();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            // Usa TypeToken per indicare a Gson che deve deserializzare una List<DatiSalvataggio>
            Type listType = new TypeToken<ArrayList<DatiSalvataggio>>() {}.getType();
            List<DatiSalvataggio> lista = gson.fromJson(reader, listType);
            return (lista != null) ? lista : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
