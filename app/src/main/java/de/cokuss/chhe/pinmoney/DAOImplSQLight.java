package de.cokuss.chhe.pinmoney;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DAOImplSQLight extends SQLiteOpenHelper implements BuchungDAO, KontoDAO, ZahlungenDAO {
    private static final String LOG_TAG = DAOImplSQLight.class.getSimpleName();

    private SQLiteDatabase db;
    DateHelper dateHelper = new DateHelper();
    static DAOImplSQLight daoImplSQLight;

    //Datenbank
    private static final String DB_NAME = "taschengeldkonto.db";
    private static final int DB_VERSION = 2;
    //Spalten für Konto
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "datum";
    private static final String COLUMN_VALUE = "betrag";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_VERI_ID = "verifikation_id";
    private static final String COLUMN_VERI_TYPE = "verifikation_typ";
    private static final String COLUMN_BALANCE = "kontostand";

    //PinInfo: id aktdate name dateStart turnus value aktion (create | update | delete )
    private static final String TABLE_PM_INFO = "PinInfo";
    private static final String COLUMN_PM_ID = "id";
    private static final String COLUMN_PM_ENTRYDATE = "e_date";
    private static final String COLUMN_PM_NAME = "name";
    private static final String COLUMN_PM_STARTDATE = "s_date";
    private static final String COLUMN_PM_CYCLE = "cycle";
    private static final String COLUMN_PM_VALUE = "value";
    private static final String COLUMN_PM_AKTION = "action";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_PM_INFO + ";";
    private static final String SQL_SELECT_FROM_PIN_MONEY = "select " + COLUMN_PM_STARTDATE + ", " + COLUMN_PM_CYCLE + ", " + COLUMN_PM_VALUE
            + " from " + TABLE_PM_INFO + " where " + COLUMN_PM_ID + " = (SELECT MAX( " + COLUMN_PM_ID + " )  FROM  "
            + TABLE_PM_INFO + " where " + COLUMN_PM_NAME + " like ";

    private static final String INSERT_INTO_PIN = "insert into " + TABLE_PM_INFO + "( " + COLUMN_PM_ID
            + ", " + COLUMN_PM_ENTRYDATE + ", " + COLUMN_PM_NAME + ", " + COLUMN_PM_CYCLE
            + ", " + COLUMN_PM_VALUE + ", " + COLUMN_PM_AKTION + " )";

    private static final String SQL_CREATE_PINMONEY = "CREATE TABLE " + TABLE_PM_INFO + "(" +
            COLUMN_PM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PM_ENTRYDATE + " NOT NULL, " +
            COLUMN_PM_NAME + " NOT NULL, " +
            COLUMN_PM_STARTDATE + " , " +
            COLUMN_PM_CYCLE + " , " +
            COLUMN_PM_VALUE + " , " +
            COLUMN_PM_AKTION + " )";

    private DAOImplSQLight(Context con) {
        super(con, DB_NAME, null, DB_VERSION);
        log("Helper hat die DB " + getDatabaseName() + " erzeugt");
    }

    static DAOImplSQLight getInstance(Context c) {
        if (daoImplSQLight == null) {
            daoImplSQLight = new DAOImplSQLight(c);
        }
        return daoImplSQLight;
    }

    @Override
    public void addEntryToPinMoney(String name, Zahlungen zahlungen, String aktion) {
        db = getWritableDatabase();
        String sql = INSERT_INTO_PIN
                + " values ( null, date('now'), '" + name + "', '" + zahlungen.getTurnusStr()
                + "', " + zahlungen.getBetrag() + ", '" + aktion + "')";
        db.execSQL(sql);
    }

    @Override
    public void addEntryToPinMoney(String name, String aktion) {
        db = getWritableDatabase();
        String sql = INSERT_INTO_PIN
                + " values ( null, date('now'), " + name + ", null, null, " + aktion + ")";
        db.execSQL(sql);
    }

    //Lies den letzten eintrag zu dem Konto
    //Zahlungsinfo zum Inhaber auslesen
    @Override
    public Zahlungen getZahlungenFromPinMoney(String inhaber) {
        db = getWritableDatabase();
        Zahlungen zahlungen;
        Date date;
        float value;
        Turnus turnus;
        String sql = SQL_SELECT_FROM_PIN_MONEY + inhaber + " )";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        date = new Date(c.getColumnIndex(COLUMN_PM_STARTDATE));
        value = c.getFloat(c.getColumnIndex(COLUMN_PM_VALUE));
        switch (c.getString(c.getColumnIndex(COLUMN_PM_CYCLE))) {
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
        c.close();
        zahlungen = new Zahlungen(date, turnus, value);
        log("getZahlungenFromPinMoney ausgeführt für Konto " + inhaber + "\n" + sql);
        return zahlungen;
    }

    @Override
    public PinMoneyEnrty getEntryFromPinMoney(String inhaber) {
        db = getWritableDatabase();
        Zahlungen zahlungen;
        Date date;
        float value;
        Turnus turnus;
        //// TODO: 04.10.16
        String sql = SQL_SELECT_FROM_PIN_MONEY + inhaber + " )";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        date = new Date(c.getColumnIndex(COLUMN_PM_STARTDATE));
        value = c.getFloat(c.getColumnIndex(COLUMN_PM_VALUE));
        switch (c.getString(c.getColumnIndex(COLUMN_PM_CYCLE))) {
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
        return null;
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
        log("Neues Konto '" + konto + "' erstellen mit: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void createBuchung(Konto konto, Buchung buchung) {
        db = getWritableDatabase();
        DateHelper dateHelper = new DateHelper();
        String datum = dateHelper.setNowDate();
        //Achtung Hier wird direkt Buchung geschrieben : Balance ist vorher korrekt zu setzen!
        String sql = "Insert into " + konto.getInhaber() + " ( "
                + COLUMN_DATE + "," + COLUMN_VALUE + "," + COLUMN_TEXT + ","
                + COLUMN_VERI_ID + "," + COLUMN_VERI_TYPE + "," + COLUMN_BALANCE + ")"
                + "values ('" + datum + "', "
                + buchung.getValue() + ",'" + buchung.getText() + "'," + buchung.getVeri_id() + ",'"
                + buchung.getVeri_type() + "'," + buchung.getBalance() + ")";
        log("Buchung erstellen mit: " + sql);
        db.execSQL(sql);
    }


    Konto getKonto(String kontoname) {
        Konto konto = null;
        if (kontoExists(kontoname)) {
            float kontostand = getKontostand(kontoname);
            konto = new Konto(kontoname, kontostand);
        }
        return konto;
    }

    @Override
    public ArrayList<Buchung> getAllBuchungen(String name) {
        db = getWritableDatabase();
        ArrayList<Buchung> buchungen = new ArrayList<>();
        Buchung buchung;
        Long id;
        Date date;
        float value;
        String text;
        Long veri_id;
        Integer veri_type;
        float balance;

        String sql = "Select * from " + name;
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
            try {
                date = dateHelper.sdfLong.parse(c.getString(c.getColumnIndex(COLUMN_DATE)));
                buchung = new Buchung(id, date, value, text, veri_id, veri_type, balance);
            } catch (ParseException e) {
                buchung = new Buchung(id, null, value, text, veri_id, veri_type, balance);
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
            buchungen.add(buchung);
            log("getAllBuchungen : " +buchung.toString());
            c.moveToNext();
        }
        c.close();
        return buchungen;
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
                if (!inhaber.equals(TABLE_PM_INFO)) {
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
    public void deleteKonto(String konto) {
        db = getWritableDatabase();
        String sql = "DROP TABLE IF EXISTS " + konto;
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

        } else {
            schon_existent = false;
            log("kontoExists: Das Konto " + string + " wurde nicht gefunden.");
        }
        c.close();
        return schon_existent;
    }

    @Override
    public float getKontostand(String inhaber) {
        db = getWritableDatabase();
        if (daoImplSQLight.isValidKontoName(inhaber)) {
            log("getKontostand: Ermittle den Kontostand für " + inhaber);
            float kontostand = 0;
            String sql = "SELECT " + COLUMN_BALANCE + " FROM " + inhaber + " WHERE   ID = (SELECT MAX(ID)  FROM  " + inhaber + " )";
            Cursor c = db.rawQuery(sql, null);
            log("getKontostand: habe Treffer : " + c.getCount());
            c.moveToFirst();
            switch (c.getCount()) {
                case 0:
                    log("getKontostand: Keine Datensätze gefunden ");
                    break;
                case 1:
                    kontostand = c.getFloat(c.getColumnIndex(COLUMN_BALANCE));
                    log("getKontostand ist : " + kontostand);
                    break;
                default:
                    log("getKontostand: Ich habe " + c.getCount() + " 'letzte' Einträge gefunden!?");
                    break;
            }
            c.close();
            return kontostand;
        }
        return 0f;
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
            db.execSQL(SQL_CREATE_PINMONEY);
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


