package de.cokuss.chhe.pinmoney.fundamentals;

import java.util.ArrayList;

public interface KontoDAO {
    //Account Methoden
    ArrayList<Account> getAllKonten();

    void createKonto(Account konto);

    void deleteKonto(String konto);

    boolean kontoExists(String string);

    float getKontostand(String inhaber);

    //nicht unbedingt im DAO richtig aber so ist schöner
    boolean isValidKontoName(String string);
}
