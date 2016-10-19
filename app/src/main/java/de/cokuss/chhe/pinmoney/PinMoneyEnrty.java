package de.cokuss.chhe.pinmoney;

import java.util.Date;

class PinMoneyEnrty {
    private Zahlungen zahlungen; //includes StartDate Cycle and Value
    private Date entryDate, birthDate;
    private String kontoName, action;

    PinMoneyEnrty(Zahlungen zahlungen, Date entryDate, String kontoName, Date birthDate, String action) {
        this.zahlungen = zahlungen;
        this.entryDate = entryDate;
        this.kontoName = kontoName;
        this.action = action;
        this.birthDate = birthDate;
    }

    Zahlungen getZahlungen() {
        return zahlungen;
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
