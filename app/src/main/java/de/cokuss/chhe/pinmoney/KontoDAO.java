package de.cokuss.chhe.pinmoney;

import java.util.ArrayList;

interface KontoDAO {
    //Konto Methoden
    ArrayList<Konto> getAllKonten();

    void createKonto(Konto konto);

    void deleteKonto(String konto);

    boolean kontoExists(String string);

    float getKontostand(String inhaber);

    //nicht unbedingt im DAO richtig aber so ist schöner
    boolean isValidKontoName(String string);
}
