package de.cokuss.chhe.pinmoney;

import java.util.ArrayList;


//im using Denglish v1.0 :-)

public interface BuchungDAO extends KontoDAO {
    public ArrayList<Buchung> getAllBuchungen (String name);

    public void createBuchung (Konto konto, Buchung buchung);

    public void setPinMoney (String inhaber, Zahlungen zahlungen);

    public Zahlungen getPinMoney (String inhaber);


}

