package de.cokuss.chhe.pinmoney;


import android.content.Context;

import java.util.Date;

interface ZahlungenDAO {
    void addEntryToPinMoney (String name, Date gebDatum, Zahlungen zahlungen, String aktion);
    void addEntryToPinMoney (String name, String aktion);
    PinMoneyEnrty getEntryFromPinMoney(Context context, String name);
}

