package de.cokuss.chhe.pinmoney;

import java.util.Date;

/**
 * Created by christian on 16.10.16.
 */
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
