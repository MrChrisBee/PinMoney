package de.cokuss.chhe.pinmoney;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chrisbee on 15.09.16.
 */
public class DAODatabase {

    private static final String LOG_TAG = DAODatabase.class.getSimpleName();
    private SQLiteDatabase db;
    private DAOImplSQLight helper;

    private String[] columns =
            {
                    DAOImplSQLight.COLUMN_ID,
                    DAOImplSQLight.COLUMN_DATE,
                    DAOImplSQLight.COLUMN_VALUE,
                    DAOImplSQLight.COLUMN_TEXT,
                    DAOImplSQLight.COLUMN_VERI_ID,
                    DAOImplSQLight.COLUMN_VERI_TYPE,
                    DAOImplSQLight.COLUMN_BALANCE
            };

    public DAODatabase (Context con) {
        Log.d(LOG_TAG, "Der Helper wird erzeugt.");
        helper = new DAOImplSQLight(con);
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

    public Buchung createBuchung(Date date, double value, String text, Long veri_id, Integer veri_type, double balance) {
        ContentValues values = new ContentValues();
        values.put(DAOImplSQLight.COLUMN_DATE, date.toString());
        values.put(DAOImplSQLight.COLUMN_VALUE, value);
        values.put(DAOImplSQLight.COLUMN_TEXT, text);
        values.put(DAOImplSQLight.COLUMN_VERI_ID, veri_id);
        values.put(DAOImplSQLight.COLUMN_VERI_TYPE, veri_type);
        values.put(DAOImplSQLight.COLUMN_BALANCE, balance);

        long insertId = db.insert(DAOImplSQLight.TABLE_NAME, null, values);

        Cursor cursor = db.query(DAOImplSQLight.TABLE_NAME, columns,
                DAOImplSQLight.COLUMN_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Buchung buchung = cursorToBuchung(cursor);
        cursor.close();
        return buchung;
    }


    private Buchung cursorToBuchung(Cursor cursor) {
        int idDate =   cursor.getColumnIndex(DAOImplSQLight.COLUMN_DATE);
        int idValue = cursor.getColumnIndex(DAOImplSQLight.COLUMN_VALUE);
        int idText = cursor.getColumnIndex(DAOImplSQLight.COLUMN_TEXT);
        int idVeryID = cursor.getColumnIndex(DAOImplSQLight.COLUMN_VERI_ID);
        int idVeryType =cursor.getColumnIndex(DAOImplSQLight.COLUMN_VERI_TYPE);
        int idBalanve = cursor.getColumnIndex(DAOImplSQLight.COLUMN_BALANCE);

// Beispiel Am Samstag dem 17.8.2016 gab es eine Einzahlung mit dem Text "Zahlungen von Oma"

//        String product = cursor.getString(idProduct);
//        int quality = cursor.getInt(idQuantity);
//        long id = cursor.getLong(idIndex);
//        int intValueChecked = cursor.getInt(idChecked);
//        boolean isChecked = (intValueChecked != 0);
//        // TODO buchung erzeugen
        Buchung buchung = new Buchung();
        return buchung;
    }


    public List<Buchung> getAllBuchungen() {
        List<Buchung> konto = new ArrayList<>();
        Cursor cursor = db.query(DAOImplSQLight.TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        //// TODO: 15.09.16 hier gehts weiter 
        Buchung buchung;
        while (!cursor.isAfterLast()) {
            buchung = cursorToBuchung(cursor);
            konto.add(buchung);
            cursor.moveToNext();
        }
        cursor.close();
        return konto;
    }

    public Buchung updateKonto() {
//        int intValueChecked = (newChecked) ? 1 : 0;
//        ContentValues values = new ContentValues();
//        values.put(ShoppingMemoDbHelper.COLUMN_PRODUCT, produkt);
//        values.put(ShoppingMemoDbHelper.COLUMN_QUANTITY, quantity);
//        values.put(ShoppingMemoDbHelper.COLUMN_CHECKED, intValueChecked);
//
//        db.update(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, values, ShoppingMemoDbHelper.COLUMN_ID + "=" + id, null);
//        Cursor cursor = db.query(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, columns,
//                ShoppingMemoDbHelper.COLUMN_ID + "=" + id, null, null, null, null);
//        cursor.moveToFirst();
//        ShoppingMemo shoppingMemo = cursorToShoppingMemo(cursor);
//        cursor.close();
        return null;
    }
}

