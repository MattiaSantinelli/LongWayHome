package it.unicam.cs.mpgc.rpg130324.model.persistence;

public class SaveData {

    // Variabili per salvataggio su file
    private final String namePlayer;
    private final long gameTime; // Espresso in secondi
    private final int defeatedEnemies;

    public SaveData(String namePlayer, long gameTime, int defeatedEnemies) {
        this.namePlayer = namePlayer;
        this.gameTime = gameTime;
        this.defeatedEnemies = defeatedEnemies;
    }

    // Metodi getters
    public String getNamePlayer() { return namePlayer; }
    public long getGameTime() { return gameTime; }
    public int getDefeatedEnemies() { return defeatedEnemies; }
}
