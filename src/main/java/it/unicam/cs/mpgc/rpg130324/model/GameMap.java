package it.unicam.cs.mpgc.rpg130324.model;

import it.unicam.cs.mpgc.rpg130324.model.entity.EnemyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameMap {

    private static final int ROWS = 10;
    private static final int COLS = 10;
    private final String[][] grid = new String[ROWS][COLS];

    public GameMap() {
        initializeMap();
    }

    /**
     * Ripristina la mappa e distribuisce casualmente i nemici rispettando OCP.
     */
    public void initializeMap() {
        // 1. Pulisci la matrice
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                grid[r][c] = "";
            }
        }

        // 2. Posizioni fisse prescritte
        grid[0][0] = "Eroe";
        grid[9][9] = "Casa";
        grid[8][8] = EnemyType.MAGO.getName();
        grid[8][9] = EnemyType.DRAGO.getName();
        grid[9][8] = EnemyType.DRAGO.getName();

        // 3. Generazione dinamica del pool (OCP)
        List<String> pool = new ArrayList<>();

        // Scorre automaticamente TUTTI gli EnemyType e legge il loro spawnWeight
        for (EnemyType type : EnemyType.values()) {
            for (int i = 0; i < type.getSpawnWeight(); i++) {
                pool.add(type.getName());
            }
        }

        // Calcola quante celle vuote servono per completare le celle rimaste libere
        int fixedOccupied = 5; // Eroe, Casa, Mago, 2x Drago
        int totalFreeCells = (ROWS * COLS) - fixedOccupied;
        int emptyCellsNeeded = Math.max(0, totalFreeCells - pool.size());

        for (int i = 0; i < emptyCellsNeeded; i++) {
            pool.add("");
        }

        // 4. Mescola e assegna
        Collections.shuffle(pool);

        int index = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c].isEmpty()) {
                    grid[r][c] = pool.get(index++);
                }
            }
        }
    }

    public String getCell(int row, int col) {
        if (!isValidPosition(row, col)) return "";
        return grid[row][col];
    }

    public void setCell(int row, int col, String value) {
        if (isValidPosition(row, col)) {
            grid[row][col] = value;
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    /**
     * Restituisce una copia difensiva della griglia (Encapsulation).
     */
    public String[][] getGrid() {
        return grid;
    }
}