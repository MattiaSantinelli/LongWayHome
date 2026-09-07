package it.unicam.cs.mpgc.rpg130324.model.persistence;

public class SaveData {

    private String namePlayer;
    private long gameTime; // Espresso in secondi
    private int defeatedEnemies;

    public SaveData(String namePlayer, long gameTime, int defeatedEnemies) {
        this.namePlayer = namePlayer;
        this.gameTime = gameTime;
        this.defeatedEnemies = defeatedEnemies;
    }

    // Metodi getters
    public String getNamePlayer() { return namePlayer; }
    public long getGameTime() { return gameTime; }
    public int getdefeatedEnemies() { return defeatedEnemies; }

    // Metodi setters
    public void setNamePlayer(String namePlayer) { this.namePlayer = namePlayer; }
    public void setGameTime(long gameTime) { this.gameTime = gameTime; }
    public void setdefeatedEnemies(int defeatedEnemies) { this.defeatedEnemies = defeatedEnemies; }
}
