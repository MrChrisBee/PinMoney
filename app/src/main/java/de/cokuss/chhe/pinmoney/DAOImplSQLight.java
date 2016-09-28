package de.cokuss.chhe.pinmoney;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

public class DAOImplSQLight extends SQLiteOpenHelper implements BuchungDAO, KontoDAO {
    private static final String LOG_TAG = DAOImplSQLight.class.getSimpleName();

    private SQLiteDatabase db;
    public static DAOImplSQLight daoImplSQLight;

    //Datenbank
    private static final String DB_NAME = "taschengeldkonto.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "history";
    //Spalten für Konto
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "datum";
    private static final String COLUMN_VALUE = "betrag";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_VERI_ID = "verifikation_id";
    private static final String COLUMN_VERI_TYPE = "verifikation_typ";
    private static final String COLUMN_BALANCE = "kontostand";

    //Spalten für History
    //Wann (Datum) wurde was (erstellen|löschen) mit welchen Konto (Kontoname) gemacht
    private static final String COLUMN_HIST_ID = "id";
    private static final String COLUMN_HIST_TABLE = "kontoname";
    private static final String COLUMN_HIST_AKTION = "aktion";
    private static final String COLUMN_HIST_DATE = "datum";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_NAME + ";";

    private static final String SQL_CREATE_HISTORY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_HIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_HIST_TABLE + " NOT NULL, " +
            COLUMN_HIST_AKTION + " NOT NULL, " +
            COLUMN_HIST_DATE + "  NOT NULL)";

    private DAOImplSQLight(Context con) {
        super(con, DB_NAME, null, DB_VERSION);
        log("Helper hat die DB " + getDatabaseName() + " erzeugt");
    }

    public static DAOImplSQLight getInstance(Context c) {
        if(daoImplSQLight == null) {
            daoImplSQLight = new DAOImplSQLight(c);
        }
        return daoImplSQLight;
    }
    @Override
    public void createKonto(Konto konto) {
        String sql = "Create table " + konto.getInhaber() + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + "," +
                COLUMN_VALUE + "," +
                COLUMN_TEXT + "," +
                COLUMN_VERI_ID + "," +
                COLUMN_VERI_TYPE + "," +
                COLUMN_BALANCE + " )";
        log("Neues Konto erstellen mit: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void createBuchung(Konto konto, Buchung buchung) {
        db = getWritableDatabase();
        String sql = "Insert into " + konto.getInhaber() + " ( "
                + COLUMN_DATE + "," + COLUMN_VALUE + "," + COLUMN_TEXT + ","
                + COLUMN_VERI_ID + "," + COLUMN_VERI_TYPE + "," + COLUMN_BALANCE + ")"
                + "values (" + "datetime('now')," + buchung.getValue()
                + "," + buchung.getText() + "," + buchung.getVeri_id()
                + buchung.getVeri_type() + (buchung.getBalance() + buchung.getValue() + ")");
        log("Buchung erstellen mit: " + sql);
        db.execSQL(sql);
    }

    @Override
    public ArrayList<Buchung> getAllBuchungen(String name) {
        db = getWritableDatabase();
        ArrayList<Buchung> buchungen = new ArrayList<>();
        Buchung buchung;
        Konto konto;
        Long id;
        Date date;
        float value;
        String text;
        Long veri_id;
        Integer veri_type;
        float balance;

        //die erste Zeile ist für Zahlungen und mögliche andere Kontodaten reserviert
        String sql = "Select * from " + name + " where id > 1";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        log(c.getCount() + " Buchungssätze eingelesen!");

        while (!c.isAfterLast()) {
            text = c.getString(c.getColumnIndex(COLUMN_TEXT));
            value = c.getFloat(c.getColumnIndex(COLUMN_VALUE));
            id = c.getLong(c.getColumnIndex(COLUMN_ID));
            veri_id = c.getLong(c.getColumnIndex(COLUMN_VERI_ID));
            veri_type = c.getInt(c.getColumnIndex(COLUMN_VERI_TYPE));
            balance = c.getFloat(c.getColumnIndex(COLUMN_BALANCE));
            date = new Date(c.getColumnIndex(COLUMN_DATE));
            konto = new Konto(name, balance);
            buchung = new Buchung(id, konto, date, value, text, veri_id, veri_type);
            buchungen.add(buchung);
            c.moveToNext();
        }
        c.close();
        return buchungen;
    }

    @Override
    public void setPinMoney(Konto konto, Zahlungen zahlungen) {

        db = getWritableDatabase();
        //Datum zahlung.betrag zahlung.turnus startbetrag
        String sql = "insert into " + konto.getInhaber() + "(" + COLUMN_DATE + "," + COLUMN_VALUE + "," + COLUMN_VERI_TYPE + "," + COLUMN_BALANCE + ")" +
                " values ( '" + zahlungen.getDate() + "' , " + zahlungen.getBetrag() + " ,'" + zahlungen.getTurnusStr() + "', " + konto.getKontostand() + ")";
        log("setPinMoney start " + sql);
        db.execSQL(sql);
        log("SetPinMoney ausgeführt für Konto " + konto.getInhaber());
    }

    @Override
    public Zahlungen getPinMoney(String inhaber) {
        db = getWritableDatabase();
        Zahlungen zahlungen;
        Date date;
        float value;
        float balance;
        Turnus turnus;
        String string;
        String sql = "select " + COLUMN_DATE + "," + COLUMN_VALUE + "," + COLUMN_VERI_TYPE + "," + COLUMN_BALANCE + " from " + inhaber + " where id = 1";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        log("PinMoney Kontoinfo eingelesen!");
        date = new Date(c.getColumnIndex(COLUMN_DATE));
        value = c.getFloat(c.getColumnIndex(COLUMN_VALUE));
        switch (c.getString(c.getColumnIndex(COLUMN_VERI_TYPE))) {
            case "taeglich":
                turnus = Turnus.TAEGLICH;
                break;
            case "woechentlich":
                turnus = Turnus.WOECHENTLICH;
                break;
            case "monatlich":
                turnus = Turnus.MONATLICH;
                break;
            default:
                turnus = Turnus.TAEGLICH;
        }

        balance = c.getFloat(c.getColumnIndex(COLUMN_BALANCE));
        c.close();
        zahlungen = new Zahlungen(date, turnus, value);
        log("getPinMoney ausgeführt für Konto " + inhaber);
        return zahlungen;
    }

    @Override
    public ArrayList<Konto> getAllKonten() {
        db = getWritableDatabase();
        ArrayList<Konto> result = new ArrayList<>();
        Konto konto;
        String inhaber;
        float kontostand;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT tbl_name FROM sqlite_master ");
        sb.append("WHERE type IN ('table','view') AND tbl_name NOT LIKE 'sqlite_%' ");
        sb.append("AND tbl_name NOT LIKE 'android_metadata'");
        sb.append("UNION ALL ");
        sb.append("SELECT tbl_name FROM sqlite_temp_master ");
        sb.append("WHERE type IN ('table','view') ");
        sb.append("ORDER BY tbl_name");
        try {
            Cursor c = db.rawQuery(sb.toString(), null);
            c.moveToFirst();
            log("getAllKonten: Erfolgreich " + c.getCount() + " Tabellen erkannt!");

            while (!c.isAfterLast()) {
                inhaber = c.getString(c.getColumnIndex("tbl_name"));
                if (!inhaber.equals(TABLE_NAME)) {
                    kontostand = getKontostand(inhaber);
                    konto = new Konto(inhaber, kontostand);
                    result.add(konto);
                }
                c.moveToNext();
            }
            log("getAllKonten: Result enthält  " + result.size() + " Einträge");
            c.close();
        } catch (SQLiteException e) {
            log("getAllKonten : " + e.toString());
            e.getStackTrace();
        }
        return result;
    }

    @Override
    public void deleteKonto(Konto konto) {
        db = getWritableDatabase();
        String sql = "DROP TABLE IF EXIST " + konto.getInhaber();
        db.execSQL(sql);
    }

    @Override
    public boolean kontoExists(String string) {
        db = getWritableDatabase();
        boolean schon_existent = true;
        String sql = "SELECT name FROM sqlite_master where name LIKE '" + string + "';";
        Cursor c = db.rawQuery(sql, null);
        if (c.getCount() > 0) {
            schon_existent = true;
            log("kontoExists: Das Konto " + string + " existiert bereits.");
            c.close();
        } else {
            schon_existent = false;
            log("kontoExists: Das Konto " + string + " wurde nicht gefunden.");
        }
        return schon_existent;
    }

    @Override
    public float getKontostand(String inhaber) {
        db = getWritableDatabase();
        log("getKontostand: Ermittle den Kontostand für " + inhaber);
        float kontostand = 0;
        String sql = "SELECT " + COLUMN_BALANCE + " FROM " + inhaber + " WHERE   ID = (SELECT MAX(ID)  FROM  " + inhaber + " )";
        Cursor c = db.rawQuery(sql, null);
        log("getKontostand: habe Treffer : " + c.getCount());
        c.moveToFirst();
        switch(c.getCount()){
            case 0:
                log("getKontostand: Keine Datensätze gefunden ");
                break;
            case 1:
                kontostand = c.getFloat(c.getColumnIndex(COLUMN_BALANCE));
                break;
            default:
                log("getKontostand: Ich habe " + c.getCount() + " 'letzte' Einträge gefunden!?");
                break;

        }
        c.close();
        return kontostand;
    }

    @Override
    public boolean isValidKontoName(String string) {
        db = getWritableDatabase();
        return string.matches("\\w+");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        log("Versuch die Tabelle zu erstellen !");
        try {
            db.execSQL(SQL_CREATE_HISTORY);
        } catch (SQLException e) {
            log("Fehler beim Anlegen " + e.getMessage());
            e.printStackTrace();
        } finally {
            log("Tabelle erstellt !");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public synchronized void close() {
        super.close();
        db = getWritableDatabase();
        db.close();
    }

    private void log(String string) {
        Log.d(LOG_TAG, string);
    }

    public void open() {
        db = getWritableDatabase();
    }
}


