package de.cokuss.chhe.pinmoney;

public class Konto {
    private String inhaber;
    private float kontostand;

    public Konto (String inhaber,float kontostand) {
        this.kontostand = kontostand;
        this.inhaber = inhaber;
    }

    String getInhaber() {
        return inhaber;
    }

    float getKontostand() {
        return kontostand;
    }
}
