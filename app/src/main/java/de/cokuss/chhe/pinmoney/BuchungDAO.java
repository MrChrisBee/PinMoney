package de.cokuss.chhe.pinmoney;

import java.util.ArrayList;


//im using Denglish v1.0 :-)

public interface BuchungDAO extends KontoDAO {
    public ArrayList<Buchung> getAllBuchungen (String name);

    public void createBuchung(Buchung buchung);

    //StartDatum Inhaber StartBetrag Betrag Turnus
    public void setPinMoney (Konto konto, Zahlungen zahlungen);

    public Zahlungen getPinMoney (String inhaber);

}

