package de.cokuss.chhe.pinmoney;


import java.util.Date;

interface ZahlungenDAO {
    void addEntryToPinMoney (String name, Date gebDatum, Zahlungen zahlungen, String aktion);
    void addEntryToPinMoney (String name, String aktion);
    PinMoneyEnrty getEntryFromPinMoney(String name);
}

