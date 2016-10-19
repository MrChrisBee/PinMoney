package de.cokuss.chhe.pinmoney;

public enum Turnus {

    TAEGLICH("tag"), WOECHENTLICH("woche"), MONATLICH("monat"), JAEHRLICH("jahr"), KEINE_ANGABE("keine");

    private final String bez;

    private Turnus(String bez) {
        this.bez = bez;
    }

    public String getBezeichner () {
        return bez;
    }
}
