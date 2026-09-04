package it.unicam.cs.mpgc.rpg130324.view;

import it.unicam.cs.mpgc.rpg130324.persistence.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg130324.persistence.GestoreSalvataggio;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class Classifica {

    public static void mostraClassifica() {

        List<DatiSalvataggio> lista = GestoreSalvataggio.caricaTuttiSalvataggi();

        StringBuilder testoClassifica = new StringBuilder();

        if (lista.isEmpty()) {

            testoClassifica.append("Nessun salvataggio presente.");

        } else {

            int posizione = 1;
            for (DatiSalvataggio dati : lista) {
                testoClassifica.append(posizione)
                        .append(". ")
                        .append(dati.getNomeGiocatore())
                        .append(" - Nemici sconfitti: ")
                        .append(dati.getNemiciSconfitti())
                        .append(" | Tempo: ")
                        .append(dati.getTempoTrascorso())
                        .append("s\n");
                posizione++;
            }

        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Storico Giocatori");
        alert.setHeaderText("🏆 Storico delle Partite");
        alert.setContentText(testoClassifica.toString());

        alert.showAndWait();

    }
}