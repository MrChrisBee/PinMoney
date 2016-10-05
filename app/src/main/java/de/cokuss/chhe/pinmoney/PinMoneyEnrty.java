package de.cokuss.chhe.pinmoney;

class PinMoneyEnrty {
    Zahlungen zahlungen; //includes StartDate Cycle and Value
    Date entryDate;
    String kontoName, action;

    PinMoneyEnrty (Zahlungen zahlungen, Date entryDate, String kontoName, String action) {
        this.zahlungen = zahlungen;
        this.entryDate = entryDate;
        this.kontoName = kontoName;
        this.action = action;
    }

    Zahlungen getZahlungen () {
        return zahlungen;
    }

    Date getEntryDate () {
        return entryDate;
    }

    String getKontoName () {
        return kontoName;
    }

    String getAction () {
        return action;
    }
}
