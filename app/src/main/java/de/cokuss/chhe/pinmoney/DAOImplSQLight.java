package de.cokuss.chhe.pinmoney;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class DAOImplSQLight extends SQLiteOpenHelper implements BuchungDAO, KontoDAO {
    private static final String LOG_TAG = DAOImplSQLight.class.getSimpleName();

    private SQLiteDatabase db;

    //Datenbank
    public static final String DB_NAME = "taschengeldkonto.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "history";

    //Spalten für Konto
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "datum";
    public static final String COLUMN_VALUE = "betrag";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_VERI_ID = "verifikation_id";
    public static final String COLUMN_VERI_TYPE = "verifikation_typ";
    public static final String COLUMN_BALANCE = "kontostand";

    //Spalten für History
    //Wann (Datum) wurde was (erstellen|löschen) mit welchen Konto (Kontoname) gemacht
    public static final String COLUMN_HIST_ID = "id";
    public static final String COLUMN_HIST_TABLE = "kontoname";
    public static final String COLUMN_HIST_AKTION = "aktion";
    public static final String COLUMN_HIST_DATE = "datum";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_NAME + ";";

    public static final String SQL_CREATE_HISTORY = "CREATE TABLE " + TABLE_NAME + "(" +
            COLUMN_HIST_ID + " PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_HIST_TABLE + " NOT NULL, " +
            COLUMN_HIST_AKTION + " NOT NULL, " +
            COLUMN_HIST_DATE + "  NOT NULL);";

    public DAOImplSQLight (Context con) {
        super(con, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "Helper hat die DB " + getDatabaseName() + " erzeugt");
    }

    public void open () {
        if (db == null) {
            Log.d(LOG_TAG, "Öffne die Datenbank im RW Mode.");
            db = getWritableDatabase();
            Log.d(LOG_TAG, "Pfad zur DB " + db.getPath());
        }
    }

    @Override
    public void createKonto (Konto konto) {
        open();
        String sql = "Create table " + konto.getInhaber() + "(" +
                COLUMN_ID + " PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " NOT NULL, " +
                COLUMN_VALUE + " NOT NULL, " +
                COLUMN_TEXT + " NOT NULL, " +
                COLUMN_VERI_ID + " NOT NULL, " +
                COLUMN_VERI_TYPE + " NOT NULL, " +
                COLUMN_BALANCE + " NOT NULL, ";
        Log.d(LOG_TAG,"Neues Konto erstellen mit: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void createBuchung (Konto konto, Buchung buchung) {
        open();
        String sql = "Insert into " + konto.getInhaber() + " ( "
                + COLUMN_DATE +","+COLUMN_VALUE+","+COLUMN_TEXT+ ","
                + COLUMN_VERI_ID+","+COLUMN_VERI_TYPE+","+COLUMN_BALANCE+")"
                + "values (" + "datetime('now')," +buchung.getValue()
                +","+buchung.getText()+","+buchung.getVeri_id()
                +buchung.getVeri_type()+(buchung.getBalance()+buchung.getValue());
        Log.d(LOG_TAG,"Buchung erstellen mit: " + sql);
        db.execSQL(sql);
        }

    @Override
    public ArrayList<Buchung> getAllBuchungen (String name) {
        open();
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
        Log.d(LOG_TAG, c.getCount() + " Buchungssätze eingelesen!");

        while (!c.isAfterLast()) {
            text = c.getString(c.getColumnIndex(COLUMN_TEXT));
            value = c.getFloat(c.getColumnIndex(COLUMN_VALUE));
            id = c.getLong(c.getColumnIndex(COLUMN_ID));
            veri_id = c.getLong(c.getColumnIndex(COLUMN_VERI_ID));
            veri_type = c.getInt(c.getColumnIndex(COLUMN_VERI_TYPE));
            balance = c.getFloat(c.getColumnIndex(COLUMN_BALANCE));
            date = new Date(c.getColumnIndex(COLUMN_DATE));
            konto = new Konto(name,balance);
            buchung = new Buchung(id,konto,date,value,text,veri_id,veri_type);
            buchungen.add(buchung);
            c.moveToNext();
        }
        return buchungen;
    }

    @Override
    public void setPinMoney (String inhaber, Zahlungen zahlungen) {
        open();
        //was ich noch unterbringen muss:
            //zahlung.betrag zahlung.turnus
        //Todo setze die Werte passend in die erste Zeile
    }

    @Override
    public Zahlungen getPinMoney (String inhaber) {
        open();
        //Todo hole die passenden werte aus der ersten Zeile
        return null;
    }

    @Override
    public ArrayList<Konto> getAllKonten () {
        open();
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
            Log.d(LOG_TAG, "Erfolgreich " + c.getCount() + " Tabellen erkannt!");

            while (!c.isAfterLast()) {
                inhaber = c.getString(c.getColumnIndex("tbl_name"));
                kontostand = getKontostand(inhaber);
                konto = new Konto(inhaber, kontostand);
                result.add(konto);
                c.moveToNext();
            }

            Log.d(LOG_TAG, "Result enthält  " + result.size() + " Einträge");
            c.close();
        } catch (SQLiteException e) {
            Log.e(LOG_TAG, e.toString());
            e.getStackTrace();
        }
        return result;
    }

    @Override
    public void deleteKonto (Konto konto) {
        open();
        String sql = "DROP TABLE IF EXIST " + konto.getInhaber();
        db.execSQL(sql);
    }

    @Override
    public boolean kontoExists (Konto konto) {
        open();
        boolean schon_existent = true;
        String string = "SELECT name FROM sqlite_master where name LIKE '" + konto.getInhaber() + "';";
        Cursor c = db.rawQuery(string, null);
        if (c != null && c.getCount() > 0) {
            schon_existent = true;
            Log.d(LOG_TAG, "Das Konto " + konto.getInhaber() + " existiert bereits.");
        } else {
            schon_existent = false;
            Log.d(LOG_TAG, "Das Konto " + konto.getInhaber() + " wurde nicht gefunden.");
        }
        return schon_existent;
    }

    @Override
    public float getKontostand (String inhaber) {
        open();
        float kontostand = 0;
        String sql = "SELECT " + COLUMN_BALANCE + " FROM " + inhaber + "WHERE   ID = (SELECT MAX(ID)  FROM" + inhaber + ");";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        if (c.getCount() > 1)
            Log.e(LOG_TAG, "Fahler! Ich habe " + c.getCount() + " letzte Einträge gefunden!");
        kontostand = c.getInt(c.getColumnIndex(COLUMN_BALANCE));
        return kontostand;
    }

    @Override
    public boolean isValidKontoName (String string) {
        return string.matches("\\w+");
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        Log.d(LOG_TAG, "Versuch die Tabelle zu erstellen !");
        try {
            db.execSQL(SQL_CREATE_HISTORY);
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Fehler beim Anlegen " + e.getMessage());
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "Tabelle erstellt !");
        }
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public synchronized void close () {
        super.close();
        db.close();
    }
}


