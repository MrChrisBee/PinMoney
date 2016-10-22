package de.cokuss.chhe.pinmoney;

import java.util.Date;

class Payments {
    private Turnus turnus;
    private float betrag;
    private Date date;

    Payments(Date date, Turnus turnus, float betrag) {
        this.date = date;
        this.turnus = turnus;
        this.betrag = betrag;
    }

    Date getDate () {
        return date;
    }

    Turnus getTurnus () {
        return turnus;
    }

    String getTurnusStr () {
        return turnus.getBezeichner();
    }

    String getTurnusStrShort () {
        return turnus.getBezeichnerLetter();
    }

    public void setTurnus (Turnus turnus) {
        this.turnus = turnus;
    }

    float getBetrag () {
        return betrag;
    }

    public void setBetrag (float betrag) {
        this.betrag = betrag;
    }

}
