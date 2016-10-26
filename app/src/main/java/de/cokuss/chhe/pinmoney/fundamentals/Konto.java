package de.cokuss.chhe.pinmoney.fundamentals;

public class Konto {
    private String inhaber;
    private float kontostand;

    public Konto (String inhaber,float kontostand) {
        this.kontostand = kontostand;
        this.inhaber = inhaber;
    }

    public String getInhaber () {
        return inhaber;
    }

    public float getKontostand () {
        return kontostand;
    }
}
