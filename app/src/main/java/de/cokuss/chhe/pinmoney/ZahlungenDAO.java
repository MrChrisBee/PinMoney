package de.cokuss.chhe.pinmoney;


import java.util.Date;

interface ZahlungenDAO {
    void addEntryToHPinMoney (String name, Zahlungen zahlungen, String aktion);
    void addEntryToHPinMoney (String name, String aktion);
    Zahlungen getZahlungenFromPinMoney (String name);
    PinMoneyEnrty getEntryFromPinMoney(String name);
}

class PinMoneyEnrty {
    Zahlungen zahlungen; //includes StartDate Cycle and Value
    Date entryDate;
    String kontoName, action;

    public PinMoneyEnrty (Zahlungen zahlungen, Date entryDate, String kontoName, String action) {
        this.zahlungen = zahlungen;
        this.entryDate = entryDate;
        this.kontoName = kontoName;
        this.action = action;
    }

    public Zahlungen getZahlungen () {
        return zahlungen;
    }

    public Date getEntryDate () {
        return entryDate;
    }

    public String getKontoName () {
        return kontoName;
    }

    public String getAction () {
        return action;
    }
}
