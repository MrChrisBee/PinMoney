package de.cokuss.chhe.pinmoney;

import java.util.ArrayList;


//im using Denglish v1.0 :-)

public interface BuchungDAO {
    public ArrayList<Buchung> getAllBuchungen (String name);

    public ArrayList<Konto> getAllKonten ();

    public void createKonto (Konto konto);

    public void deleteKonto (Konto konto);

    public void createBuchung (Konto konto, Buchung buchung);

    public boolean kontoExists (Konto konto);

    public float getKontostand (String inhaber);

    //nicht unbedingt im DAO richtig aber so ist schöner
    public boolean isValidKontoName (String string);

    public void setPinMoney (String inhaber, Zahlungen zahlungen);

    public Zahlungen getPinMoney (String inhaber);
}

