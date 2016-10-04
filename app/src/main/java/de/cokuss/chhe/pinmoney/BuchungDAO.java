package de.cokuss.chhe.pinmoney;

import java.util.ArrayList;

interface BuchungDAO extends KontoDAO {
    ArrayList<Buchung> getAllBuchungen (String name);

    void createBuchung (Konto konto, Buchung buchung);

}

