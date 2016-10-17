package de.cokuss.chhe.pinmoney;


import java.util.Date;

interface ZahlungenDAO {
    void addEntryToPinMoney (String name, Zahlungen zahlungen, String aktion);
    void addEntryToPinMoney (String name, String aktion);
    PinMoneyEnrty getEntryFromPinMoney(String name);
}

