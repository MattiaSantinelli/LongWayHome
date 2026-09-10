package it.unicam.cs.mpgc.rpg130324.model;

import it.unicam.cs.mpgc.rpg130324.model.entity.EnemyType;
import it.unicam.cs.mpgc.rpg130324.model.entity.CellType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap {

    private static final int ROWS = 10;
    private static final int COLS = 10;
    private final CellType[][] grid = new CellType[ROWS][COLS];

    // Coordinate dell'Eroe incapsulate nel Model (Integrità MVC)
    private int heroRow = 0;
    private int heroColumn = 0;

    public GameMap() {
        initializeMap();
    }

    /**
     * Ripristina la mappa e distribuisce casualmente i nemici.
     */
    public void initializeMap() {
        // Reset coordinate dell'Eroe
        this.heroRow = 0;
        this.heroColumn = 0;

        // 1. Pulisci la matrice
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }

        // 2. Posizioni fisse prescritte
        grid[0][0] = CellType.HERO;
        grid[9][9] = CellType.HOUSE;
        grid[8][8] = CellType.MAGO;
        grid[8][9] = CellType.DRAGO;
        grid[9][8] = CellType.DRAGO;

        // 3. Generazione dinamica del pool
        List<CellType> pool = new ArrayList<>();

        for (EnemyType type : EnemyType.values()) {
            CellType cellType = CellType.valueOf(type.name());
            for (int i = 0; i < type.getSpawnWeight(); i++) {
                pool.add(cellType);
            }
        }

        int fixedOccupied = 5; // HERO, HOUSE, MAGO, 2x DRAGO
        int totalFreeCells = (ROWS * COLS) - fixedOccupied;
        int emptyCellsNeeded = Math.max(0, totalFreeCells - pool.size());

        for (int i = 0; i < emptyCellsNeeded; i++) {
            pool.add(CellType.EMPTY);
        }

        // 4. Mescola e assegna
        Collections.shuffle(pool);

        int index = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == CellType.EMPTY && !(r == 0 && c == 0)) {
                    grid[r][c] = pool.get(index++);
                }
            }
        }
    }

    /**
     * Sposta l'Eroe sulla mappa aggiornando lo stato interno.
     * @param direction La direzione del movimento ("SU", "GIU", "SINISTRA", "DESTRA").
     * @return Il tipo di cella su cui l'Eroe è atterrato (CellType), oppure null se il movimento non è valido.
     */
    public CellType moveHero(String direction) {
        int newRow = heroRow;
        int newColumn = heroColumn;

        switch (direction) {
            case "SU" -> newRow--;
            case "GIU" -> newRow++;
            case "SINISTRA" -> newColumn--;
            case "DESTRA" -> newColumn++;
            default -> { return null; }
        }

        if (!isValidPosition(newRow, newColumn)) {
            return null; // Movimento fuori dai confini della mappa
        }

        // Recupera cosa c'è nella cella di destinazione
        CellType destination = grid[newRow][newColumn];

        // Aggiorna lo stato interno della matrice
        grid[heroRow][heroColumn] = CellType.EMPTY;
        this.heroRow = newRow;
        this.heroColumn = newColumn;
        grid[heroRow][heroColumn] = CellType.HERO;

        return destination;
    }

    public CellType getCell(int row, int col) {
        if (!isValidPosition(row, col)) return CellType.EMPTY;
        return grid[row][col];
    }

    public void setCell(int row, int col, CellType value) {
        if (isValidPosition(row, col)) {
            grid[row][col] = (value != null) ? value : CellType.EMPTY;
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    public int getHeroRow() {
        return heroRow;
    }

    public int getHeroColumn() {
        return heroColumn;
    }

    /**
     * Restituisce una REALE copia difensiva della griglia (Encapsulation).
     * Modifiche all'array restituito non influenzeranno lo stato interno di GameMap.
     */
    public CellType[][] getGrid() {
        CellType[][] copy = new CellType[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            System.arraycopy(grid[r], 0, copy[r], 0, COLS);
        }
        return copy;
    }
}