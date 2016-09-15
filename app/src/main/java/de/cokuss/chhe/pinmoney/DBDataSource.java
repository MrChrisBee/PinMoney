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
public class DBDataSource {

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

    public DBDataSource(Context con) {
        Log.d(LOG_TAG, "Der Helper wird erzeugt.");
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

    public Buchung createBuchung(Date date, double value, String text, Long veri_id, Integer veri_type, double balance) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DATE, date.toString());
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
        int idIndex = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_ID);
        int idProduct = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_PRODUCT);
        int idQuantity = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_QUANTITY);
        int idChecked = cursor.getColumnIndex(ShoppingMemoDbHelper.COLUMN_CHECKED);

        String product = cursor.getString(idProduct);
        int quality = cursor.getInt(idQuantity);
        long id = cursor.getLong(idIndex);
        int intValueChecked = cursor.getInt(idChecked);
        boolean isChecked = (intValueChecked != 0);
        // TODO buchung erzeugen
        Buchung buchung = new Buchung(alle nötigen parameter übergeben);
        return buchung;
    }


    public List<Buchung> getAllBuchungen() {
        List<Buchung> konto = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, columns, null, null, null, null, null);
        cursor.moveToFirst();
        //// TODO: 15.09.16 hier gehts weiter 
        ShoppingMemo memo;
        while (!cursor.isAfterLast()) {
            memo = cursorToShoppingMemo(cursor);
            shoppingMemoList.add(memo);
            cursor.moveToNext();
        }
        cursor.close();
        return shoppingMemoList;
    }

    public void deleteShoppingMemo(ShoppingMemo shoppingMemo) {
        long id = shoppingMemo.getId();
        db.delete(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, ShoppingMemoDbHelper.COLUMN_ID + "=" + id, null);
    }

    public ShoppingMemo updateShoppingMemo(long id, String produkt, int quantity, boolean newChecked) {
        int intValueChecked = (newChecked) ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(ShoppingMemoDbHelper.COLUMN_PRODUCT, produkt);
        values.put(ShoppingMemoDbHelper.COLUMN_QUANTITY, quantity);
        values.put(ShoppingMemoDbHelper.COLUMN_CHECKED, intValueChecked);

        db.update(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, values, ShoppingMemoDbHelper.COLUMN_ID + "=" + id, null);
        Cursor cursor = db.query(ShoppingMemoDbHelper.TABLE_SHOPPING_LIST, columns,
                ShoppingMemoDbHelper.COLUMN_ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        ShoppingMemo shoppingMemo = cursorToShoppingMemo(cursor);
        cursor.close();
        return shoppingMemo;
    }
}

