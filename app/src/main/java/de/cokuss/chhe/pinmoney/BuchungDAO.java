package de.cokuss.chhe.pinmoney;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

interface BuchungDAO extends KontoDAO {
    ArrayList<Buchung> getAllBuchungen (String name);

    void createBuchung (Konto konto, Buchung buchung);
    Buchung calcSavings(Context context, String owner);
}

