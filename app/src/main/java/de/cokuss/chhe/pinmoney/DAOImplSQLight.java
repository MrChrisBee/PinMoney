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

import de.cokuss.chhe.pinmoney.fundamentals.Buchung;
import de.cokuss.chhe.pinmoney.fundamentals.BuchungDAO;
import de.cokuss.chhe.pinmoney.fundamentals.Cycle;
import de.cokuss.chhe.pinmoney.fundamentals.Konto;
import de.cokuss.chhe.pinmoney.fundamentals.KontoDAO;
import de.cokuss.chhe.pinmoney.fundamentals.Payments;
import de.cokuss.chhe.pinmoney.fundamentals.PaymentsDAO;

public class DAOImplSQLight extends SQLiteOpenHelper implements BuchungDAO, KontoDAO, PaymentsDAO {
    private static final String LOG_TAG = DAOImplSQLight.class.getSimpleName();
    //Datenbank
    private static final String DB_NAME = "taschengeldkonto.db";
    private static final int DB_VERSION = 2;
    //Reseved for PM Booking
    private static final String DONT_USE_THIS_FOR_NORMAL_BOOKING = "PMTG";
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
    private static final String COLUMN_PM_BIRTHDAY = "birthdate";
    private static final String COLUMN_PM_CYCLE = "cycle";
    private static final String COLUMN_PM_VALUE = "value";
    private static final String COLUMN_PM_ACTION = "action";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_PM_INFO + ";";
    private static final String SQL_SELECT_FROM_PIN_MONEY = "select * from " + TABLE_PM_INFO + " where " + COLUMN_PM_NAME + " = '";
    private static final String SQL_SELECT_ALL_FROM_PIN_MONEY = "select * from " + TABLE_PM_INFO;
    private static final String INSERT_INTO_PIN = " insert into " + TABLE_PM_INFO + "( " + COLUMN_PM_ID
            + ", " + COLUMN_PM_ENTRYDATE + ", " + COLUMN_PM_NAME + ", " + COLUMN_PM_BIRTHDAY + ", " + COLUMN_PM_STARTDATE + ", " + COLUMN_PM_CYCLE
            + ", " + COLUMN_PM_VALUE + ", " + COLUMN_PM_ACTION + " )";
    private static final String SQL_CREATE_PINMONEY = "CREATE TABLE " + TABLE_PM_INFO + " ( " +
            COLUMN_PM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PM_ENTRYDATE + " NOT NULL, " +
            COLUMN_PM_NAME + " NOT NULL, " +
            COLUMN_PM_BIRTHDAY + " , " +
            COLUMN_PM_STARTDATE + " , " +
            COLUMN_PM_CYCLE + " , " +
            COLUMN_PM_VALUE + " , " +
            COLUMN_PM_ACTION + " )";
    private static DAOImplSQLight daoImplSQLight;
    private SQLiteDatabase db;
    private DateHelper dateHelper = new DateHelper();

    private DAOImplSQLight(Context con) {
        super(con, DB_NAME, null, DB_VERSION);
        log("Helper hat die DB " + getDatabaseName() + " erzeugt");
    }

    public static DAOImplSQLight getInstance(Context c) {
        if (daoImplSQLight == null) {
            daoImplSQLight = new DAOImplSQLight(c);
        }
        return daoImplSQLight;
    }

    @Override
    public void addEntryToPinMoney(String name, Date gebDate, Payments payments, String aktion) {
        db = getWritableDatabase();
        DateHelper dateHelper = new DateHelper();
        String startDateStr = dateHelper.sdfLong.format(payments.getDate());
        String gebDateStr = dateHelper.sdfLong.format(gebDate);
        String nowDate = dateHelper.setNowDate();
        String sql = INSERT_INTO_PIN
                + " values ( null, '" + nowDate + "' ,'" + name + "', '" + gebDateStr + "', '" + startDateStr
                + "', '" + payments.getTurnusStr() + "', " + payments.getBetrag() + ", '" + aktion + "')";
        db.execSQL(sql);
    }

    @Override
    public void addEntryToPinMoney(String name, String aktion) {
        db = getWritableDatabase();

        String sql = INSERT_INTO_PIN
                + " values ( null, '" + dateHelper.setNowDate() + "', '" + name + "',null , null, null, null,'" + aktion + "')";
        // id eintragsdatum kontoinhaber startdatum turnus betrag aktion
        db.execSQL(sql);
    }

    private Date getDate(Cursor cursor, String column, String name) {
        int i = cursor.getColumnIndex(column);
        Date date;
        if (i > -1 && !cursor.isNull(i)) {
            try {
                date = dateHelper.sdfLong.parse(cursor.getString(i));
            } catch (ParseException e) {
                date = null;
                log("getDate() Kein " + column + " für den Namen " + name);
            }
        } else date = null;
        return date;
    }

    private String getStringFromCursor(Context context, String column, Cursor cursor) {
        String string;
        int i = cursor.getColumnIndex(column);
        if (i > -1 && !cursor.isNull(i)) {
            string = cursor.getString(i);
        } else string = context.getResources().getString(R.string.noEntry);
        return string;
    }

    private Cycle getTurnusFromCursor(Cursor cursor) {
        Cycle cycle;
        int i = cursor.getColumnIndex(COLUMN_PM_CYCLE);
        if (i > -1 && !cursor.isNull(i)) { // if you find a Column  check that it is not null
            switch (cursor.getString(i)) {
                case "tag":
                    log("getEntryListFromPinMoney() " + cursor.getString(i));
                    cycle = Cycle.TAEGLICH;
                    break;
                case "woche":
                    log("getEntryListFromPinMoney() " + cursor.getString(i));
                    cycle = Cycle.WOECHENTLICH;
                    break;
                case "monat":
                    log("getEntryListFromPinMoney() " + cursor.getString(i));
                    cycle = Cycle.MONATLICH;
                    break;
                default:
                    log("der Untersuchte String für den Cycle ist: " + cursor.getString(i));
                    cycle = Cycle.KEINE_ANGABE;
            }
        } else cycle = Cycle.KEINE_ANGABE;
        return cycle;
    }

    //read the last entry that fits to the owner
    @Override
    public PinMoneyEntry getEntryFromPinMoney(Context context, String owner) {
        db = getWritableDatabase();
        PinMoneyEntry result;
        Payments payments;
        Date startDate, entryDate, birthDate;
        String action;
        float value;
        Cycle cycle;
        //give the last entry for a given account
        String sql = SQL_SELECT_FROM_PIN_MONEY + owner + "' order by " + COLUMN_PM_ID + " desc limit 1";
        Cursor c = db.rawQuery(sql, null);
        if (c.getCount() < 1) return null; //safest way ? Only neet to check this
        c.moveToFirst();
        action = getStringFromCursor(context, COLUMN_PM_ACTION, c);
        startDate = getDate(c, COLUMN_PM_STARTDATE, owner);
        entryDate = getDate(c, COLUMN_PM_ENTRYDATE, owner);
        birthDate = getDate(c, COLUMN_PM_BIRTHDAY, owner);
        value = c.getFloat(c.getColumnIndex(COLUMN_PM_VALUE));
        cycle = getTurnusFromCursor(c);
        payments = new Payments(startDate, cycle, value);
        result = new PinMoneyEntry(payments, entryDate, owner, birthDate, action);
        c.close();
        return result;
    }


    public ArrayList<PinMoneyEntry> getEntryListFromPinMoney(Context context) {
        //This Class got no context, for use of resources you need it
        db = getWritableDatabase();
        String name, action, cycleStr;
        Date startDate, entryDate, birthDate;
        Float value;
        Cycle cycle;
        Payments payments;
        DateHelper dateHelper = new DateHelper();
        String sql = SQL_SELECT_ALL_FROM_PIN_MONEY;
        ArrayList<PinMoneyEntry> entrys = new ArrayList<>();
        //hole alle Einträge
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        log("getEntryListFromPinMoney " + c.getCount() + " HistoryEntrys found");
        while (!c.isAfterLast()) {
            int i;
            name = getStringFromCursor(context, COLUMN_PM_NAME, c);
            i = c.getColumnIndex(COLUMN_PM_VALUE);
            if (i > -1 && !c.isNull(i)) {
                value = c.getFloat(i);
            } else value = 0.00f;
            cycle = getTurnusFromCursor(c);
            startDate = getDate(c, COLUMN_PM_STARTDATE, name);
            action = getStringFromCursor(context, COLUMN_PM_ACTION, c);
            entryDate = getDate(c, COLUMN_PM_ENTRYDATE, name);
            birthDate = getDate(c, COLUMN_PM_BIRTHDAY, name);
            //create a new payments instance
            payments = new Payments(startDate, cycle, value);
            //add a new PinMoneyEntry
            entrys.add(new PinMoneyEntry(payments, entryDate, name, birthDate, action));
            c.moveToNext();
        }
        c.close();
        log("getEntryListFromPinMoney() : ArrayList with " + entrys.size() + " elements created.");
        return entrys;
    }

    //Todo wenn mal Zeit ist: Braucht create Konto ein Konto oder reicht auch ein String
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


    //here Konto is the right parameter
    @Override
    public void createBuchung(Konto konto, Buchung buchung) {
        db = getWritableDatabase();
        DateHelper dateHelper = new DateHelper();
        String datum = dateHelper.setNowDate();
        //Achtung Hier wird direkt Buchung geschrieben : Balance ist vorher korrekt zu setzen!
        String sql = "Insert into " + konto.getInhaber() + " ( "
                + COLUMN_DATE + "," + COLUMN_VALUE + "," + COLUMN_TEXT + ","
                + COLUMN_VERI_ID + "," + COLUMN_VERI_TYPE + "," + COLUMN_BALANCE + " ) "
                + "values ('" + datum + "', "
                + buchung.getValue() + ",'" + buchung.getText() + "'," + buchung.getVeri_id() + ",'"
                + buchung.getVeri_type() + "'," + buchung.getBalance() + ")";
        log("Buchung erstellen mit: " + sql);
        db.execSQL(sql);
    }

    //It must be called from somewhere you can get a context,
    //For getting a Instance of this Class (DAOImplSQLight) you also need a context that seems ok for me
    @Override
    public Buchung calcSavings(Context context, String owner) {
        Calendar historyDate, bookingDate, today, startDate;
        historyDate = Calendar.getInstance();
        bookingDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        Payments payments;
        //this should offer the last line in the history for owner
        //perhaps this should be called from ChangeHistoryEntry to make a cut from the old to the new calculating system
        PinMoneyEntry pinMoneyEntry = daoImplSQLight.getEntryFromPinMoney(context, owner);
        if (pinMoneyEntry != null) {
            payments = pinMoneyEntry.getPayments();
            if (payments == null) {
                //senseless to continue, massive Error owner without account!!!
                log("PaymentsError : " + context.getString(R.string.wrongData) + owner);
                return null;
            }
            //getting here only when payments != null so this should work
            historyDate.setTime(payments.getDate());
        } else {
            //senseless to continue, massive Error owner without account!!!
            log("PinMoneyError : " + context.getString(R.string.wrongData) + owner);
            return null;
        }
        //give me the last automatic booking of pinmoney
        Buchung automaticBooking = daoImplSQLight.getLastRelevantBooking(owner);
        if (automaticBooking != null) {
            //there was a automatic booking of pinmoney before
            bookingDate.setTime(automaticBooking.getDate());
            //welches Datum nutzen? Das jüngste! (Hier kommt die nachträgliche Änderung ins Spiel)
            startDate.setTime(getMostRecentDate(bookingDate, historyDate));
        } else {
            //no automatic booking of pinmoney
            startDate = historyDate;
        }
        Cycle cycle = payments.getCycle();
        float valuePerCycle = payments.getBetrag();
        int countCycles = 0;
        float valueToBook = 0f;
        Calendar setDate = (Calendar) startDate.clone();
        //Vergleiche der Daten erfolgen auf Basis von Millisekunden also müssen die Daten in der Zeit völlig übereinstimmen
        //nur das Datum darf variieren
        today = setToSameTimeButDifferentDate(startDate, Calendar.getInstance());
        log("StartTime: " + startDate.getTime().toString() + " Today : " + today.getTime().toString());
        log("Vergleich startDate.compareTo(today) : " + startDate.compareTo(today));
        Buchung result = null;
        if (startDate.before(today)) {
            switch (cycle) {
                case TAEGLICH:
                    countCycles = 1;
                    log("Pro Tag " + valuePerCycle + " € vom " + dateHelper.sdfShort.format(startDate.getTime()) + " bis " + dateHelper.sdfShort.format(today.getTime()));
                    //countCycles ermitteln wir sind mit setDate vor today!!
                    while (!(setDate.compareTo(today) == 0)) {
                        setDate.add(Calendar.DAY_OF_YEAR, 1);
                        countCycles++;
                    }
                    valueToBook = valuePerCycle * countCycles;
                    break;
                case WOECHENTLICH:
                    countCycles = 1;
                    log("Pro Woche " + valuePerCycle + "€ vom " + dateHelper.sdfShort.format(startDate.getTime()) + " bis " + dateHelper.sdfShort.format(today.getTime()));
                    while (setDate.before(today)) {
                        setDate.add(Calendar.DAY_OF_YEAR, 7);
                        countCycles++;
                    }
                    if(setDate.after(today)) {
                        setDate.add(Calendar.DAY_OF_YEAR, -7);
                        countCycles--;
                    }
                    valueToBook = valuePerCycle * countCycles;
                    break;
                case MONATLICH:
                    countCycles = 1;
                    log("Pro Monat " + valuePerCycle + "€ vom " + dateHelper.sdfShort.format(startDate.getTime()) + " bis " + dateHelper.sdfShort.format(today.getTime()));
                    while (setDate.before(today)) {
                        setDate.add(Calendar.MONTH, 1);
                        countCycles++;
                    } //Vielleicht bin ich jetzt einen Monat zu weit? Immerhin ist gestern auch vor heute (setDate.before(today))
                    if(setDate.after(today)) {
                        setDate.add(Calendar.MONTH, -1);
                        countCycles--;
                    }
                    valueToBook = valuePerCycle * countCycles;
                    break;
                default:
                    log("Cycle : " + context.getString(R.string.wrongData) + owner);
                    return null;
            }
        }

        if ( valueToBook > 0f) {
            String text = DONT_USE_THIS_FOR_NORMAL_BOOKING +": "+ countCycles + cycle.getBezeichnerLetter()+". a " +  valuePerCycle+ "€";
            result = new Buchung(null, setDate.getTime(), valueToBook, text, 0l, 0, (daoImplSQLight.getKontostand(owner) + valueToBook));
        }

        return result;
    }

    private Calendar setToSameTimeButDifferentDate(Calendar getTimeFrom, Calendar getDateFrom) {
        //gib ein Datum zurück mit der Zeit von gTimeF und dem Datum von gDateF
        Calendar result;
        result = (Calendar) getTimeFrom.clone();
        result.set(Calendar.DAY_OF_YEAR, getDateFrom.get(Calendar.DAY_OF_YEAR));
        result.set(Calendar.MONTH, getDateFrom.get(Calendar.MONTH));
        result.set(Calendar.YEAR, getDateFrom.get(Calendar.YEAR));
        return result;
    }

    private Date getMostRecentDate(Calendar first, Calendar second) {
        Date result = null;
        if (first.before(second)) {
            result = second.getTime();
        } else if (second.before(first)) {
            result = first.getTime();
        } else {
            result = first.getTime();
        }
        log("gmrd: first: " + dateHelper.sdfShort.format(first.getTime()) + " secound: "
                + dateHelper.sdfShort.format(second.getTime()) + " Result " + dateHelper.sdfShort.format(result));
        return result;
    }


    private Buchung getLastRelevantBooking(String owner) {
        ArrayList<Buchung> allBuchungen = daoImplSQLight.getAllBuchungen(owner);
        Buchung buchung = null;
        //wenn Du im Buchungstext DONT_USE_THIS_FOR_NORMAL_BOOKING als Teilstring findest
        //  weise diese Buchung buchung zu
        // mache das bis zum letzten Eintrag in allBuchungen
        // der Inhalt von buchung ist jetzt null oder die letzte gesuchte Buchung
        if (allBuchungen != null && !allBuchungen.isEmpty()) {
            //hoffe es geht in der richtigen Reihenfolge
            for (Buchung aktBuchung : allBuchungen) {
                if (aktBuchung.getText().startsWith(DONT_USE_THIS_FOR_NORMAL_BOOKING)) {
                    buchung = aktBuchung;
                }
            }
        }
        return buchung;
    }

    public Konto getKonto(String kontoname) {
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
                //buchung = new Buchung(id, null, value, text, veri_id, veri_type, balance);
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
                //todo Vielleicht falsch ? Ich denke es ist so am Sichersten
                return null;
            }
            buchungen.add(buchung);
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
            float kontostand = 0;
            String sql = "SELECT " + COLUMN_BALANCE + " FROM " + inhaber + " WHERE   ID = (SELECT MAX(ID)  FROM  " + inhaber + " )";
            Cursor c = db.rawQuery(sql, null);
            c.moveToFirst();
            switch (c.getCount()) {
                case 0:
                    log("getKontostand: Keine Datensätze gefunden ");
                    break;
                case 1:
                    kontostand = c.getFloat(c.getColumnIndex(COLUMN_BALANCE));
                    log("getKontostand: Der Kontostand für " + inhaber + " ist " + kontostand + " €");
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


