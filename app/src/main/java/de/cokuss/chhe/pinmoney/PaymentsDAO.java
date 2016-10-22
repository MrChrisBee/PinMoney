package de.cokuss.chhe.pinmoney;


import android.content.Context;

import java.util.Date;

interface PaymentsDAO {
    void addEntryToPinMoney (String name, Date gebDatum, Payments payments, String aktion);
    void addEntryToPinMoney (String name, String aktion);
    PinMoneyEnrty getEntryFromPinMoney(Context context, String name);
}

