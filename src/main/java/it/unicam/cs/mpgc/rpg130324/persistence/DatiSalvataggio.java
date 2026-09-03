package it.unicam.cs.mpgc.rpg130324.persistence;

public class DatiSalvataggio {

    private String nomeGiocatore;
    private long tempoTrascorso; // Espresso in secondi
    private int nemiciSconfitti;

    public DatiSalvataggio(String nomeGiocatore, long tempoTrascorso, int nemiciSconfitti) {
        this.nomeGiocatore = nomeGiocatore;
        this.tempoTrascorso = tempoTrascorso;
        this.nemiciSconfitti = nemiciSconfitti;
    }

    // Metodi getters
    public String getNomeGiocatore() { return nomeGiocatore; }
    public long getTempoTrascorso() { return tempoTrascorso; }
    public int getNemiciSconfitti() { return nemiciSconfitti; }

    // Metodi setters
    public void setNomeGiocatore(String nomeGiocatore) { this.nomeGiocatore = nomeGiocatore; }
    public void setTempoTrascorso(long tempoTrascorso) { this.tempoTrascorso = tempoTrascorso; }
    public void setNemiciSconfitti(int nemiciSconfitti) { this.nemiciSconfitti = nemiciSconfitti; }
}
