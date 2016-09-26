package de.cokuss.chhe.pinmoney;

/**
 * Created by chrisbee on 26.09.16.
 */
public class Zahlungen {
    Turnus turnus;
    float betrag;

    public Zahlungen (Turnus turnus, float betrag) {
        this.turnus = turnus;
        this.betrag = betrag;
    }

    public Turnus getTurnus () {
        return turnus;
    }

    public void setTurnus (Turnus turnus) {
        this.turnus = turnus;
    }

    public float getBetrag () {
        return betrag;
    }

    public void setBetrag (float betrag) {
        this.betrag = betrag;
    }
}
