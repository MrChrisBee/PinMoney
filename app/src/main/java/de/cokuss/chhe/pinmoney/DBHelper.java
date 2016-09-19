package de.cokuss.chhe.pinmoney;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DBHelper.class.getSimpleName();
    //Datenbank
    public static final String DB_NAME = "taschengeldkonto.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "konto";
    //Spalten
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "datum";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VALUE = "betrag";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_VERI_ID = "verifikation_id";
    public static final String COLUMN_VERI_TYPE = "verifikation_typ";
    public static final String COLUMN_BALANCE = "kontostand";

    public static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "+
            COLUMN_DATE + " DATE NOT NULL, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_VALUE + " DOUBLE NOT NULL, " +
            COLUMN_TEXT + " TEXT NOT NULL, " +
            COLUMN_VERI_ID + " LONG NOT NULL, " +
            COLUMN_VERI_TYPE + " INTEGER NOT NULL DEFAULT 0, " +
            COLUMN_BALANCE + " DOUBLE NOT NULL DEFAULT 0);";

    public static final String SQL_DROP_TABLE = "DROP TABLE IF EXIST " + TABLE_NAME + ";";

    public DBHelper(Context con){
        super(con,DB_NAME,null,DB_VERSION);
        Log.d(LOG_TAG,"Helper hat die DB " + getDatabaseName() + " erzeugt");
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG,"Versuch die Tabelle zu erstellen !" );
        try {
            db.execSQL(SQL_CREATE);
        } catch (SQLException e) {
            Log.e(LOG_TAG,"Fehler beim Anlegen " + e.getMessage());
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG,"Tabelle erstellt !" );
        }
    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }
}



