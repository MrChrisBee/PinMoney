package de.cokuss.chhe.pinmoney;

public enum Turnus {

    TAEGLICH("tag"), WOECHENTLICH("woche"), MONATLICH("monat"), JAEHRLICH("jahr"), KEINE_ANGABE("keine");

    private final String bez;

    private Turnus(String bez) {
        this.bez = bez;
    }

    String getBezeichner() {
        return bez;
    }

    String getBezeichnerLetter() {
        return "" + bez.charAt(0);
    }
}
