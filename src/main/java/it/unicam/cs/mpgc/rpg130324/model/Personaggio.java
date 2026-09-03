package it.unicam.cs.mpgc.rpg130324.model;

public abstract class Personaggio {
    private String nome;
    private int hpAttuali;
    private int hpMassimi;
    private int forzaAttacco;

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

    public void potenziati (int aumentoHp, int aumentoAttacco){
        this.hpMassimi += aumentoHp;
        this.hpAttuali += aumentoHp;
        this.forzaAttacco += aumentoAttacco;
    }
    // Metodi getters
    public String getNome(){ return nome; }
    public int getHpAttuali(){ return hpAttuali; }
    public int getHpMassimi(){ return hpMassimi; }
    public int getForzaAttacco(){ return forzaAttacco; }

    // Metodi setters
    public void setHpAttuali(int hpAttuali){ this.hpAttuali = hpAttuali; }
}
