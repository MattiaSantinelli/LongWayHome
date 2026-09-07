package it.unicam.cs.mpgc.rpg130324.model.entity;

public abstract class Character {
    private String name;
    private int currentHp;
    private int maxHp;
    private int attackPower;

    public Character(String name, int maxHp, int attackPower) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attackPower = attackPower;
    }

    public void takeDamage(int damage){
        this.currentHp = Math.max(0, this.currentHp - damage);
    }

    public boolean isAlive(){
        return this.currentHp > 0;
    }

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

    // Metodi setters
    public void setCurrentHp(int currentHp){ this.currentHp = currentHp; }
}
