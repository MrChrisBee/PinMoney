package de.cokuss.chhe.pinmoney;

public enum Turnus {

    TAEGLICH("taeglich"), WOECHENTLICH("woechentlich"), MONATLICH("monatlich"), JAEHRLICH("jaehrlich");

    private final String bez;

    private Turnus(String bez) {
        this.bez = bez;
    }

    public String getBezeichner () {
        return bez;
    }
}
