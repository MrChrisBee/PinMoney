package de.cokuss.chhe.pinmoney;

public enum Cycle {

    TAEGLICH("tag"), WOECHENTLICH("woche"), MONATLICH("monat"), JAEHRLICH("jahr"), KEINE_ANGABE("keine");

    private final String bez;

    private Cycle(String bez) {
        this.bez = bez;
    }

    String getBezeichner() {
        return bez;
    }

    String getBezeichnerLetter() {
        return "" + bez.charAt(0);
    }
}
