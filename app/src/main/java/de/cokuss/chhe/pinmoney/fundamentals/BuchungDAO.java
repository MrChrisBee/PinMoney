package de.cokuss.chhe.pinmoney.fundamentals;

import android.content.Context;

import java.util.ArrayList;

public interface BuchungDAO extends KontoDAO {
    ArrayList<Buchung> getAllBuchungen (String name);

    void createBuchung (Konto konto, Buchung buchung);
    Buchung calcSavings(Context context, String owner);
}

