package de.cokuss.chhe.pinmoney;


import java.util.Date;

interface ZahlungenDAO {
    void addEntryToPinMoney (String name, Zahlungen zahlungen, String aktion);
    void addEntryToPinMoney (String name, String aktion);
    // Zahlungen getZahlungenFromPinMoney (String name);  brauche ich das noch Todo
    PinMoneyEnrty getEntryFromPinMoney(String name);
}

class PinMoneyEnrty {
    private Zahlungen zahlungen; //includes StartDate Cycle and Value
    private Date entryDate;
    private String kontoName, action;

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
