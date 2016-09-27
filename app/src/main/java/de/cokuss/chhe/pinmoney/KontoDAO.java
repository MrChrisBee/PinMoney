package de.cokuss.chhe.pinmoney;

import java.util.ArrayList;

/**
 * Created by Master on 27.09.2016.
 */
public interface KontoDAO {
    //Konto Methoden
    ArrayList<Konto> getAllKonten();

    void createKonto(Konto konto);

    void deleteKonto(Konto konto);

    boolean kontoExists(Konto konto);

    float getKontostand(String inhaber);

    //nicht unbedingt im DAO richtig aber so ist schöner
    boolean isValidKontoName(String string);
}
