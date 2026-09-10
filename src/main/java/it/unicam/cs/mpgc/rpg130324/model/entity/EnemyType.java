package it.unicam.cs.mpgc.rpg130324.model.entity;

public enum EnemyType {
    GOBLIN("Goblin", 50, 10, "/image/imgGoblin.png", "#8BC34A", 1.0, 9),
    GIGANTE("Gigante", 120, 20, "/image/imgGigante.png", "#FFC107", 1.0, 9),
    STREGA("Strega", 80, 30, "/image/imgStrega.png", "#880E4F", 1.0, 7),
    MAGO("Mago", 80, 30, "/image/imgMago.png", "#1E88E5", 1.0, 7),
    DRAGO("Drago", 200, 40, "/image/imgDrago.png", "#B71C1C", 0.5, 0);

    private final String name;
    private final int baseHp;
    private final int baseAttack;
    private final String imagePath;
    private final String glowColor;
    private final double attackIntervalSeconds;
    private final int spawnWeight;

    EnemyType(String name, int baseHp, int baseAttack, String imagePath, String glowColor, double attackIntervalSeconds, int spawnWeight) {
        this.name = name;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.imagePath = imagePath;
        this.glowColor = glowColor;
        this.attackIntervalSeconds = attackIntervalSeconds;
        this.spawnWeight = spawnWeight;
    }

    /**
     * Factory method per istanziare un nuovo nemico basato sulle statistiche base.
     */
    public Enemy createEnemy() {
        return new Enemy(this.name, getBaseHp(), getBaseAttack());
    }

    // --- GETTER D'ISTANZA (senza parametri) ---
    public String getName() { return name; }

    public int getBaseHp() { return baseHp; }

    public int getBaseAttack() { return baseAttack; }

    public String getImagePath() { return imagePath; }

    public String getGlowColor() { return glowColor; }

    public double getAttackIntervalSeconds() { return attackIntervalSeconds; }

    public int getSpawnWeight(){ return spawnWeight; }

    // --- METODO STATICO PER LA RICERCA ---
    /**
     * Cerca l'istanza dell'enum a partire dal nome testuale del nemico.
     */
    public static EnemyType fromName(String name) {
        if (name == null || name.isEmpty()) return null;

        for (EnemyType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}