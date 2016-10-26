package de.cokuss.chhe.pinmoney.fundamentals;

public enum Cycle {

    TAEGLICH("tag"), WOECHENTLICH("woche"), MONATLICH("monat"), JAEHRLICH("jahr"), KEINE_ANGABE("keine");

    private final String bez;

    private Cycle(String bez) {
        this.bez = bez;
    }

    String getBezeichner() {
        return bez;
    }

    public String getBezeichnerLetter() {
        return "" + bez.charAt(0);
    }
}
