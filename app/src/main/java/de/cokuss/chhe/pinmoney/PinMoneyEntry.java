package de.cokuss.chhe.pinmoney;

import java.util.Date;

import de.cokuss.chhe.pinmoney.fundamentals.Payments;

public class PinMoneyEntry {
    private Payments payments; //includes StartDate Cycle and Value
    private Date entryDate, birthDate;
    private String kontoName, action;

    PinMoneyEntry(Payments payments, Date entryDate, String kontoName, Date birthDate, String action) {
        this.payments = payments;
        this.entryDate = entryDate;
        this.kontoName = kontoName;
        this.action = action;
        this.birthDate = birthDate;
    }

    public Payments getPayments() {
        return payments;
    }

    Date getEntryDate() {
        return entryDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    String getKontoName() {
        return kontoName;
    }

    String getAction() {
        return action;
    }
}
