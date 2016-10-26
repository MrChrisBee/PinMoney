package de.cokuss.chhe.pinmoney.fundamentals;


import android.content.Context;

import java.util.Date;

import de.cokuss.chhe.pinmoney.PinMoneyEntry;
import de.cokuss.chhe.pinmoney.fundamentals.Payments;

public interface PaymentsDAO {
    void addEntryToPinMoney (String name, Date gebDatum, Payments payments, String aktion);
    void addEntryToPinMoney (String name, String aktion);
    PinMoneyEntry getEntryFromPinMoney(Context context, String name);
}

