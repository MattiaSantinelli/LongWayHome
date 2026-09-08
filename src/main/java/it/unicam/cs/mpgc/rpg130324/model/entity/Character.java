package it.unicam.cs.mpgc.rpg130324.model.entity;

public abstract class Character {

    private final String name;
    private int currentHp;
    private int maxHp;
    private int attackPower;

    public Character(String name, int maxHp, int attackPower) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attackPower = attackPower;
    }

    /**
     * Riduce i punti vita dei personaggi in base ai danni subiti.
     */
    public void takeDamage(int damage){
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    /**
     * Metodo per il potenziamento dei punti vita/danno dei personaggi.
     */
    public void isBuffed(int hpBoost, int attackBoost){
        this.maxHp += hpBoost;
        this.currentHp += hpBoost;
        this.attackPower += attackBoost;
    }

    // Metodi getters
    public String getName(){ return name; }
    public int getCurrentHp(){ return currentHp; }
    public int getMaxHp(){ return maxHp; }
    public int getAttackPower(){ return attackPower; }
}
