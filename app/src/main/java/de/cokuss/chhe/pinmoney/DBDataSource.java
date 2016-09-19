package de.cokuss.chhe.pinmoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBDataSource {
    private String client_name;
    private static final String LOG_TAG = DBDataSource.class.getSimpleName();
    private SQLiteDatabase db;
    private DBHelper helper;

    private String[] columns =
            {
                    DBHelper.COLUMN_ID,
                    DBHelper.COLUMN_DATE,
                    DBHelper.COLUMN_VALUE,
                    DBHelper.COLUMN_TEXT,
                    DBHelper.COLUMN_VERI_ID,
                    DBHelper.COLUMN_VERI_TYPE,
                    DBHelper.COLUMN_BALANCE
            };

    public DBDataSource(Context con, String clientName) {
        Log.d(LOG_TAG, "Der Helper wird erzeugt.");
        this.client_name = client_name;
        helper = new DBHelper(con);
    }

    public void open() {
        Log.d(LOG_TAG, "Wir öffnen die Datenbank im RW Mode.");
        db = helper.getWritableDatabase();
        Log.d(LOG_TAG, "Pfad zur DB " + db.getPath());
    }

    public void close() {
        helper.close();
        Log.d(LOG_TAG, "Datenbank wurde geschlossen.");
    }

    public Double getBalance(String name) {


        return null;
    }

    public Buchung createBuchung(String name, Date date, double value, String text, Long veri_id, Integer veri_type, double balance) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, (byte[]) null);
        values.put(DBHelper.COLUMN_DATE, date.toString());
        values.put(DBHelper.COLUMN_NAME, date.toString());
        values.put(DBHelper.COLUMN_VALUE, value);
        values.put(DBHelper.COLUMN_TEXT, text);
        values.put(DBHelper.COLUMN_VERI_ID, veri_id);
        values.put(DBHelper.COLUMN_VERI_TYPE, veri_type);
        values.put(DBHelper.COLUMN_BALANCE, balance);
        long insertId = db.insert(DBHelper.TABLE_NAME, null, values);
        Cursor cursor = db.query(DBHelper.TABLE_NAME, columns,
                DBHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Buchung buchung = cursorToBuchung(cursor);
        cursor.close();
        return buchung;
    }

    private Buchung cursorToBuchung(Cursor cursor) {
        int idDate =   cursor.getColumnIndex(DBHelper.COLUMN_DATE);
        int idValue = cursor.getColumnIndex(DBHelper.COLUMN_VALUE);
        int idText = cursor.getColumnIndex(DBHelper.COLUMN_TEXT);
        int idVeryID = cursor.getColumnIndex(DBHelper.COLUMN_VERI_ID);
        int idVeryType =cursor.getColumnIndex(DBHelper.COLUMN_VERI_TYPE);
        int idBalance = cursor.getColumnIndex(DBHelper.COLUMN_BALANCE);

        Date date = new Date(cursor.getLong(idDate));
        Double value  = new Double(cursor.getDouble(idValue));
        String text = new String(cursor.getString(idText));
        Long veri_id = new Long(cursor.getLong(idVeryID));
        Integer veri_type = new Integer(cursor.getInt(idVeryType));

        Buchung buchung = new Buchung(date, value, text, veri_id, veri_type, idBalance);
        return buchung;
    }

    public List<Buchung> getAllBuchungen() {
        List<Buchung> konto = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        Buchung buchung;
        while (!cursor.isAfterLast()) {
            buchung = cursorToBuchung(cursor);
            konto.add(buchung);
            cursor.moveToNext();
        }
        cursor.close();
        return konto;
    }

}

