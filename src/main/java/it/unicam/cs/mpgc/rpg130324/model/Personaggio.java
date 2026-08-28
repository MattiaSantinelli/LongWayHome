package it.unicam.cs.mpgc.rpg130324.model;

public abstract class Personaggio {
    private final String nome;
    private int hpAttuali;
    private final int hpMassimi;
    private final int forzaAttacco;

    public Personaggio(String nome, int hpMassimi, int forzaAttacco) {
        this.nome = nome;
        this.hpMassimi = hpMassimi;
        this.hpAttuali = hpMassimi;
        this.forzaAttacco = forzaAttacco;
    }

    public void subisciDanno(int danno){
        this.hpAttuali = Math.max(0, this.hpAttuali - danno);
    }

    public boolean isVivo(){
        return this.hpAttuali > 0;
    }

    // Metodi getters
    public String getNome(){ return nome; }
    public int getHpAttuali(){ return hpAttuali; }
    public int getHpMassimi(){ return hpMassimi; }
    public int getForzaAttacco(){ return forzaAttacco; }
}
