package de.cokuss.chhe.pinmoney;

/**
 * Created by chrisbee on 26.09.16.
 */

public enum Turnus {

    TAEGLICH("täglich"), WOECHENTLICH("wöchentlich"), MONATLICH("monatlich"), JAEHRLICH("jährlich");

    private final String bez;
    private Turnus(String bez) {
        this.bez = bez;
    }

    public String getBezeichner () {
        return bez;
    }
}
