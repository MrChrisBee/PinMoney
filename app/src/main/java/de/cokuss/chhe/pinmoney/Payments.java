package de.cokuss.chhe.pinmoney;

import java.util.Date;

class Payments {
    private Cycle cycle;
    private float betrag;
    private Date date;

    Payments(Date date, Cycle cycle, float betrag) {
        this.date = date;
        this.cycle = cycle;
        this.betrag = betrag;
    }

    Date getDate () {
        return date;
    }

    Cycle getCycle() {
        return cycle;
    }

    String getTurnusStr () {
        return cycle.getBezeichner();
    }

    String getTurnusStrShort () {
        return cycle.getBezeichnerLetter();
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    float getBetrag () {
        return betrag;
    }

    public void setBetrag (float betrag) {
        this.betrag = betrag;
    }

}
