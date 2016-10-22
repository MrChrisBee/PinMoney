package de.cokuss.chhe.pinmoney;

import java.util.Date;

class PinMoneyEnrty {
    private Payments payments; //includes StartDate Cycle and Value
    private Date entryDate, birthDate;
    private String kontoName, action;

    PinMoneyEnrty(Payments payments, Date entryDate, String kontoName, Date birthDate, String action) {
        this.payments = payments;
        this.entryDate = entryDate;
        this.kontoName = kontoName;
        this.action = action;
        this.birthDate = birthDate;
    }

    Payments getPayments() {
        return payments;
    }

    Date getEntryDate() {
        return entryDate;
    }

    Date getBirthDate() {
        return birthDate;
    }

    String getKontoName() {
        return kontoName;
    }

    String getAction() {
        return action;
    }
}
