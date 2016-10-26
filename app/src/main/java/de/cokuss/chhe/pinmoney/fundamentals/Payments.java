package de.cokuss.chhe.pinmoney.fundamentals;

import java.util.Date;

public class Payments {
    private Cycle cycle;
    private float betrag;
    private Date date;

    public Payments(Date date, Cycle cycle, float betrag) {
        this.date = date;
        this.cycle = cycle;
        this.betrag = betrag;
    }

    public Date getDate() {
        return date;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public String getTurnusStr() {
        return cycle.getBezeichner();
    }

    public String getTurnusStrShort() {
        return cycle.getBezeichnerLetter();
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public float getBetrag() {
        return betrag;
    }

    public void setBetrag (float betrag) {
        this.betrag = betrag;
    }

}
