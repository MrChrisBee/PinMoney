package de.cokuss.chhe.pinmoney;

import java.util.Date;

class Zahlungen {
    private Turnus turnus;
    private float betrag;
    private Date date;

    Zahlungen (Date date, Turnus turnus, float betrag) {
        this.date = date;
        this.turnus = turnus;
        this.betrag = betrag;
    }

    Date getDate () {
        return date;
    }

    public Turnus getTurnus () {
        return turnus;
    }

    String getTurnusStr () {
        return turnus.getBezeichner();
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
