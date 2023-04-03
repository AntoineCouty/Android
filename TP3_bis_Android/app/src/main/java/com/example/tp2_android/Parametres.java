package com.example.tp2_android;

public class Parametres {

    protected String clair;
    protected String chiffre;
    protected int cle;

    public Parametres (String clair, String chiffre, int cle){
        this.chiffre = chiffre;
        this.clair = clair;
        this.cle = cle;
    }

    public int getCle() {
        return cle;
    }

    public String getChiffre() {
        return chiffre;
    }

    public String getClair() {
        return clair;
    }

    public void setChiffre(String chiffre) {
        this.chiffre = chiffre;
    }

    public void setClair(String clair) {
        this.clair = clair;
    }

    public void setCle(int cle) {
        this.cle = cle;
    }
}
